package com.example.mary.utotal.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Mary on 23/04/2018.
 */

public class NetworkHelper {

    public static boolean hasNetworkAcces(Context context){
        ConnectivityManager conMan = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetwork = conMan.getActiveNetworkInfo();
            return activeNetwork !=null && activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }//close has method

}//close main class
