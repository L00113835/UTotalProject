package com.example.mary.utotal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.mary.utotal.dataReturn.ApiReturn;
import com.example.mary.utotal.services.NamePriceService;

public class CompareShopActivity extends AppCompatActivity {
    TextView dunnesprice;
    String message = " ";


    // getting broadcast message from the NamePriceService and returning the name and price from the API
    private BroadcastReceiver priceMessBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ApiReturn[] apiReturns = (ApiReturn[]) intent.getParcelableArrayExtra(NamePriceService.PRICE_PAYLOAD);
            for (ApiReturn namePriceReturn : apiReturns) {
                TextView tescoName = (TextView) findViewById(R.id.tescoName);
                tescoName.setText(namePriceReturn.getName());
                Log.i("nameOnRec",namePriceReturn.getName());
                TextView tescoPrice = (TextView) findViewById(R.id.tescoPrice);
                tescoPrice.setText(String.valueOf(namePriceReturn.getPrice()));
            }
        }

    };//close price broadcast message


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare_shop);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(priceMessBroadcast,
                        new IntentFilter(NamePriceService.PRICE_MESSAGE));

      //gets the message returned from the  initial scan
        Intent intentM = getIntent();
        if (intentM != null) {
            message = getIntent().getStringExtra("info");
            dunnesprice = (TextView) findViewById(R.id.dunnesPrice);
            dunnesprice.setText(message);

            Log.i("onCreatMess", message);
        }

    }//close onCreate

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //closing the broadcast receiver
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(priceMessBroadcast);


    }
    //returns to Calculate activity
    public void backToCalAct(View view) {

        finish();
    }
}//close main