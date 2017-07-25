package com.example.yousry.amunland;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yousry.amunland.MainClasses.Landmark;
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

import java.util.Arrays;
import java.util.List;

public class ShareActivity extends AppCompatActivity {

    Landmark landmark;
    private CallbackManager callbackManager;
    private TextView info;
    private LoginButton loginButton;
    LoginManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        landmark =(Landmark)getIntent().getSerializableExtra("LandmarkShare");
        if(landmark ==null){
            finish();
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        List<String> permissionNeeds = Arrays.asList("publish_actions");

        //this loginManager helps you eliminate adding a LoginButton to your UI
        manager = LoginManager.getInstance();


        manager.logInWithPublishPermissions(this, permissionNeeds);

        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                sharePhotoToFacebook();
                finish();
            }

            @Override
            public void onCancel()
            {
                System.out.println("onCancel");
                finish();

            }

            @Override
            public void onError(FacebookException exception)
            {
                System.out.println("onError");
                finish();

            }
        });
    /*
        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                info.setText("User ID:  " +
                        loginResult.getAccessToken().getUserId() + "\n" +
                        "Auth Token: " + loginResult.getAccessToken().getToken());

                sharePhotoToFacebook();
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("KEYERROR",e.getMessage());
                info.setText("Login attempt failed.");
            }
        });*/
    }
    private void sharePhotoToFacebook(){

        //Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Bitmap image =getIntent().getParcelableExtra("image");
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption(landmark.getDescription())
                .build();

        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        ShareApi.share(content, null);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
