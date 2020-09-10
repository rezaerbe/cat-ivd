package app.com.kucingapps;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import app.com.kucingapps.Adapter.Adapter;
import app.com.kucingapps.Adapter.ArticleAdapter;
import app.com.kucingapps.Adapter.CategoryAdapter;
import app.com.kucingapps.Model.Article;
import app.com.kucingapps.Model.Category;
import app.com.kucingapps.Model.Pets;
import app.com.kucingapps.Retrofit.ApiInterface;
import app.com.kucingapps.Utils.Common;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArticleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArticleAdapter adapter;
    private List<Article> articleList;
    ArticleAdapter.aRecyclerViewClickListener listener;
    ProgressBar progressBar;
    ApiInterface mService;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        mService = Common.getAPI();

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        listener = new ArticleAdapter.aRecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, final int position) {

                Intent intent = new Intent(ArticleActivity.this, DetailActivity.class);
                intent.putExtra("id", articleList.get(position).getId());
                intent.putExtra("name", articleList.get(position).getJudul());
                intent.putExtra("artikel", articleList.get(position).getArtikel());
                intent.putExtra("picture", articleList.get(position).getPicture());
                startActivity(intent);

            }
        };

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

    }

    public void getArticle(){

        final String mid = id;

        Call<List<Article>> call = mService.getArticle(mid);
        call.enqueue(new Callback<List<Article>>() {
            @Override
            public void onResponse(Call<List<Article>> call, Response<List<Article>> response) {
                progressBar.setVisibility(View.GONE);
                articleList = response.body();
                Log.i(ArticleActivity.class.getSimpleName(), response.body().toString());
                adapter = new ArticleAdapter(articleList, ArticleActivity.this, listener);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Article>> call, Throwable t) {
                Toast.makeText(ArticleActivity.this, "rp :"+
                                t.getMessage().toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getArticle();
    }
}
