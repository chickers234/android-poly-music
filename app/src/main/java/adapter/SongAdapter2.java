package adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.polymusic.R;
import com.example.polymusic.SongDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.Song;

public class SongAdapter2 extends RecyclerView.Adapter<SongAdapter2.RecyclerViewHolder> {
    Activity context;
    List<Song> data = new ArrayList<>();

    public SongAdapter2(Activity context, List<Song> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public SongAdapter2.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(R.layout.song_item, parent, false);
        return new SongAdapter2.RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final SongAdapter2.RecyclerViewHolder holder, final int position) {
        Song song = this.data.get(position);

        try {
            Picasso.with(context)
                    //.load("http://192.168.1.157:3000/" + song.getPoster())
                    .load("https://poly-music.herokuapp.com/" + song.getPoster())
                    .placeholder(R.drawable.ic_music)
                    .into(holder.imgPoster);
        } catch (Exception e) {

        }

        holder.tvTitle.setText(song.getTitle());
        holder.tv_Singer.setText(song.getSinger());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SongDetail.class);
                intent.putExtra("id", data.get(position).get_id());
                intent.putExtra("title", data.get(position).getTitle());
                intent.putExtra("singer", data.get(position).getSinger());
                intent.putExtra("author", data.get(position).getAuthor());
                intent.putExtra("category", data.get(position).getCategory());
                intent.putExtra("poster", data.get(position).getPoster());
                intent.putExtra("song", data.get(position).getSong());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView tvTitle, tv_Singer;
        LinearLayout item;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tv_Singer = itemView.findViewById(R.id.tvSinger);
            item = itemView.findViewById(R.id.item);
        }
    }
}

