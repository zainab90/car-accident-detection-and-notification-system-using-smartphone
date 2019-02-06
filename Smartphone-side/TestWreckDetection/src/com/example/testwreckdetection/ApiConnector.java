package com.example.testwreckdetection;

import android.content.Context;
import android.util.Log;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by tahseen0amin on 16/02/2014.
 */
public class ApiConnector {


    static InputStream is = null;
    String json_str;
    HttpEntity httpEntity;
    JSONObject json;
    BufferedReader reader;
    DefaultHttpClient httpClient;
    HttpPost httpPost;
    HttpResponse httpResponse;
    
    public JSONObject InsertIntoDB( List<NameValuePair> params ,String url)
    {
    	 json=null;
    //	 String url = "http://192.168.0.102/WebService/add_accident_record.php";
    	     httpEntity = null;
    	    try
            {

                 httpClient = new DefaultHttpClient();  // Default HttpClient
                 httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
 
                 httpResponse = httpClient.execute(httpPost);
                 httpEntity = httpResponse.getEntity();
                 is = httpEntity.getContent();


            } catch (ClientProtocolException e) {

                // Signals error in http protocol
              e.printStackTrace();
          
            	
} catch (IOException e) {
               e.printStackTrace();

            }
    	    
    	    try {
                 reader = new BufferedReader(new InputStreamReader(
                        is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json_str = sb.toString();
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            

            }
    	    try {
				json=     new JSONObject(json_str.substring(json_str.indexOf("{"), json_str.lastIndexOf("}") + 1));
			      }
    	    
    	    
    	    
    	    catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
          Log.d("JSONException", e.getMessage());

				
			}
    	    return json;
    	
   
    	
    	
    	
    }
}
