package com.example.polymusic;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import api.APIKey;
import model.Video;

public class DialogYoutube extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener  {
    YouTubePlayerView youTubeView;
    private String VIDEO_ID;
    public static  boolean isDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_youtube);
        WindowManager.LayoutParams a = getWindow().getAttributes();
        a.dimAmount = 0;
        getWindow().setAttributes(a);

        isDialog = false;

        youTubeView = findViewById(R.id.youtube_view);
        youTubeView.initialize(APIKey.YOUTUBE_APP_KEY, DialogYoutube.this);
        Bundle bundle = getIntent().getExtras();
        Video video = (Video) bundle.getSerializable("Video");
        VIDEO_ID = video.getUrlVideo();

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            youTubePlayer.setShowFullscreenButton(true);
            youTubePlayer.cueVideo(VIDEO_ID);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        String error = "Không thể load video! Kiểm tra Internet và ứng dụng Youtube trên máy của bạn!";
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isDialog = true;
    }
}
