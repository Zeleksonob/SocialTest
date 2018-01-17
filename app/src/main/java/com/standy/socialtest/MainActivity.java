package com.standy.socialtest;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static String Facebook = "com.facebook.katana";
    static String Instagram = "com.instagram.android";
    static String WhatApp = "com.whatsapp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStoragePermissionGranted();
    }
    private Uri takeScreenshotAndGetUri() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)  ;

        try {
            // image naming and path  to include sd card  appending name you choose for file

            // create bitmap screen capture
            Bitmap bitmap =getScreenShot();

            return store(bitmap,now.toString()+ ".jpg");

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
            return  null;
        }
    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }

        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }

        return  true;
    }
    public Bitmap getScreenShot() {
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);


        View screenView = rootView.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }
    public  Uri store(Bitmap bm, String fileName){
        final  String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".my.package.name.provider", file);

            return  photoURI;//Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return  Uri.EMPTY;
        }
    }

















    public void SharingGenral(String application) {

        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            Uri var = takeScreenshotAndGetUri();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, var);
            startActivity(Intent.createChooser(intent, "Share via"));



        }
        catch (Exception e){
            Log.e("sdsdf","sfsdfsd");
        }

    }



    public void SharingToSocialMedia(String application) {

        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri var = takeScreenshotAndGetUri();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, var);
            boolean installed = checkAppInstall(application);
            if (installed) {
                intent.setPackage(application);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Installed application first", Toast.LENGTH_LONG).show();
            }

        }
        catch (Exception e){
Log.e("sdsdf","sfsdfsd");
        }

    }


    private boolean checkAppInstall(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }



    public void onShareImage(View view) {

        SharingToSocialMedia(Facebook);


    }

    public void onShareInstagramImage(View view) {

        SharingToSocialMedia(Instagram);

    }

    public void onWhatUpClick(View view) {
        SharingToSocialMedia(WhatApp);

    }


    public void genralShareClick(View view) {
        SharingGenral("");
    }
}
