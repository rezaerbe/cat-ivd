package app.com.kucingapps;

import android.content.Intent;
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

import app.com.kucingapps.Retrofit.ApiInterface;
import app.com.kucingapps.Utils.Common;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password, c_password;
    private Button btn_register;
    private ProgressBar loading;
    ApiInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mService = Common.getAPI();

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        btn_register = findViewById(R.id.btn_register);
        loading = findViewById(R.id.loading);

        final String password = this.password.getText().toString().trim();
        final String c_password = this.c_password.getText().toString().trim();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.equals(c_password)){
                    Register();
                } else {
                    Toast.makeText(RegisterActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void Register(){
        loading.setVisibility(View.VISIBLE);
        btn_register.setVisibility(View.GONE);

        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        mService.registerRequest(name, email, password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            loading.setVisibility(View.GONE);
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("success").equals("1")){
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    String message = jsonRESULTS.getString("message");
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            loading.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.setVisibility(View.GONE);
                    }
                });
    }
}
