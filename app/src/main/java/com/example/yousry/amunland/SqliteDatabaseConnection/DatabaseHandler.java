package com.example.yousry.amunland.SqliteDatabaseConnection;

import android.content.Context;

/**
 * Created by yousry on 6/13/2017.
 */

public class DatabaseHandler {
    LocalDbHelper localDbHelper;
    Context context;

    public DatabaseHandler(Context context) {
        localDbHelper =new LocalDbHelper(context);
        this.context =context;
    }


}
