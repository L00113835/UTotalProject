package com.example.mary.utotal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.utotal.dataReturn.DataReturn;
import com.example.mary.utotal.services.DataService;
import com.example.mary.utotal.utils.DataConnect;
import com.example.mary.utotal.utils.NetworkHelper;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity {


    private ZXingScannerView scannerView;
    private boolean True;
    private boolean networkOK;
    public static final String DATA_RETURN = "dataReturn";
    public static final String PRICE = "piceS";
    public static final String BARCODE = "barcodeResult";
    public static final String APIRETURN ="apiReturn";
    public static final String MESSAGE ="message";
    String barcodeResult =" ";
    TextView messageOut;
    TextView nameOut;
    String message= " ";
    Double priceIn;
    String priceS ;
    // returns the connecting php file on aws to Data_url
    private static final String DATA_URL = "http://ec2-34-244-206-59.eu-west-1.compute.amazonaws.com/connect.php";
    //wrote where the message is intended to go
    private BroadcastReceiver messBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            message = intent.getStringExtra(DataService.DATASERVICE_PAYLOAD);
            nameOut= findViewById(R.id.nameOutput);
            nameOut.setText(message);
            convertPrice(message);
        }
    };
    @Nullable
    private Double convertPrice(String message) {

        if (!message.isEmpty()) {
            //taking the string message and extracting the last word which contains the price
            priceS = (message.substring(message.lastIndexOf(" "),message.length()-1));
            //converting the string price to a double inorder for it to be passed to the retunPrice() method
            priceIn = Double.parseDouble(priceS);
            Log.i("convertPrice", priceS);
        }
        return  priceIn;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //a tester to show that the app is connected to tha internet
        networkOK = NetworkHelper.hasNetworkAcces(this);
        messageOut = findViewById(R.id.messageOut);
        messageOut.append("Network active :" + networkOK);

        nameOut= findViewById(R.id.nameOutput);
        //listening for the message from the broadcast receiver goes in the onCreate method of the class
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(messBroadcast,
                        new IntentFilter(DataService.DATASERVICE_MESSAGE));


    } //closs onCreate
    @Override
    protected void onDestroy(){
        super.onDestroy();
    //closes the broadcast manager
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(messBroadcast);
    }

    public void check(View view) {
        if (networkOK) {
            //bundling the values to pass on to the calculate activity class
            Intent intentS=new Intent(MainActivity.this,CalculateActivity.class);
            Bundle bundleS =new Bundle();
            bundleS.putString(BARCODE,barcodeResult);
            bundleS.putString(PRICE,priceS);
            bundleS.putString(MESSAGE,message);
            intentS.putExtras(bundleS);
            startActivity(intentS);

            Log.i("intentS",priceS);

        }
        else {
            Toast.makeText(this,"No Network",Toast.LENGTH_LONG).show();
        }

    }

    public void scanBarcode(View view) {

        scannerView = new ZXingScannerView(this);
        scannerView.setResultHandler(new ZXingScannerResultHandler());

        setContentView(scannerView);
        scannerView.startCamera();
    }//close scanBarcode

    @Override
    public  void  onPause(){
        super.onPause();
        scannerView.stopCamera();
    }//close onPause

    //onnect to the parcelable class
   /* public void  returnPrice() {
        DataReturn dataReturn = new DataReturn();
        //getting the price from the convert price method and returning it to DataReturn class
        dataReturn.setPrice(priceIn);
        Intent intentR = new Intent(MainActivity.this, CalculateActivity.class);
        Bundle bundleR = new Bundle();
        bundleR.putParcelable(DATA_RETURN, dataReturn);
        intentR.putExtras(bundleR);
        startActivity(intentR);

    }// close return price*/
    class ZXingScannerResultHandler implements ZXingScannerView.ResultHandler{

        @Override
        public void handleResult(Result result) {

            barcodeResult = result.getText();
            if (barcodeResult != null) {
                if (networkOK) {
        //connecting the enc point and params for the query
                    DataConnect dataConnect= new DataConnect();
                    dataConnect.setEndPoint(DATA_URL);
                    dataConnect.setParam("barcode",barcodeResult);
                // passing the data through and intent to the values back
                    Intent intent = new Intent(MainActivity.this, DataService.class);
                    intent.setData(Uri.parse(DATA_URL));
                    intent.putExtra(DataService.DATA_CONNECT,dataConnect);
                    startService(intent);

                }
                else {
                    Toast.makeText(MainActivity.this,"No Network",Toast.LENGTH_LONG).show();
                }
            }
            setContentView(R.layout.activity_main);
            scannerView.stopCamera();
        }//close handleResult

    }//close resultHandler


// closing the app
    public void exit(View view) {
        Toast.makeText(this, "Goodbye", Toast.LENGTH_LONG).show();
        finish();
        System.exit(0);

    }
}//close main
