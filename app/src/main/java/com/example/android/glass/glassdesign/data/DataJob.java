package com.example.android.glass.glassdesign.data;

public class DataJob {

    private String key;
    private String title;
    private String recipe_title;
    private String desc;
    private Boolean status;

    public DataJob() {
    }

    public DataJob(String key, String title, String recipe_title, String desc, Boolean status) {
        this.key = key;
        this.title = title;
        this.recipe_title = recipe_title;
        this.desc = desc;
        this.status = status;
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

    public String getRecipe_title() {
        return recipe_title;
    }

    public void setRecipe_title(String recipe_title) {
        this.recipe_title = recipe_title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
