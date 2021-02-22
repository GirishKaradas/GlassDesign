package com.example.android.glass.glassdesign;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.glass.glassdesign.data.DataLoad;
import com.example.android.glass.glassdesign.data.DataRecipe;
import com.example.glass.ui.GlassGestureDetector;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MonitorActivity extends BaseActivity {

    private DatabaseReference reference;
    private RecyclerView recyclerView;
    private ArrayList<DataLoad> arrayList = new ArrayList<>();
    public static ArrayList<DataRecipe> recipeArrayList = new ArrayList<>();
    private LoadAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        reference = FirebaseDatabase.getInstance().getReference().child("recipes");
        recyclerView = findViewById(R.id.activity_monitor_rcView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        adapter = new LoadAdapter(this, arrayList);
        recyclerView.setAdapter(adapter);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ArrayList<DataRecipe> dataRecipes = new ArrayList<>();
                for (int i=0; i< snapshot.child("arrayList").getChildrenCount(); i++){
                    dataRecipes.add(i, snapshot.child("arrayList").child(String.valueOf(i)).getValue(DataRecipe.class));
                }
                arrayList.add(new DataLoad(snapshot.child("key").getValue().toString(),
                        snapshot.child("title").getValue().toString(), dataRecipes));
                adapter.notifyDataSetChanged();
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


    }

    public class LoadAdapter extends RecyclerView.Adapter<LoadAdapter.ViewHolder>{

        private Context context;
        private ArrayList<DataLoad> list=new ArrayList<>();

        public LoadAdapter(Context context, ArrayList<DataLoad> list) {
            this.context=context;
            this.list=list;
        }

        @NonNull
        @Override
        public LoadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe, parent, false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull LoadAdapter.ViewHolder holder, final int position) {
            holder.tvTitle.setText(list.get(position).getTitle());


        }
        @Override
        public int getItemCount() {
            return list.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.list_recipe_tvTitle);
            }
        }
    }

    @Override
    public boolean onGesture(GlassGestureDetector.Gesture gesture) {

        switch (gesture){
            case TAP:
                recipeArrayList = new ArrayList<>();
                for (int i =0; i<arrayList.get(0).getArrayList().size(); i++){
                    recipeArrayList.add(i, arrayList.get(0).getArrayList().get(i));
                }
                Intent intent = new Intent(MonitorActivity.this, ChartActivity.class);
                startActivity(intent);
                break;
        }

        return super.onGesture(gesture);
    }
}