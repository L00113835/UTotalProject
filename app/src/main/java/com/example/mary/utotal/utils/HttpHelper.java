package com.example.mary.utotal.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Mary on 23/04/2018.
 */

public class HttpHelper {

    public static String getUrl(DataConnect dataConnect)throws IOException {

        String  address = dataConnect.getEndPoint();
        String encodeParams = dataConnect.getEncodeParams();

        if (dataConnect.getMethod().equals("GET")&& encodeParams.length()>0){

            address =String.format("%s?%s",address, encodeParams);
        }

        InputStream is = null;

        try {
            URL url= new URL(address);
            HttpURLConnection conn =(HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(20000);
            conn.setRequestMethod(dataConnect.getMethod());
            conn.setDoInput(true);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode !=200){
                throw new IOException("Got response code"+ responseCode);
            }
            is=conn.getInputStream();
            return readStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is !=null){
                is.close();
            }
        }
        return null;
    }//close getUrl

    private static String readStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        BufferedOutputStream outStream =null;

        try {
            int length =0;
            outStream = new BufferedOutputStream(byteOutput);
            while ((length= stream.read(buffer))>0){
                outStream.write(buffer,0,length);
            }
            outStream.flush();
            return byteOutput.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (outStream !=null){
                outStream.close();
            }
        }

    }//close readStream


}//close httphelper

