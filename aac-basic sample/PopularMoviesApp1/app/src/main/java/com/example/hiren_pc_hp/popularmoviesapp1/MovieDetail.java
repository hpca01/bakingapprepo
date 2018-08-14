package com.example.hiren_pc_hp.popularmoviesapp1;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hiren_pc_hp.popularmoviesapp1.data.Result;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetail extends AppCompatActivity {
    @BindView(R.id.movie_original_title)
    TextView title;
    @BindView(R.id.detail_image)
    ImageView poster_detail_view;
    @BindView(R.id.overview)
    TextView overview;
    @BindView(R.id.ratingBar)
    RatingBar movieRating;
    @BindView(R.id.release_date)
    TextView releaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Result object = getIntent().getParcelableExtra("movieOb");
        populateUi(object);

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork!=null && activeNetwork.isConnected();
    }

    private void populateUi(final Result ob) {
        if(!isNetworkAvailable()){
            AlertDialog.Builder build = new AlertDialog.Builder(this);
            build.setTitle("No Network Connectivity")
                    .setMessage("Please enable network connectivity")
                    .setPositiveButton("Enabled", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            populateUi(ob);
                        }
                    }).create().show();
        }
        title.setText(ob.getOriginalTitle());
        Picasso.with(this).load("http://image.tmdb.org/t/p/w500/"+ob.getPosterPath())
                .into(poster_detail_view);
        overview.setText(ob.getOverview());
        releaseDate.setText(ob.getReleaseDate());
        movieRating.setNumStars(5);
        Double ratingStars = ob.getVoteAverage()/2.0;
        movieRating.setRating(ratingStars.floatValue());
    }
}
