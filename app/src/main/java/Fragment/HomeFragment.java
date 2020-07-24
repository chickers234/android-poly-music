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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polymusic.FetchDataActivity;
import com.example.polymusic.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import adapter.ArtistAdapter;
import adapter.SliderAdapter;
import adapter.SongAdapter2;
import model.Artist;

public class HomeFragment extends Fragment {
    SliderView sliderView;
    RecyclerView rvSongs, rvArtist;
    SongAdapter2 songAdapter2;
    ArtistAdapter artistAdapter;
    TextView tvAllSongs, tvArtist, tvViewAll1, tvViewAll2;
    List<Artist> artistList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        artistList.add(new Artist("Bùi Anh Tuấn", "https://photo-zmp3.zadn.vn/thumb/94_94/avatars/4/1/41f229e867c3787ed1ddee90a4bd2bbb_1506073807.jpg"));
        artistList.add(new Artist("Hương Tràm", "https://photo-zmp3.zadn.vn/thumb/94_94/avatars/b/f/b/9/bfb9522fe78758e81dfcb4b70d3f8f52.jpg"));
        artistList.add(new Artist("Hòa Minzy", "https://photo-zmp3.zadn.vn/thumb/94_94/avatars/a/d/ad0cab0b6065c39e007a15ef67dcf2f3_1516243109.jpg"));
        artistList.add(new Artist("Hồ Ngọc Hà", "https://photo-zmp3.zadn.vn/thumb/94_94/avatars/7/a/7a002f1f15a5f4deed3bd6735428f35b_1477391700.jpg"));
        artistList.add(new Artist("Nguyễn Đình Vũ", "https://photo-zmp3.zadn.vn/thumb/94_94/avatars/4/5/a/b/45ab1296d73b215629fcbab092687d4c.jpg"));
        artistList.add(new Artist("Hà Anh Tuấn", "https://photo-zmp3.zadn.vn/thumb/94_94/avatars/2/4/24460d034acfc91cb5a32c7611316324_1451752817.jpg"));
        artistList.add(new Artist("Ưng Hoàng Phúc", "https://photo-zmp3.zadn.vn/thumb/94_94/avatars/c/6/d/1/c6d12d4113ade62f26be411ea9d0e07a.jpg"));

        //ánh xạ
        rvSongs = view.findViewById(R.id.rvSongs);
        rvArtist = view.findViewById(R.id.rvArtist);
        sliderView = view.findViewById(R.id.imgSlider);
        tvAllSongs = view.findViewById(R.id.tvAllSongs);
        tvArtist = view.findViewById(R.id.tvArtist);
        tvViewAll1 = view.findViewById(R.id.tvViewAll1);
        tvViewAll2 = view.findViewById(R.id.tvViewAll2);

        //custom slider
        SliderAdapter adapter = new SliderAdapter(getActivity());
        sliderView.setSliderAdapter(adapter);
        //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setIndicatorAnimation(IndicatorAnimations.FILL);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        //sliderView.setSliderTransformAnimation(SliderAnimations.CUBEOUTROTATIONTRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        //set scroll delay in seconds
        sliderView.setScrollTimeInSec(3);
        sliderView.startAutoCycle();

        //đổ dữ liệu lên recyclerview
        artistAdapter = new ArtistAdapter(getActivity(), artistList);
        //layout ngang
        rvArtist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvArtist.setAdapter(artistAdapter);

        int resId = R.anim.layout_animation_down_to_up;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), resId);

        songAdapter2 = new SongAdapter2(getActivity(), FetchDataActivity.songList);
        //gridlayout
        rvSongs.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rvSongs.setLayoutAnimation(animation);
        rvSongs.setAdapter(songAdapter2);

        //LinearGradient textview
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.parseColor("#EE0979"), Color.parseColor("#FF6A00"),
                Shader.TileMode.CLAMP);
        tvAllSongs.getPaint().setShader(myShader);
        tvArtist.getPaint().setShader(myShader);
        tvViewAll1.getPaint().setShader(myShader);
        tvViewAll2.getPaint().setShader(myShader);

    }

}
