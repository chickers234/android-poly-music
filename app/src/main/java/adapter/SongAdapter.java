package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polymusic.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Song;

public class SongAdapter extends BaseAdapter {
    Activity context;
    int resource;
    List<Song> objects;

    public SongAdapter(Activity context, int resource, List<Song> objects) {
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = this.context.getLayoutInflater();
            convertView = inflater.inflate(this.resource, null);
            viewHolder.imgPoster = convertView.findViewById(R.id.imgPoster);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            viewHolder.tv_Singer = convertView.findViewById(R.id.tvSinger);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Song song = this.objects.get(position);

        try {
            //Picasso.with(context).load("http://192.168.1.157:3000/"+song.getPoster()).into(viewHolder.imgPoster);
            Picasso.with(context).load("https://poly-music.herokuapp.com/"+song.getPoster()).into(viewHolder.imgPoster);
        } catch (Exception e) {

        }

        viewHolder.tvTitle.setText(song.getTitle());
        viewHolder.tv_Singer.setText(song.getSinger());

        return convertView;
    }

    static class ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tv_Singer;
    }
}
