package com.example.testwreckdetection;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("NewApi")
public class Activate_Accident_Detection extends Activity implements  OnClickListener {

	private NotificationManager mNM;
	Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
	int NotificationId=10;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		

	}

@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}

boolean isRunning;
ImageButton activate_Button;
Accessing_Accelerometer_Sensor  mAccelerationService;
boolean Acce_bound,Loca_bound;
Accessing_GPS_Reciever mLocationService;
boolean startService;


	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accident_detection);
		activate_Button=(ImageButton)findViewById(R.id.activate_service);
	    activate_Button.setOnClickListener(this);

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
			Intent resultIntent = new Intent(this, SpeedTestWithInterleavedPeriod.class);

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(SpeedTestWithInterleavedPeriod.class);
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
	 

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		
		
		
	}

	

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "activate", Toast.LENGTH_LONG).show();
		activate_Button.setBackground(getResources().getDrawable(R.drawable.service_on_1));
		/*
		startService(new Intent(getApplicationContext(), Accessing_Accelerometer_Sensor.class));
		startService(new Intent(getApplicationContext(), Accessing_GPS_Reciever.class));
		startService(new Intent(getApplicationContext(), Accessing_Microphone.class));
		//new FindSD().StartTimer();
		 
		 */
	ShowNotification();
		
		
		
	}
	
	
	
	
	
	}
	