package Fragment;


import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polymusic.R;

import java.util.ArrayList;
import java.util.List;

import DAO.FavouriteDAO;
import adapter.SongAdapter2;
import model.Song;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {
    RecyclerView rvFavourite;
    SongAdapter2 songAdapter2;
    TextView tvFavourite;


    public ChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //anh xa
        rvFavourite = view.findViewById(R.id.rvFavourite);
        tvFavourite = view.findViewById(R.id.tvFavourite);

        getData();
        //LinearGradient textview
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.parseColor("#EE0979"), Color.parseColor("#FF6A00"),
                Shader.TileMode.CLAMP);
        tvFavourite.getPaint().setShader(myShader);

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        List<Song> list = new ArrayList<>();
        for (int i = 0; i < FavouriteDAO.favouriteList.size(); i++) {
            list.add(0, FavouriteDAO.favouriteList.get(i));
        }

        //animation
        int resId = R.anim.layout_animation_down_to_up;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
        rvFavourite.setLayoutAnimation(animation);

        songAdapter2 = new SongAdapter2(getActivity(), list);
        rvFavourite.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rvFavourite.setAdapter(songAdapter2);

    }
}
