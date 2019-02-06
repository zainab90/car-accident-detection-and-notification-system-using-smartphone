package com.example.testwreckdetection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


@SuppressLint("SimpleDateFormat")
public class AttachLoctionToImg_Vid extends  AsyncTask<ApiConnector, Long,JSONObject> {
    private static final String TAG_SUCCESS = "success";
    String deviceId;
    double lat,lon;
    String img_vid_uri;
    ApiConnector mApiConnector;
    Context context;
    static int success;
    RowItem r;
    UploadListFragment mUploadListFragment;
    int index;
    String repType;
   

    
    public AttachLoctionToImg_Vid(Context ctx,String img_vid_uri,Double lat,Double lon,RowItem r,UploadListFragment m,int index,String repType)
    { 
    	this.img_vid_uri=img_vid_uri;
    	this.lat=lat;
    	this.lon=lon;
    	this.context=ctx;
    	this.r=r;
    	this.mUploadListFragment=m;
    	this.index=index;
    	this.repType=repType;
    
    	
    	
  }
    
  
    
    
	@Override
	protected JSONObject doInBackground(ApiConnector... arg0) {
		// TODO Auto-generated method stub
		
		TelephonyManager man = (TelephonyManager)context.
	              getSystemService(Context.TELEPHONY_SERVICE);
	            deviceId = man.getDeviceId();
	            String time = new SimpleDateFormat("yyyy-MM-dd HH:mmZ").format(new Date());
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
      
        params.add(new BasicNameValuePair("imgVidUrl", img_vid_uri));
        params.add(new BasicNameValuePair("lat", String.valueOf(lat)));
        params.add(new BasicNameValuePair("lon",  String.valueOf(lon)));
        params.add(new BasicNameValuePair("phoneId", deviceId));
        params.add(new BasicNameValuePair("time", time));
        params.add(new BasicNameValuePair("showState", "true"));
        params.add(new BasicNameValuePair("reportType", repType));

   //     String url = "http://192.168.0.102//WebService/addToBystandesTb.php";
        String url="http://emergencyservice.hostingsiteforfree.com/addToBystandesTb.php";
        return arg0[0].InsertIntoDB(params,url);
		
	}
	
    protected void onPostExecute(JSONObject jsonObject) {
      	

    	
      	if (jsonObject != null)
      	{
      	
      	
      	  // check log cat fro response
          Log.d("Create Response", jsonObject.toString());
          try {
               success = jsonObject.getInt(TAG_SUCCESS);

              if (success == 1) {
                  // successfully created product
            	 // Log.d("Create Response", "insert");
Toast.makeText(context, "insert", Toast.LENGTH_SHORT).show();
r=mUploadListFragment.uploao_Apapter.getItem(index);
r.setProgressVisiblity(View.INVISIBLE);
 r.setDownloadStatus("upload is complete.");
 mUploadListFragment.uploao_Apapter.notifyDataSetChanged();

                  // closing this screen
                
              } else {
                  // failed to create product
            	  //Log.d("Create Response", "fail");
            	  Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
              }
          } catch (JSONException e) {
            //  e.printStackTrace();
        		Toast.makeText(context, "JSON exception", Toast.LENGTH_SHORT).show();
        		
          }
        
      	}
      	else
      	{
      		Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
      		 Log.d("Create Response", "error");
      	}

      }
	
	
	
}
