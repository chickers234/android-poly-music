package Fragment;


import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polymusic.R;

import java.util.ArrayList;
import java.util.List;

import adapter.CategoryAdapter;
import model.Category;

public class CategoryFragment extends Fragment {
    RecyclerView rvCategory;
    List<Category> categoryList = new ArrayList<>();
    TextView tvCategories;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2019/01/02/a/2/d/3/1546411716099.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2019/11/27/4/6/e/b/1574827148109.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2019/11/27/4/6/e/b/1574824820983.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2019/11/27/4/6/e/b/1574825808743.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2018/11/19/c/0/6/c/1542614343626.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2019/11/27/4/6/e/b/1574841142056.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2018/11/19/c/0/6/c/1542607757503.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2019/10/04/a/e/4/b/1570179279283.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2018/11/19/c/0/6/c/1542617437485.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2018/12/06/9/d/e/b/1544092654428.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2018/12/05/8/d/b/9/1543984650801.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2018/11/19/c/0/6/c/1542613850091.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2018/12/12/2/3/8/5/1544584228590.jpg"));
        categoryList.add(new Category("https://avatar-nct.nixcdn.com/topic/mobile/2018/11/19/c/0/6/c/1542616603210.jpg"));

        //anh xa
        tvCategories = view.findViewById(R.id.tvCategories);
        rvCategory = view.findViewById(R.id.rvCategory);

        //LinearGradient textview
        Shader myShader = new LinearGradient(
                0, 0, 0, 100,
                Color.parseColor("#EE0979"), Color.parseColor("#FF6A00"),
                Shader.TileMode.CLAMP);
        tvCategories.getPaint().setShader(myShader);

        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(),categoryList);
        rvCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        rvCategory.setAdapter(categoryAdapter);

    }

}
