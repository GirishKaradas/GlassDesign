package com.example.android.glass.glassdesign;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.glass.glassdesign.data.DataDQ;
import com.example.android.glass.glassdesign.data.DataIssue;
import com.example.android.glass.glassdesign.data.DataSingle;
import com.example.android.glass.glassdesign.fragments.BaseFragment;
import com.example.android.glass.glassdesign.fragments.QualityFragment;
import com.example.android.glass.glassdesign.menu.MenuActivity;
import com.example.glass.ui.GlassGestureDetector;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QualityActivity extends BaseActivity {

    private String MENU_KEY="menu_key";
    protected static final int REQUEST_CODE = 900;
    DatabaseReference reference, ref1;
    private List<BaseFragment> fragments = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout indicator;
    public static ArrayList<DataSingle> arrayList = new ArrayList<>();
    private TextView tvPage;
    private String moduleKey = "";
    private TextView tvTitle;
    private ArrayList<Integer> status = new ArrayList<Integer>();
    private String module_name;
    private ImageView imageView;

    final ScreenSlidePagerAdapter screenSliderPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quality);

        moduleKey = getIntent().getStringExtra("module_key");

        viewPager = findViewById(R.id.activity_quality_viewpager);
        indicator = findViewById(R.id.activity_quality_indicator);
        tvTitle = findViewById(R.id.activity_quality_tvTitle);
        tvPage = findViewById(R.id.activity_quality_tvPage);
        arrayList = new ArrayList<>();
        indicator.setupWithViewPager(viewPager, true);
        viewPager.setAdapter(screenSliderPagerAdapter);
        imageView = findViewById(R.id.activity_quality_imageview);

        reference = FirebaseDatabase.getInstance().getReference().child("DQ").child(moduleKey);
        ref1 = FirebaseDatabase.getInstance().getReference().child("validator");

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
                imageView.setBackground(getResources().getDrawable(R.drawable.back_circle_yellow));

                break;
            case 6:
                imageView.setBackground(getResources().getDrawable(R.drawable.back_circle_purple));

                break;

        }

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataDQ dataDQ = snapshot.getValue(DataDQ.class);
                module_name = dataDQ.getTitle().toString();
                tvTitle.setText("Module: " + dataDQ.getTitle().toString());
                for (int i=0; i< dataDQ.getArrayList().size(); i++){
                    DataSingle dataSingle = dataDQ.getArrayList().get(i);
                    arrayList.add(i, dataSingle);
                    status.add(i, 0);
                    fragments.add(QualityFragment.newInstance( i, dataSingle.getTitle(), dataSingle.getValue()));
                    screenSliderPagerAdapter.notifyDataSetChanged();
                    viewPager.setAdapter(screenSliderPagerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tvPage.setText("" + (indicator.getSelectedTabPosition()+1) + " of "+ indicator.getTabCount());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {
        switch (gesture) {
            case TAP:

                    Intent intent = new Intent(QualityActivity.this, MenuActivity.class);
                    intent.putExtra(MENU_KEY, R.menu.menu_quality);
                    startActivityForResult(intent, REQUEST_CODE );

                return true;
            default:
                return super.onGesture(gesture);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            final int id = data.getIntExtra(MenuActivity.EXTRA_MENU_ITEM_ID_KEY,
                    MenuActivity.EXTRA_MENU_ITEM_DEFAULT_VALUE);
            String selectedOption = "";
            switch (id) {
                case R.id.bAccept:
                    selectedOption = "Accepted";
                    status.set(indicator.getSelectedTabPosition(), 1);
                    updateStatus(indicator.getSelectedTabPosition(), 1);
                    indicator.getTabAt(indicator.getSelectedTabPosition()).setIcon(getResources().getDrawable(R.drawable.ic_accept_round));
                    indicator.getTabAt(indicator.getSelectedTabPosition()).getIcon().setColorFilter(getResources().getColor(R.color.color_normal), PorterDuff.Mode.SRC_IN);
                    break;
                case R.id.bReject:
                    selectedOption = "Rejected";
                    status.set(indicator.getSelectedTabPosition(), 2);
                    updateStatus(indicator.getSelectedTabPosition(), 2);
                    indicator.getTabAt(indicator.getSelectedTabPosition()).setIcon(getResources().getDrawable(R.drawable.ic_reject_round));
                    indicator.getTabAt(indicator.getSelectedTabPosition()).getIcon().setColorFilter(getResources().getColor(R.color.color_critical), PorterDuff.Mode.SRC_IN);
                    break;
                case R.id.bIssue:
                    selectedOption = "Issue Added";
                    String key = ref1.push().getKey();
                    DataIssue dataIssue = new DataIssue(key, module_name ,  arrayList.get(indicator.getSelectedTabPosition()).getTitle(), "", true);
                    ref1.child(key).setValue(dataIssue);
                    status.set(indicator.getSelectedTabPosition(), 3);
                    updateStatus(indicator.getSelectedTabPosition(), 3);
                    indicator.getTabAt(indicator.getSelectedTabPosition()).setIcon(getResources().getDrawable(R.drawable.ic_help));
                    indicator.getTabAt(indicator.getSelectedTabPosition()).getIcon().setColorFilter(getResources().getColor(R.color.color_camera), PorterDuff.Mode.SRC_IN);

                    break;
            }
            Toast.makeText(this.getApplicationContext(),  selectedOption, Toast.LENGTH_SHORT)
                    .show();
        }
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

    private void updateStatus(int id, int response){

    }

}