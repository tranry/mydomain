package com.example.mydomain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {
    ViewPager mView;
    BottomNavigationView bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getSupportActionBar().hide();
        mView=findViewById(R.id.viewPager);
        bottom=findViewById(R.id.bottom_navigation);
        ViewPagerAdapter adapter= new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mView.setAdapter(adapter);
        mView.setOffscreenPageLimit(2);
        mView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position)
                {
                    case 0:
                        bottom.getMenu().findItem(R.id.navigation_store).setChecked(true);
                        break;
                    case 1:
                        bottom.getMenu().findItem(R.id.navigation_check_domain).setChecked(true);
                        break;
                    case 2:
                        bottom.getMenu().findItem(R.id.navigation_setting).setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_store:
                        mView.setCurrentItem(0);
                        break;
                    case R.id.navigation_check_domain:
                        mView.setCurrentItem(1);
                        break;
                    case R.id.navigation_setting:
                        mView.setCurrentItem(2);
                        break;
                }
                return true;
            }

        });
}
}