package com.example.testwreckdetection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;


@SuppressLint({ "NewApi", "HandlerLeak" })
public class ServiceStatusActivity extends Activity implements OnCheckedChangeListener {
	
	

	private Intent intent,acc_intent,sound_intent;
	Switch switch_service;
	 NotificationManager mNotificationManager;
	 int NotificationId=10;
	 TextView latitude,longitude,speedView, sound_dp,  Acc_txt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_status);
        switch_service=(Switch)findViewById(R.id.switch_service);
         latitude = (TextView) findViewById(R.id.lat_cord);
         longitude = (TextView) findViewById(R.id.lon_cord);
         speedView = (TextView) findViewById(R.id.speed_value);
        sound_dp=(TextView)findViewById(R.id.sound_value);
        Acc_txt=(TextView)findViewById(R.id.acc_axsie);
        acc_intent=new Intent(getApplicationContext(), Accessing_Accelerometer_Sensor.class);
        intent = new Intent(this, Accessing_GPS_Reciever.class);
        sound_intent=new Intent(getApplicationContext(), Accessing_Microphone.class);
       mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        switch_service.setOnCheckedChangeListener(this);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        updateUI(intent);       
        }
    };    
    
    private BroadcastReceiver ACCbroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	updateAccUI(intent);       
        }
    };
    private BroadcastReceiver SoundbroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	updateSoundUI(intent);       
        }
    };
    
   @Override
	public void onResume() {
		super.onResume();		
	
		registerReceiver(broadcastReceiver, new IntentFilter(Accessing_GPS_Reciever.BROADCAST_ACTION));
		registerReceiver(ACCbroadcastReceiver, new IntentFilter(Accessing_Accelerometer_Sensor.BROADCAST_ACTION));
		registerReceiver(SoundbroadcastReceiver, new IntentFilter(Accessing_Microphone.BROADCAST_ACTION));
	}
	
	protected void updateSoundUI(Intent intent2) {
	// TODO Auto-generated method stub
		
		String soundLevel=intent2.getStringExtra("sound");
		sound_dp.setText(soundLevel);
		
	
}

	protected void updateAccUI(Intent intent2) {
		// TODO Auto-generated method stub
	    float x=intent2.getFloatExtra("float_x", (float) 100.0);
	    float y=intent2.getFloatExtra("float_y", (float) 100.0);
	    float z=intent2.getFloatExtra("float_z", (float) 100.0);
	   
	    Acc_txt.setText("X:"+x+"\n Y:"+y+"\n Z:"+z);
	}

	
	    
    private void updateUI(Intent intent) {
    	
    
       double lat=intent.getDoubleExtra("lat", 0.0);
       double lon=intent.getDoubleExtra("lon", 0.0);
       float speed =intent.getFloatExtra("speed", 0);
       this.latitude.setText(""+lat);
       longitude.setText(""+lon);
       this.speedView.setText(""+speed+"Km/h");
    }

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		if (!arg1)
		{unregisterReceiver(broadcastReceiver);
		unregisterReceiver(ACCbroadcastReceiver);
		unregisterReceiver(SoundbroadcastReceiver);
		stopService(intent);
		stopService(acc_intent);
		stopService(sound_intent);
		mNotificationManager.cancel(NotificationId);
	}
		else
		{
			startService(intent);
			startService(acc_intent);
			startService(sound_intent);
			registerReceiver(broadcastReceiver, new IntentFilter(Accessing_GPS_Reciever.BROADCAST_ACTION));
			registerReceiver(ACCbroadcastReceiver, new IntentFilter(Accessing_Accelerometer_Sensor.BROADCAST_ACTION));
			registerReceiver(SoundbroadcastReceiver, new IntentFilter(Accessing_Microphone.BROADCAST_ACTION));
			
		}
		
	}
	
	
	
	
	
}
