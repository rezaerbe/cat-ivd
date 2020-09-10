package app.com.kucingapps.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import app.com.kucingapps.Model.Pets;
import app.com.kucingapps.R;
import app.com.kucingapps.Utils.CustomFilter;
import de.hdodenhof.circleimageview.CircleImageView;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.MyViewHolder> {
    public List<Pets> petsPemilik;
    private Context context;
    private pRecyclerViewClickListener mListener;

    public PetsAdapter(List<Pets> petsPemilik, Context context, pRecyclerViewClickListener listener) {
        this.petsPemilik = petsPemilik;
        this.context = context;
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pets_item_layout, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.mName.setText(petsPemilik.get(position).getName());
        holder.mType.setText(petsPemilik.get(position).getBreed() + " / "
                + petsPemilik.get(position).getSpecies());
        holder.mDate.setText(petsPemilik.get(position).getBirth());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.skipMemoryCache(true);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        requestOptions.placeholder(R.drawable.logo);
        requestOptions.error(R.drawable.logo);

        Glide.with(context)
                .load(petsPemilik.get(position).getPicture())
                .apply(requestOptions)
                .into(holder.mPicture);

    }

    @Override
    public int getItemCount() {
        return petsPemilik.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private pRecyclerViewClickListener mListener;
        private CircleImageView mPicture;
        private TextView mName, mType, mDate;
        private RelativeLayout mPetsContainer;

        public MyViewHolder(View itemView, pRecyclerViewClickListener listener) {
            super(itemView);
            mPicture = itemView.findViewById(R.id.picture);
            mName = itemView.findViewById(R.id.name);
            mType = itemView.findViewById(R.id.type);
            mDate = itemView.findViewById(R.id.date);
            mPetsContainer = itemView.findViewById(R.id.pets_container);

            mListener = listener;
            mPetsContainer.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pets_container:
                    mListener.onRowClick(mPetsContainer, getAdapterPosition());
                    break;
                default:
                    break;
            }
        }
    }

    public interface pRecyclerViewClickListener {
        void onRowClick(View view, int position);
    }


}
