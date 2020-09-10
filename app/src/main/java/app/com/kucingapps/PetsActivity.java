package app.com.kucingapps;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.Console;
import java.util.List;

import app.com.kucingapps.Adapter.PetsAdapter;
import app.com.kucingapps.Model.Pets;
import app.com.kucingapps.Model.User;
import app.com.kucingapps.Retrofit.ApiInterface;
import app.com.kucingapps.Utils.Common;
import app.com.kucingapps.Utils.SessionManager;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetsActivity extends AppCompatActivity {

    private EditText mName, mSpecies, mBreed, mBirth, mPid, mRiwayat, mKesehatan, mVaksinasi, mPengobatan;
    private TextView mId, mNama, mEmail, mAddress, mPhone;
    private CircleImageView mPicture, mPicture1;

    private Spinner mGenderSpinner;

    private String name, species, breed, picture, birth, getId, pid, riwayat, kesehatan, vaksinasi, pengobatan, nama_pemilik, email_pemilik, picture1, address_pemilik, phone_pemilik;
    private int id, gender, uid;

    Integer uidOK, id_pemilik;

    private int mGender = 0;
    public static final int GENDER_UNKNOWN = 0;
    public static final int GENDER_MALE = 1;
    public static final int GENDER_FEMALE = 2;

    ApiInterface mService;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        mService = Common.getAPI();

        sessionManager = new SessionManager(this);

        mName = findViewById(R.id.name);
        mSpecies = findViewById(R.id.species);
        mBreed = findViewById(R.id.breed);
        mBirth = findViewById(R.id.birth);
        mPicture = findViewById(R.id.picture);
        mPid =  findViewById(R.id.id_pets);
        mRiwayat = findViewById(R.id.riwayat);
        mKesehatan = findViewById(R.id.kesehatan);
        mVaksinasi = findViewById(R.id.vaksinasi);
        mPengobatan = findViewById(R.id.pengobatan);

        mPicture1 = findViewById(R.id.picture1);
        mNama = findViewById(R.id.nama_pemilik);
        mEmail = findViewById(R.id.email_pemilik);
        mAddress = findViewById(R.id.address_pemilik);
        mPhone = findViewById(R.id.phone_pemilik);

        mGenderSpinner = findViewById(R.id.gender);
        mBirth = findViewById(R.id.birth);

        setupSpinner();

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        name = intent.getStringExtra("name");
        species = intent.getStringExtra("species");
        breed = intent.getStringExtra("breed");
        birth = intent.getStringExtra("birth");
        picture = intent.getStringExtra("picture");
        gender = intent.getIntExtra("gender", 0);
        pid = intent.getStringExtra("pid");
        uid = intent.getIntExtra("uid",0);
        riwayat = intent.getStringExtra("riwayat");
        kesehatan = intent.getStringExtra("kesehatan");
        vaksinasi = intent.getStringExtra("vaksinasi");
        pengobatan = intent.getStringExtra("pengobatan");

        getUser();

        setDataFromIntentExtra();

    }

    private void setDataFromIntentExtra() {

        if (id != 0) {

            readMode();

            mName.setText(name);
            mSpecies.setText(species);
            mBreed.setText(breed);
            mBirth.setText(birth);

            mPid.setText(pid);
            mRiwayat.setText(riwayat);
            mKesehatan.setText(kesehatan);
            mVaksinasi.setText(vaksinasi);
            mPengobatan.setText(pengobatan);

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.skipMemoryCache(true);
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.logo);
            requestOptions.error(R.drawable.logo);

            Glide.with(PetsActivity.this)
                    .load(picture)
                    .apply(requestOptions)
                    .into(mPicture);

            switch (gender) {
                case GENDER_MALE:
                    mGenderSpinner.setSelection(1);
                    break;
                case GENDER_FEMALE:
                    mGenderSpinner.setSelection(2);
                    break;
                default:
                    mGenderSpinner.setSelection(0);
                    break;
            }
        }
    }

    private void setupSpinner(){
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender_options, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = GENDER_FEMALE;
                    } else {
                        mGender = GENDER_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0;
            }
        });
    }


    void readMode(){

        mName.setFocusableInTouchMode(false);
        mSpecies.setFocusableInTouchMode(false);
        mBreed.setFocusableInTouchMode(false);
        mName.setFocusable(false);
        mSpecies.setFocusable(false);
        mBreed.setFocusable(false);
        mPid.setFocusableInTouchMode(false);
        mRiwayat.setFocusableInTouchMode(false);
        mKesehatan.setFocusableInTouchMode(false);
        mVaksinasi.setFocusableInTouchMode(false);
        mPengobatan.setFocusableInTouchMode(false);
        mPid.setFocusable(false);
        mRiwayat.setFocusable(false);
        mKesehatan.setFocusable(false);
        mVaksinasi.setFocusable(false);
        mPengobatan.setFocusable(false);

        mGenderSpinner.setEnabled(false);
        mBirth.setEnabled(false);

    }

    private void getUser(){

        mService.getUser(uid)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()){
                            User user = response.body();

                            if (user.getSuccess().equals("1"))
                            {

                                nama_pemilik = user.getName();
                                email_pemilik = user.getEmail();
                                id_pemilik = user.getId();
                                picture1 = user.getPicture();
                                address_pemilik = user.getAddress();
                                phone_pemilik = user.getPhone();

                                mNama.setText(nama_pemilik);
                                mEmail.setText(email_pemilik);
                                mAddress.setText(address_pemilik);
                                mPhone.setText(phone_pemilik);

                                mNama.setFocusableInTouchMode(false);
                                mEmail.setFocusableInTouchMode(false);
                                mAddress.setFocusableInTouchMode(false);
                                mPhone.setFocusableInTouchMode(false);

                                mNama.setFocusable(false);
                                mEmail.setFocusable(false);
                                mAddress.setFocusable(false);
                                mPhone.setFocusable(false);

                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.skipMemoryCache(true);
                                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                                requestOptions.placeholder(R.drawable.logo);
                                requestOptions.error(R.drawable.logo);

                                Glide.with(PetsActivity.this)
                                        .load(picture1)
                                        .apply(requestOptions)
                                        .into(mPicture1);

                            } else {
                                String message = user.getMessage();
                                Toast.makeText(PetsActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}
