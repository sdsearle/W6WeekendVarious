package com.example.admin.w6weekendvarious;

import android.app.Application;
import android.content.res.Configuration;

import com.orm.SugarApp;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by admin on 10/6/2017.
 */

public class MyApplication extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();

        //Person.findById(Person.class,1l);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
        Timber.plant(new Timber.DebugTree());


    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }
}
