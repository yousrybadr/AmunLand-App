package com.example.yousry.amunland;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.yousry.amunland.MainClasses.Tourist;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {

    TextView name;
    TextView country;
    TextView email;
    ImageLoader imageLoader ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        name = (TextView) findViewById(R.id.name);
        country = (TextView) findViewById(R.id.country);
        email = (TextView) findViewById(R.id.email);

        setTitle(Tourist.getInstance().getmName() +"'s Profile");
        name.setText("Name is : "+Tourist.getInstance().getmName());
        country.setText("Country :"+Tourist.getInstance().getCountry());
        email.setText("Email is : "+Tourist.getInstance().getmEmail());
        imageLoader = ImageLoader.getInstance();

    }
}
