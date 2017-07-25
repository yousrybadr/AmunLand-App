package com.example.yousry.amunland.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.yousry.amunland.HistoryActivity;
import com.example.yousry.amunland.R;
import com.example.yousry.amunland.SqliteDatabaseConnection.LocalDbHelper;
import com.example.yousry.amunland.ViewLandmark;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.example.yousry.amunland.MainClasses.Landmark;
import com.example.yousry.amunland.MainClasses.URLLinks;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by mahmoud on 2016-05-05.
 */
public class LandmarkAdapter extends ArrayAdapter {
    private List list = new ArrayList();


    public boolean checker;
    Context context;
    Landmark landmarkModelItem ;
    public LandmarkAdapter(Context context, int resource) {
        super(context, resource);
        this.context=context;
        checker =false;
        landmarkModelItem =new Landmark();
    }
    static class DataHandler {
        TextView name;
        RoundedImageView imageView;
        TextView like;


    }

    @Override
    public void add(Object object) {
        super.add(object);
        list.add(object);
    }

    @Override
    public void addAll(Collection collection) {
        super.addAll(collection);
        list.addAll(collection);
    }

    @Override
    public int getPosition(Object item) {
        return super.getPosition(item);
    }

    @Override
    public void remove(Object object) {
        super.remove(object);
        list.remove(object);
    }

    @Override
    public void clear() {
        super.clear();
        list.clear();
    }

    @Override
    public Object getItem(int position) {
        return this.list.get(position);
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View row;
        row = convertView;
        DataHandler handler ;

        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_landmark,parent,false);
            handler = new DataHandler();
            handler.name = (TextView)row.findViewById(R.id.title);
            handler.imageView = (RoundedImageView) row.findViewById(R.id.image);
            handler.like = (TextView) row.findViewById(R.id.like_textView);

            row.setTag(handler);
        }
        else
        {
            handler = (DataHandler)row.getTag();
        }
        if(handler ==null) {
            Log.d("Error", "handler = null");
        }
        else {
            landmarkModelItem = (Landmark) getItem(position);
            if(landmarkModelItem!=null){
                handler.name.setText(landmarkModelItem.getName());
                Picasso.with(context)
                        .load(URLLinks.URL_Image2d +landmarkModelItem.getImageName())
                        .into(handler.imageView);
            }

        }
        if (handler != null) {
            if(checker) {
                handler.like.setText("UnLike");
                handler.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        landmarkModelItem =(Landmark) getItem(position);
                        LocalDbHelper localDbHelper=new LocalDbHelper(context);
                        try {
                            localDbHelper.deleteLandmark(landmarkModelItem);
                            Toast.makeText(context,"Deleted from your Favorite Landmarks",Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(context, HistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        }catch (SQLiteException ex){
                            Log.e("KEYERROR",ex.getMessage());
                        }catch (SQLException ex){
                            Log.e("KEYERROR",ex.getMessage());
                        }

                    }
                });
            }else {
                handler.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        landmarkModelItem =(Landmark) getItem(position);
                        LocalDbHelper localDbHelper=new LocalDbHelper(context);
                        try {
                            localDbHelper.addLandmark(landmarkModelItem);
                            Toast.makeText(context,"Added into your Favorite Landmarks",Toast.LENGTH_LONG).show();

                        }catch (SQLiteException ex){
                            Log.e("KEYERROR",ex.getMessage());
                        }catch (SQLException ex){
                            Log.e("KEYERROR",ex.getMessage());
                        }

                    }
                });
            }

        }



        RelativeLayout relativeLayout = (RelativeLayout) row.findViewById(R.id.relative10);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Landmark temp = (Landmark) getItem(position);
                Intent intent=new Intent(context,ViewLandmark.class);
                intent.putExtra("LandmarkItemSelected",temp);
                context.startActivity(intent);
            }
        });

        return row;

    }

}
