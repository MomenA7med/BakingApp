package com.example.momen.baking_app;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.RequiresApi;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

public class VideoActivity extends AppCompatActivity implements ExoPlayer.EventListener{

    String url_string,desc,thumbnail;
    private SimpleExoPlayer simpleExoPlayer;
    private SimpleExoPlayerView simpleExoPlayerView;
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder playCallbackBuilder;
    private long positionPlayer;
    private boolean playWhenReady;
    private ImageView noVideo;
    private TextView textView;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        if (savedInstanceState != null)
        {
            positionPlayer = savedInstanceState.getLong("POSITION");
            playWhenReady = savedInstanceState.getBoolean("PLAY_WHEN_READY");
        }
        url_string = getIntent().getStringExtra("URL");
        desc = getIntent().getStringExtra("DESC");
        thumbnail = getIntent().getStringExtra("THUMBNAIL");
        noVideo = findViewById(R.id.placeholder_no_video_image);
        simpleExoPlayerView = findViewById(R.id.video_view_recipe);
        textView = findViewById(R.id.step_description_text_view);
        if (url_string != null){
            if (url_string.equals("")){
                simpleExoPlayerView.setVisibility(View.GONE);
                noVideo.setVisibility(View.VISIBLE);
                if (!thumbnail.equals("")) {
                    Picasso.with(this).load(thumbnail).into(noVideo);
                }
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    textView.setText(desc);
                } else {
                    hideUI();
                    simpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    simpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                }
            }else {
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                noVideo.setVisibility(View.GONE);
                uri = Uri.parse(url_string);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    textView.setText(desc);
                } else {
                    hideUI();
                    simpleExoPlayerView.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    simpleExoPlayerView.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                }
                intializeMedia();
                intailizePlayer(uri);
            }
        }
    }
    private void intializeMedia(){
        mediaSessionCompat = new MediaSessionCompat(this,VideoActivity.class.getSimpleName());
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

        if (simpleExoPlayer == null){
            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this,selector,control);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            String userAgent = Util.getUserAgent(this,"BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(uri,
                    new DefaultDataSourceFactory(this,userAgent),
                    new DefaultExtractorsFactory(),null,null,null);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setPlayWhenReady(true);
        }
    }
    private void hideUI() {
        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().hide();
            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
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
        if (simpleExoPlayer != null){
            playWhenReady = simpleExoPlayer.getPlayWhenReady();
            positionPlayer = simpleExoPlayer.getCurrentPosition();
            releasePlayer();
            simpleExoPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("VIEW_PLAYER",simpleExoPlayerView.getVisibility());
        outState.putInt("VIEW_PIC",noVideo.getVisibility());
        outState.putLong("POSITION",positionPlayer);
        outState.putBoolean("PLAY_WHEN_READY",playWhenReady);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mediaSessionCompat.setActive(false);
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

        if (playbackState == ExoPlayer.STATE_READY && playWhenReady){
            playCallbackBuilder.setState(PlaybackStateCompat.STATE_PLAYING,simpleExoPlayer.getCurrentPosition(),1f);
        }else if (playbackState == ExoPlayer.STATE_READY) {
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
}
