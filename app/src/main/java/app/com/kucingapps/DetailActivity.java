package app.com.kucingapps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import app.com.kucingapps.Retrofit.ApiInterface;
import app.com.kucingapps.Utils.Common;

public class DetailActivity extends AppCompatActivity {

    private TextView mJudul, mArtikel;
    private ImageView mPicture;

    private String judul, artikel, picture;
    private int id;

    ApiInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mService = Common.getAPI();

        mJudul = findViewById(R.id.judul);
        mArtikel = findViewById(R.id.artikel);
        mPicture = findViewById(R.id.img_detail);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        judul = intent.getStringExtra("name");
        artikel = intent.getStringExtra("artikel");
        picture = intent.getStringExtra("picture");

        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {

        mJudul.setText(judul);
        mArtikel.setText(artikel);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);

        Glide.with(DetailActivity.this)
                .load(picture)
                .apply(requestOptions)
                .into(mPicture);

    }


}
