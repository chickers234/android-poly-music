package DAO;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import model.Song;

import static android.content.Context.MODE_PRIVATE;

public class FavouriteDAO {
    public static String DATABASE_NAME = "musicDatabase.db";
    public static String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;
    public static List<Song> favouriteList = new ArrayList<>();
    Activity activity;

    public FavouriteDAO() {
    }

    public FavouriteDAO(Activity activity) {
        this.activity = activity;
    }

    public void xulySaoChepCSDl() {
        File dbFile = activity.getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                copyDataBaseFromAsset();
                Log.d("sqlite", "Sao chép thành công!");
            } catch (Exception e) {
                Log.d("sqlite", e.toString());
            }
        }
    }

    private void copyDataBaseFromAsset() {
        try {
            InputStream myInput = activity.getAssets().open(DATABASE_NAME);
            String outFileName = layDuongDanLuuTru();
            File f = new File(activity.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.e("Lỗi sao chép!", e.toString());
        }
    }

    private String layDuongDanLuuTru() {
        return activity.getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }

    public void docCSDL() {
        database = activity.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.query("Favourite", null, null, null, null, null, null);
        favouriteList.clear();
        while (cursor.moveToNext()) {
            String _id = cursor.getString(0);
            String title = cursor.getString(1);
            String singer = cursor.getString(2);
            String author = cursor.getString(3);
            String category = cursor.getString(4);
            String poster = cursor.getString(5);
            String song = cursor.getString(6);

            Song newSong = new Song();
            newSong.set_id(_id);
            newSong.setTitle(title);
            newSong.setSinger(singer);
            newSong.setAuthor(author);
            newSong.setCategory(category);
            newSong.setPoster(poster);
            newSong.setSong(song);
            favouriteList.add(newSong);
        }
        cursor.close();
    }
}
