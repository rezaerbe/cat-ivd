package app.com.kucingapps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import app.com.kucingapps.Adapter.ArticleAdapter;
import app.com.kucingapps.Model.Article;
import app.com.kucingapps.Model.Pets;
import app.com.kucingapps.Model.User;
import app.com.kucingapps.Retrofit.ApiInterface;
import app.com.kucingapps.Utils.Common;
import app.com.kucingapps.Utils.SessionManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button btn_login, btn_register;
    private ProgressBar loading;
    private String mName, mEmail, mId, mPicture;
    private Integer idaja;
    SessionManager sessionManager;
    ApiInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mService = Common.getAPI();

        sessionManager = new SessionManager(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loading = findViewById(R.id.loading);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPass = password.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPass.isEmpty()) {
                    Login(mEmail, mPass);
                } else {
                    email.setError("Please insert email");
                    password.setError("Please insert password");
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private void Login(final String email, final String password) {

        loading.setVisibility(View.VISIBLE);
        btn_login.setVisibility(View.GONE);
        btn_register.setVisibility(View.GONE);

        mService.loginRequest(email, password)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()){
                            User user = response.body();
                            Common.currentUser = response.body();

                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            btn_register.setVisibility(View.VISIBLE);

                            if (user.getSuccess().equals("1"))
                            {
                                mName = user.getName();
                                mEmail = user.getEmail();
                                idaja = user.getId();
                                mPicture = user.getPicture();

                                mId = idaja.toString();

                                sessionManager.createSession(mName, mEmail, mId, mPicture);
                                Intent intent = new Intent(LoginActivity.this, FrontActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                String message = user.getMessage();
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            loading.setVisibility(View.GONE);
                            btn_login.setVisibility(View.VISIBLE);
                            btn_register.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());

                        loading.setVisibility(View.GONE);
                        btn_login.setVisibility(View.VISIBLE);
                        btn_register.setVisibility(View.VISIBLE);
                    }
                });
    }
}
