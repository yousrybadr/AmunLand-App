package com.example.yousry.amunland;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.location.Location;

import com.example.yousry.amunland.MainClasses.GPSTracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.example.yousry.amunland.MainClasses.Landmark;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Button locationButton;
    CircleImageView imageView;
    ImageButton uberButton;
    LatLng currentLocation;
    ArrayList<Landmark> landmarks;
    List<DistenceObject>distenceObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        landmarks = HomeActivity.landmarks;
        distenceObjects =new ArrayList<>();

        locationButton = (Button) findViewById(R.id.locationBtn);
        imageView = (CircleImageView) findViewById(R.id.imageButton);
        uberButton = (ImageButton) findViewById(R.id.uber_button);

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        uberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();
                try {
                    pm.getPackageInfo("com.ubercab", PackageManager.GET_ACTIVITIES);
                    String uri = "uber://?action=setPickup&pickup=my_location";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.ubercab")));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.ubercab")));
                    }
                }
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTheLocation();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLocation ==null){
                    Toast.makeText(getApplicationContext(),"press on Locaion Button for getting nearest locations, please",Toast.LENGTH_LONG).show();
                }else{
                    float[] results;
                    float distance;
                    for(int i=0;i<landmarks.size();i++){
                        results = new float[1];
                        Location.distanceBetween(currentLocation.latitude, currentLocation.longitude
                                , landmarks.get(i).getLat(), landmarks.get(i).getLng()
                                , results);
                        distance = results[0];
                        distenceObjects.add(new DistenceObject(distance,
                                new LatLng(landmarks.get(i).getLat(),landmarks.get(i).getLng()),
                                landmarks.get(i).getName()
                        ));


                    }
                    DistenceObject var =getMinimumValue(distenceObjects);
                    mMap.clear();
                    BitmapDescriptor descriptorFactory=BitmapDescriptorFactory.fromResource(R.drawable.map_marker);
                    mMap.addMarker(new MarkerOptions().title("Current Location").position(currentLocation).icon(descriptorFactory));
                    mMap.addMarker(new MarkerOptions().title("Nearest Location is: "+var.name).position(var.latLng).icon(descriptorFactory));


                }
            }
        });
    }

    private DistenceObject getMinimumValue(List<DistenceObject> list){
        LatLng latLng =list.get(0).latLng;
        String name =list.get(0).name;
        float distence;
        float min=list.get(0).distence;

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).distence < min) {
                min = list.get(i).distence;
                latLng =list.get(i).latLng;
                name =list.get(i).name;
            }
        }
        distence =min;
        return new DistenceObject(distence,latLng,name);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < HomeActivity.landmarks.size(); i++) {
            LatLng latLng = new LatLng(HomeActivity.landmarks.get(i).getLat(), HomeActivity.landmarks.get(i).getLng());
            MarkerOptions markerOptions=new MarkerOptions();

            mMap.addMarker(new MarkerOptions().position(latLng).title(HomeActivity.landmarks.get(i).getName()));

            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,5.0f));



        }

        /*mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(),
                        "You Clicked on Marker "
                                +
                                marker.getId()
                                +
                        " For "
                                +
                                marker.getTitle(),
                        Toast.LENGTH_LONG)
                        .show();
                Landmark landmark =new Landmark();
                for(int i=0;i<HomeActivity.landmarks.size();++i){
                    if(HomeActivity.landmarks.get(i).getName().contains(marker.getTitle())){
                        landmark =HomeActivity.landmarks.get(i);
                        break;
                    }
                }

                startActivity(new Intent(getApplicationContext(),ViewLandmark.class).putExtra("LandmarkItemSelected",landmark));



                return false;
            }
        });*/
        // Add a marker in Sydney and move the camera

    }

    private void getTheLocation() {

        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            LatLng currentPosition = new LatLng(latitude, longitude);

            //mMap.clear();
            //mMap.addMarker(new MarkerOptions().position(currentPosition).icon(mapMarker));
            //CameraUpdate center = CameraUpdateFactory.newLatLng(currentPosition);
            //CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
            //mMap.moveCamera(center);
            //mMap.animateCamera(zoom);
            currentLocation =currentPosition;

            BitmapDescriptor descriptorFactory=BitmapDescriptorFactory.fromResource(R.drawable.map_marker);
            mMap.addMarker(new MarkerOptions().title("Current Location").position(currentPosition).icon(descriptorFactory));
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                Toast.makeText(getApplicationContext(),addresses.get(0).getCountryName(),Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            showGPSAlertDialog();
            //Toast.makeText(getApplicationContext(), "Can not get location", Toast.LENGTH_SHORT).show();
        }
    }
    public void showGPSAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapsActivity.this);


        //Setting Dialog Title
        alertDialog.setTitle("GPS Setting");
        alertDialog.setIcon(R.drawable.logo);

        //setting dialog message
        alertDialog.setMessage("Your GPS is Offline");

        //setting button
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        //on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = alertDialog.create();

        // Make some UI changes for AlertDialog
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                // Add or create your own background drawable for AlertDialog window



                // Customize POSITIVE, NEGATIVE and NEUTRAL buttons.
                Button positiveButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.FontColor2));
                positiveButton.setTypeface(Typeface.DEFAULT_BOLD);
                positiveButton.invalidate();

                Button negativeButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(getResources().getColor(R.color.FontColor2));
                negativeButton.setTypeface(Typeface.DEFAULT_BOLD);
                negativeButton.invalidate();


            }
        });
        dialog.show();
    }
    private class DistenceObject{
        public float distence;
        public LatLng latLng;
        public String name;


        DistenceObject(){
            distence =0.0f;
            latLng =new LatLng(0,0);
            name ="";
        }

        public DistenceObject(float distence, LatLng latLng,String name) {
            this.distence = distence;
            this.latLng = latLng;
            this.name =name;
        }
    }

}
