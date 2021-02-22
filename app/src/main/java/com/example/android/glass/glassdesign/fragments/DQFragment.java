package com.example.android.glass.glassdesign.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.glass.glassdesign.R;

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
        if (getArguments() != null){
            final TextView tvTitle = view.findViewById(R.id.list_dq_title);

            tvTitle.setText(getArguments().getString("title", ""));

        }
        return view;
    }
}
