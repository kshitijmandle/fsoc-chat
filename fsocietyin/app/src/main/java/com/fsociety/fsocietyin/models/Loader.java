package com.fsociety.fsocietyin.models;

import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.fsociety.fsocietyin.R;

public class Loader {
    Activity activity;
    AlertDialog alertDialog;
    public Loader(Activity activity){
        this.activity = activity;

    }
    public void startloading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loader,null));
        builder.setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void loadstop(){
        alertDialog.dismiss();
    }
}
