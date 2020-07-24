package com.example.polymusic;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import api.APIKey;
import model.Video;

public class PlayVideoYoutube extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    YouTubePlayerView youTubeView;
    TextView txtName, txtDecription;
    ImageView btnBack;
    private String VIDEO_ID;
    private String TITLE_VIDEO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_youtube);
        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(PlayVideoYoutube.this, R.color.dark));

        btnBack = findViewById(R.id.btnBack);
        youTubeView = findViewById(R.id.youtube_view);
        txtName = findViewById(R.id.txtName);
        txtDecription = findViewById(R.id.txtDecription);
        youTubeView.initialize(APIKey.YOUTUBE_APP_KEY, PlayVideoYoutube.this);
        Bundle bundle = getIntent().getExtras();
        Video video = (Video) bundle.getSerializable("Video");
        TITLE_VIDEO = video.getTitle();
        txtName.setText(TITLE_VIDEO);
        txtDecription.setText(video.getDecription());
        VIDEO_ID = video.getUrlVideo();
        //Toast.makeText(this, VIDEO_ID, Toast.LENGTH_SHORT).show();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
}

