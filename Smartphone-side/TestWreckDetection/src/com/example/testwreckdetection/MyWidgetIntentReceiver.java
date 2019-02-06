package com.example.testwreckdetection;

import java.lang.annotation.Retention;

import com.example.testwreckdetection.Second_startup_Activity.ConnectionDetector;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.RemoteViews;

/**
 * Created by Jacek Milewski
 * looksok.wordpress.com
 */

public class MyWidgetIntentReceiver extends BroadcastReceiver {

	private static int clickCount = 0;
	LocationManager lm;
  
    
	@Override
	public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals("testwreckdetection.intent.action.CHANGE_PICTURE")){
			updateWidgetPictureAndButtonListener(context);
		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
		remoteViews.setImageViewResource(R.id.widget_image, getImageToSet(context));
		//REMEMBER TO ALWAYS REFRESH YOUR BUTTON CLICK LISTENERS!!!
		remoteViews.setOnClickPendingIntent(R.id.widget_image, MyWidgetProvider.buildButtonPendingIntent(context));
		
		MyWidgetProvider.pushWidgetUpdate(context.getApplicationContext(), remoteViews);
	}

	private int getImageToSet(Context context) {
		
		clickCount++;
		if (clickCount % 2==0)
		{ 
			context.stopService(new Intent(context, Accessing_Accelerometer_Sensor.class));
			context.stopService(new Intent(context, Accessing_GPS_Reciever.class));
			context.stopService(new Intent(context, Accessing_Microphone.class));
			 
			return R.drawable.widget_off_1; 
			
		
		
		
		
		
		}
		else
		{//context.stopService(new Intent(context, MyService.class));
			
			
			
			
			context.startService(new Intent(context, Accessing_Accelerometer_Sensor.class));
			context.startService(new Intent(context, Accessing_GPS_Reciever.class));
			context.startService(new Intent(context, Accessing_Microphone.class));
			
			
			
			return R.drawable.widget_on_1;
			
		
		
		}
		
		
		
	}
	
	
	
	public class CheckForGps extends AsyncTask<Context, Integer, Boolean>
	 {
Context ctx;
		@Override
		protected Boolean doInBackground(Context... params) {
			
			
			clickCount++;
			if (clickCount % 2==0)
			{ 
				params[0].stopService(new Intent(params[0], Accessing_Accelerometer_Sensor.class));
				params[0].stopService(new Intent(params[0], Accessing_GPS_Reciever.class));
				params[0].stopService(new Intent(params[0], Accessing_Microphone.class));
				 
				
			
			}
			
			
			
			
			
			// TODO Auto-generated method stub
			lm=(LocationManager)params[0].getSystemService(Context.LOCATION_SERVICE);
			ctx=params[0];
		    
				if (! lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
				{
					return false;
				}
				else
					return true;
			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (! result)
			{ Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			ctx. startActivity(intent);
		}
			else
			{
				// check for network connection
				
				new ConnectionDetector().execute(ctx);
				
			}
		}
	}
	
	public class ConnectionDetector extends AsyncTask<Context, Integer, Boolean>
	{

		@Override
		protected Boolean doInBackground(Context... params) {
			// TODO Auto-generated method stub
			
			
			ConnectivityManager connectivity = (ConnectivityManager) params[0].getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null) 
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null) 
	                  for (int i = 0; i < info.length; i++) 
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
	  }
	          return false;}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if ( ! result)
			{
				//showAlertDialog(Second_startup_Activity.this, "Internet Connection",
				  //      "You don't have internet connection..Please check your connection");
		
			
			}
			else
			{
				
			}
			
			
		}
	
	
	
	}}
