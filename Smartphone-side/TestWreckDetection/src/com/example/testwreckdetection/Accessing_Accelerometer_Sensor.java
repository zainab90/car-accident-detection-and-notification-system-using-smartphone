package com.example.testwreckdetection;

import java.util.Date;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class Accessing_Accelerometer_Sensor  extends Service implements SensorEventListener{
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	private static final String TAG = "BroadcastService";
	public static final String BROADCAST_ACTION = "com.example.testwreckdetection.displayevent";
	Intent intent;
	int counter = 0;
	SensorManager mSensorManager;
	Sensor Acceleraton_Sensor;
	private final Handler Ac_handler = new Handler();
	float x,y,z;
	Accident_Detection_Confirmer mAccidentWreckDetection;
	private NotificationManager mNM;
	Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	int NotificationId=10;
	boolean isRunning=false;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	public void onCreate() {
		super.onCreate();

    	intent = new Intent(BROADCAST_ACTION);	
	}
	
	  @Override
	    public void onStart(Intent intent, int startId) {
	        Ac_handler.removeCallbacks(sendUpdatesToUI);
	        Ac_handler.postDelayed(sendUpdatesToUI, 1000); // 1 second
	        ShowNotification();
	   
	    }
		private void ShowNotification() {
			// TODO Auto-generated method stub
			 mNM = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
			 NotificationCompat.Builder mBuilder =
				        new NotificationCompat.Builder(this)
				        .setSmallIcon(R.drawable.ic_launcher)
				        .setContentTitle("Accident Detection Service")
				        .setContentText("Service is Running")
				        .setSound(alarmSound)
				        .setLights(Color.GREEN, 500, 500)
				      
				        .setTicker("Srevice Status");
				// Creates an explicit intent for an Activity in your app
				Intent resultIntent = new Intent(this, ServiceStatusActivity.class);

				TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
				// Adds the back stack for the Intent (but not the Intent itself)
				stackBuilder.addParentStack(ServiceStatusActivity.class);
				// Adds the Intent that starts the Activity to the top of the stack
				stackBuilder.addNextIntent(resultIntent);
				PendingIntent resultPendingIntent =
				        stackBuilder.getPendingIntent(
				            0,
				            PendingIntent.FLAG_UPDATE_CURRENT
				        );
				mBuilder.setContentIntent(resultPendingIntent);
				mBuilder.setAutoCancel(false).setOngoing(true);
				mNM.notify(NotificationId, mBuilder.build());
		}
	  private Runnable sendUpdatesToUI = new Runnable() {
	    	public void run() {
	    		mSensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    		if (mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0)
	    		{
	    			Acceleraton_Sensor=mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
	    			mSensorManager.registerListener(Accessing_Accelerometer_Sensor.this, Acceleraton_Sensor, SensorManager.SENSOR_DELAY_NORMAL);
	    			Log.d("b", "begin");
	    		}
	    		
	    		DisplayLoggingInfo();    		
	    		Ac_handler.postDelayed(this, 1000); // 10 seconds
	    	}
	  };  
	    
	    
	    private void DisplayLoggingInfo() {
	    	Log.d(TAG, "entered DisplayLoggingInfo");
       intent.putExtra("float_x", x);
       intent.putExtra("float_y", y);
       intent.putExtra("float_z", z);
       intent.putExtra("run", isRunning);
	    sendBroadcast(intent);
	    }
	    
	    

		@Override
		public void onDestroy() {		
	        Ac_handler.removeCallbacks(sendUpdatesToUI);
	        mSensorManager.unregisterListener(this);
			super.onDestroy();
		}
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onSensorChanged(SensorEvent e) {
			// TODO Auto-generated method stub
		
		x= e.values[0];
		y= e.values[1];
		z=e.values[2];
		mAccidentWreckDetection=Accident_Detection_Confirmer.get(getApplicationContext());
 		mAccidentWreckDetection.checkTheAcceleration(Math.max(Math.max(z, x),y));
			
		}
	  
}
