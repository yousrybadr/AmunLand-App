package com.example.yousry.amunland.MainClasses;

/**
 * Created by yousry on 4/6/2017.
 */
public class Tourist {
    private static Tourist ourInstance = new Tourist();

    private String Password;

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    private String mName;
    private String mEmail;
    private int ID;
    private String mAddress;
    private String Country;

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public static Tourist getInstance() {
        return ourInstance;
    }

    private Tourist() {
        mName ="";
        mEmail ="";
        ID=0;
        mAddress ="";
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }
}
