package com.example.android.glass.glassdesign.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.glass.glassdesign.R;

public class QualityFragment extends BaseFragment{

    public static QualityFragment newInstance(int id, String title, String value) {

        final QualityFragment fragment =new QualityFragment();

        final Bundle args = new Bundle();
        args.putInt("id", id);
        args.putString("title", title);
        args.putString("value", value);
        fragment.setArguments(args);

        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.list_quality, container, false);
        if (getArguments() != null){
            final TextView tvTitle = view.findViewById(R.id.list_quality_tvTitle);
            final TextView tvPara = view.findViewById(R.id.list_quality_tvPara);

            tvTitle.setText(getArguments().getString("title", ""));
            tvPara.setText(getArguments().getString("value", ""));

        }
        return view;
    }
}
