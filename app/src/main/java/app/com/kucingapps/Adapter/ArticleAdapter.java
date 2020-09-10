package app.com.kucingapps.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import app.com.kucingapps.Model.Article;
import app.com.kucingapps.Model.Category;
import app.com.kucingapps.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
    public List<Article> articles;
    private Context context;
    private aRecyclerViewClickListener mListener;

    public ArticleAdapter(List<Article> articles, Context context, aRecyclerViewClickListener listener) {
        this.articles = articles;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public ArticleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item_layout, parent, false);
        return new ArticleAdapter.MyViewHolder(view, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final ArticleAdapter.MyViewHolder holder, int position) {

        holder.mName.setText(articles.get(position).getJudul());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);

        Glide.with(context)
                .load(articles.get(position).getPicture())
                .apply(requestOptions)
                .into(holder.mPicture);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private aRecyclerViewClickListener mListener;
        private CircleImageView mPicture;
        private TextView mName;
        private RelativeLayout mArticleContainer;

        public MyViewHolder(View itemView, aRecyclerViewClickListener listener) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.img_article);
            mName = itemView.findViewById(R.id.article_name);
            mArticleContainer = itemView.findViewById(R.id.article_container);

            mListener = listener;
            mArticleContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.article_container:
                    mListener.onRowClick(mArticleContainer, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface aRecyclerViewClickListener {
        void onRowClick(View view, int position);
    }

}
