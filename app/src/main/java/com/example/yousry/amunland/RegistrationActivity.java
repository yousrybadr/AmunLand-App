package com.example.yousry.amunland;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.example.yousry.amunland.BackgroundTask.BackgroundTask;
import com.example.yousry.amunland.MainClasses.URLLinks;
import com.example.yousry.amunland.MainClasses.ValidationTask;
import de.hdodenhof.circleimageview.CircleImageView;


public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener{


    private static final int REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST = 1888;

    Bitmap bitmap;
    String convertedImage;

    BackgroundTask backgroundTask;

    CircleImageView imageView;
    Button takePhoto;
    Button choosePhoto;


    RelativeLayout relativeLayout;
    CircleImageView mContentView;
    Button button;

    Button mNext1;
    Button mNext2;
    Button mNext3;

    ViewFlipper viewFlipper;

    TextView error1;


    AppCompatEditText nameEditText;
    AppCompatEditText usernameEditText;
    AppCompatEditText passwordEditText;
    AppCompatEditText repasswordEditText;
    AppCompatEditText emailEditText;
    AppCompatEditText phoneEditText;

    Spinner spinner;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_registration);

        error1 =(TextView)findViewById(R.id.error1);

        // Data
        nameEditText = (AppCompatEditText) findViewById(R.id.editTextNameRegist);
        usernameEditText = (AppCompatEditText) findViewById(R.id.editTextUserNameRegist);
        passwordEditText = (AppCompatEditText) findViewById(R.id.editTextPassordRegist);
        repasswordEditText=(AppCompatEditText) findViewById(R.id.editTextRepasswordRegist);
        emailEditText =(AppCompatEditText)findViewById(R.id.editTextEmailRegist);
        phoneEditText = (AppCompatEditText) findViewById(R.id.editTextPhoneRegist);

        spinner = (Spinner) findViewById(R.id.spinner);

        ValidationTask validationTask =new ValidationTask();




        imageView= (CircleImageView) findViewById(R.id.photo_profile);
        takePhoto= (Button) findViewById(R.id.registeration_button_take_photo);
        choosePhoto= (Button) findViewById(R.id.registeration_button_choose_photo);


        mNext1= (Button) findViewById(R.id.next1);
        mNext2 = (Button) findViewById(R.id.next2);
        mNext3 = (Button) findViewById(R.id.next3);


        mNext1.setOnClickListener(this);
        mNext2.setOnClickListener(this);
        mNext3.setOnClickListener(this);


        viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);
        viewFlipper.setVisibility(View.GONE);

        mContentView = (CircleImageView) findViewById(R.id.logo_registration_image);

        final Animation an= AnimationUtils.loadAnimation(getBaseContext(),R.anim.in_animation);
        final Animation an2= AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.slide_in_left);
        an2.setDuration(2000);
        mContentView.setAnimation(an);
        an.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                viewFlipper.setVisibility(View.VISIBLE);
                viewFlipper.setAnimation(an2);
                viewFlipper.setDisplayedChild(0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = null;
                pickImage(v);
            }
        });



    }
    public void pickImage(View View) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE);
        onActivityResult(REQUEST_CODE, 1, intent);
    }


    //for buttons
    @Override
    public void onClick(View v) {
        ValidationTask validationTask;
        switch (v.getId()){
            case R.id.next1:
                error1.setVisibility(View.GONE);
                if(bitmap ==null || bitmap.getByteCount()==0)
                {
                    Toast.makeText(getApplicationContext(),"You must Enter your Image",Toast.LENGTH_SHORT).show();
                    error1.setVisibility(View.VISIBLE);
                    error1.setText("Here");
                    error1.setTextColor(Color.RED);
                    break;
                }
                viewFlipper.setDisplayedChild(1);
                break;
            case R.id.next2:
                validationTask= isDataEmptyAndCorrect(2,new ValidationTask());
                if(validationTask.valid.state){
                    viewFlipper.setDisplayedChild(2);
                }
                break;
            case R.id.next3:
                validationTask= isDataEmptyAndCorrect(3,new ValidationTask());
                if(validationTask.valid.state){
                    final Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    //Connection Sending Request Into Server
                    backgroundTask =new BackgroundTask(RegistrationActivity.this);
                    backgroundTask.setTag(BackgroundTask.TAGS.Registration);
                    backgroundTask.setParamsItem("a.jpg");
                    backgroundTask.setParamsItem(nameEditText.getText().toString());
                    backgroundTask.setParamsItem(usernameEditText.getText().toString());
                    backgroundTask.setParamsItem(passwordEditText.getText().toString());
                    backgroundTask.setParamsItem(emailEditText.getText().toString());
                    backgroundTask.setParamsItem(phoneEditText.getText().toString());

                    Resources resources =getResources();
                    CharSequence[] arr =resources.getTextArray(R.array.countries_array);
                    Toast.makeText(getApplicationContext(),arr[spinner.getSelectedItemPosition()].toString(),Toast.LENGTH_SHORT).show();
                    backgroundTask.setParamsItem(spinner.getSelectedItem().toString());
                    backgroundTask.execute(URLLinks.URL_Regist);
                    backgroundTask.setListener(new BackgroundTask.MyAsyncListener() {
                        @Override
                        public void onSuccessfulExecute(String data) {

                            Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    });




                }
                break;

            default:
                break;
        }
    }

    private ValidationTask isDataEmptyAndCorrect(int sectionNum, ValidationTask validationTask){

        ValidationTask.ValidMessage validMessage;
        switch (sectionNum){
            case 1:
                validationTask.valid.state =true;
                break;
            case 2:
                if(validationTask.checkisEmpty(nameEditText.getText().toString())) {
                    if(validationTask.checkisEmpty(usernameEditText.getText().toString())){
                        if(validationTask.checkisEmpty(passwordEditText.getText().toString())){
                            validMessage =validationTask.checkPasswordIsValid(passwordEditText.getText().toString());
                            if(validMessage.state){
                                if(validationTask.checkisEmpty(repasswordEditText.getText().toString())){

                                    if(validationTask.checkRePasswordValid(passwordEditText.getText().toString(),repasswordEditText.getText().toString())){
                                        validationTask.valid.state =true;
                                    }else{
                                        validationTask.valid.message="Repassword is not equal to your Password that you entered";
                                        repasswordEditText.setError(validationTask.valid.message);
                                        validationTask.valid.state =false;
                                        Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    validationTask.valid.message="Repassword is Empty";
                                    repasswordEditText.setError(validationTask.valid.message);
                                    validationTask.valid.state =false;
                                    Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }else{
                                validationTask.valid.message=validMessage.message;
                                passwordEditText.setError(validationTask.valid.message);
                                validationTask.valid.state =false;
                                Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            validationTask.valid.message="Password is Empty";
                            passwordEditText.setError(validationTask.valid.message);
                            validationTask.valid.state =false;
                            Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        validationTask.valid.message="UserName is Empty";
                        usernameEditText.setError(validationTask.valid.message);
                        validationTask.valid.state =false;
                        Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    validationTask.valid.message="Your Name is Empty";
                    nameEditText.setError(validationTask.valid.message);
                    validationTask.valid.state =false;
                    Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                if(validationTask.checkisEmpty(emailEditText.getText().toString())){
                    if(validationTask.checkEmailIsValid(emailEditText.getText().toString())){
                        if(validationTask.checkisEmpty(phoneEditText.getText().toString()))
                        {
                            if(validationTask.checkPhoneValid(phoneEditText.getText().toString()))
                            {
                                validationTask.valid.state=true;
                            }else{
                                validationTask.valid.message="Phone is not valid, please enter it again.";
                                phoneEditText.setError(validationTask.valid.message);
                                validationTask.valid.state =false;
                                Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();

                            }

                        }else{
                            validationTask.valid.message="Phone is Empty.";
                            phoneEditText.setError(validationTask.valid.message);
                            validationTask.valid.state =false;
                            Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        validationTask.valid.message="Email Address is not Correct, please enter it again";
                        emailEditText.setError(validationTask.valid.message);
                        validationTask.valid.state =false;
                        Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    validationTask.valid.message="Email Address is Empty";
                    emailEditText.setError(validationTask.valid.message);
                    validationTask.valid.state =false;
                    Toast.makeText(getApplicationContext(),"Fill All Data",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(getApplicationContext(),"Error in Filling User Data",Toast.LENGTH_LONG).show();
                validationTask.valid.state =false;
                break;
        }
        return validationTask;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                // We need to recyle unused bitmaps
                if (bitmap != null) {
                    bitmap.recycle();
                }
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                bitmap = BitmapFactory.decodeStream(stream);
               // convertedImage=MainFunctions.BitMapToString(bitmap);
                //blobImage =MainFunctions.getBytesFromBitmap(bitmap);
                stream.close();
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else  if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    //for textViews BackOptions
    public void Back1(View view) {
        finish();
    }

    public void Back2(View view) {
        viewFlipper.setDisplayedChild(0);
    }

    public void Back3(View view) {
        viewFlipper.setDisplayedChild(1);
    }
}
