package adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.polymusic.FetchDataActivity;
import com.example.polymusic.R;
import com.example.polymusic.SongDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import api.API;
import api.RetrofitClient;
import model.Song;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongAdapter2 extends RecyclerView.Adapter<SongAdapter2.RecyclerViewHolder> {
    Activity context;
    List<Song> data = new ArrayList<>();
    RetrofitClient retrofit = new RetrofitClient();

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
        final Song song = this.data.get(position);

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
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FetchDataActivity.admin) {
                    final API api = retrofit.getClient().create(API.class);
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.edit_dialog);

                    final EditText edt_title = dialog.findViewById(R.id.edt_title);
                    final EditText edt_singer = dialog.findViewById(R.id.edt_singer);
                    Button btnHuy = dialog.findViewById(R.id.btnHuy);
                    Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);

                    edt_title.setText(song.getTitle());
                    edt_singer.setText(song.getSinger());

                    btnHuy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    btnXacNhan.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Song newSong = new Song(song.get_id(), edt_title.getText().toString(), edt_singer.getText().toString(), song.getAuthor(), song.getCategory(), song.getPoster(), song.getSong());
                            api.editSong(song.get_id(), newSong).enqueue(new Callback<Song>() {
                                @Override
                                public void onResponse(Call<Song> call, Response<Song> response) {
                                    if (response.isSuccessful()){
                                        Toast.makeText(context, "Update Successful!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Song> call, Throwable t) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    });


                    dialog.show();
                } else {
                    Toast.makeText(context, "You don't have permission to edit song!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        ImageView imgPoster, btnEdit;
        TextView tvTitle, tv_Singer;
        LinearLayout item;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tv_Singer = itemView.findViewById(R.id.tvSinger);
            item = itemView.findViewById(R.id.item);
        }
    }
}

