package com.example.yousry.amunland;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.example.yousry.amunland.Adapters.LandmarkAdapter;
import com.example.yousry.amunland.BackgroundTask.BackgroundTask;
import com.example.yousry.amunland.MainClasses.Landmark;
import com.example.yousry.amunland.MainClasses.Tourist;
import com.example.yousry.amunland.MainClasses.URLLinks;
import de.hdodenhof.circleimageview.CircleImageView;


//Facebool SDK
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

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    SearchView searchView;
    ViewFlipper viewFlipper1;
    ViewFlipper viewFlipper2;
    BackgroundTask backgroundTask;
    LandmarkAdapter landmarkAdapter;


    TextView NameTextView ;
    TextView EmailTextView;


     public static ArrayList<Landmark> landmarks;


    TextView mTitleTextView;
    CircleImageView LandmarkImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        Toast.makeText(getApplicationContext(),"Welcome " +Tourist.getInstance().getmName(),Toast.LENGTH_LONG).show();
        //NameTextView.setText(Tourist.getInstance().getmName());
        //EmailTextView.setText(Tourist.getInstance().getmEmail());

        landmarks =new ArrayList<>();
        viewFlipper1 = (ViewFlipper) findViewById(R.id.ViewFlipperHome);
        viewFlipper2 = (ViewFlipper) findViewById(R.id.ViewFlipperHome2);
        mTitleTextView = (TextView) findViewById(R.id.titleTextViewHome);
        LandmarkImage = (CircleImageView) findViewById(R.id.imageViewHome);


        listView = (ListView) findViewById(R.id.listItems);
        landmarkAdapter=new LandmarkAdapter(HomeActivity.this,R.layout.item);


        backgroundTask =new BackgroundTask(HomeActivity.this);
        backgroundTask.setTag(BackgroundTask.TAGS.DisplayLandmark);
        backgroundTask.execute(URLLinks.URL_DisplayLandmark);


        backgroundTask.setListener(new BackgroundTask.MyAsyncListener() {
            @Override
            public void onSuccessfulExecute(String data) {
                try {
                    JSONArray jsonArray=new JSONArray(data);
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject =jsonArray.getJSONObject(i);
                        Landmark landmark=new Landmark();
                        landmark.setID(jsonObject.getInt("landmark_id"));
                        landmark.setName(jsonObject.getString("landmark_name"));
                        landmark.setAddress(jsonObject.getString("landmark_address"));
                        landmark.setDescription(jsonObject.getString("landmark_description"));
                        landmark.setLat(jsonObject.getDouble("landmark_latitude"));
                        landmark.setLng(jsonObject.getDouble("landmark_longitude"));
                        landmark.setTranslatedArabic(jsonObject.getString("translated_ar"));
                        landmark.setTranslatedFrench(jsonObject.getString("translated_fr"));
                        landmark.setImageName(jsonObject.getString("landmark_photo"));
                        landmarks.add(landmark);
                        landmarkAdapter.add(landmark);
                    }
                    listView.setAdapter(landmarkAdapter);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        listView.setClickable(true);




        searchView = (SearchView) findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                BackgroundTask backgroundTask1 =new BackgroundTask(HomeActivity.this);
                backgroundTask1.setTag(BackgroundTask.TAGS.SearchForLandmark);
                backgroundTask1.setURL_KEY(URLLinks.URL_SearchForLandmark);
                backgroundTask1.setParamsItem(query);
                backgroundTask1.execute(backgroundTask1.getURL_KEY());
                backgroundTask1.setListener(new BackgroundTask.MyAsyncListener() {
                    @Override
                    public void onSuccessfulExecute(String data) {
                        if(data.contains("row")){
                            viewFlipper2.setDisplayedChild(1);
                        }else{
                            viewFlipper2.setDisplayedChild(0);
                        }

                        try {
                            JSONArray jsonArray=new JSONArray(data);
                            JSONObject jsonObject =jsonArray.getJSONObject(0);
                            mTitleTextView.setText(jsonObject.getString("landmark_name"));
                            Toast.makeText(getApplicationContext(),jsonObject.getString("landmark_photo"),Toast.LENGTH_LONG).show();
                            Picasso.with(HomeActivity.this)
                                    .load(URLLinks.URL_Image2d+jsonObject.getString("landmark_photo"))
                                    .into(LandmarkImage);
                            //.load("http://cdn.history.com/sites/2/2015/06/ask-great-pyramid-iStock_000015224988Large-E.jpeg")


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    viewFlipper1.setDisplayedChild(0);
                }else{
                    viewFlipper1.setDisplayedChild(1);
                    viewFlipper2.setDisplayedChild(1);
                }
                return false;
            }
        });


        //listView.setAdapter(landmarkAdapter);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navHeaderView = navigationView.getHeaderView(0);
        NameTextView = (TextView) navHeaderView.findViewById(R.id.textViewName_nav_header);
        EmailTextView = (TextView) navHeaderView.findViewById(R.id.textViewEmail_nav_header);
        NameTextView.setText(Tourist.getInstance().getmName());
        EmailTextView.setText(Tourist.getInstance().getmEmail());
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(viewFlipper1.getDisplayedChild() ==1){
                viewFlipper1.setDisplayedChild(0);
                searchView.setQuery("",false);
            }else{
                SharedPreferences sharedPreferences = getSharedPreferences(getResources().getString(R.string.MyNamePref), MODE_PRIVATE);
                if(sharedPreferences.contains("login")){
                    finish();
                }else{
                    super.onBackPressed();
                }
            }

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id ==R.id.action_logout){
            SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.MyNamePref), MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
            //editor.commit();
            startActivity(new Intent(HomeActivity.this,LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
            startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
        } else if (id == R.id.nav_map) {
            Intent i =new Intent(HomeActivity.this,MapsActivity.class);
            startActivity(i);

        }  else if (id == R.id.nav_feedback) {
            CustomDialogClass cdd = new CustomDialogClass(HomeActivity.this);
            cdd.show();

        } else if (id == R.id.nav_history) {
            startActivity(new Intent(HomeActivity.this,HistoryActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onEventClick(View view) {
        //start Activity

        Toast.makeText(getApplicationContext(),"Clicked",Toast.LENGTH_LONG).show();
    }
}
