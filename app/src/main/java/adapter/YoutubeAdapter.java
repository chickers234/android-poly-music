package adapter;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.polymusic.DialogYoutube;
import com.example.polymusic.PlayVideoYoutube;
import com.example.polymusic.R;

import java.util.ArrayList;

import Fragment.MVFragment;
import model.Video;

public class YoutubeAdapter extends RecyclerView.Adapter<YoutubeAdapter.VideoHolder> {
    private Activity activity;
    private ArrayList<Video> listVideo;

    public YoutubeAdapter(Activity activity, ArrayList<Video> listVideo) {
        this.activity = activity;
        this.listVideo = listVideo;
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
        final Video video = listVideo.get(position);
        holder.tvTitle.setText(video.getTitle());
        Glide.with(activity)
                .load(video.getThumnail())
                .centerCrop()
                .into(holder.imgThumnail);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MVFragment.background.setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("Video", video);
                Intent intent = new Intent(activity, DialogYoutube.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Video", video);
                Intent intent = new Intent(activity, PlayVideoYoutube.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listVideo.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder {
        ImageView imgThumnail;
        TextView tvTitle;

        public VideoHolder(View itemView) {
            super(itemView);
            imgThumnail = itemView.findViewById(R.id.img_thumnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
