package com.example.polymusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import api.API;
import api.RetrofitClient;
import model.Song;
import model.SongResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FetchDataActivity extends AppCompatActivity {
    public static List<Song> songList = new ArrayList<>();
    public static boolean admin;
    RetrofitClient retrofit = new RetrofitClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch_data);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(FetchDataActivity.this, R.color.black));

        Intent intent = getIntent();
        try {
            admin = intent.getBooleanExtra("admin", false);
        } catch (Exception e) {

        }

        getData();
    }

    void getData() {
        //final ProgressDialog loading = ProgressDialog.show(FetchDataActivity.this, "Fetching Data", "Please wait...", false, false);

        API api = retrofit.getClient().create(API.class);

        api.getSongs().enqueue(new Callback<SongResponse>() {
            @Override
            public void onResponse(Call<SongResponse> call, Response<SongResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().isStatus()) {
                        songList = response.body().getSongList();
                        if (songList != null) {
                            Intent intent = new Intent(FetchDataActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<SongResponse> call, Throwable t) {

            }
        });

//        api.deleteSong("5e8fa9b70abd2e3a482889b7").enqueue(new Callback<Song>() {
//            @Override
//            public void onResponse(Call<Song> call, Response<Song> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(FetchDataActivity.this, "xoa r", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Song> call, Throwable t) {
//
//            }
//        });


//        Song song = new Song();
//        song.setTitle("Test");
//        api.editSong("5e8fab6c846e4c4268bd0119", song).enqueue(new Callback<Song>() {
//            @Override
//            public void onResponse(Call<Song> call, Response<Song> response) {
//                Toast.makeText(FetchDataActivity.this, "đã update", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Call<Song> call, Throwable t) {
//
//            }
//        });
    }
}
