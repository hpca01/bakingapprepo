package com.example.hiren_pc_hp.popularmoviesapp1.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hiren_pc_hp.popularmoviesapp1.R;
import com.example.hiren_pc_hp.popularmoviesapp1.data.Movie;
import com.example.hiren_pc_hp.popularmoviesapp1.data.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MoviesResponseAdapter extends RecyclerView.Adapter<MoviesResponseAdapter.ViewHolder>{
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updatePost(List<Movie.Result> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private Movie.Result getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(Movie.Result item);
    }

    private List<Movie.Result> mItems;
    private Context mContext;
    private PostItemListener mItemListener;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imgView;
        PostItemListener mItemListener;

        public ViewHolder(View itemView, PostItemListener postItemListener) {
            super(itemView);
            imgView = (ImageView) itemView.findViewById(R.id.posterImage);

            this.mItemListener = postItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Movie.Result item = getItem(getAdapterPosition());
            this.mItemListener.onPostClick(item);

            notifyDataSetChanged();
        }
    }

    public MoviesResponseAdapter(Context context, List<Movie.Result> results, PostItemListener itemListener) {
        mItems = results;
        mContext = context;
        mItemListener = itemListener;
    }

    @NonNull
    @Override
    public MoviesResponseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int rvlayout = R.layout.rv_layout;

        View postView = inflater.inflate(rvlayout, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView, this.mItemListener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesResponseAdapter.ViewHolder holder, int position) {
        Movie.Result item = mItems.get(position);
        ImageView poster = holder.imgView;
        Picasso.with(poster.getContext())
                .load("http://image.tmdb.org/t/p/w500/"+item.getPosterPath()).into(poster);
    }


}