package app.com.kucingapps;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
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

public class FrontActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PetsAdapter adapter;
    private List<Pets> petsListPemilik;
    private List<User> userOK;
    PetsAdapter.pRecyclerViewClickListener listener;
    ProgressBar progressBar;
    ApiInterface mService;

    TextView txt_nameok, txt_emailok;
    CircleImageView imageViewok;
    SessionManager sessionManager;

    String getId, getName, getEmail, getPicture, nama_user, email_user, picture_user, address_user, phone_user;

    Integer uid, idok, id_user;

    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        // apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        mService = Common.getAPI();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);
        getEmail = user.get(sessionManager.EMAIL);
        getName = user.get(sessionManager.NAME);
        getPicture = user.get(sessionManager.PICTURE);

        idok = Integer.parseInt(getId);

//        uid = Common.currentUser.getId();

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        listener = new PetsAdapter.pRecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(FrontActivity.this, EditorActivity.class);
                intent.putExtra("id", petsListPemilik.get(position).getId());
                intent.putExtra("name", petsListPemilik.get(position).getName());
                intent.putExtra("species", petsListPemilik.get(position).getSpecies());
                intent.putExtra("breed", petsListPemilik.get(position).getBreed());
                intent.putExtra("gender", petsListPemilik.get(position).getGender());
                intent.putExtra("picture", petsListPemilik.get(position).getPicture());
                intent.putExtra("birth", petsListPemilik.get(position).getBirth());
                intent.putExtra("pid", petsListPemilik.get(position).getPid());
                intent.putExtra("riwayat", petsListPemilik.get(position).getRiwayat());
                intent.putExtra("kesehatan", petsListPemilik.get(position).getKesehatan());
                intent.putExtra("vaksinasi", petsListPemilik.get(position).getVaksinasi());
                intent.putExtra("pengobatan", petsListPemilik.get(position).getPengobatan());
                startActivity(intent);

            }
        };

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ImageButton search = findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton cat = findViewById(R.id.btn_category);
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        ImageButton con = findViewById(R.id.btn_consult);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FrontActivity.this, ConsultActivity.class);
                startActivity(intent);
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        imageViewok = headerView.findViewById(R.id.imageView);
        txt_nameok = headerView.findViewById(R.id.txt_name);
        txt_emailok = headerView.findViewById(R.id.txt_email);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.front, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(FrontActivity.this, UserActivity.class);
            intent.putExtra("id", id_user);
            intent.putExtra("name", nama_user);
            intent.putExtra("email", email_user);
            intent.putExtra("picture", picture_user);
            intent.putExtra("address", address_user);
            intent.putExtra("phone", phone_user);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(FrontActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signout) {
            sessionManager.logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getPetsPemilikOK(){

        uid = Integer.parseInt(getId);

        Call<List<Pets>> call = mService.getPetsPemilik(uid);
        call.enqueue(new Callback<List<Pets>>() {
            @Override
            public void onResponse(Call<List<Pets>> call, Response<List<Pets>> response) {
                progressBar.setVisibility(View.GONE);
                petsListPemilik = response.body();
                Log.i(FrontActivity.class.getSimpleName(), response.body().toString());
                adapter = new PetsAdapter(petsListPemilik, FrontActivity.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Pets>> call, Throwable t) {
                Toast.makeText(FrontActivity.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void getUserData(){
//        txt_nameok.setText(getName);
//        txt_emailok.setText(getEmail);
//
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.skipMemoryCache(true);
//        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
//        requestOptions.placeholder(R.drawable.logo);
//        requestOptions.error(R.drawable.logo);
//
//        Glide.with(FrontActivity.this)
//                .load(getPicture)
//                .apply(requestOptions)
//                .into(imageViewok);
//    }

    private void getUser(){

        mService.getUser(idok)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful()){
                            User user = response.body();

                            if (user.getSuccess().equals("1"))
                            {

                                nama_user = user.getName();
                                email_user = user.getEmail();
                                id_user = user.getId();
                                picture_user = user.getPicture();
                                address_user = user.getAddress();
                                phone_user = user.getPhone();

                                txt_nameok.setText(nama_user);
                                txt_emailok.setText(email_user);

                                RequestOptions requestOptions = new RequestOptions();
                                requestOptions.skipMemoryCache(true);
                                requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
                                requestOptions.placeholder(R.drawable.logo);
                                requestOptions.error(R.drawable.logo);

                                Glide.with(FrontActivity.this)
                                        .load(picture_user)
                                        .apply(requestOptions)
                                        .into(imageViewok);

                            } else {
                                String message = user.getMessage();
                                Toast.makeText(FrontActivity.this, message, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();
        getUser();
        getPetsPemilikOK();
    }
}
