package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.example.polymusic.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.RecyclerViewHolder> {
    Activity context;
    List<Category> data = new ArrayList<>();

    public CategoryAdapter(Activity context, List<Category> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public CategoryAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View view = inflater.inflate(R.layout.category_item, parent, false);
        return new CategoryAdapter.RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CategoryAdapter.RecyclerViewHolder holder, final int position) {
        Category category = this.data.get(position);

        try {
            Picasso.with(context).load(category.getUrl()).into(holder.imgCategory);
        } catch (Exception e) {
        }

        setAnimation(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position % 2 == 0) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.left_to_right);
            viewToAnimate.startAnimation(animation);
        } else if (position % 2 == 1) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.right_to_left);
            viewToAnimate.startAnimation(animation);
        }
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        LinearLayout itemCat;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            itemCat = itemView.findViewById(R.id.itemCat);
        }
    }
}

