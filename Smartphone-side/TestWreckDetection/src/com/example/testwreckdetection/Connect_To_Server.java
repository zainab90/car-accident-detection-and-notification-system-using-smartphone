package com.example.testwreckdetection;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class Connect_To_Server  extends AsyncTask<ApiConnector, Long,JSONObject>{
double acc,lat,lon,speed=0.0;
String imgVidUrl,deviceId="";
private static final String TAG_SUCCESS = "success";
Context ctx;
String state="true";
	public Connect_To_Server(Context ctx,String img_vid_uri,Double lat,Double lon,Double acc,Double speed,String deviceId  ){
		this.acc=acc;
		this.lat=lat;
		this.lon=lon;
		this.speed=speed;
		this.imgVidUrl=img_vid_uri;
		this.ctx=ctx;
		this.deviceId=deviceId;
	}
	
	@Override
	protected JSONObject doInBackground(ApiConnector... params_x) {
		// TODO Auto-generated method stub
		List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("largestAccel", String.valueOf(acc)));
        params.add(new BasicNameValuePair("speed",  String.valueOf(speed)));
        params.add(new BasicNameValuePair("lat",  String.valueOf(lat)));
        params.add(new BasicNameValuePair("lon",  String.valueOf(lon)));
        params.add(new BasicNameValuePair("phoneId",  deviceId));
        params.add(new BasicNameValuePair("showState",state  ));
        params.add(new BasicNameValuePair("vidUrl",  imgVidUrl));
        String url = "http://192.168.0.102//WebService/add_accident_record.php";
         return params_x[0].InsertIntoDB(params,url);
	}

	  @Override
      protected void onPostExecute(JSONObject jsonObject) {
      	
      	if (jsonObject != null)
      	{
      	
      	
      	  // check log cat fro response
          Log.d("Create Response", jsonObject.toString());
          try {
              int success = jsonObject.getInt(TAG_SUCCESS);

              if (success == 1) {
                  // successfully created product
               
Toast.makeText(ctx, "insert record in DB", Toast.LENGTH_SHORT).show();
                  // closing this screen
                
              } else {
                  // failed to create product
              	Toast.makeText(ctx, "fail to insert in DB.", Toast.LENGTH_SHORT).show();
              }
          } catch (JSONException e) {
              e.printStackTrace();
              Toast.makeText(ctx, "JSONException is :"+ e.getMessage(), Toast.LENGTH_SHORT).show();
   
          }
        
      	}
      	else
      	{
      		Toast.makeText(ctx, "error.. json object is null", Toast.LENGTH_SHORT).show();
      	}

      }
	
}
