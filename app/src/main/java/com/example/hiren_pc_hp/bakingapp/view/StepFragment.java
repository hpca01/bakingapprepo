package com.example.hiren_pc_hp.bakingapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hiren_pc_hp.bakingapp.MainActivity;
import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.data.RecipeViewModel;
import com.example.hiren_pc_hp.bakingapp.network.Step;
import com.example.hiren_pc_hp.bakingapp.ui.UtilsForUi;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.GONE;


public class StepFragment extends Fragment {

    @BindView(R.id.step_video_player)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.imageView)
    TextView imageView;

    @BindView(R.id.textView)
    TextView textDescription;

    @BindView(R.id.prev_button)
    Button prevButton;

    @BindView(R.id.next_button)
    Button nextButton;

    @BindView(R.id.recipeImageView)
    ImageView recipeImage;

    private SimpleExoPlayer mPlayer;

    //all steps related vars
    ArrayList<Step> allSteps = new ArrayList<>();
    String videoUrl;
    Step aStep;
    int currentPos;
    long videoPosition = 0;

    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    void getViewData(){
        ViewModelProviders.of(getActivity()).get(RecipeViewModel.class).getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                allSteps.addAll(steps);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_step, container, false);
        unbinder = ButterKnife.bind(this, rootview);

        if(savedInstanceState!= null){
            Step step = savedInstanceState.getParcelable(getString(R.string.step_url));
            videoUrl=step.getVideoURL();
            aStep = step;
            currentPos = savedInstanceState.getInt(getString(R.string.step_id));
            videoPosition= savedInstanceState.getLong(getString(R.string.current_position));
            getViewData();

        } else {
            Bundle args = getArguments();
            if (args != null) {
                aStep = args.getParcelable(getString(R.string.step_url));
                getViewData();
                //Step default videourl, for the first step, incase viewmodel takes too long to load
                videoUrl = aStep.getVideoURL();
                currentPos = aStep.getId();
            }
        }

        if(videoUrl.isEmpty()){
            mPlayerView.setVisibility(GONE);
        }else{
            imageView.setVisibility(GONE);
            initPlayer();
        }

        textDescription.setText(aStep.getDescription());
        loadImageIfExists();

        return rootview;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    void loadImageIfExists(){
        if(!TextUtils.isEmpty(aStep.getThumbnailURL())){
            //non-empty image  url
            Picasso.with(getActivity()).load(aStep.getThumbnailURL()).into(recipeImage);
        }
    }

    void initPlayer(){
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        mPlayerView.setUseController(true);
        mPlayerView.requestFocus();

        mPlayerView.setPlayer(mPlayer);


        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(
                getActivity(),
                Util.getUserAgent(getContext(),getString(R.string.app_name)),
                null);


        Uri videoUri = Uri.parse(videoUrl);

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);

        mPlayer.prepare(videoSource);
        if(videoPosition!=0){
            mPlayer.seekTo(videoPosition);
            mPlayer.setPlayWhenReady(true);
            //reset positions = 0 for next rotation or next video
            videoPosition = 0;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        long currentPosition=mPlayerView.getPlayer().getCurrentPosition();

        outState.putLong(getString(R.string.current_position), currentPosition);
        outState.putInt(getString(R.string.step_id), currentPos);
        outState.putParcelable(getString(R.string.step_url), aStep);
    }

    public void refreshStep(int positionToGo){
        aStep = allSteps.get(positionToGo);
        videoUrl = aStep.getVideoURL();
        if(this.videoUrl.isEmpty()){
            //put up image
            mPlayerView.setVisibility(GONE);
            imageView.setVisibility(View.VISIBLE);
        }else{
            mPlayerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(GONE);
            initPlayer();
        }
        textDescription.setText(aStep.getDescription());
        loadImageIfExists();
    }
    @Override
    public void onPause() {
        super.onPause();
        if(mPlayer!=null){
            mPlayer.release();
            mPlayer=null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initPlayer();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @OnClick(value = R.id.next_button)
    public void nextClick(){
        currentPos+=1;
        if(allSteps.size()+1>currentPos) {//is currentpos greater than the size of the steps?
            refreshStep(currentPos);
        }
    }

    @OnClick(value = R.id.prev_button)
    public void prevClick(){
        currentPos-=1;
        if(currentPos>=0){//is currentpos greater than 0?
            refreshStep(currentPos);
        }
    }
}
