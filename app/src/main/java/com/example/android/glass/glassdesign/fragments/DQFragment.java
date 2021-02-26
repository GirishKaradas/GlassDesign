package com.example.android.glass.glassdesign.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.glass.glassdesign.BaseActivity;
import com.example.android.glass.glassdesign.R;
import com.example.android.glass.glassdesign.SplashActivity;

public class DQFragment extends BaseFragment{

    public static DQFragment newInstance(String key, String title) {

        final DQFragment fragment =new DQFragment();

        final Bundle args = new Bundle();
        args.putString("key", key);
        args.putString("title", title);
        fragment.setArguments(args);

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.list_dq, container, false);
        if (getArguments() != null) {
            final TextView tvTitle = view.findViewById(R.id.list_dq_title);

            tvTitle.setText(getArguments().getString("title", ""));
            switch (SplashActivity.color_code){

                case 1:
                    tvTitle.setTextColor(getResources().getColor(R.color.design_green));
                    break;

                case 2:
                    tvTitle.setTextColor(getResources().getColor(R.color.design_red));

                    break;

                case 3:
                    tvTitle.setTextColor(getResources().getColor(R.color.design_yellow));

                    break;
                case 4:
                    tvTitle.setTextColor(getResources().getColor(R.color.design_blue));

                    break;
                case 5:
                    tvTitle.setTextColor(getResources().getColor(R.color.design_orange));

                    break;
                case 6:
                    tvTitle.setTextColor(getResources().getColor(R.color.design_purple));

                    break;

            }

        }

        return view;
    }
}
