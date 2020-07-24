package Fragment;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polymusic.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import adapter.YoutubeAdapter;
import api.APIKey;
import model.Video;

public class MVFragment extends Fragment {
    public static View background;
    RecyclerView rvVideo;
    TextView tvTrending;
    ProgressBar progressBar;

    public MVFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mv, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ánh xạ
        background = view.findViewById(R.id.background);
        background.setVisibility(View.INVISIBLE);
        rvVideo = view.findViewById(R.id.rvVideo);
        tvTrending = view.findViewById(R.id.tvTrending);
        progressBar = view.findViewById(R.id.spin_kit);
        new ParseVideoYoutube().execute();

        //LinearGradient textview
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.parseColor("#EE0979"), Color.parseColor("#FF6A00"),
                Shader.TileMode.CLAMP);
        tvTrending.getPaint().setShader(myShader);

    }

    //Setup recyclerView
    private void setupRecyclerView(ArrayList<Video> listVideo) {
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity(), 2);
        rvVideo.setHasFixedSize(true);
        rvVideo.setLayoutManager(manager);
        rvVideo.setItemAnimator(new DefaultItemAnimator());
        try {
            int resId = R.anim.layout_animation_up_to_down;
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
            YoutubeAdapter adapter = new YoutubeAdapter(getActivity(), listVideo);
            rvVideo.setLayoutAnimation(animation);
            rvVideo.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        background.setVisibility(View.INVISIBLE);
    }

    //AsyncTask parse Json
    private class ParseVideoYoutube extends AsyncTask<Void, Void, ArrayList<Video>> {
        @SuppressLint("WrongThread")
        @Override
        protected ArrayList<Video> doInBackground(Void... params) {
            ArrayList<Video> listVideo = new ArrayList<>();
            URL jSonUrl;
            URLConnection jSonConnect;
            try {
                jSonUrl = new URL(APIKey.API_URI + APIKey.PLAYLIST_ID + "&key=" + APIKey.KEY_BROWSE);
                jSonConnect = jSonUrl.openConnection();
                InputStream inputstream = jSonConnect.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"), 8);
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }
                inputstream.close();
                String jSontxt = stringBuilder.toString();

                JSONObject jsonobject = new JSONObject(jSontxt);
                JSONArray allItem = jsonobject.getJSONArray("items");
                for (int i = 0; i < allItem.length(); i++) {
                    JSONObject item = allItem.getJSONObject(i);
                    JSONObject snippet = item.getJSONObject("snippet");
                    String title = snippet.getString("title");// Get Title Video
                    String decription = snippet.getString("description");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");//Get Url Thumnail
                    JSONObject thumnailsIMG = thumbnails.getJSONObject("medium");
                    String thumnailurl = thumnailsIMG.getString("url");

                    JSONObject resourceId = snippet.getJSONObject("resourceId");    //Get ID Video
                    String videoId = resourceId.getString("videoId");

                    Video video = new Video();
                    video.setTitle(title);
                    video.setDecription(decription);
                    video.setThumnail(thumnailurl);
                    video.setUrlVideo(videoId);
                    //Add video to List
                    listVideo.add(video);
                }
                if (listVideo.size() != 0) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listVideo;
        }

        @Override
        protected void onPostExecute(ArrayList<Video> videos) {
            super.onPostExecute(videos);
            setupRecyclerView(videos);
        }
    }
}