package com.example.android.glass.glassdesign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.glass.glassdesign.data.DataJob;
import com.example.android.glass.glassdesign.fragments.BaseFragment;
import com.example.android.glass.glassdesign.fragments.JobFragment;
import com.example.android.glass.glassdesign.menu.MenuActivity;
import com.example.glass.ui.GlassGestureDetector;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class JobActivity extends BaseActivity {

    private DatabaseReference reference;
    private ViewPager viewPager;
    private TabLayout indicator;
    private TextView textView;
    private ArrayList<DataJob> arrayList = new ArrayList<>();
    private static int REQUEST_CODE = 789;
    private String MENU_KEY="menu_key";
    private ImageView imageView;

    private List<BaseFragment> fragments = new ArrayList<>();
    final ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(
            getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);


        reference = FirebaseDatabase.getInstance().getReference().child("jobs");

        viewPager = findViewById(R.id.activity_job_viewpager);
        textView = findViewById(R.id.activity_job_tvCount);
        indicator = findViewById(R.id.activity_job_indicator);
        indicator.setupWithViewPager(viewPager, true);
        imageView = findViewById(R.id.activity_job_imageview);

        switch (SplashActivity.color_code){

            case 1:
                imageView.setBackground(getResources().getDrawable(R.drawable.back_circle_green));
                break;
            case 2:
                imageView.setBackground(getResources().getDrawable(R.drawable.back_circle_red));
                break;
            case 3:
                imageView.setBackground(getResources().getDrawable(R.drawable.back_circle_yellow));
                break;
            case 4:
                imageView.setBackground(getResources().getDrawable(R.drawable.back_circle_blue));
                break;
            case 5:
                imageView.setBackground(getResources().getDrawable(R.drawable.back_circle_orange));
                break;
            case 6:
                imageView.setBackground(getResources().getDrawable(R.drawable.back_circle_purple));
                break;

        }

        indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                textView.setText((indicator.getSelectedTabPosition()+1) + " of "+ indicator.getTabCount());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataJob dataJob = snapshot.getValue(DataJob.class);
                arrayList.add(dataJob);
                Log.e("Sample: ", dataJob.getTitle().toString());
                fragments.add(JobFragment.newInstance(dataJob.getKey(), dataJob.getTitle(), dataJob.getRecipe_title(), dataJob.getDesc(), dataJob.getStatus()));
                screenSlidePagerAdapter.notifyDataSetChanged();
                viewPager.setAdapter(screenSlidePagerAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
     /*   for (int i=0; i<arrayList.size(); i++){
            DataJob dataJob = arrayList.get(i);
            fragments.add(JobFragment.newInstance(dataJob.getKey(), dataJob.getTitle(), dataJob.getRecipe_title(), dataJob.getDesc(), dataJob.getStatus()));
            screenSlidePagerAdapter.notifyDataSetChanged();
            viewPager.setAdapter(screenSlidePagerAdapter);
        }

      */

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

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
                Intent intent = new Intent(JobActivity.this, MenuActivity.class);
                intent.putExtra(MENU_KEY, R.menu.menu_job);
                startActivityForResult(intent, REQUEST_CODE );
                break;
        }

        return super.onGesture(gesture);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            final int id = data.getIntExtra(MenuActivity.EXTRA_MENU_ITEM_ID_KEY,
                    MenuActivity.EXTRA_MENU_ITEM_DEFAULT_VALUE);
            DatabaseReference reference1 =reference.child(arrayList.get(indicator.getSelectedTabPosition()).getKey());
            Log.e("Sample: ", arrayList.get(indicator.getSelectedTabPosition()).getKey()+ "");

            switch (id) {
                case R.id.bMarkComplete:
                    reference1.child("status").setValue(true);
                    arrayList.get(indicator.getSelectedTabPosition()).setStatus(true);
                    screenSlidePagerAdapter.notifyDataSetChanged();
                    break;
                case R.id.bMarkUnComplete:
                    reference1.child("status").setValue(false);
                    arrayList.get(indicator.getSelectedTabPosition()).setStatus(false);
                    screenSlidePagerAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}