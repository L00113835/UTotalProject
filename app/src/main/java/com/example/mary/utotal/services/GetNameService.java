package com.example.mary.utotal.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.mary.utotal.dataReturn.ApiReturn;
import com.example.mary.utotal.utils.DataConnect;
import com.example.mary.utotal.utils.TesHttpHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mary on 28/04/2018.
 */

public class GetNameService extends IntentService {
    public static final String TAG1 ="GetNameService";
    public static final String GET_NAME_MESSAGE ="GetNameMessage";
    public static final String GET_NAME_PAYLOAD = "GetNamePayload";
    public static final String DATA_CONNECT= "dataConnectCal";
    String nameResponse= " ";
    // String nameReturned =" ";
    public GetNameService() {
        super("GetNameService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        DataConnect dataConnectCal = intent.getParcelableExtra(DATA_CONNECT);
        Uri uri= intent.getData();
        //Log.i( "onHandleGetName :"+ uri.toString());
        String nameResponse= " ";
        try {
            nameResponse= TesHttpHelper.getUrl(dataConnectCal);
            // Log.d(TAG1, "onHandleIntent res :"+ nameResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        ArrayList<String> nameReturned = new ArrayList<String>();
        JSONObject jsonResponse;
        try { //looping through the array to return the description
            jsonResponse = new JSONObject(nameResponse);
            JSONArray products = jsonResponse.getJSONArray("products");
            for (int i=0; i<products.length();i++){
                JSONObject product = products.getJSONObject(i);
                nameReturned.add( products.getString(i));
            }
           // Toast.makeText(this,"nameResponse :" +nameReturned,Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson=new Gson();
        ApiReturn[] apiReturns; //converting json and returning it to the API parcelable class
        apiReturns = gson.fromJson(String.valueOf(nameReturned),ApiReturn[].class);

        Log.d(" onHandeleGetNamw gson ", String.valueOf(nameReturned));
        //gets the message
        Intent calMessageIntent= new Intent(GET_NAME_MESSAGE);
        //sends the message
        calMessageIntent.putExtra(GET_NAME_PAYLOAD,apiReturns);
        //sends the message through a local broadcast manager
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.sendBroadcast(calMessageIntent);


    }







    public void onCreate(){
        super.onCreate();
    }
    public void onDestroy(){
        super.onDestroy();
    }
}// close main class
