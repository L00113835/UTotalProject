package com.example.mary.utotal.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.mary.utotal.dataReturn.ApiReturn;
import com.example.mary.utotal.utils.QueryConnect;
import com.example.mary.utotal.utils.QueryHttpHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Mary on 28/04/2018.
 */

public class NamePriceService extends IntentService {
    public static final String QUERY_CONNECT= "queryConnect";
    public static final String TAG ="NamePriceService";
    public static final String PRICE_MESSAGE= "PriceMessge";
    public static final String PRICE_PAYLOAD= "PricePayload";


    public NamePriceService() {
        super("NamePriceService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        QueryConnect queryConnect=intent.getParcelableExtra(QUERY_CONNECT);
        Uri uri= intent.getData();
        Log.i(TAG, "onHandleNamePrice :"+ uri.toString());

        // Log.i(TAG,"onHandleIntent Nameprice   "+ uri.toString());
        String namePriceResponse;
        try {
            namePriceResponse = QueryHttpHelper.getUrl(queryConnect);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ArrayList<String> priceReturned = new ArrayList<>();
        JSONObject jsonResponse;
        try {  // parsing the json objects to get to the json array
            jsonResponse = new JSONObject(namePriceResponse);
            JSONObject uk = jsonResponse.getJSONObject("uk");
            JSONObject ghs = uk.getJSONObject("ghs");
            JSONObject products = ghs.getJSONObject("products");
            //breaking down the array to get the name and price
            JSONArray priceResults = products.getJSONArray("results");
            for (int i=0; i<priceResults .length();i++){
                JSONObject results = priceResults.getJSONObject(i);
                String description= " ";
                // passing over the description array
                if (description .equals("description"))
                {
                    continue;
                }
                //getting the name and price values
                String name = results.getString("name");
                String price = results.getString(String.valueOf("price"));

                //converting them back into a json array
                Gson gBuilder = new GsonBuilder().create();
                HashMap<String,String> result= new HashMap<>();
                result.put("name",name);
                result.put("price",price);

                String converString=gBuilder.toJson(result);
                priceReturned.add(String.valueOf(converString));
                Log.i("onhadShop",String.valueOf(priceReturned));
            }

          //  Toast.makeText(this,"namePrice Response :" + priceReturned, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Gson gson=new Gson();
        ApiReturn[] apiPriceReturns;
        apiPriceReturns = gson.fromJson(String.valueOf(priceReturned),ApiReturn[].class);

       // Log.d(" onHandele gson price", namePriceResponse);
        //gets the message
        Intent priceMessageIntent= new Intent(PRICE_MESSAGE);
        //sends the message
        priceMessageIntent.putExtra(PRICE_PAYLOAD,apiPriceReturns);
        //sends the message through a local broadcast manager
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.sendBroadcast(priceMessageIntent);
        // Log.d(" onHandeleGsonNprice",String.valueOf(apiPriceReturns) );

    }//close onHandle Intent

    public void onCreate(){
        super.onCreate();
    }
    public void onDestroy(){
        super.onDestroy();
    }
}//close main class

