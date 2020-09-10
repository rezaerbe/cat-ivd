package app.com.kucingapps;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import app.com.kucingapps.Model.Pets;
import app.com.kucingapps.Model.User;
import app.com.kucingapps.Retrofit.ApiInterface;
import app.com.kucingapps.Utils.Common;
import app.com.kucingapps.Utils.SessionManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    private EditText mNama, mEmail, mAddress, mPhone;
    private CircleImageView mPicture;
    private FloatingActionButton mFabChoosePic;

    Integer uid;

    private String nama, email, phone,address, getId, picture;
    private int id;

    private Menu action;
    private Bitmap bitmap;

    ApiInterface mService;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mService = Common.getAPI();

        sessionManager = new SessionManager(this);

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

        uid = Integer.parseInt(getId);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mNama = findViewById(R.id.name_user);
        mEmail = findViewById(R.id.email_user);
        mPicture = findViewById(R.id.picture);
        mAddress = findViewById(R.id.address_user);
        mPhone = findViewById(R.id.phone_user);
        mFabChoosePic = findViewById(R.id.fabChoosePic);

        mFabChoosePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        nama = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        picture = intent.getStringExtra("picture");
        address = intent.getStringExtra("address");
        phone = intent.getStringExtra("phone");

        setDataFromIntentExtra();

    }

    private void setDataFromIntentExtra() {

        if (id != 0) {

            readMode();
            getSupportActionBar().setTitle("Edit " + nama.toString());

            mNama.setText(nama);
            mEmail.setText(email);
            mAddress.setText(address);
            mPhone.setText(phone);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.logo);
            requestOptions.error(R.drawable.logo);

            Glide.with(UserActivity.this)
                    .load(picture)
                    .apply(requestOptions)
                    .into(mPicture);

        } else {
            getSupportActionBar().setTitle("Add a User");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        action = menu;
        action.findItem(R.id.menu_save).setVisible(false);

        if (id == 0){

            action.findItem(R.id.menu_edit).setVisible(false);
            // action.findItem(R.id.menu_delete).setVisible(false);
            action.findItem(R.id.menu_save).setVisible(true);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();

                return true;
            case R.id.menu_edit:
                //Edit

                editMode();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mNama, InputMethodManager.SHOW_IMPLICIT);

                action.findItem(R.id.menu_edit).setVisible(false);
                // action.findItem(R.id.menu_delete).setVisible(false);
                action.findItem(R.id.menu_save).setVisible(true);

                return true;
            case R.id.menu_save:
                //Save

                if (id == 0) {

                    if (TextUtils.isEmpty(mNama.getText().toString()) ||
                            TextUtils.isEmpty(mEmail.getText().toString()) ||
                            TextUtils.isEmpty(mAddress.getText().toString()) ||
                            TextUtils.isEmpty(mPhone.getText().toString()) ){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setMessage("Please complete the field!");
                        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }

//                    else {
//
//                        postData("insert");
//                        action.findItem(R.id.menu_edit).setVisible(true);
//                        action.findItem(R.id.menu_save).setVisible(false);
//                        // action.findItem(R.id.menu_delete).setVisible(true);
//
//                        readMode();
//
//                    }

                } else {

                    updateData("update", id);
                    action.findItem(R.id.menu_edit).setVisible(true);
                    action.findItem(R.id.menu_save).setVisible(false);
                    // action.findItem(R.id.menu_delete).setVisible(true);

                    readMode();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    // Todo : Picture 1
    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
    }

    // Todo : Picture 2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                mPicture.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void updateData(final String key, final int id) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        readMode();

        String name = mNama.getText().toString().trim();
        String email = mEmail.getText().toString().trim();
        String address = mAddress.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();

        // Todo : Picture 5 OK
        String picture = null;
        if (bitmap == null) {
            picture = "";
        } else {
            picture = getStringImage(bitmap);
        }

        // apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // Todo : Picture 6 OK
        Call<User> call = mService.updateUser(key, id, name, email, picture, address, phone);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                progressDialog.dismiss();

                Log.i(UserActivity.class.getSimpleName(), response.toString());

                String value = response.body().getSuccess();
                String message = response.body().getMessage();

                if (value.equals("1")){
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                // Toast.makeText(UserActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void readMode(){

        mNama.setFocusableInTouchMode(false);
        mEmail.setFocusableInTouchMode(false);
        mAddress.setFocusableInTouchMode(false);
        mPhone.setFocusableInTouchMode(false);

        mNama.setFocusable(false);
        mEmail.setFocusable(false);
        mAddress.setFocusable(false);
        mPhone.setFocusable(false);

        mFabChoosePic.setVisibility(View.INVISIBLE);

    }

    private void editMode(){

        mNama.setFocusableInTouchMode(true);
        mEmail.setFocusableInTouchMode(true);
        mAddress.setFocusableInTouchMode(true);
        mPhone.setFocusableInTouchMode(true);

        mFabChoosePic.setVisibility(View.VISIBLE);
    }
}
