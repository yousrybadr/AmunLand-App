package com.example.yousry.amunland.BackgroundTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * Created by yousry on 28/8/2016.
 */

public class BackgroundTask extends AsyncTask<String,Void,String> {

    // set Constructor with parameters to take context from running activity even make dialog
    public BackgroundTask(Context context) {
        this.URL_KEY="";
        this.context = context;
        items=new ArrayList<String>();
        mErrorMessageConnection ="Can not Connect Internet";

    }

    public String mErrorMessageConnection;
    //filter the operations
    public enum  TAGS{
        NULL,
        LOGIN,
        Registration,
        SearchForLandmark,
        DisplayLandmark,
        DisplayImages,
        LoadLandmarksCoordinates

    }

    //member Variables
    private TAGS tag;
    private String URL_KEY;  // this variable will take link URL
    private Context context;
    private ArrayList<String> items;
    private String Result;



    private RequestBody DetectionRequestBody(RequestBody body)
    {

        switch (getTag()) {
            case LOGIN:
                body = getRequestBodyLogin();
                break;
            case Registration:
                body =getRequestBodyRegistration();
                break;
            case SearchForLandmark:
                body =getRequestBodySearching();
                break;
            case DisplayLandmark:
                body =getRequestBodyDisplayAllLandmarks();
                break;
            case DisplayImages:
                body =getRequestBodyDisplayImages();
                break;
            case LoadLandmarksCoordinates:
                break;
            case NULL:
                body = null;
                break;
            default:
                body = null;
                break;
        }
        return body;
    }



    //RequestBodies for Login
    private RequestBody getRequestBodyLogin(){
        RequestBody body = new FormEncodingBuilder()
                .add("email", items.get(0))
                .build();
        return body;
    }
    //RequestBodies for Displaying Landmarks
    private RequestBody getRequestBodyDisplayAllLandmarks(){
        RequestBody body = new FormEncodingBuilder()
                .build();
        return body;
    }



    //RequestBodies for Displaying Images Of Landmark
    private RequestBody getRequestBodyDisplayImages(){
        RequestBody body = new FormEncodingBuilder()
                .add("landmark_id",items.get(0))
                .build();
        return body;
    }

    //RequestBodies for Regist
    private RequestBody getRequestBodyRegistration(){
        RequestBody body = new FormEncodingBuilder()
                .add("image_name", items.get(0))
                .add("name", items.get(1))
                .add("password", items.get(2))
                .add("email", items.get(3))
                .add("country", items.get(4))
                .build();
        return body;
    }
    //RequestBodies for Search For Landmark
    private RequestBody getRequestBodySearching(){
        RequestBody body = new FormEncodingBuilder()
                .add("landmark_name", items.get(0))
                .build();
        return body;
    }




    private ProgressDialog dialog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(getTag()!=TAGS.NULL || getTag() != TAGS.DisplayLandmark || getTag() != TAGS.DisplayImages) {
            dialog = new ProgressDialog(this.context);
            dialog.setMessage("Loading...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        this.URL_KEY =params[0];
        try {
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            client.setWriteTimeout(30, TimeUnit.SECONDS);
            RequestBody body = null;
            body = DetectionRequestBody(body);
            if(body ==null) {
                return "No Request Body";
            }
            Request request = new Request.Builder().url(getURL_KEY()).post(body).build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private MyAsyncListener listener ;

    public void setListener(MyAsyncListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPostExecute(String data) { //Finish MyDialog
        super.onPostExecute(data);
        try {
            if(getTag()!=TAGS.NULL || getTag() != TAGS.DisplayLandmark || getTag() != TAGS.DisplayImages) {
                dialog.dismiss();
                dialog.cancel();
            }
            items.clear();
            setURL_KEY("");
            listener.onSuccessfulExecute(data);
        }catch (Exception ex)
        {

        }

    }




    public interface MyAsyncListener {
        void onSuccessfulExecute(String data);
    }




    //Checking Network Connection

     public boolean isNetworkAvailable() {

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected())
        {
            isAvailable = true;

        }
        return isAvailable;
    }





    // getter and setter
    public TAGS getTag() {
        return tag;
    }
    public void setTag(TAGS tag) {
        this.tag = tag;
    }
    public String getURL_KEY() {
        return URL_KEY;
    }
    public void setURL_KEY(String URL_KEY) {
        this.URL_KEY = URL_KEY;
    }
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public void setParamsItem(String item) {
        this.items.add(item);
    }
    public void setParams(ArrayList<String> params) {
        this.items = params;
    }
    public String getResult() {
        return Result;
    }
    public void setResult(String result) {
        Result = result;
    }
    public ArrayList<String> getParams() {
        return items;
    }


}

