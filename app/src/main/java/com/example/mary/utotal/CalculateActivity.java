package com.example.mary.utotal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mary.utotal.dataReturn.ApiReturn;
import com.example.mary.utotal.services.GetNameService;
import com.example.mary.utotal.services.NamePriceService;
import com.example.mary.utotal.utils.DataConnect;
import com.example.mary.utotal.utils.QueryConnect;

import static com.example.mary.utotal.MainActivity.BARCODE;
import static com.example.mary.utotal.MainActivity.MESSAGE;
import static com.example.mary.utotal.MainActivity.PRICE;

public class CalculateActivity extends AppCompatActivity {
    private static final String GET_NAME ="https://dev.tescolabs.com/product/";
    public static final String NAME_PRICE = "https://dev.tescolabs.com/grocery/products/";
    public static final String APIRETURN = "apiReturn";
    public static final String DUNNES ="message";
    public static final String  CALPREF ="calPref";
    public static final String TOTAL = "Total";

    TextView totalTextBox;
    TextView addTextBox;

    String totalReturn="0.00";

    SharedPreferences preferences;

    String  totalReturns;
    String barcodeCode = " ";
    String message = " ";
    String results=" ";
    Bundle bundleS;
    String result= " ";
    String convertedPrice;
    String nameNprice;
    private BroadcastReceiver calMessBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ApiReturn[] apiReturns =(ApiReturn[])intent.getParcelableArrayExtra(GetNameService.GET_NAME_PAYLOAD);
            for (ApiReturn returnName : apiReturns){
                result = returnName.getDescription();
            }
        }
    } ; // close brosdcast message

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);


        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(calMessBroadcast,
                        new IntentFilter(GetNameService.GET_NAME_MESSAGE));


    //extracting the data from the bundle and passing them to variables
        Bundle bundleS = this.getIntent().getExtras();
        barcodeCode=bundleS.getString(BARCODE);
        convertedPrice= bundleS.getString(PRICE);
        message= bundleS.getString(MESSAGE);
        Log.i("CalOnCreteBundle  ",message);
        Log.i("OnCreate test", barcodeCode);

        addTextBox = findViewById(R.id.addTotal);//getting the add text area
        //adding the value to the add text box
        addTextBox.setText(convertedPrice);
        //  Log.i("onCreateConvert",convertedPrice);
        totalTextBox = findViewById(R.id.totalText);//getting the total text area
        // finding the total text box and getting the value from it to the variable totalReturn
        totalReturn = totalTextBox.getText().toString();
        //if there is no value in total return set it to default 0.00
        if (totalReturn == null){
            totalReturn="0.00 ";
            totalTextBox.setText(totalReturn);
        }
        else //return the value stored in the named shared preferences
        {
            preferences = getSharedPreferences(CALPREF,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            totalReturn = preferences.getString(TOTAL, "0.00 ");
            totalTextBox.setText(totalReturn);
            Log.d("onCreateTotal",totalReturn);
            editor.apply();


        }

    }//close onCreate


    @Override
    protected void onDestroy(){
        super.onDestroy();

       LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(calMessBroadcast);

    }


    public void addTotal(View view) {
        addTextBox = findViewById(R.id.addTotal);//getting the add text area
        totalTextBox = findViewById(R.id.totalText);//getting the total text area
        totalReturn = totalTextBox.getText().toString();

       Double total = Double.parseDouble(totalReturn);//changing the string to a double
        //converting the  text in the add text view to a string
        String addText = addTextBox.getText().toString();
        Double adding = Double.parseDouble(addText);//changing the string to a double
        total += adding;// adding the amount to the total amount;

        //converting the double total back to a string
        totalReturn = String.valueOf(total);
        totalTextBox.setText(totalReturn);// setting the total as text in the total box
        Log.i("addTotal",totalReturn);
        //saving the value in the totalReturn to a sharedPreferences

        preferences= getSharedPreferences(CALPREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TOTAL,totalReturn);
        editor.apply();

        addTextBox.setText("");
    }//close add

   public void compare(View view) {
        //sending the end point and params for the
       //first part of the query
        DataConnect dataConnectCal= new DataConnect();
        dataConnectCal.setEndPoint(GET_NAME);
        dataConnectCal.setParam("gtin",barcodeCode);
       // getting the description from the first api to return it as name for second part
        Intent intent = new Intent(CalculateActivity.this,GetNameService.class);
        intent.setData(Uri.parse(GET_NAME));
        intent.putExtra(GetNameService.DATA_CONNECT,dataConnectCal);
        startService(intent);
    // a delay handler for delaying the query connect from running before
       //the results from the dataconnect returned
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
        //sending end point and params to queryconnect class
        QueryConnect queryConnect = new QueryConnect();
        queryConnect.setEndPoint(NAME_PRICE);
        queryConnect.setParam("query",result);
        Log.d("compareName",result);
        //sending results from queryconnect to pass to NamePrieService
               //for the name and price
        final Intent intentN;
         intentN =new Intent(CalculateActivity.this,NamePriceService.class);
        intentN.setData(Uri.parse(NAME_PRICE));
        intentN.putExtra(NamePriceService.QUERY_CONNECT,queryConnect);
        startService(intentN);
        //starting the CompareShopActivity to display the comparison  shop prices
        final Intent intent1;
       intent1 = new Intent(CalculateActivity.this,CompareShopActivity.class);
       intent1.putExtra("info",message);
       startActivity(intent1);

            }
        },900);

    }//close compare


    public void mini(View view) {
        Intent intent= new Intent(this,MiniActivity.class);
        intent.putExtra("info",message);
        startActivity(intent);
    }//close fresh

    public void returnToMain(View view) {
        Intent intentR= new Intent(CalculateActivity.this,MainActivity.class);
        startActivity(intentR);

    }//close retrun

private void ShowMessageOkCancel(String message, DialogInterface.OnClickListener oKListener){
        new android.support.v7.app.AlertDialog.Builder(CalculateActivity.this)
                .setMessage(message)
                .setPositiveButton("Yes",oKListener)
                .setNegativeButton("Cancel",null)
                .create()
                .show();
}

        public void exit(View view) {

            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setTitle("Do you want to leave UTotal?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(CalculateActivity.this, "Goodbye", Toast.LENGTH_LONG).show();
                    finishAffinity();
                }
            });
            builder.setNeutralButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setMessage(" ");
            AlertDialog alertDialog = builder.create();
            alertDialog.show();


             SharedPreferences  preferences= getSharedPreferences(CALPREF,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit().remove(CALPREF);
            editor.clear();
            editor.apply();

        }//close exit

        public void cancelPrice(View view) {
            addTextBox.setText("");


        }//close cancel

}//close main
