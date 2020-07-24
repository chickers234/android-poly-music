package Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polymusic.FetchDataActivity;
import com.example.polymusic.R;

import java.util.ArrayList;
import java.util.List;

import adapter.SongAdapter2;
import model.Song;

public class SearchFragment extends Fragment {
    RecyclerView rvSongs;
    SongAdapter2 songAdapter2;
    List<Song> songList;
    EditText edtSearch;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //ánh xạ
        rvSongs = view.findViewById(R.id.rvSongs);
        edtSearch = view.findViewById(R.id.edtSearch);

        //animation
        int resId = R.anim.layout_animation_right_to_left;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
        rvSongs.setLayoutAnimation(animation);

        songAdapter2 = new SongAdapter2(getActivity(), FetchDataActivity.songList);
        rvSongs.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rvSongs.setAdapter(songAdapter2);

        //sự kiện
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                songList = new ArrayList<>();
                if (s.length() == 0) {
                    songList = FetchDataActivity.songList;

                    //hide keyboard
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                    }, 500);

                } else {
                    for (Song song : FetchDataActivity.songList) {
                        if (song.getSinger().toLowerCase().contains(s.toString().toLowerCase())
                                || song.getTitle().toLowerCase().contains(s.toString().toLowerCase())
                                || song.getAuthor().toLowerCase().contains(s.toString().toLowerCase())
                                || song.getCategory().toLowerCase().contains(s.toString().toLowerCase())) {
                            songList.add(song);
                        }
                    }
                }

                //animation
                int resId = R.anim.layout_animation_up_to_down;
                LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                rvSongs.setLayoutAnimation(animation);

                songAdapter2 = new SongAdapter2(getActivity(), songList);
                rvSongs.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                rvSongs.setAdapter(songAdapter2);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

}
