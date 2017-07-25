package com.example.yousry.amunland.Adapters;

/**
 * Created by yousry on 4/26/2017.
 */

public class LandmarkModelItem {
    private String Title;
    private String ImageName;


    public LandmarkModelItem(String title, String imageName) {
        Title = title;
        ImageName = imageName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }
}
