package com.example.yousry.amunland;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yousry.amunland.Adapters.LandmarkAdapter;
import com.example.yousry.amunland.MainClasses.Landmark;
import com.example.yousry.amunland.SqliteDatabaseConnection.LocalDbHelper;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    LandmarkAdapter landmarkAdapter;
    ListView listView;
    LocalDbHelper localDbHelper;
    List<Landmark>landmarks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = (ListView) findViewById(R.id.listItems);
        landmarks =new ArrayList<>();
        landmarkAdapter =new LandmarkAdapter(HistoryActivity.this,R.layout.item);
        landmarkAdapter.checker=true;
        localDbHelper =new LocalDbHelper(HistoryActivity.this);
        localDbHelper.Open();
        if(localDbHelper.isOpen()){
            landmarks = localDbHelper.getAllLandmarks();
            localDbHelper.Close();
            if(landmarks != null ){
                if(landmarks.size() <=0){
                    Toast.makeText(getApplicationContext(),"No Items in History",Toast.LENGTH_LONG).show();
                }else {
                    for (int i=0;i<landmarks.size();i++){
                        landmarkAdapter.add(landmarks.get(i));
                    }
                    listView.setAdapter(landmarkAdapter);
                }
            }
        }



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            localDbHelper =new LocalDbHelper(HistoryActivity.this);
            localDbHelper.Open();
            localDbHelper.onDelete(localDbHelper.getDb());
            landmarkAdapter.clear();
            listView.setAdapter(landmarkAdapter);
            localDbHelper.close();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
