package com.example.shlim.baseandroidproject.common;

import android.os.Environment;

import java.io.File;

public class Define {


    //DB
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "db.sqlite";
    public static final String DB_PATH = Environment.getDataDirectory().getPath() + Environment.getDataDirectory().getPath() + File.separator + "com.example.shlim.baseandroidproject"+ File.separator+"databases"+ File.separator+DB_NAME;


    //Preference
    public static final String PREFER_FCM_TOKEN = "fcm_token";                                                  // FCM Toekn


}
