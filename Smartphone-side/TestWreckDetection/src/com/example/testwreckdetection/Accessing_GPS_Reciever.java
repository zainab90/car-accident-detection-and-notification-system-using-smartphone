package com.example.testwreckdetection;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class Accessing_GPS_Reciever extends Service implements LocationListener  {

	LocationManager mLocationManager;
	 Accident_Detection_Confirmer mAccidentWreckDetection;
		public static final String BROADCAST_ACTION = "com.example.testwreckdetection.displayLocationevent";
		Intent intent;
		private final Handler Loc_handler = new Handler();
		double lat,lon;

		
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	     mLocationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
	     intent=new Intent(BROADCAST_ACTION);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLocationManager.removeUpdates(this);
		
	}
		
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		Loc_handler.removeCallbacks(sendUpdatesToUI);
		Loc_handler.postDelayed(sendUpdatesToUI, 50); // 1 second
		
		return START_STICKY;
	}
	 
	private Runnable sendUpdatesToUI = new Runnable() {
	    	public void run() {
	    		SendingLocationInfo();    		
	    		Loc_handler.postDelayed(this, 50); // 10 seconds
	    	}
	  };  
	    
	    
	 private void SendingLocationInfo() {
     intent.putExtra("lat", lat);
     intent.putExtra("lon", lon);
	    sendBroadcast(intent);
	    }
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		if (location.getAccuracy() <= 40.0F)
		{ 
		lat=location.getLatitude();
		lon=location.getLongitude();
			//mAccidentWreckDetection=Accident_Detection_Confirmer.get(getApplicationContext());
			//mAccidentWreckDetection.checkTheSpeed(location.getSpeed(), lat,lon);
	
		}
		else 
		{
			return;
		}
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	

}
