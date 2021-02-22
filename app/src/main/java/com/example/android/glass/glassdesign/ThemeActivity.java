    package com.example.android.glass.glassdesign;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.glass.glassdesign.data.DataMCQ;
import com.example.android.glass.glassdesign.fragments.BaseFragment;
import com.example.android.glass.glassdesign.fragments.ThemeFragment;
import com.example.glass.ui.GlassGestureDetector;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ThemeActivity extends BaseActivity {

    private List<BaseFragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout indicator;
    public static ArrayList<DataMCQ> arrayList = new ArrayList<>();
    private TextView tvPage;

    private static final int REQUEST_CODE = 601;
    private String MENU_KEY="menu_key";

    final ScreenSlidePagerAdapter screenSliderPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_theme);

        viewPager = findViewById(R.id.activity_theme_viewpager);
        indicator = findViewById(R.id.activity_theme_indicator);

        fragments.add(ThemeFragment.newInstance("Black Theme", 1));
        fragments.add(ThemeFragment.newInstance("White Theme", 2));
        screenSliderPagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(screenSliderPagerAdapter);
        indicator.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(screenSliderPagerAdapter);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {

        switch (gesture){
            case TAP:
            /*    Intent intent = new Intent(ThemeActivity.this, MenuActivity.class);
                intent.putExtra(MENU_KEY, R.menu.menu_test);
                startActivityForResult(intent, REQUEST_CODE );
                break;

             */
                SharedPreferences sharedPreferences = getSharedPreferences("glass_prefs", Context.MODE_PRIVATE);

                if (sharedPreferences == null){
                    SharedPreferences.Editor editor= sharedPreferences.edit();
                    editor.putInt("theme_code", 1);
                    editor.commit();
                    editor.apply();
                }else {
                    switch (indicator.getSelectedTabPosition()){
                        case  0:
                            SharedPreferences.Editor editor= sharedPreferences.edit();
                            editor.putInt("theme_code", 1);
                            editor.commit();
                            editor.apply();
                            Toast.makeText(this, "black Is selected", Toast.LENGTH_SHORT).show();
                            break;

                        case 1:
                            SharedPreferences.Editor editor1= sharedPreferences.edit();
                            editor1.putInt("theme_code", 2);
                            editor1.commit();
                            editor1.apply();
                            // this.setTheme(R.style.AppThemeWhite);
                            Toast.makeText(this, "white Is selected", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

        }

        return super.onGesture(gesture);
    }
}