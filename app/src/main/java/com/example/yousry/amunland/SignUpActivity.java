package com.example.yousry.amunland;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yousry.amunland.BackgroundTask.BackgroundTask;
import com.example.yousry.amunland.MainClasses.URLLinks;
import com.example.yousry.amunland.MainClasses.ValidationTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;

    Bitmap bitmap;
    BackgroundTask backgroundTask;

    CircleImageView imageView;
    Button takePhoto;
    AppCompatEditText usernameEditText;
    AppCompatEditText passwordEditText;
    AppCompatEditText repasswordEditText;
    AppCompatEditText emailEditText;
    Spinner spinner;
    Button registerBtn ;
    ValidationTask validationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = (AppCompatEditText) findViewById(R.id.editTextUserNameRegist);
        passwordEditText = (AppCompatEditText) findViewById(R.id.editTextPassordRegist);
        repasswordEditText=(AppCompatEditText) findViewById(R.id.editTextRepasswordRegist);
        emailEditText =(AppCompatEditText)findViewById(R.id.editTextEmailRegist);

        spinner = (Spinner) findViewById(R.id.spinner_signup);
        imageView= (CircleImageView) findViewById(R.id.imageView_signUp);
        takePhoto= (Button) findViewById(R.id.take_button_signUp);
        registerBtn = (Button) findViewById(R.id.register_btn);


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundTask = new BackgroundTask(SignUpActivity.this);
                if(backgroundTask.isNetworkAvailable()) {


                    validationTask = isDataEmptyAndCorrect(new ValidationTask());
                    if (validationTask.valid.state) {
                        final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);


                        //Connection Sending Request Into Server
                        backgroundTask.setTag(BackgroundTask.TAGS.Registration);
                        backgroundTask.setParamsItem("a.jpg");
                        backgroundTask.setParamsItem(usernameEditText.getText().toString());
                        backgroundTask.setParamsItem(passwordEditText.getText().toString());
                        backgroundTask.setParamsItem(emailEditText.getText().toString());

                        Resources resources = getResources();
                        CharSequence[] arr = resources.getTextArray(R.array.countries_array);
                        Toast.makeText(getApplicationContext(), arr[spinner.getSelectedItemPosition()].toString(), Toast.LENGTH_SHORT).show();


                        backgroundTask.setParamsItem(spinner.getSelectedItem().toString());


                        backgroundTask.execute(URLLinks.URL_Regist);
                        backgroundTask.setListener(new BackgroundTask.MyAsyncListener() {
                            @Override
                            public void onSuccessfulExecute(String data) {

                                Toast.makeText(getApplicationContext(), data, Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        });

                        startActivity(intent);

                    }
                }else{
                    Toast.makeText(getApplicationContext(),backgroundTask.mErrorMessageConnection,Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }else{
            Toast.makeText(getApplicationContext(),"please, try to click on Take photo button agian.",Toast.LENGTH_LONG).show();
        }
    }
    private ValidationTask isDataEmptyAndCorrect(ValidationTask validationTask){

        ValidationTask.ValidMessage validMessage;

        if(validationTask.checkisEmpty(emailEditText.getText().toString())) {
            if (validationTask.checkisEmpty(usernameEditText.getText().toString())) {
                if (validationTask.checkisEmpty(passwordEditText.getText().toString())) {
                    validMessage = validationTask.checkPasswordIsValid(passwordEditText.getText().toString());
                    if (validMessage.state) {
                        if (validationTask.checkisEmpty(repasswordEditText.getText().toString())) {

                            if (validationTask.checkRePasswordValid(passwordEditText.getText().toString(), repasswordEditText.getText().toString())) {
                                validationTask.valid.state = true;
                            } else {
                                validationTask.valid.message = "Repassword is not equal to your Password that you entered";
                                repasswordEditText.setError(validationTask.valid.message);
                                validationTask.valid.state = false;
                                Toast.makeText(getApplicationContext(), "Fill All Data", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            validationTask.valid.message = "Repassword is Empty";
                            repasswordEditText.setError(validationTask.valid.message);
                            validationTask.valid.state = false;
                            Toast.makeText(getApplicationContext(), "Fill All Data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        validationTask.valid.message = validMessage.message;
                        passwordEditText.setError(validationTask.valid.message);
                        validationTask.valid.state = false;
                        Toast.makeText(getApplicationContext(), "Fill All Data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    validationTask.valid.message = "Password is Empty";
                    passwordEditText.setError(validationTask.valid.message);
                    validationTask.valid.state = false;
                    Toast.makeText(getApplicationContext(), "Fill All Data", Toast.LENGTH_SHORT).show();
                }
            } else {
                validationTask.valid.message = "UserName is Empty";
                usernameEditText.setError(validationTask.valid.message);
                validationTask.valid.state = false;
                Toast.makeText(getApplicationContext(), "Fill All Data", Toast.LENGTH_SHORT).show();
            }
        }else{
            validationTask.valid.message="Email Address is not Correct, please enter it again";
            emailEditText.setError(validationTask.valid.message);
            validationTask.valid.state =false;
            Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();
        }

        return validationTask;
    }

}
