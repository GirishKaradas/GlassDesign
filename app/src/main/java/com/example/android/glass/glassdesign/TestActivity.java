package com.example.android.glass.glassdesign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.glass.glassdesign.data.DataMCQ;
import com.example.android.glass.glassdesign.fragments.BaseFragment;
import com.example.android.glass.glassdesign.fragments.TestFragment;
import com.example.android.glass.glassdesign.menu.MenuActivity;
import com.example.glass.ui.GlassGestureDetector;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestActivity extends BaseActivity {

    private List<BaseFragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout indicator;
    public static ArrayList<DataMCQ> arrayList = new ArrayList<>();
    private TextView tvPage;

    private final String ACCESS_TOKEN="ds7P8ue7X0PmwaS-pSYAIZqbCZ1ZhrwO4iASYCmsTJY";
    private final String SPACE_ID= "h7copa94aofe";

    private static final int REQUEST_CODE = 701;
    private String MENU_KEY="menu_key";
    private DatabaseReference reference, ref1;
    public static ArrayList<Integer> responses = new ArrayList<>();
    private Boolean FLAG = false;
    private TextView tvResponse;

    final ScreenSlidePagerAdapter screenSliderPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        viewPager = findViewById(R.id.activity_test_viewpager);
        indicator = findViewById(R.id.activity_test_indicator);
        tvPage = findViewById(R.id.activity_test_tvPage);
        tvResponse = findViewById(R.id.activity_test_tvResponse);
        arrayList = new ArrayList<>();
        responses = new ArrayList<>(Arrays.asList(4,4,4,4,4));
        indicator.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(screenSliderPagerAdapter);

        reference = FirebaseDatabase.getInstance().getReference().child("skilltest");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                DataMCQ dataMCQ = snapshot.getValue(DataMCQ.class);
                arrayList.add(dataMCQ);
                fragments.add(TestFragment.newInstance(dataMCQ.getKey(), dataMCQ.getQuestion(), dataMCQ.getAns(), dataMCQ.getAnsID(), 4));
                screenSliderPagerAdapter.notifyDataSetChanged();
                viewPager.setAdapter(screenSliderPagerAdapter);
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

        indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tvPage.setText((indicator.getSelectedTabPosition()+1) + " of "+ indicator.getTabCount());
                switch (responses.get(indicator.getSelectedTabPosition())){

                    case 0:
                        tvResponse.setText("Selected answer: A");
                        break;
                    case 1:
                        tvResponse.setText("Selected answer: B");
                        break;
                    case 2:
                        tvResponse.setText("Selected answer: C");
                        break;
                    case 3:
                        tvResponse.setText("Selected answer: D");
                        break;
                    case 4:
                        tvResponse.setText("Not Answered");
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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
                Intent intent = new Intent(TestActivity.this, MenuActivity.class);
                intent.putExtra(MENU_KEY, R.menu.menu_test);
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

            int response = 4;

            switch (id) {
                case R.id.bOptionA:
                    responses.set(indicator.getSelectedTabPosition(), 0);
                    response = 0;
                    break;
                case R.id.bOptionB:
                    responses.set(indicator.getSelectedTabPosition(), 1);
                    response = 1;
                    break;
                case R.id.bOptionC:
                    responses.set(indicator.getSelectedTabPosition(), 2);
                    response = 2;
                    break;
                case R.id.bOptionD:
                    responses.set(indicator.getSelectedTabPosition(), 3);
                    response = 3;
                    break;
            }
            indicator.getTabAt(indicator.getSelectedTabPosition()).setIcon(getResources().getDrawable(R.drawable.ic_accept_round));
            indicator.getTabAt(indicator.getSelectedTabPosition()).getIcon().setColorFilter(getResources().getColor(R.color.color_normal), PorterDuff.Mode.SRC_IN);

            DataMCQ dataMCQ = arrayList.get(indicator.getSelectedTabPosition());
            fragments.set(indicator.getSelectedTabPosition(), TestFragment.newInstance(dataMCQ.getKey(), dataMCQ.getQuestion(), dataMCQ.getAns(), dataMCQ.getAnsID(), response));
            screenSliderPagerAdapter.notifyDataSetChanged();


            Log.e("Arraylist: ", responses.toString());
            for (int i=0; i< arrayList.size(); i++){
                if (responses.get(i) != 4){
                    FLAG = true;
                }else {
                    FLAG = false;
                    break;
                }
            }
            if (FLAG){
                startActivity(new Intent(TestActivity.this, ResultActivity.class));
                finish();
            }
        }
    }
}