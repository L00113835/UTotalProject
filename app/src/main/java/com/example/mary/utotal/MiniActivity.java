package com.example.mary.utotal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.utotal.services.MiniService;
import com.example.mary.utotal.utils.DataConnect;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MiniActivity extends AppCompatActivity {

    String minMessage;
    private ZXingScannerView scannerView;
    private static final String DATA_URL ="http://ec2-34-244-206-59.eu-west-1.compute.amazonaws.com/connect.php";
    private static final String MINITOTAL="miniTotal";
    public static final String MESSG="premade";
    private static final String MTOTAL ="mtotal";
    public static final String MESSGKEY="messKey";

    String addMiniTotal = "0.00";

    TextView addMiniBox;
    TextView madePrice;
    TextView addTotalBox;

    String  price;
    String cost= " ";
    String  minibarcodeResult;
    SharedPreferences preference;
    SharedPreferences pref;
    SharedPreferences preferencesM;

    private BroadcastReceiver miniReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            minMessage =intent.getStringExtra(MiniService.MINISERVICE_PAYLOAD);
            TextView scanReturn=(TextView)findViewById(R.id.miniScan);
            scanReturn.setText(minMessage);
            Log.i("miniReceier",minMessage);
            //Toast.makeText(MiniActivity.this,minMessage,Toast.LENGTH_LONG).show();
            getprice(minMessage);
        }
    };
    private void getprice(String minMessage){
        if (minMessage != null) {
            //taking the string message and extracting the last word which contains the price
            price = (minMessage.substring(minMessage.lastIndexOf(" "), minMessage.length() - 1));

            addMiniBox=(TextView)findViewById(R.id.addMiniT);
            addMiniBox.setText(price);
            Log.i("getPrice", price);
        }
       // return priceReturned;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini);



        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(miniReceiver,
                        new IntentFilter(MiniService.MINISERVICE_MESSAGE));


        addTotalBox=findViewById(R.id.miniTotal);
         addMiniTotal=addTotalBox.getText().toString();
            //checking to see if there is a value in addMinitotal
        if (addMiniTotal==null) {
            addMiniTotal="0.00";
            addTotalBox.setText(addMiniTotal);
            Log.i("onCreate mini",addMiniTotal);
        }

        else {
            //returning the shared preferences to the total box
            pref=getSharedPreferences(MTOTAL,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1= pref.edit();
            addMiniTotal = pref.getString(MINITOTAL,"0.00");
            addTotalBox.setText(addMiniTotal);

           editor1.apply();
            Log.i("onMiniSet",addMiniTotal);
        }

        //extracting the data from the intent and setting it into a text view
        Intent intent= getIntent();
        if (intent != null){
            cost= getIntent().getStringExtra("info");
            madePrice=(TextView)findViewById(R.id.made);
           madePrice.setText(cost);
            //SharedPreferences.Editor editor = getSharedPreferences(MESSGKEY,Context.MODE_PRIVATE).edit();

             preferencesM =getSharedPreferences(MESSGKEY,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 =preferencesM.edit();
            editor2.putString(MESSGKEY,cost);
            editor2.apply();
            Log.d("miniMessPref",cost);

        }


        if (cost==null){
            madePrice=findViewById(R.id.made);
            preferencesM =getSharedPreferences(MESSGKEY,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = preferencesM.edit();
            cost =preferencesM.getString(MESSGKEY,"  ");
            madePrice.setText(cost);
            editor2.apply();
            Log.d("miniPutMess",cost);

        }



    }//close onCreate

 //activating the barcode reader
    public void scanBar(View view) {
        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new MiniActivity.ZXingScannerResultHandler());

        setContentView(scannerView);
        scannerView.startCamera();
    }//close scan

    @Override
    public  void  onPause(){
        super.onPause();
        scannerView.stopCamera();
    }

    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler{
        @Override
        public void handleResult(Result result) {
            minibarcodeResult = result.getText();
            Log.i("onHandleMini",minibarcodeResult);
            if (minibarcodeResult != null) {
        //setting the end point and parameters for the query
                DataConnect dataConnectMi= new DataConnect();
                dataConnectMi.setEndPoint(DATA_URL);
                dataConnectMi.setParam("barcode",minibarcodeResult);
                    //passing the data through an intent to return values
                Intent intent = new Intent(MiniActivity.this,MiniService.class);
                intent.setData(Uri.parse(DATA_URL));
                intent.putExtra(MiniService.DATA_CONNECT,dataConnectMi);
                startService(intent);

            }
            setContentView(R.layout.activity_mini);
            scannerView.stopCamera();
        }//close handleResult
    }//close resultHandler

    @Override
    protected void onDestroy(){
        super.onDestroy();
    //closing the broadcast manager
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver( miniReceiver);
    }

    public void addMini(View view) {
            addMiniBox=(TextView)findViewById(R.id.addMiniT);

        //getting a value from the total box
         addMiniTotal = addTotalBox.getText().toString();

       if(addMiniTotal.isEmpty()){
           addMiniTotal="0.00";
       }
              Log.i("addml",addMiniTotal);
        //parsing the value to a double for calculation
         Double total= Double.parseDouble(addMiniTotal);
            //getting the value to add
            String miniAdd= addMiniBox.getText().toString();
            Log.d("addMini",miniAdd);
            Double minitotal= Double.parseDouble(miniAdd);
            total += minitotal;
            //returning the value of total to addMiniTotal
            addMiniTotal = String.valueOf(total);
            //setting the value in the text view
         addTotalBox=findViewById(R.id.miniTotal);
            addTotalBox.setText(addMiniTotal);
            Log.d("add SetText",addMiniTotal);

            //saving the value to a shared preference
           pref = getSharedPreferences(MTOTAL,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1= pref.edit();
            editor1.putString(MINITOTAL ,addMiniTotal);
            editor1.apply();

            Log.i("after pref",addMiniTotal);
            addMiniBox.setText(" ");
        }//close add



       public void back(View view) {
        //clearing the value from the shared preferences when leaving the page
       SharedPreferences preferences= getSharedPreferences(MTOTAL,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit().remove(MTOTAL);
        editor.clear();
        editor.apply();
        //returning to the previous page
          finish();

       }
}//close main class
