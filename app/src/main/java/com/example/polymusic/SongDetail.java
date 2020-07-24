package com.example.polymusic;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;

import DAO.FavouriteDAO;
import api.API;
import api.RetrofitClient;
import model.Song;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static DAO.FavouriteDAO.database;

public class SongDetail extends AppCompatActivity {
    public static MediaPlayer mediaPlayer;
    public static TextView tvCurrentTime, tvTotalDuration;
    public static SeekBar seekBar;
    public static Handler handler = new Handler();
    public static Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            tvCurrentTime.setText(milliSecondstoTimer(currentDuration));
        }
    };
    static ImageView btnBack, disc, btnPlay, btnPause, btnPrev, btnNext, btnLike_false, btnLike_true, btnLoop_false, btnLoop_true, btnDownload;
    static Animation animation;
    static NotificationCompat.Builder builder;
    static NotificationManager notifManager;
    static String channelId = "Default Channel ID";
    static String channelDescription = "Default Channel";
    static String id, title, singer, author, category, poster, song;
    static String songUrl;
    static Notification noti;
    int STEP_VALUE = 10000;
    TextView tvTitle, tvSinger, tvAuthor, tvPlaying;
    Boolean loop = false;
    Intent intent;
    RetrofitClient retrofit = new RetrofitClient();

    public static void pause() {
        handler.removeCallbacks(updater);
        mediaPlayer.pause();
        disc.clearAnimation();
        btnPlay.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.INVISIBLE);
    }

    public static void play() {
        mediaPlayer.start();
        updateSeekBar();
        disc.startAnimation(animation);
        btnPlay.setVisibility(View.INVISIBLE);
        btnPause.setVisibility(View.VISIBLE);
    }

    public static void delete() {
        notifManager.cancelAll();
        seekBar.setProgress(0);
        mediaPlayer.reset();
        disc.clearAnimation();
        btnPlay.setVisibility(View.VISIBLE);
        btnPause.setVisibility(View.INVISIBLE);
        prepareMediaPlayer();
    }

    public static Bitmap getImageBitmapFromURL(final Context context, final String imageUrl) {
        Bitmap imageBitmap = null;
        try {
            imageBitmap = new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(Void... params) {
                    try {
                        int targetHeight = 200;
                        int targetWidth = 200;

                        return Picasso.with(context).load(String.valueOf(imageUrl))
                                //.resize(targetWidth, targetHeight)
                                .get();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return imageBitmap;
    }

    private static void updateSeekBar() {
        if (mediaPlayer.isPlaying()) {
            seekBar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            handler.postDelayed(updater, 1000);
        }
    }

    private static String milliSecondstoTimer(long milliSeconds) {
        String timerString = "";
        String secondsString;

        int hours = (int) (milliSeconds / (1000 * 60 * 60));
        int minutes = (int) (milliSeconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliSeconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            timerString = hours + ":";
        }
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        timerString = timerString + minutes + ":" + secondsString;

        return timerString;
    }

    private static void prepareMediaPlayer() {
        //songUrl = "http://192.168.1.157:3000/" + song;
        songUrl = "https://poly-music.herokuapp.com/" + song;
        try {
            mediaPlayer.setDataSource(songUrl);
            mediaPlayer.prepare();
            tvTotalDuration.setText(milliSecondstoTimer(mediaPlayer.getDuration()));
            disc.startAnimation(animation);
        } catch (Exception e) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(SongDetail.this, R.color.dark));

        final FavouriteDAO favouriteDAO = new FavouriteDAO(SongDetail.this);

        //getIntent
        Intent intent = getIntent();
        try {
            id = intent.getStringExtra("id");
            title = intent.getStringExtra("title");
            singer = intent.getStringExtra("singer");
            author = intent.getStringExtra("author");
            category = intent.getStringExtra("category");
            poster = intent.getStringExtra("poster");
            song = intent.getStringExtra("song");
        } catch (Exception e) {

        }


        //anh xa
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        btnLike_false = findViewById(R.id.btnLike_false);
        btnLike_true = findViewById(R.id.btnLike_true);
        btnLoop_false = findViewById(R.id.btnLoop_false);
        btnLoop_true = findViewById(R.id.btnLoop_true);
        seekBar = findViewById(R.id.seekBar);
        tvTitle = findViewById(R.id.tvTitle);
        tvSinger = findViewById(R.id.tvSinger);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvTotalDuration = findViewById(R.id.tvTotalDuration);
        mediaPlayer = new MediaPlayer();
        btnBack = findViewById(R.id.btnBack);
        disc = findViewById(R.id.dianhac);
        btnDownload = findViewById(R.id.btnDownload);
        tvPlaying = findViewById(R.id.tvPlaying);
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.parseColor("#EE0979"), Color.parseColor("#FF6A00"),
                Shader.TileMode.CLAMP);
        tvPlaying.getPaint().setShader(myShader);

        //animation disc
        animation = AnimationUtils.loadAnimation(SongDetail.this, R.anim.rotate);
        try {
            Picasso.with(SongDetail.this)
                    //.load("http://192.168.1.157:3000/" + poster)
                    .load("https://poly-music.herokuapp.com/" + poster)
                    .placeholder(R.drawable.ic_disc)
                    .into(disc);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        tvTitle.setText(title);
        tvAuthor.setText(author);
        tvSinger.setText(singer);

        seekBar.setMax(100);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareMediaPlayer();
                Notification();
            }

        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.start();
                updateSeekBar();
            }

        }, 2000);

        //event
        btnPlay.setVisibility(View.INVISIBLE);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();
                updateSeekBar();
                disc.startAnimation(animation);
                btnPlay.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(updater);
                mediaPlayer.pause();
                disc.clearAnimation();
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.INVISIBLE);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seekto = mediaPlayer.getCurrentPosition() - STEP_VALUE;
                if (seekto < 0)
                    seekto = 0;
                mediaPlayer.pause();
                mediaPlayer.seekTo(seekto);
                mediaPlayer.start();
                updateSeekBar();
                btnPlay.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int seekto = mediaPlayer.getCurrentPosition() + STEP_VALUE;
                if (seekto > mediaPlayer.getDuration())
                    seekto = mediaPlayer.getDuration();
                mediaPlayer.pause();
                mediaPlayer.seekTo(seekto);
                mediaPlayer.start();
                updateSeekBar();
                btnPlay.setVisibility(View.INVISIBLE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });

        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                SeekBar seekBar = (SeekBar) v;
                int playPosition = (mediaPlayer.getDuration() / 100) * seekBar.getProgress();
                mediaPlayer.seekTo(playPosition);
                tvCurrentTime.setText(milliSecondstoTimer(mediaPlayer.getCurrentPosition()));
                return false;
            }
        });

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                seekBar.setSecondaryProgress(percent);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar.setProgress(0);
                tvCurrentTime.setText("0:00");
                tvTotalDuration.setText("0:00");
                mediaPlayer.reset();
                prepareMediaPlayer();
                disc.clearAnimation();
                btnPlay.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.INVISIBLE);

                if (loop == true) {
                    mediaPlayer.start();
                    updateSeekBar();
                    disc.startAnimation(animation);
                    btnPlay.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
                }
            }
        });

        //sự kiện
        checkFavourite();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnLoop_true.setVisibility(View.INVISIBLE);

        btnLoop_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoop_false.setVisibility(View.INVISIBLE);
                btnLoop_true.setVisibility(View.VISIBLE);
                loop = true;
            }
        });

        btnLoop_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLoop_false.setVisibility(View.VISIBLE);
                btnLoop_true.setVisibility(View.INVISIBLE);
                loop = false;
            }
        });

        btnLike_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues row = new ContentValues();
                row.put("_id", id);
                row.put("title", title);
                row.put("singer", singer);
                row.put("author", author);
                row.put("category", category);
                row.put("poster", poster);
                row.put("song", song);
                database.insert("Favourite", null, row);
                favouriteDAO.docCSDL();
                checkFavourite();
                Toast.makeText(SongDetail.this, "Add to Favourite List!", Toast.LENGTH_SHORT).show();
            }
        });

        btnLike_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.delete("Favourite", "_id = ?", new String[]{id});
                favouriteDAO.docCSDL();
                checkFavourite();
            }
        });

        //download song
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(SongDetail.this);
                dialog.setContentView(R.layout.download_dialog);

                ImageView imgPoster = dialog.findViewById(R.id.poster);
                TextView tvTitle = dialog.findViewById(R.id.tvTitle);
                TextView tvSinger = dialog.findViewById(R.id.tvSinger);
                LinearLayout btn128 = dialog.findViewById(R.id.btn128);

                Picasso.with(SongDetail.this).load("https://poly-music.herokuapp.com/" + poster).into(imgPoster);
                tvTitle.setText(title);
                tvSinger.setText(singer);

                btn128.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        download();
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    public void Notification() {
        //action pause music
        Intent intentAction1 = new Intent(SongDetail.this, MusicReceiver.class);
        intentAction1.putExtra("action", "pause");
        PendingIntent pIntent_1 = PendingIntent.getBroadcast(SongDetail.this, 1, intentAction1, PendingIntent.FLAG_UPDATE_CURRENT);

        //action play music
        Intent intentAction2 = new Intent(SongDetail.this, MusicReceiver.class);
        intentAction2.putExtra("action", "play");
        PendingIntent pIntent_2 = PendingIntent.getBroadcast(SongDetail.this, 2, intentAction2, PendingIntent.FLAG_UPDATE_CURRENT);

        //action stop music
        Intent intentAction3 = new Intent(SongDetail.this, MusicReceiver.class);
        intentAction3.putExtra("action", "stop");
        PendingIntent pIntent_3 = PendingIntent.getBroadcast(SongDetail.this, 3, intentAction3, PendingIntent.FLAG_UPDATE_CURRENT);

        MediaSessionCompat mediaSession = new MediaSessionCompat(getApplicationContext(), "session tag");
        MediaSessionCompat.Token token = mediaSession.getSessionToken();

        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notifManager.getNotificationChannel(channelId);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                notificationChannel = new NotificationChannel(channelId, channelDescription, importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notifManager.createNotificationChannel(notificationChannel);
            }
            builder = new NotificationCompat.Builder(SongDetail.this, channelId);
            //intent = new Intent(SongDetail.this, SongDetail.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            // use System.currentTimeMillis() to have a unique ID for the pending intent
            try {
                PendingIntent pIntent = PendingIntent.getActivity(SongDetail.this, (int) System.currentTimeMillis(), intent, 0);
                builder.setContentTitle(title)
                        .setStyle(
                                new androidx.media.app.NotificationCompat.MediaStyle()
                                        .setShowActionsInCompactView(0, 1, 2)
                                        .setMediaSession(token))
                        .setLargeIcon(getImageBitmapFromURL(SongDetail.this, "https://poly-music.herokuapp.com/" + poster))
                        .setSmallIcon(R.drawable.ic_logo)
                        .setContentText(singer)
                        //.setAutoCancel(true)
                        .setColor(getResources().getColor(R.color.colorAccent))
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setOnlyAlertOnce(true)
                        .setShowWhen(false)
                        .setContentIntent(pIntent)
                        .addAction(R.drawable.pause, "Pause", pIntent_1)
                        .addAction(R.drawable.play, "Play", pIntent_2)
                        .addAction(R.drawable.close, "Close", pIntent_3);
            } catch (Exception e) {

            }

        } else {
            builder = new NotificationCompat.Builder(SongDetail.this, channelId);
            builder.setContentTitle(title)
                    .setStyle(
                            new androidx.media.app.NotificationCompat.MediaStyle()
                                    .setMediaSession(token)
                                    .setShowActionsInCompactView(0, 1, 2))
                    .setLargeIcon(getImageBitmapFromURL(SongDetail.this, "https://poly-music.herokuapp.com/" + poster))
                    .setSmallIcon(R.drawable.ic_logo)
                    .setColor(getResources().getColor(R.color.black))
                    .setContentText(singer)
                    //.setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .addAction(R.drawable.pause, "Pause", pIntent_1)
                    .addAction(R.drawable.play, "Play", pIntent_2)
                    .addAction(R.drawable.close, "Close", pIntent_3);
        }

        try {
            noti = builder.build();
            notifManager.notify(0, noti);
        } catch (Exception e) {

        }

    }

    private void checkFavourite() {
        boolean check = false;
        for (Song song : FavouriteDAO.favouriteList) {
            if (song.get_id().equals(id)) {
                check = true;
                break;
            }
        }
        if (check == true) {
            btnLike_false.setVisibility(View.INVISIBLE);
            btnLike_true.setVisibility(View.VISIBLE);
        } else {
            btnLike_false.setVisibility(View.VISIBLE);
            btnLike_true.setVisibility(View.INVISIBLE);
        }
    }

    public void download() {
        API api = retrofit.getClient().create(API.class);

        String url = "http://poly-music.herokuapp.com/" + song;
        api.downloadFileWithDynamicUrlAsync(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                            Log.d("file download ", writtenToDisk + "");

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    if (notifManager == null) {
                                        notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    }
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        NotificationChannel notificationChannel = notifManager.getNotificationChannel(channelId);
                                        if (notificationChannel == null) {
                                            int importance = NotificationManager.IMPORTANCE_HIGH; //Set the importance level
                                            notificationChannel = new NotificationChannel(channelId, channelDescription, importance);
                                            notificationChannel.setLightColor(Color.GREEN); //Set if it is necesssary
                                            notificationChannel.enableVibration(true); //Set if it is necesssary
                                            notifManager.createNotificationChannel(notificationChannel);
                                        }
                                        builder = new NotificationCompat.Builder(SongDetail.this, channelId);

                                        // use System.currentTimeMillis() to have a unique ID for the pending intent
                                        PendingIntent pIntent = PendingIntent.getActivity(SongDetail.this, (int) System.currentTimeMillis(), intent, 0);
                                        builder.setContentTitle("Poly Music")                            // required
                                                .setSmallIcon(R.drawable.ic_music)   // required
                                                .setContentText("Download successful!") // required
                                                .setDefaults(Notification.DEFAULT_ALL)
                                                .setAutoCancel(true)
                                                .setContentIntent(pIntent);
                                    } else {
                                        builder = new NotificationCompat.Builder(SongDetail.this, channelId);

                                        // use System.currentTimeMillis() to have a unique ID for the pending intent
                                        PendingIntent pIntent = PendingIntent.getActivity(SongDetail.this, (int) System.currentTimeMillis(), intent, 0);

                                        // use System.currentTimeMillis() to have a unique ID for the pending intent
                                        builder.setContentTitle("Poly Music")                            // required
                                                .setSmallIcon(R.drawable.ic_music)   // required
                                                .setContentText("Download complete!") // required
                                                .setDefaults(Notification.DEFAULT_ALL)
                                                .setAutoCancel(true)
                                                .setContentIntent(pIntent);
                                    }

                                    noti = builder.build();
                                    notifManager.notify(1, noti);

                                }
                            });
                            return null;
                        }
                    }.execute();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + song);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("file download ", fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
