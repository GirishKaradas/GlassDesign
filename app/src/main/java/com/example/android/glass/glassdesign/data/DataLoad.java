package com.example.android.glass.glassdesign.data;

import java.util.ArrayList;

public class DataLoad {

    private String key;
    private String title;
    private ArrayList<DataRecipe> arrayList;

    public DataLoad() {
    }

    public DataLoad(String key, String title, ArrayList<DataRecipe> arrayList) {
        this.key = key;
        this.title = title;
        this.arrayList = arrayList;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<DataRecipe> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<DataRecipe> arrayList) {
        this.arrayList = arrayList;
    }
}
