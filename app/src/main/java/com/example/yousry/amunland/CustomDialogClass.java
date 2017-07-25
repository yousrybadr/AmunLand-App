package com.example.yousry.amunland;

import android.*;
import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by mahmoud on 2016-04-17.
 */
public class CustomDialogClass extends Dialog implements View.OnClickListener {
    public Context context;
    //OnMyDialogResult mDialogResult ;

    public Dialog d;
    public Button cancelButton;

    public CustomDialogClass(Context a) {
        super(a);
        this.context=a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);
        TextView textView = (TextView) findViewById(R.id.textViewPhone);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone("+201055664477");
            }
        });
        cancelButton = (Button) findViewById(R.id.custom_dialog_button_cancel);


        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.custom_dialog_button_cancel:
                dismiss();
                break;
            case R.id.textViewPhone:
                break;
            default:
                break;
        }
    }

    private void dialContactPhone(final String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));



    }

    //if you want to hundle event
    /*public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }
    public interface OnMyDialogResult{
        void onResult(boolean result, int mYear, int mMonth, int mDay, int mHour, int mMinute);
    }*/


}
