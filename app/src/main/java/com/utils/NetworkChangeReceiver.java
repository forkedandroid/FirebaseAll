package com.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.firebaseall.App;

/**
 * Created by prashant.patel on 12/4/2017.
 *
 */

public class NetworkChangeReceiver extends BroadcastReceiver
{
    public static ConnectivityReceiverListener connectivityReceiverListener;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            App.showLog("========NetworkChangeReceiver=====");
            if (isNetworkAvailable(context)) {
                //dialog(true);
                App.showLog("======Online Connect Internet=====");
                if(connectivityReceiverListener !=null)
                {
                    connectivityReceiverListener.onNetworkConnectionChanged(true);
                }

            } else {
                //dialog(false);
                App.showLog("=====Connectivity Failure !!!=======");
                if(connectivityReceiverListener !=null)
                {
                    connectivityReceiverListener.onNetworkConnectionChanged(false);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}