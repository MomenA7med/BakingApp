package com.example.momen.baking_app;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.momen.baking_app.fragments.DetailFragmet;
import com.example.momen.baking_app.model.Ingredients;
import com.example.momen.baking_app.model.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements DetailFragmet.VideoClick ,ExoPlayer.EventListener {
    private static boolean mTwoPane;
    List<Ingredients> ingredientList;
    List<Steps> stepsList;
    Gson gson;
    ImageView noVideo;
    TextView textView;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder playCallbackBuilder;
    private long positionPlayer;
    private boolean playWhenReady;
    String url_string,desc,thumbnail;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String stepString = getIntent().getStringExtra("STEPS");
        String ingreString = getIntent().getStringExtra("INGRES");
        String recipeName = getIntent().getStringExtra("NAME");

        gson = new Gson();

        ingredientList = gson.fromJson(stepString,
                new TypeToken<List<Ingredients>>() {
                }.getType());

        stepsList = gson.fromJson(stepString,
                new TypeToken<List<Steps>>() {
                }.getType());

        DetailFragmet detailFragmet = new DetailFragmet();
        detailFragmet.setIngredientList(ingreString);
        detailFragmet.setStepsList(stepString);
        detailFragmet.setRecipeName(recipeName);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.detail_frame, detailFragmet).commit();

        if (findViewById(R.id.video_layout) != null) {
            mTwoPane = true;
            noVideo = findViewById(R.id.placeholder_no_video_image);
            simpleExoPlayerView = findViewById(R.id.video_view_recipe);
            textView = findViewById(R.id.step_description_text_view);
            if (savedInstanceState != null){
                url_string = savedInstanceState.getString("url");
                thumbnail = savedInstanceState.getString("thum");
                desc = savedInstanceState.getString("desc");
                positionPlayer = savedInstanceState.getLong("POSITION");
                playWhenReady = savedInstanceState.getBoolean("PLAY_WHEN_READY");
                if (url_string != null){
                    if (url_string.equals("")){
                        simpleExoPlayerView.setVisibility(View.GONE);
                        noVideo.setVisibility(View.VISIBLE);
                        if (!thumbnail.equals("")) {
                            Picasso.with(this).load(thumbnail).into(noVideo);
                        }
                    }else {
                        simpleExoPlayerView.setVisibility(View.VISIBLE);
                        noVideo.setVisibility(View.GONE);
                        uri = Uri.parse(url_string);
                        intializeMedia();
                        intailizePlayer(uri);
                    }
                }
                textView.setText(desc);
            }
        } else
            mTwoPane = false;
    }

    public static boolean getNoPane() {
        return mTwoPane;
    }

    private void intializeMedia() {
        mediaSessionCompat = new MediaSessionCompat(this, VideoActivity.class.getSimpleName());
        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSessionCompat.setMediaButtonReceiver(null);
        playCallbackBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
        );
        mediaSessionCompat.setPlaybackState(playCallbackBuilder.build());
        mediaSessionCompat.setCallback(new SessionCallBacks());
        mediaSessionCompat.setActive(true);
    }

    private void intailizePlayer(Uri uri) {

        if (simpleExoPlayer == null) {
            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, selector, control);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri,
                    new DefaultDataSourceFactory(this, userAgent),
                    new DefaultExtractorsFactory(), null, null, null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }

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

        if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
            playCallbackBuilder.setState(PlaybackStateCompat.STATE_PLAYING, simpleExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            playCallbackBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    simpleExoPlayer.getCurrentPosition(), 1f);
        }
        mediaSessionCompat.setPlaybackState(playCallbackBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    private class SessionCallBacks extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            simpleExoPlayer.seekTo(0);
        }
    }

    @Override
    public void onClicked(int position) {
        if (!mTwoPane) {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("URL", stepsList.get(position).getVideoURL());
            intent.putExtra("DESC", stepsList.get(position).getDesc());
            intent.putExtra("THUMBNAIL", stepsList.get(position).getThumbnailURL());
            startActivity(intent);
        }else {
            url_string = stepsList.get(position).getVideoURL();
            desc = stepsList.get(position).getDesc();
            thumbnail = stepsList.get(position).getThumbnailURL();
            if (url_string != null){
                if (url_string.equals("")){
                    simpleExoPlayerView.setVisibility(View.GONE);
                    noVideo.setVisibility(View.VISIBLE);
                    if (!thumbnail.equals("")) {
                        Picasso.with(this).load(thumbnail).into(noVideo);
                    }
                }else {
                    simpleExoPlayerView.setVisibility(View.VISIBLE);
                    noVideo.setVisibility(View.GONE);
                    uri = Uri.parse(url_string);
                    intializeMedia();
                    intailizePlayer(uri);
                }
            }
            textView.setText(desc);
        }

    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTwoPane) {
            if (simpleExoPlayer != null){
                playWhenReady = simpleExoPlayer.getPlayWhenReady();
                positionPlayer = simpleExoPlayer.getCurrentPosition();
                releasePlayer();
                simpleExoPlayer = null;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mTwoPane) {
            if (simpleExoPlayer != null) {
                simpleExoPlayer.setPlayWhenReady(playWhenReady);
                simpleExoPlayer.seekTo(positionPlayer);
            } else {
                intializeMedia();
                intailizePlayer(uri);
                simpleExoPlayer.setPlayWhenReady(playWhenReady);
                simpleExoPlayer.seekTo(positionPlayer);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTwoPane) {
            releasePlayer();
            mediaSessionCompat.setActive(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mTwoPane){
            outState.putString("url",url_string);
            outState.putString("thum",thumbnail);
            outState.putString("desc",desc);
            outState.putLong("POSITION",positionPlayer);
            outState.putBoolean("PLAY_WHEN_READY",playWhenReady);
        }
    }
}