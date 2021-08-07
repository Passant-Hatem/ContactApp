package com.example.contactapp;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplictationClass extends Application {


    public static final String APPLICATION_ID = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
    public static final String API_KEY = "XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
    public static final String SERVER_URL = "https://api.backendless.com";

    public static BackendlessUser user;
    public static List<Contact> contacts;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );

    }
}
