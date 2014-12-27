package com.appmunki.burritoapp;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.serializer.CalendarSerializer;
import com.appmunki.burritoapp.backend.userApi.UserApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;

/**
 * Created by radzell on 12/23/14.
 */
public class BurritoApp extends Application {
    private UserApi mUserApi;

    @Override
    public void onCreate() {
        super.onCreate();
        Configuration config = new Configuration.Builder(this).setDatabaseName("burrito.db").create();
        ActiveAndroid.initialize(config, true);

        mUserApi = new UserApi.Builder(AndroidHttp.newCompatibleTransport(), new AndroidJsonFactory(), null).setRootUrl(BuildDebug.SERVER).build();


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public UserApi getUserApi() {
        return mUserApi;
    }


}
