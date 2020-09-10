package app.com.kucingapps;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.com.kucingapps.Adapter.Adapter;
import app.com.kucingapps.Adapter.PetsAdapter;
import app.com.kucingapps.Model.Pets;
import app.com.kucingapps.Model.User;
import app.com.kucingapps.Retrofit.ApiClient;
import app.com.kucingapps.Retrofit.ApiInterface;
import app.com.kucingapps.Utils.Common;
import app.com.kucingapps.Utils.SessionManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PetsAdapter adapter;
    private List<Pets> petsListPemilik;
    PetsAdapter.pRecyclerViewClickListener listener;
    ProgressBar progressBar;
    ApiInterface mService;

    SessionManager sessionManager;

    String getId;

    Integer uid;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        // apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        mService = Common.getAPI();

        HashMap<String, String> user = sessionManager.getUserDetail();
        getId = user.get(sessionManager.ID);

//        uid = Common.currentUser.getId();

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        listener = new PetsAdapter.pRecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(DashboardActivity.this, EditorActivity.class);
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
                Intent intent = new Intent(DashboardActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ImageButton search = findViewById(R.id.btn_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageButton cat = findViewById(R.id.btn_category);
        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

        ImageButton con = findViewById(R.id.btn_consult);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, FrontActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {

            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void getPetsPemilik(){

        uid = Integer.parseInt(getId);

        Call<List<Pets>> call = mService.getPetsPemilik(uid);
        call.enqueue(new Callback<List<Pets>>() {
            @Override
            public void onResponse(Call<List<Pets>> call, Response<List<Pets>> response) {
                progressBar.setVisibility(View.GONE);
                petsListPemilik = response.body();
                Log.i(DashboardActivity.class.getSimpleName(), response.body().toString());
                adapter = new PetsAdapter(petsListPemilik, DashboardActivity.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Pets>> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPetsPemilik();
    }

}
