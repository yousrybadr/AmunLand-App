package com.example.yousry.amunland.MainClasses;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yousry on 4/6/2017.
 */

public class Landmark implements Serializable{
    private String Name;
    private int ID;
    private String Description;
    private String Address;
    private double lat;
    private double lng;
    private ArrayList<String> ImageList;
    private String ImageName;
    private String TranslatedArabic;
    private String TranslatedFrench;

    public String getTranslatedArabic() {
        return TranslatedArabic;
    }

    public void setTranslatedArabic(String translatedArabic) {
        TranslatedArabic = translatedArabic;
    }

    public String getTranslatedFrench() {
        return TranslatedFrench;
    }

    public void setTranslatedFrench(String translatedFrench) {
        TranslatedFrench = translatedFrench;
    }

    public Landmark() {
        ID =0;
        Name ="";
        Description ="";
        Address ="";
        lat =0.0;
        lng=0.0;
        ImageList =new ArrayList<>();
        ImageName ="";
        TranslatedArabic ="";
        TranslatedFrench ="";
    }

    public Landmark(int ID, String name, String description, String address, double lat, double lng, String imageName) {
        this.ID = ID;
        Name = name;
        Description = description;
        Address = address;
        this.lat = lat;
        this.lng = lng;
        ImageName = imageName;
    }

    public Landmark(int ID) {
        this.ID = ID;
        Name ="";
        Description ="";
        Address ="";
        lat =0.0;
        lng=0.0;
        ImageList =new ArrayList<>();
        ImageName ="";
        TranslatedArabic ="";
        TranslatedFrench ="";
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public ArrayList<String> getImageList() {
        return ImageList;
    }

    public void setImageList(ArrayList<String> imageList) {
        ImageList = imageList;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }


    public void addItemIntoImageList(String name){
        this.getImageList().add(name);
    }


    @Override
    public String toString() {
        return "Landmark{" +
                "Name='" + Name + '\'' +
                ", ID=" + String.valueOf(ID) +
                ", Description='" + Description + '\'' +
                ", Addtess='" + Address + '\'' +
                ", lat=" + String.valueOf(lat) +
                ", lng=" + String.valueOf(lng) +
                ", ImageList=" + ImageList +
                ", ImageName='" + ImageName + '\'' +
                ", TranslatedArabic='" + TranslatedArabic + '\'' +
                ", TranslatedFrench='" + TranslatedFrench + '\'' +
                '}';
    }


}
