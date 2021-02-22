package com.example.android.glass.glassdesign;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.android.glass.glassdesign.data.DataLyo;
import com.example.android.glass.glassdesign.fragments.BaseFragment;
import com.example.android.glass.glassdesign.fragments.MainLayoutFragment;
import com.example.glass.ui.GlassGestureDetector;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class LyoManualActivity extends Base2Activity {

    private List<BaseFragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout indicator;
    private ArrayList<DataLyo> arrayList = new ArrayList<>();

    private final String ACCESS_TOKEN="ds7P8ue7X0PmwaS-pSYAIZqbCZ1ZhrwO4iASYCmsTJY";
    private final String SPACE_ID= "h7copa94aofe";

    private static final int REQUEST_CODE = 301;
    private String MENU_KEY="menu_key";

    final ScreenSlidePagerAdapter screenSliderPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lyo_manual);

        viewPager = findViewById(R.id.activity_lyo_viewpager);
        indicator = findViewById(R.id.activity_lyo_indicator);
        arrayList = new ArrayList<>();
        indicator.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(screenSliderPagerAdapter);

        ManualActivity.FLAG5 = false;
        ManualActivity.FLAG8 = false;

        if (fragments.size()==0){
            fragments.add(MainLayoutFragment.newInstance(getString(R.string.batch_manual), "", "", R.drawable.baseline_class_24 , null));
            fragments.add(MainLayoutFragment.newInstance(getString(R.string.defrost_manual), "", "", R.drawable.baseline_class_24 , null));
            fragments.add(MainLayoutFragment.newInstance(getString(R.string.skill_test), "", "", R.drawable.baseline_content_paste_24 , null));
        }
        screenSliderPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case TAP:
                int id = viewPager.getCurrentItem();
                switch (id){
                    case 0:
                        Intent intent = new Intent(this.getApplicationContext(), ManualActivity.class);
                        intent.putExtra("manual_type", "batch");
                        startActivity(intent);
                        Toast.makeText(this, "Opening " + getString(R.string.batch_manual), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Intent intent1 = new Intent(this.getApplicationContext(), ManualActivity.class);
                        intent1.putExtra("manual_type", "defrost");
                        startActivity(intent1);
                        Toast.makeText(this, "Opening " + getString(R.string.defrost_manual), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Intent intent2 = new Intent(this.getApplicationContext(), TestActivity.class);
                        startActivity(intent2);
                        Toast.makeText(this, "Opening " + getString(R.string.skill_test), Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            default:
                return super.onGesture(gesture);
        }
    }
/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            final int id = data.getIntExtra(MenuActivity.EXTRA_MENU_ITEM_ID_KEY,
                    MenuActivity.EXTRA_MENU_ITEM_DEFAULT_VALUE);
            switch (id) {
                case R.id.bBatchManual:
                    Intent intent = new Intent(this.getApplicationContext(), ManualActivity.class);
                    intent.putExtra("manual_type", "batch");
                    startActivity(intent);
                    break;
                case R.id.bDefrostManual:
                    Intent intent1 = new Intent(this.getApplicationContext(), ManualActivity.class);
                    intent1.putExtra("manual_type", "defrost");
                    startActivity(intent1);
                    break;
                case R.id.bReport:
                    startActivity(new Intent(this.getApplicationContext(), AgoraActivity.class));
                    break;
            }
        }
    }


 */

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

}