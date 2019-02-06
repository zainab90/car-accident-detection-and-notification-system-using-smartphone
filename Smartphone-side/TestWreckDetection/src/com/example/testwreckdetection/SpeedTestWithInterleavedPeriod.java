package com.example.testwreckdetection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.Timer;









import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SpeedTestWithInterleavedPeriod extends Activity implements LocationListener, OnCheckedChangeListener{

LocationManager mLocationManager;
private Location oldLocation;
float maxSpeed,distance;
TextView maxSpeedLabel,speedLabel,Loc_label,SpeedStatus,Accelerometer_tv, Sound_tv;
float unitconversion = 3.6F;
private int unit = 1;
String unitNameDistance;
String unitName;
float unitConversionDistance = 1.0F;
NotificationManager mNotificationManager;
int NotificationId=10;
TextView view_SD;

List<Float>speedValues= new ArrayList<Float>();
float mean=0F;
float summation,sub,sum,SD=0F;
int No_minutes=0;
Timer timer;
TimerTask timerTask;
 String strDate,TotalSpeed;
 String Total="[";
 int period, counter,SSDIsgreaterSSP=0;
 boolean start=true;
Thread t= new Thread();
boolean speedState=false;
boolean period_timer_state, timer_state, MP, CanIrecordVideo,CanISendSMS,AirbagStae,HighSpeedFlag=false;
double lat,lon=0.0;
static final int MY_MSG = 1;
static final int MAXOVER_MSG = 2;
static final int ERROR_MSG = -1;
Boolean mMode = false; // false -> fast , true -> slow
Boolean mCalib = false;
Boolean mLog = false;
Boolean mMax = false;
Switch switch_service;

List <Float> previous_speed= new ArrayList<Float>();

//we are going to use a handler to be able to run in our TimerTask
final Handler handler = new Handler();

String state,path;
File PathFile,mYFile=null;
boolean CanR,CanW;
BufferedWriter out;
 File root,gpxfile;
 FileWriter gpxwriter;
int size=0;
long t1, t2;


float Speed_threshold=(float) 6.667;
float Send_Speed=0;
SensorManager mSensorManager;
Sensor Acceleraton_Sensor;
AccelerationThread Acc_thread;

String Scenario="";
int elapsedPeriod=0;
SplEngine mEngin=null;


CountDownTimer PeriodTimer= new CountDownTimer(30000,1000) {
	
	@Override
	public void onTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
		period_timer_state=true;
		MP=true;
		elapsedPeriod++;
		SpeedStatus.setText("speed is changed from high to low during "+elapsedPeriod);
	}
	
	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		elapsedPeriod=29;// finish 30s
		period_timer_state=false;
		speedState=false;
		calculateSD();
		
	}
};

Thread timer2 = new Thread (){

	@Override
	public void run() {
		// TODO Auto-generated method stub
	
		try {
			Calendar calendar = Calendar.getInstance();

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
			strDate = simpleDateFormat.format(calendar.getTime());
			out.append(String.valueOf(0.0)+","+String.valueOf(strDate)+","+String.valueOf(0.0));
		       out.newLine();
		       
		     
		     		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally{
runOnUiThread(new Runnable() {

@Override
public void run() {
	// TODO Auto-generated method stub
	if (!speedState)
	{
// still low speed
		Toast.makeText(getApplicationContext(), "wake up and speed is low", Toast.LENGTH_SHORT).show();

		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
		strDate = simpleDateFormat.format(calendar.getTime());
	calculateSD();
	period=15000;
	startTimer();
	}
	
	// speed state = true
	else
	{
		Toast.makeText(getApplicationContext(), "wake up and speed is high", Toast.LENGTH_SHORT).show();

	}
}
});				
		
		}	
	}
};
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lunch_activity);
	    this.distance = 0.0F;
	      this.maxSpeed = 0.0F;
		maxSpeedLabel=(TextView)findViewById(R.id.txt_max_speed);
		speedLabel=(TextView)findViewById(R.id.txt_speed_label);
		Loc_label=(TextView)findViewById(R.id.txt_distance_label);
		
		view_SD=(TextView)findViewById(R.id.tv_SD);
		SpeedStatus=(TextView)findViewById(R.id.tv_speed_status);
        switch_service=(Switch)findViewById(R.id.switch_service);
        switch_service.setOnCheckedChangeListener(this);

		Accelerometer_tv=(TextView)findViewById(R.id.tv_accelerometer);
		Sound_tv=(TextView)findViewById(R.id.tv_sound);
		
	       mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	       CanIrecordVideo=true;
	       CanISendSMS=true;
	       AirbagStae=true;
		mLocationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
	//speedValues.add((float) 0);
		setup();
		CreateNewFile();
		Acc_thread=new AccelerationThread(getApplicationContext());
		Acc_thread.start_Engin();
	start_meter();
		
	}

	
	
	
	public void start_meter()
	{
		mEngin=new SplEngine(mh, getApplicationContext());
		mEngin.start_engine();
		
	}
	
	
	Handler mh=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
		        switch (msg.what) {
		                case MY_MSG :
		                	
		                	Sound_tv.setText("Sound dB level is:"+msg.obj.toString());
		               
		                        break;
		                case MAXOVER_MSG :
		                        mMax = false;
		                        handle_mode_display();
		                      
		                        break;
		                case ERROR_MSG:
		                 
		                        stop_meter();
		                    	mEngin.start_engine();
		                        break;
		                default :
		                        super.handleMessage(msg);
		                        break;
		        }
		}

		private void handle_mode_display() {
			// TODO Auto-generated method stub
			
		}

		

		};
	
		private void stop_meter() {
			// TODO Auto-generated method stub
			 mEngin.stop_engine();
			
		}
		
	
private void CreateNewFile() {
		// TODO Auto-generated method stub
	checkState();
	
	if (CanR=CanW=true)
	{
		
		try {
		     root = Environment.getExternalStorageDirectory();
		     
		    if (root.canWrite()){
		        gpxfile = new File(root, "Speed_SD.csv");
		        gpxwriter = new FileWriter(gpxfile,true);
		       out = new BufferedWriter(gpxwriter);
out.append("Standard_Deviation"+","+"time"+","+"speed_values");
		       out.newLine();
		    }
		} catch (IOException e) {
		    //Log.e(, "Could not write file " + e.getMessage());
e.printStackTrace();
		
	}
		
}
		}


private void checkState() {
	// TODO Auto-generated method stub
	state=Environment.getExternalStorageState();
    // now cheak what state is if we can access to sd card or not
    if (state.equals(Environment.MEDIA_MOUNTED))//MEDIA_MOUNTED it mean we can read and write
    {	
	     CanR=CanW=true;
    }
    else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
    { // read but not write
    	
		CanW=false;
		CanR=true;}
		else 
			//it can't read or write
		{
		CanR=CanW=false;}
	
}
	
	

	@Override
	public void onLocationChanged(Location paramLocation) {
		// TODO Auto-generated method stub
		float speed_unit=3.6F;
		  if (paramLocation.getAccuracy() <= 40.0F)
		  try
		    {
			  lat =paramLocation.getLatitude();
			  lon=paramLocation.getLongitude();
			 speedLabel.setText(getUnitSpeed(paramLocation.getSpeed(),0)+"  accuarcy is  "+paramLocation.getAccuracy());
			 Loc_label.setText("Latitude is"+lat+"\nLongitude is"+lon);
			 
			 // low speed
			 if (paramLocation.getSpeed() < Speed_threshold && !speedState)
			 {
				 SpeedStatus.setText("low speed");
				 HighSpeedFlag=false;
				 
			 speedValues.add(paramLocation.getSpeed() * speed_unit); 
			 Send_Speed=paramLocation.getSpeed() * speed_unit;
			
			 // start calculate sd.
			 if (timer2.getState()== Thread.State.NEW)
			 {
				 timer2.start();
			 }
			 
			 
			 }
			 
			 // high speed
			 if (paramLocation.getSpeed() > Speed_threshold) 
			 {
				
				 speedState=true;
				 SD=0;
				 elapsedPeriod=0;
	               HighSpeedFlag=true;
				 SpeedStatus.setText("hihg speed");
				 // check if there is count down timer in the background
				 if (period_timer_state)
				 {
					 PeriodTimer.cancel();
					 
					 // new addition
					 period_timer_state=false;
				 }
				 
				 // new addition
				 if (speedValues.size() !=0)
				 {
					 speedValues.clear();
				 }
				 
				 
				 Send_Speed= paramLocation.getSpeed()* speed_unit;
				
			 }
			 
			 // travelling in high speed and then reducing the speed
			 if (paramLocation.getSpeed() < Speed_threshold && speedState)
			 {
				 speedValues.add(paramLocation.getSpeed() * speed_unit); 
				 Send_Speed=paramLocation.getSpeed() * speed_unit;
				// SpeedStatus.setText("speed is changed from high to low");
				 HighSpeedFlag=false;
				 
				 
				 
				 
				 if (!period_timer_state) // for first start only 
			
				{
				elapsedPeriod=0;
				PeriodTimer.start();
			 	}
				
			 
			 }
			 
			 
		      return;
		    }
		    catch (RuntimeException localRuntimeException)
		    {
		     // while (true)
		        Log.v("onLocationchange", ""+localRuntimeException);
		    }
		
	}

	

	@SuppressWarnings("unused")
	private String getUnitDistance(float paramFloat)
	  {
	    @SuppressWarnings("resource")
		Formatter localFormatter = new Formatter();
	    StringBuilder localStringBuilder = new StringBuilder();
	    Object[] arrayOfObject = new Object[1];
	    arrayOfObject[0] = Float.valueOf(paramFloat / 1000.0F * this.unitConversionDistance);
	    return localFormatter.format("%.2f", arrayOfObject) + " " + this.unitNameDistance;
	  }

	 @SuppressWarnings({ "resource", "unused" })
	private String getUnitSpeed(float paramFloat, int paramInt)
	  {
	    Formatter localFormatter = new Formatter();
	    StringBuilder localStringBuilder = new StringBuilder();
	    String str = "%." + paramInt + "f";
	    Object[] arrayOfObject = new Object[1];
	    arrayOfObject[0] = Float.valueOf(paramFloat * this.unitconversion);
	    return localFormatter.format(str, arrayOfObject) + " " + this.unitName;
	  }

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	private void setupUnits()
	  {
	    if (this.unit == 2)
	    {
	  //    this.speedoMaxSpeed = 83.300003F;
	      this.unitconversion = 2.236F;
	     this.unitName = "mph";
	      this.unitConversionDistance = 0.6213F;
	     this.unitNameDistance = "mls";
	   
	      return;
	    }
	    
	 //    this.speedoMaxSpeed = 80.400002F;
	   this.unitconversion = 3.6F;
	   this.unitName = "km/h";
	    this.unitConversionDistance = 1.0F;
	    this.unitNameDistance = "km";
	
	  }
	
	
	 private void setup()
	  {
	    
	    setupUnits();
	 
	    this.speedLabel.setText(getUnitSpeed(0.0F, 0));
	    this.Loc_label.setText(this.distance+"m");
	    this.maxSpeedLabel.setText("Time is:");
	  }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mLocationManager.removeUpdates(this);
		stoptimertask();
		Acc_thread.stopEngin();
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		//startTimer();
	}

	private void startTimer() {
		// TODO Auto-generated method stub
		//set a new Timer
		timer = new Timer();
		
		//initialize the TimerTask's job
		initializeTimerTask();
		
		//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
		timer.schedule(timerTask, 0, period); //
		
	}
	public void initializeTimerTask() {
		
		timerTask = new TimerTask() {
			public void run() {
				
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
				strDate = simpleDateFormat.format(calendar.getTime());
				//use a handler to run a toast that shows the current timestamp
				handler.post(new Runnable() {
					public void run() {
						//get the current timeStamp
					maxSpeedLabel.setText("Time is"+ strDate);
			CalculateSD15Sec();	
					}
				});
			}
		};
	}
	
	
	public void stoptimertask() {
		//stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
			  try {
				out.flush();
				 out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		      
		}
	}
	
	

	
	
	
	private void calculateSD() {
		// TODO Auto-generated method stub
		mLocationManager.removeUpdates(this);
		
		new Thread (new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			elapsedPeriod=0;	
				summation=0;
				mean=0;
				sub=0;
				sum=0;
				SD=0;

				if ( !speedValues.isEmpty() )
				{
				
					
                     // all speed values are in km/h
					// keep the the values of speed samples in array for next period of 15 sec
					
				for (int k=speedValues.size()/2; k < speedValues.size();k++ )
				{
					previous_speed.add(speedValues.get(k));
				     
				}
					
					
					
					
				for (int i=0; i< speedValues.size();i++)
				{
					summation+=speedValues.get(i);
						
				}
				mean=summation/speedValues.size();
				
				for (int j=0; j< speedValues.size();j++)
				{
					sub=(float) Math.pow(speedValues.get(j)-mean, 2);
					sum+=sub;
					
				}
				
				SD=(float) Math.sqrt(sum/speedValues.size());
				TotalSpeed=String.valueOf(speedValues);
				speedValues.clear();
				if (SD > 2)
				{
					SSDIsgreaterSSP=1;
				}
				
				
				else
				{

					SSDIsgreaterSSP=0;
					
				}
				
				try {
					out.append(String.valueOf(SD)+","+String.valueOf(strDate)+","+TotalSpeed);
				       out.newLine();
				       
				     
				     		} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						view_SD.setText("Standard deviation is "+SD+"\n");
						//Toast.makeText(getApplicationContext(), ""+siz, Toast.LENGTH_LONG).show();
						
					}
				});
				
				}
				
			}
		}).start();
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		

		
	}


	
	private void CalculateSD15Sec()
	{
		
		mLocationManager.removeUpdates(this);
		
		new Thread (new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				elapsedPeriod=0;
				summation=0;
				mean=0;
				sub=0;
				sum=0;
				SD=0;
				counter=0;
				if ( !speedValues.isEmpty() )
				{
					
					// read samples from previous period
					for (int y=0; y< previous_speed.size();y++)
					{
						speedValues.add(previous_speed.get(y));
					}
					
					
					for (int i=0; i< speedValues.size();i++)
				{
					summation+=speedValues.get(i);
					
					
				}
				mean=summation/speedValues.size();
				
				for (int j=0; j< speedValues.size();j++)
				{
					sub=(float) Math.pow(speedValues.get(j)-mean, 2);
					sum+=sub;
					
				}
				
				SD=(float) Math.sqrt(sum/speedValues.size());
				
				// keep the values for next period but before, clear all last values
				previous_speed.clear();
				
				for (int k=0; k < speedValues.size()/2;k++ )
				{
					previous_speed.add(speedValues.get(k));
				     
				}
				 
			
				
				// save in right way
			Total=String.valueOf(speedValues);
				
			if (SD > 2)
			{
				SSDIsgreaterSSP=1;
			}
			
			
			else
			{

				SSDIsgreaterSSP=0;
				
			}	
			
			
			
			
				
				speedValues.clear(); // now, just the previous_speed list has values and speedValues is cleared to save new speed values for next period.
				
				try {
					out.append(String.valueOf(SD)+","+String.valueOf(strDate)+","+Total);
				       out.newLine();
				       
				     
				     		} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						view_SD.setText("Standard deviation is "+SD+"\n");
						//Toast.makeText(getApplicationContext(), ""+siz, Toast.LENGTH_LONG).show();
						
					}
				});
				
				}
				
			}
		}).start();
		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		

		
		
		
	}
	
	

	
	
	
	public class AccelerationThread extends Thread implements SensorEventListener{
		SensorManager mSensorManager;
		Sensor Acceleraton_Sensor;
		float sensorX,sensorY,sensorZ=1;
		//private Handler Ah;
		Context ctx;
		boolean mIsRunning;
		double Acc_thr= 19.6;
		double Send_Acc;
		
		public AccelerationThread(Context applicationContext) {
			// TODO Auto-generated constructor stub
			//this.Ah=ah;
			this.ctx=applicationContext;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			mSensorManager=(SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
			if (mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size()!=0)
			{
				Acceleraton_Sensor=mSensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
				mSensorManager.registerListener(this, Acceleraton_Sensor, SensorManager.SENSOR_DELAY_NORMAL);
				Log.d("b", "begin");
			}
			else 
			{//Toast.makeText(ctx.getApplicationContext(), "sorry your phone dosn't have an Acceleration Sensor..\n the service dosn't work ", Toast.LENGTH_LONG).show();
			stopEngin();}
			
		}

		public void stopEngin() {
			// TODO Auto-generated method stub
			this.mIsRunning=false;
			mSensorManager.unregisterListener(AccelerationThread.this);
			Log.d("stop", "thread");
			
		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSensorChanged(SensorEvent arg0) {
			// TODO Auto-generated method stub
			
			sensorX=arg0.values[0];
			sensorY=arg0.values[1];
			sensorZ=arg0.values[2];
			final Date d= new Date();
			
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.sleep(16);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Accelerometer_tv.setText("x :"+ sensorX+"\n Y: "+sensorY+"\n Z:"+sensorZ);
				}
			});
			
			
		
		 Send_Acc =Math.max(Math.max(sensorZ, sensorX),sensorY);
		 
		 if ((Send_Acc / Acc_thr > 1) && HighSpeedFlag)
		 {
			
			 
			 t1=d.getTime();
			 Scenario="first Scenario";
			 SD=0;
			 elapsedPeriod=0;
		SendParameters(Send_Acc);	 
		 
		 }
		 
		 else if ( (Send_Acc / Acc_thr) + (SSDIsgreaterSSP) > 2)
		 {
			 t1=d.getTime();
			 Scenario="second Scenario"; 
			 elapsedPeriod=0;
				SendParameters(Send_Acc);	 

		 }
		 
		 else if ((Send_Acc / Acc_thr > 1) && period_timer_state )
		 {
			 
		
			 t1=d.getTime();
			 Scenario="third Scenario";
			 SD=0;
			SendParameters(Send_Acc);
			
				

		 }
		
	
		
		 
		 
		 
		 else
		 {Scenario="no accident";
		 }
		 
		 
	
	
		 
		 

		

		
	
		
		}

		public void start_Engin() {
			// TODO Auto-generated method stub
			this.mIsRunning=true;
			if (this.getState()== Thread.State.NEW)
			{this.start();
		}
			
		}
	}
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		Acc_thread.stopEngin();
	stop_meter();
		
		// stop count down timer
		if (period_timer_state)
		{PeriodTimer.cancel();}
		
	// stop timer
		if (timer != null)
	{timer.cancel();}
		
	}
	
	private void SendParameters( double f)
	{
		Date dd=new Date();
		final long dif;
		Intent i= new Intent(getApplicationContext(), ConfirmationScreen.class);
		i.putExtra("acc", f);
		i.putExtra("speed", Send_Speed);
		i.putExtra("ssd", SD);
		i.putExtra("elpasedPeriod", elapsedPeriod);
		i.putExtra("scenario", Scenario);
		i.putExtra("lat", lat);
		i.putExtra("lon", lon);
		i.putExtra("videoState", CanIrecordVideo);
		i.putExtra("smsState", CanISendSMS);
		i.putExtra("airbagState", AirbagStae);
		t2=dd.getTime();
		dif=t2-t1;
		i.putExtra("Diff", dif);
		Toast.makeText(getApplicationContext(), ""+dif, Toast.LENGTH_LONG).show();
		startActivity(i);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					out.append("time from detection till starting confirmation screen is"+","+String.valueOf(dif)+"ms"+","+Scenario);
				       out.newLine();
				       
				     
				     		} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				  try {
						out.flush();
						 out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
				
			}
		}).start();
		finish();
	
		
	}




	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean arg1) {
		// TODO Auto-generated method stub
		
		
		if (!arg1)
		{
		mNotificationManager.cancel(NotificationId);
		finish();
	}
		else
		{
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

			Acc_thread=new AccelerationThread(getApplicationContext());
			Acc_thread.start_Engin();
		start_meter();
			
		}
		
		
		
		
		
	}


}
