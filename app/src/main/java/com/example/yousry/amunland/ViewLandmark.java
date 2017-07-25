package com.example.yousry.amunland;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.yousry.amunland.BackgroundTask.BackgroundTask;
import com.example.yousry.amunland.MainClasses.Landmark;
import com.example.yousry.amunland.MainClasses.URLLinks;
import de.hdodenhof.circleimageview.CircleImageView;

//Uber Files
import com.uber.sdk.android.core.UberSdk;
import com.uber.sdk.android.rides.RideParameters;
import com.uber.sdk.android.rides.RideRequestButtonCallback;
import com.uber.sdk.core.auth.Scope;
import com.uber.sdk.rides.client.SessionConfiguration;
import com.uber.sdk.android.rides.RideRequestButton;
import com.uber.sdk.rides.client.ServerTokenSession;
import com.uber.sdk.rides.client.error.ApiError;

import java.util.Arrays;
import java.util.List;

public class ViewLandmark extends AppCompatActivity {

    RideRequestButton requestButton;
    Button streetViewButton;
    Button modelViewButton;
    CircleImageView photo;
    TextView desc;
    Landmark landmark;
    BackgroundTask backgroundTask;
    ImageButton fr_button;
    ImageButton en_button;
    ImageButton ar_button;


    Button shareButton;


    private CallbackManager callbackManager;
    LoginManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_landmark);


        ar_button = (ImageButton) findViewById(R.id.imageButtonAr);
        fr_button = (ImageButton) findViewById(R.id.imageButtonFr);
        en_button = (ImageButton) findViewById(R.id.imageButtonEn);


        streetViewButton = (Button) findViewById(R.id.streetViewButton);
        modelViewButton = (Button) findViewById(R.id.modelViewButton);
        //Uber Request
        requestButton = (RideRequestButton) findViewById(R.id.btn);
        SessionConfiguration config = new SessionConfiguration.Builder()
                // mandatory
                .setClientId("<CLIENT_ID>")
                // required for enhanced button features
                .setServerToken("<TOKEN>")
                // required for implicit grant authentication
                .setRedirectUri("<REDIRECT_URI>")
                // required scope for Ride Request Widget features
                .setScopes(Arrays.asList(Scope.RIDE_WIDGETS))
                // optional: set Sandbox as operating environment
                .setEnvironment(SessionConfiguration.Environment.SANDBOX)
                .build();
        UberSdk.initialize(config);


        modelViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intentUnity = new Intent(Intent.ACTION_MAIN);

                intentUnity.setComponent(new ComponentName("com.unity3d.MobileDemoTest", "com.unity3d.MobileDemoTest.UnityPlayerNativeActivity"));
                startActivity(intentUnity);*/
                /*Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.unity3d.player");

                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }else{
                    try {
                        Toast.makeText(getApplicationContext(),getPackageManager().getPackageInfo("com.unity3d.player", PackageManager.GET_ACTIVITIES).activities[0].getClass().getSimpleName(),Toast.LENGTH_LONG).show();
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }*/
                //Intent LaunchK9 = getPackageManager().getLaunchIntentForPackage("com.unity3d.player");
                //startActivity(LaunchK9);
                startNewActivity(ViewLandmark.this,"com.unity3d.player");
            }
        });

        RideParameters rideParams = new RideParameters.Builder()
                // Optional product_id from /v1/products endpoint (e.g. UberX). If not provided, most cost-efficient product will be used
                .setProductId("a1111c8c-c720-46c3-8534-2fcdd730040d")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location
                .setDropoffLocation(
                        29.97648, 31.131302, "Uber HQ", "Al Haram Str., Giza")
                // Required for pickup estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of pickup location
                .setPickupLocation(30.047503, 31.233702, "Uber HQ", "Tahrir Square, Meret Basha, Qasr an Nile, Cairo Go...")
                // Required for price estimates; lat (Double), lng (Double), nickname (String), formatted address (String) of dropoff location.
                .setDropoffLocation(25.719595, 32.655807, "Embarcadero", "Luxor, Karnak")
                .build();
        // set parameters for the RideRequestButton instance
        requestButton.setRideParameters(rideParams);

        ServerTokenSession session = new ServerTokenSession(config);
        requestButton.setSession(session);
        requestButton.loadRideInformation();
        RideRequestButtonCallback callback = new RideRequestButtonCallback() {

            @Override
            public void onRideInformationLoaded() {
                // react to the displayed estimates
            }

            @Override
            public void onError(ApiError apiError) {
                // API error details: /docs/riders/references/api#section-errors
            }

            @Override
            public void onError(Throwable throwable) {
                // Unexpected error, very likely an IOException
            }
        };
        requestButton.setCallback(callback);





        shareButton = (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"clicked",Toast.LENGTH_LONG).show();
                Landmark landmark =(Landmark)getIntent().getSerializableExtra("LandmarkItemSelected");

                BitmapDrawable bitmapDrawable = (BitmapDrawable) photo.getDrawable();
                Bitmap bitmap =bitmapDrawable.getBitmap();

                startActivity(new Intent(getApplicationContext(),ShareActivity.class).putExtra("LandmarkShare",landmark)
                        .putExtra("image",bitmap));

            }
        });






        photo = (CircleImageView) findViewById(R.id.imageView_landmarkPhoto);
        desc = (TextView) findViewById(R.id.textView_landmarkDescription);

        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear1);






        backgroundTask =new BackgroundTask(ViewLandmark.this);
        if(backgroundTask.isNetworkAvailable()){
            backgroundTask.setTag(BackgroundTask.TAGS.DisplayImages);
            landmark =(Landmark)getIntent().getSerializableExtra("LandmarkItemSelected");


            //Toast.makeText(getApplicationContext(),landmark.toString(),Toast.LENGTH_LONG).show();

            setTitle(landmark.getName());

            desc.setText(landmark.getDescription());
            Picasso.with(ViewLandmark.this).load(URLLinks.URL_Image2d +landmark.getImageName()).into(photo);


            final HorizontalScrollView horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizonatalView);

            backgroundTask.setParamsItem(String.valueOf(landmark.getID()));
            backgroundTask.execute(URLLinks.URL_DisplayImages);
            backgroundTask.setListener(new BackgroundTask.MyAsyncListener() {
                @Override
                public void onSuccessfulExecute(String data) {

                    try {
                        JSONArray jsonArray =new JSONArray(data);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            ImageView imageView = new ImageView(ViewLandmark.this);
                            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(800, 500);
                            imageView.setLayoutParams(layoutParams);
                        /*imageView.setPadding(5,5,5,5);
                        //imageView.setId(i);
                        imageView.setSoundEffectsEnabled(true);
                        */
                            Picasso.with(ViewLandmark.this).load(URLLinks.URL_ImageGallery +jsonObject.getString("image_name")).into(imageView);
                            linearLayout.addView(imageView);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }else{
            Toast.makeText(getApplicationContext(),backgroundTask.mErrorMessageConnection,Toast.LENGTH_LONG).show();
        }



        streetViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(landmark ==null || landmark.getLat() ==0.0000){
                    Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
                }else {
                    startActivity(new Intent(ViewLandmark.this, StreetViewPanoramaBasicDemoActivity.class)
                            .putExtra("lat", landmark.getLat())
                            .putExtra("lng", landmark.getLng())
                    );
                }
            }
        });

    }


    public void onFrButtonClick(View view) {
        if(landmark !=null){
            desc.setText(landmark.getTranslatedFrench());
        }else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    public void onEnButtonClick(View view) {
        if(landmark !=null){
            desc.setText(landmark.getDescription());
        }else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    public void onArButtonClick(View view) {
        if(landmark !=null){
            desc.setText(landmark.getTranslatedArabic());
        }else {
            Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_LONG).show();
        }
    }

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            //intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }




}
