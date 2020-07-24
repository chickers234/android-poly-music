package com.example.polymusic;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import adapter.SongAdapter3;
import api.API;
import api.RetrofitClient;
import model.Song;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongBySinger extends AppCompatActivity {
    String singer;
    TextView tvSinger, tvNoData;
    ImageView imgBack;
    RecyclerView rvSongs;
    SongAdapter3 songAdapter3;
    RetrofitClient retrofit = new RetrofitClient();
    API api;
    List<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_by_singer);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(SongBySinger.this, R.color.dark));

        //get Intent
        try {
            singer = getIntent().getStringExtra("singer");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //ánh xạ
        rvSongs = findViewById(R.id.rvSongs);
        tvSinger = findViewById(R.id.tvSinger);
        tvSinger.setText(singer);
        tvNoData = findViewById(R.id.tvNoData);
        tvNoData.setVisibility(View.INVISIBLE);
        imgBack = findViewById(R.id.imgBack);
        //LinearGradient textview
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.parseColor("#EE0979"), Color.parseColor("#FF6A00"),
                Shader.TileMode.CLAMP);
        tvSinger.getPaint().setShader(myShader);

        //sự kiện
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getData();

    }

    void getData() {
        api = retrofit.getClient().create(API.class);
        api.search(singer).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    songList = response.body();
                    try {
                        int resId = R.anim.layout_animation_up_to_down;
                        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(SongBySinger.this, resId);
                        songAdapter3 = new SongAdapter3(SongBySinger.this, songList);
                        rvSongs.setLayoutManager(new GridLayoutManager(SongBySinger.this, 2));
                        rvSongs.setLayoutAnimation(animation);
                        rvSongs.setAdapter(songAdapter3);
                    } catch (Exception e) {

                    }

                    if (songList.size() == 0) {
                        tvNoData.setVisibility(View.VISIBLE);
                    }

                    //Toast.makeText(SongBySinger.this, songList.size() + "", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });

    }
}
