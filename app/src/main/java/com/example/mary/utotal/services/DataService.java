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

public class DataService extends IntentService {
    public static final String TAG = "DataService";
    public static final String DATASERVICE_MESSAGE="dataserviceMessage";
    public static final String DATASERVICE_PAYLOAD="dataservicePayload";
    public static final String DATASERVICE2_MESSAGE="dataserviceMessage";
    public static final String DATASERVICE2_PAYLOAD="dataservicePayload";
    public static final String DATA_CONNECT= "dataConnect";


    public DataService(){
        super("DataService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        DataConnect dataConnect = intent.getParcelableExtra(DATA_CONNECT);
        Uri uri= intent.getData();
        Log.i(TAG, "onHandleIntent :"+ uri.toString());

        String response;
        try {
            response = HttpHelper.getUrl(dataConnect);
            Log.i(TAG, "onHandleIntent res :"+ response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //gets the message
        Intent messageIntent= new Intent(DATASERVICE_MESSAGE);
        //sends the message
        messageIntent.putExtra(DATASERVICE_PAYLOAD,response);
        //sends the message through a local broadcast manager
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.sendBroadcast(messageIntent);

      /*  //gets the message
        Intent secMessageIntent= new Intent(DATASERVICE2_MESSAGE);
        //sends the message
        messageIntent.putExtra(DATASERVICE2_PAYLOAD,response);
        //sends the message through a local broadcast manager
        LocalBroadcastManager localBroadcastManager2 = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager2.sendBroadcast(secMessageIntent);*/
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
}//close dataservice
