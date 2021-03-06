package com.example.android.glass.glassdesign.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.glass.glassdesign.R;
import com.example.android.glass.glassdesign.SplashActivity;

import java.util.ArrayList;

public class TestFragment extends BaseFragment{

    private TextView tvResponse;


    public static TestFragment newInstance(String key, String question, ArrayList<String> ans, int ans_id, int response) {

        final TestFragment fragment =new TestFragment();

        final Bundle args = new Bundle();
        args.putString("key", key);
        args.putString("question", question);
        args.putStringArrayList("ans", ans);
        args.putInt("ans_id", ans_id);
        args.putInt("response", response);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.list_test, container, false);
        if (getArguments() != null){
            final TextView tvQuestion = view.findViewById(R.id.list_test_tvQuestion);
            final TextView tv1 = view.findViewById(R.id.list_test_tv1);
            final TextView tv2 = view.findViewById(R.id.list_test_tv2);
            final TextView tv3 = view.findViewById(R.id.list_test_tv3);
        //    final  TextView tv4 = view.findViewById(R.id.list_test_tv4);
            tvResponse = view.findViewById(R.id.list_test_tvResponse);
            final ImageView imageView = view.findViewById(R.id.list_test_imageview);

            tvQuestion.setText("Q. " +getArguments().getString("question"));

            ArrayList<String> list =  new ArrayList<>();
            list = getArguments().getStringArrayList("ans");
            tv1.setText("A. " + list.get(0));
            tv2.setText("B. " + list.get(1));
            tv3.setText("C. " + list.get(2));
         //   tv4.setText("D. " + list.get(3));
            tvResponse.setText(getArguments().getInt("response") + "Answer");
       /*     switch (getArguments().getInt("response")){
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

        */

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
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        switch (getArguments().getInt("response")){
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



}