package com.example.yousry.amunland;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.yousry.amunland.BackgroundTask.BackgroundTask;
import com.example.yousry.amunland.MainClasses.Tourist;
import com.example.yousry.amunland.MainClasses.URLLinks;
import com.example.yousry.amunland.MainClasses.ValidationTask;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {

    AppCompatEditText userNameEditeText;
    AppCompatEditText passwordEditeText;
    CheckBox rememberMeChecker;
    Button loginButton;
    TextView forgetTextView;
    TextView registerTextView;
    BackgroundTask backgroundTask;
    CallbackManager callbackManager;
    LoginButton FBloginButton;

    String nameOfPref ;

    final
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameOfPref =getResources().getString(R.string.MyNamePref);


        userNameEditeText = (AppCompatEditText) findViewById(R.id.editTextUserNameLogin);
        passwordEditeText = (AppCompatEditText) findViewById(R.id.editTextPasswordLogin);
        rememberMeChecker = (CheckBox) findViewById(R.id.checkBoxLogin);
        loginButton = (Button) findViewById(R.id.buttonLogin);
        forgetTextView = (TextView) findViewById(R.id.forgetTextView);
        registerTextView=(TextView)findViewById(R.id.registerTextView);

        final ValidationTask validationTask=new ValidationTask();
        FBloginButton = (LoginButton)findViewById(R.id.fb_login_button);
        callbackManager = CallbackManager.Factory.create();

        FBloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"Login ,plaese",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Log.e("KEYERROR",e.getMessage());
                Toast.makeText(getApplicationContext(),"Login ,plaese",Toast.LENGTH_LONG).show();
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validationTask.loginChecking(userNameEditeText.getText().toString(),passwordEditeText.getText().toString())){
                    backgroundTask =new BackgroundTask(LoginActivity.this);
                    if(backgroundTask.isNetworkAvailable()) {


                        backgroundTask.setTag(BackgroundTask.TAGS.LOGIN);
                        backgroundTask.setParamsItem(userNameEditeText.getText().toString());

                        backgroundTask.execute(URLLinks.URL_Login);
                        backgroundTask.setListener(new BackgroundTask.MyAsyncListener() {
                            @Override
                            public void onSuccessfulExecute(String data) {
                                if (data.contains("row")) {
                                    Toast.makeText(getApplicationContext(), "This Email don't exist", Toast.LENGTH_SHORT).show();
                                } else {
                                    try {
                                        JSONArray jsonArray = new JSONArray(data);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                                        if (jsonObject.getString("user_password").equalsIgnoreCase(passwordEditeText.getText().toString())) {
                                            Tourist.getInstance().setmName(jsonObject.getString("name"));
                                            Tourist.getInstance().setmEmail(jsonObject.getString("email"));
                                            Tourist.getInstance().setID(jsonObject.getInt("user_id"));
                                            Tourist.getInstance().setPassword(jsonObject.getString("user_password"));
                                            Tourist.getInstance().setCountry(jsonObject.getString("country"));
                                            if (rememberMeChecker.isChecked()) {
                                                SharedPreferences.Editor editor = getSharedPreferences(nameOfPref, MODE_PRIVATE).edit();
                                                editor.putString("email", Tourist.getInstance().getmEmail());
                                                editor.putString("name", Tourist.getInstance().getmName());
                                                editor.putString("password", Tourist.getInstance().getPassword());
                                                editor.putInt("id", Tourist.getInstance().getID());
                                                editor.putString("country", Tourist.getInstance().getCountry());

                                                editor.putBoolean("login", true);
                                                editor.commit();
                                            }

                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Email or password is wrong", Toast.LENGTH_SHORT).show();

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        });
                    }else{
                        Toast.makeText(getApplicationContext(),backgroundTask.mErrorMessageConnection,Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"Fill All Fields, please",Toast.LENGTH_SHORT).show();
                }

            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
            }
        });
    }
}
