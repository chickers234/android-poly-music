package com.example.polymusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MusicReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        if (action.equals("pause")) {
            SongDetail.pause();
        } else if (action.equals("play")) {
            SongDetail.play();
        } else if (action.equals("stop")) {
            SongDetail.delete();
        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

}