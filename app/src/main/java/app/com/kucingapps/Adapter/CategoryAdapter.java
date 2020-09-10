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

import app.com.kucingapps.Model.Category;
import app.com.kucingapps.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    public List<Category> categories;
    private Context context;
    private cRecyclerViewClickListener mListener;

    public CategoryAdapter(List<Category> categories, Context context, cRecyclerViewClickListener listener) {
        this.categories = categories;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        return new CategoryAdapter.MyViewHolder(view, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder holder, int position) {

        holder.mName.setText(categories.get(position).getName());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);

        Glide.with(context)
                .load(categories.get(position).getPicture())
                .apply(requestOptions)
                .into(holder.mPicture);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private cRecyclerViewClickListener mListener;
        private CircleImageView mPicture;
        private TextView mName;
        private RelativeLayout mCategoryContainer;

        public MyViewHolder(View itemView, cRecyclerViewClickListener listener) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.img_category);
            mName = itemView.findViewById(R.id.category_name);
            mCategoryContainer= itemView.findViewById(R.id.category_container);

            mListener = listener;
            mCategoryContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.category_container:
                    mListener.onRowClick(mCategoryContainer, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface cRecyclerViewClickListener {
        void onRowClick(View view, int position);
    }

}
