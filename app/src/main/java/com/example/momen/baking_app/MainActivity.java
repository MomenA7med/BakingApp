package com.example.momen.baking_app;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.momen.baking_app.utils.SimpleIdlingResource;


public class MainActivity extends AppCompatActivity  {

    private static boolean mTwoPane;

    @Nullable
    private SimpleIdlingResource simpleIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if (simpleIdlingResource == null){
            simpleIdlingResource = new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (findViewById(R.id.landScape) != null)
            mTwoPane = true;
        else
            mTwoPane = false;
    }

    public static boolean getNoPane() {
        return mTwoPane;
    }


}
