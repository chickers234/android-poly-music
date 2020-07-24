package com.example.polymusic;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import DAO.FavouriteDAO;
import Fragment.CategoryFragment;
import Fragment.ChartFragment;
import Fragment.HomeFragment;
import Fragment.MVFragment;
import Fragment.SearchFragment;


public class MainActivity extends AppCompatActivity {
    private static final int MAKE_PERMISSION_REQUEST_CODE = 1;
    AHBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window window = getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.dark));

        if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MAKE_PERMISSION_REQUEST_CODE);
        }

        //ánh xạ
        bottomNavigation = findViewById(R.id.bottom_navigation);

        // Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Trang chủ", R.drawable.ic_microphone, R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("MV", R.drawable.ic_mv, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Tìm kiếm", R.drawable.ic_search, R.color.white);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Yêu thích", R.drawable.ic_star, R.color.white);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Thể loại", R.drawable.ic_category, R.color.white);

        // Add items
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.addItem(item5);

        // Set background color
        bottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.black));

        // Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);

        // Change colors
        bottomNavigation.setAccentColor(getResources().getColor(R.color.colorAccent));
        bottomNavigation.setInactiveColor(getResources().getColor(R.color.gray));

        // Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

        //bottomNavigation.setTranslucentNavigationEnabled(true);

        // Manage titles
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        //bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

        // Use colored navigation with circle reveal effect
        bottomNavigation.setColored(false);

        // Set current item programmatically
        bottomNavigation.setCurrentItem(0);

        //default fragment
        if (DialogYoutube.isDialog) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new MVFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
        }

        //event
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                int id = position;

                if (id == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

                } else if (id == 1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MVFragment()).commit();

                } else if (id == 2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new SearchFragment()).commit();

                } else if (id == 3) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChartFragment()).commit();

                } else if (id == 4) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new CategoryFragment()).commit();

                }
                return true;
            }
        });

        FavouriteDAO favouriteDAO = new FavouriteDAO(MainActivity.this);
        favouriteDAO.xulySaoChepCSDl();
        favouriteDAO.docCSDL();
    }

    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_GRANTED;
    }
}


