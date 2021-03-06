package com.example.hiren_pc_hp.bakingapp.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.hiren_pc_hp.bakingapp.MainActivity;
import com.example.hiren_pc_hp.bakingapp.R;
import com.example.hiren_pc_hp.bakingapp.data.RecipeViewModel;
import com.example.hiren_pc_hp.bakingapp.network.Step;
import com.example.hiren_pc_hp.bakingapp.ui.UtilsForUi;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    final AtomicBoolean playBackState = new AtomicBoolean();

    Unbinder unbinder;

    UtilsForUi uiUtils;

    void getViewData(){
        ViewModelProviders.of(getActivity()).get(RecipeViewModel.class).getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                allSteps.addAll(steps);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootview = inflater.inflate(R.layout.fragment_step, container, false);
        unbinder = ButterKnife.bind(this, rootview);

        if(savedInstanceState!= null){
            Log.d(TAG, "onCreateView: resuming from instancestate return ");
            Step step = savedInstanceState.getParcelable(getString(R.string.step_url));
            videoUrl=step.getVideoURL();
            aStep = step;
            currentPos = savedInstanceState.getInt(getString(R.string.step_id));
            playBackState.set(savedInstanceState.getBoolean(getString(R.string.playback_state)));
            videoPosition= savedInstanceState.getLong(getString(R.string.current_position));
            getViewData();
            Log.d(TAG, "onCreateView: playback state "+playBackState.get());
            Log.d(TAG, "onCreateView: videoposition "+videoPosition);
        } else {
            Log.d(TAG, "onCreateView: resuming from normal state");
            Bundle args = getArguments();
            if (args != null) {
                aStep = args.getParcelable(getString(R.string.step_url));
                getViewData();
                //Step default videourl, for the first step, in case view model takes too long to load
                videoUrl = aStep.getVideoURL();
                currentPos = aStep.getId();
            }
        }
        return rootview;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: called saved instance is "+(savedInstanceState!=null));
    }

    @Override
    public void onStart() {
        uiUtils = (MainActivity)getActivity();
        super.onStart();
        if (Util.SDK_INT > 23) {
            getViewData();
            Log.d(TAG, "onStart: onstart called");
            //todo log these vars to see which bundle is coming through
            //videoPosition=bundle.getLong(getString(R.string.current_position));
            //layBackState.set(bundle.getBoolean(getString(R.string.playback_state)));
            //currentPos = bundle.getInt(getString(R.string.step_id));
            //Log.d(TAG, String.format("onStart: %d, %s, %d", videoPosition, playBackState.get(), currentPos));
            //refreshStep(currentPos);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mPlayer == null) {
            Log.d(TAG, "onResume: onResume called ");
            //onstart takes care of the re-initializing the player
            if(getArguments()!=null){

                Log.d(TAG, "onResume:  "+getArguments().toString());
                refreshStep(currentPos);
            }
        }
    }

    void loadImageIfExists(){
        if(!TextUtils.isEmpty(aStep.getThumbnailURL())){
            //non-empty image  url
            Picasso.with(getActivity()).load(aStep.getThumbnailURL()).into(recipeImage);
        }
    }

    void initPlayer(){
        //only execute if null
        if (mPlayer != null) {
            //do nothing if not null
            Log.d(TAG, "initPlayer: player is  not null ");
            return;
        }
        if(TextUtils.isEmpty(aStep.getVideoURL())){
            return;
        }
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
                Util.getUserAgent(getContext(), getString(R.string.app_name)),
                null);


        Uri videoUri = Uri.parse(videoUrl);

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(videoUri);

        mPlayer.prepare(videoSource);
        mPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                playBackState.set(playWhenReady);
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });
        if (videoPosition != 0) {
            mPlayer.seekTo(videoPosition);
            boolean play;
            if (play = playBackState.get()) {
                mPlayer.setPlayWhenReady(play);
            }
            videoPosition = 0;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: called ");
        super.onSaveInstanceState(outState);
        outState.putBoolean(getString(R.string.playback_state), playBackState.get());
        outState.putInt(getString(R.string.step_id), currentPos);
        outState.putParcelable(getString(R.string.step_url), aStep);

        if(mPlayerView.getPlayer()!= null){
            long currentPosition=mPlayerView.getPlayer().getCurrentPosition();
            outState.putLong(getString(R.string.current_position), currentPosition);
            Log.d(TAG, "onSaveInstanceState: called video Position "+mPlayerView.getPlayer().getCurrentPosition());
        }
        uiUtils.postStepsState(outState);
        Log.d(TAG, "onSaveInstanceState: called playback state "+playBackState.get());


    }

    public void refreshStep(int positionToGo){
        if(!allSteps.isEmpty()){
            aStep = allSteps.get(positionToGo);
            Log.d(TAG, "refreshStep: is full");
        }
        videoUrl = aStep.getVideoURL();
        if(mPlayer!=null){
            //release player between switching of views
            releasePlayer();
        }
        if(this.videoUrl.isEmpty()){
            //put up image
            mPlayerView.setVisibility(GONE);
            imageView.setVisibility(View.VISIBLE);
            initPlayer();
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
        if(Util.SDK_INT <= 23) {
            Log.d(TAG, "onPause: called");
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            Log.d(TAG, "onStop: called");
            releasePlayer();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @OnClick(value = R.id.next_button)
    public void nextClick(){
        currentPos+=1;
        Toast.makeText(getActivity(), "Next current pos "+currentPos, Toast.LENGTH_LONG).show();
        if(allSteps.size()+1>currentPos) {//is current pos greater than the size of the steps?
            refreshStep(currentPos);
        }
    }

    void releasePlayer(){
        if(mPlayer!=null){
            mPlayer.stop();
            mPlayer.release();
            mPlayer=null;
            playBackState.set(false);
            videoPosition=0;
        }
    }

    @OnClick(value = R.id.prev_button)
    public void prevClick(){
        currentPos-=1;
        Toast.makeText(getActivity(), "Prev current pos "+currentPos, Toast.LENGTH_LONG).show();
        if(currentPos>=0){//is currentpos greater than 0?
            refreshStep(currentPos);
        }
    }
    public void setValues(Step step, int pos, long videoPos, boolean playState){
        aStep = step;
        currentPos = pos;
        videoPosition = videoPos;
        playBackState.set(playState);
    }
}
