package com.example.hiren_pc_hp.popularmoviesapp1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.hiren_pc_hp.popularmoviesapp1.data.Movie;
import com.example.hiren_pc_hp.popularmoviesapp1.utils.ApiUtils;
import com.example.hiren_pc_hp.popularmoviesapp1.utils.MovieService;
import com.example.hiren_pc_hp.popularmoviesapp1.utils.MoviesResponseAdapter;
import com.example.hiren_pc_hp.popularmoviesapp1.data.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //menu switch to denote whether to display popular movies or highest rated
    private Boolean menuSwitch = false; //true = highest rated, false = popular
    private static final String OUTPUT_TAG = "MovieService Return";
    private MovieService mService;
    private RecyclerView mRecyclerView;
    private MoviesResponseAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Result> callbackres;
    private boolean networkconnected;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(menuSwitch==false){
            menu.findItem(R.id.popularMoviesMenuItem).setVisible(false);
            menu.findItem(R.id.highestRatedMoviesMenuItem).setVisible(true);
        }else{
            menu.findItem(R.id.highestRatedMoviesMenuItem).setVisible(false);
            menu.findItem(R.id.popularMoviesMenuItem).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.popularMoviesMenuItem:
                menuSwitch = false;
                populateMovies();
                return true;
            case R.id.highestRatedMoviesMenuItem:
                menuSwitch=true;
                populateMovies();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mService = ApiUtils.getMovieService();



        final Intent intent = new Intent(this, MovieDetail.class);

        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mLayoutManager = new GridLayoutManager(this, 2);
        mAdapter = new MoviesResponseAdapter(this, new ArrayList<Movie.Result>(0), new MoviesResponseAdapter.PostItemListener() {
            @Override
            public void onPostClick(Movie.Result item) {
                intent.putExtra("movieOb", item);
                startActivity(intent);
            }


                    });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        populateMovies();



    }
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork!=null && activeNetwork.isConnected();
    }


    private void populateMovies() {
        networkconnected = isNetworkAvailable();
        if(networkconnected==false){
            AlertDialog.Builder build = new AlertDialog.Builder(this);

            build.setTitle("No Network Connectivity")
                    .setMessage("Please enable network connectivity")
                    .setPositiveButton("Enabled", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            populateMovies();
                        }
                    }).create().show();
        }

        Callback<Movie> responseObject = new Callback<Movie>(){
            @Override
            public void onResponse(Call<Movie> call, Response <Movie> response) {

                Log.d("output", response.body().toString());
                mAdapter.updatePost(response.body().getResults());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }


        };
        if(menuSwitch==false) {
            mService.getPopularMovies().enqueue(responseObject);
        }
        else {
            mService.getHighestMovies().enqueue(responseObject);
        }
    }
}
