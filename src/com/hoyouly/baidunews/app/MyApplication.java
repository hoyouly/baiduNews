package com.hoyouly.baidunews.app;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * Created by hoyouly on 15/5/12.
 */
public class MyApplication extends Application {

    public final static String IMAGE_DIR_PATH=Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "demo";
    public static String TAG;
    private static MyApplication application;
    public static MyApplication getInstance() {
        return application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        TAG = this.getClass().getSimpleName();
        application = this;
    }
}
