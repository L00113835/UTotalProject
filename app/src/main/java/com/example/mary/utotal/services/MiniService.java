package com.example.mary.utotal.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.example.mary.utotal.utils.DataConnect;
import com.example.mary.utotal.utils.HttpHelper;

import java.io.IOException;

/**
 * Created by Mary on 23/04/2018.
 */

public class MiniService extends IntentService {

    public static final String TAG = "MiniService";
    public static final String MINISERVICE_MESSAGE="miniserviceMessage";
    public static final String MINISERVICE_PAYLOAD="miniservicePayload";
    public static final String DATA_CONNECT="dataConnectMi";


    public MiniService() {
        super("MiniService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        DataConnect dataConnectMi= intent.getParcelableExtra(DATA_CONNECT);
        Uri uri= intent.getData();
        Log.d(TAG,"onHandleMini :"+ uri.toString());

        String response;
        try {
            response = HttpHelper.getUrl(dataConnectMi);
            Log.i(TAG, "onHandleMini res :"+ response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //gets the message
        Intent messageIntent= new Intent(MINISERVICE_MESSAGE);
        //sends the message
        messageIntent.putExtra(MINISERVICE_PAYLOAD,response);
        //sends the message through a local broadcast manager
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.sendBroadcast(messageIntent);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"onCreate");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }


}///close main
