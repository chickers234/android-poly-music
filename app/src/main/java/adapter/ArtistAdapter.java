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
import com.example.polymusic.SongBySinger;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.Artist;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.RecyclerViewHolder> {
    Activity context;
    List<Artist> data = new ArrayList<>();

    public ArtistAdapter(Activity context, List<Artist> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public ArtistAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(R.layout.artist_item, parent, false);
        return new ArtistAdapter.RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ArtistAdapter.RecyclerViewHolder holder, final int position) {
        final Artist artist = this.data.get(position);

        try {
            Picasso.with(context)
                    .load(artist.getUrl())
                    .placeholder(R.drawable.ic_music)
                    .into(holder.imgArtist);
        } catch (Exception e) {
        }

        holder.tvNameArtist.setText(artist.getName());

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SongBySinger.class);
                i.putExtra("singer", artist.getName());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgArtist;
        TextView tvNameArtist;
        LinearLayout item;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imgArtist = itemView.findViewById(R.id.imgArtist);
            tvNameArtist = itemView.findViewById(R.id.tvNameArtist);
            item = itemView.findViewById(R.id.item);
        }
    }
}

