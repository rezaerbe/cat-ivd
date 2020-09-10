package app.com.kucingapps;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import app.com.kucingapps.Adapter.Adapter;
import app.com.kucingapps.Adapter.CategoryAdapter;
import app.com.kucingapps.Model.Article;
import app.com.kucingapps.Model.Category;
import app.com.kucingapps.Model.Pets;
import app.com.kucingapps.Retrofit.ApiInterface;
import app.com.kucingapps.Utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CategoryAdapter adapter;
    private List<Category> categoryList;
    CategoryAdapter.cRecyclerViewClickListener listener;
    ProgressBar progressBar;
    ApiInterface mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mService = Common.getAPI();

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listener = new CategoryAdapter.cRecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(CategoryActivity.this, ArticleActivity.class);
                intent.putExtra("id", categoryList.get(position).getId());
                intent.putExtra("name", categoryList.get(position).getName());
                intent.putExtra("picture", categoryList.get(position).getPicture());
                startActivity(intent);

            }
        };

    }

    public void getCategory(){

        Call<List<Category>> call = mService.getCategory();
        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                progressBar.setVisibility(View.GONE);
                categoryList = response.body();
                Log.i(CategoryActivity.class.getSimpleName(), response.body().toString());
                adapter = new CategoryAdapter(categoryList, CategoryActivity.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(CategoryActivity.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCategory();
    }
}
