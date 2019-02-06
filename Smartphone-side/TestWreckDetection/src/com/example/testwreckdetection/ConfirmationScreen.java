package com.example.testwreckdetection;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;














import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ConfirmationScreen extends  Activity implements OnClickListener {

    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK)
		{
	if (requestCode==100)
	{ 

		if (data.hasExtra("video_path"))
		{
			Toast.makeText(getApplicationContext(), "returned extra", Toast.LENGTH_LONG).show();
			videoPath=data.getStringExtra("video_path");
			finish();
			
			
			 new Thread(new Runnable() {
	             public void run() {
	             	uploadFilePath= videoPath.substring(videoPath.indexOf("/"), videoPath.lastIndexOf("/")+1); 
	             	 uploadFileName=videoPath.substring(videoPath.lastIndexOf("/")+1);
	           
	             runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					//	new Upload_Img_VID_File(uploadFileName).execute(uploadFilePath + "" + uploadFileName);
						new  UploadVideo().execute(videoPath);
					}
				});
			
	             
	             }
	           }).start(); 
			  
		}
		else
		{
			finish();
			Toast.makeText(getApplicationContext(), "no Extra", Toast.LENGTH_LONG).show();
		
		
		}
		
		}

	else{
		finish();
		Toast.makeText(getApplicationContext(), "error in request code", Toast.LENGTH_LONG).show();
		
	}
	
		}
		else
		{
		Toast.makeText(getApplicationContext(), "erroe", Toast.LENGTH_LONG).show();	
		}
		
		
	}


	/**
     * The string to be displayed in the timeRemaining_. ? will be substituted
     * with the actual time remaining
     */
    private static final String REM_STRING = "You have ? seconds remaining";
    /**
     * The default allowed time. Used if max time wasn't specified in the
     * preference file
     */
    private static final int DEFAULT_MAX_TIME = 10;

    /** Buttons for user input */
    private Button yesButton_, noButton_;
    /** TextView displaying the time remaining */
    private TextView timeRemaining_;
    /** The ProgressBar showing time elapsed to time allowed ratio */
    private ProgressBar bar_;

    /** The maximum time alloted to make a decision (in seconds) */
    private int maxTime_;
    /** The time the activity started. 0 means unset */
    private long startTime_ = 0;
    NotificationManager mNotificationManager;
    int NotificationId=10;
    private static final String TAG_SUCCESS = "success";
   String deviceId;
   boolean fu;	
   String state="true";
   boolean sndSms , sendVideo=true;
   SensorManager sensorManager;
   double Acc, lat,lon;
   String videoPath,uploadFilePath,uploadFileName, Scenario, timeStamp,FirstVideoName="";
   
   float speed,SD=0;
  long start,end ,diff,Diff_detection;
  
   int elapsed_period;
   boolean CanIrecordVideo,CanISendSMS,AirbageStae;
   boolean CanR,CanW;
   BufferedWriter out;
    File root,gpxfile;
    FileWriter gpxwriter;
   
    HttpURLConnection conn = null;
    String [] Ret = new String[3];
    int serverResponseCode = 0;
	String upLoadServerUri="http://emergencyservice.hostingsiteforfree.com/UploadToServer.php";
    FileInputStream fileInputStream;


   
   
 
    /** A reference to the vibrator. Used to catch user's attention */
  //  private Vibrator vibrator_;

    /**
     * The time object that schedules the countdown. This needs to be explicitly
     * deactivated in onDestroy() so that its thread dies together with the
     * Activity
     */
    private final Timer timer_ = new Timer("Countdown Timer");

    /**
     * A runnable object that is responsible for time keeping and triggering the
     * timeout event. This has to be canceled in onDestroy() so that it won't
     * run after the activity dies
     */
    private CountdownTimerTask countdownTask_;

    /** Handler for requesting the main thread to do something */
    private final Handler handler_ = new Handler();
    private Vibrator vibrator_;
  
  
    /**
     * A private inner class used for timeout control. The countdown will start
     * with the first execution of run(). This class is responsible for updating
     * the ProgressBar as well as the TextView.
     * 
     * @author Krzysztof Zienkiewicz
     * 
     */
    private class CountdownTimerTask extends TimerTask {

            /** Current time (in seconds) determined via startTime */
            private int time_;

            /**
             * A no-op constructor
             */
            public CountdownTimerTask() {
                    super();
                    time_ = (int) (System.currentTimeMillis() - startTime_) / 1000;
                    updateUI();
               //     Log.v(VUphone.tag, "TimerTask ctor");
            }

            /**
             * Returns the remaining time in milliseconds.
             * 
             * @return
             */
            public long getRemainingTime() {
                    return (maxTime_ * 1000L)
                                    - (System.currentTimeMillis() - startTime_);
            }

            /**
             * Manage timing. If this timeouts, this task is canceled, removing it
             * from the timer's queue and purges the timer. report(true) is then
             * called.
             */
            @Override
            public void run() {
                    time_ = (int) (System.currentTimeMillis() - startTime_) / 1000;
                    updateUI();

                    if (time_ >= maxTime_) {
                      //      Log.v(VUphone.tag, "TimerTask.run() entering timeout");
                            handler_.post(new Runnable() {
                                    public void run() {
                                            report(true);
                                    }
                            });
                    }
            }

            /**
             * Used to sync the UI with the data held by this object
             */
            private void updateUI() {
                    handler_.post(new Runnable() {
                            public void run() {
                                    bar_.setProgress(time_);
                                    String remTimeStr = "" + (maxTime_ - time_);
                                    timeRemaining_.setText(REM_STRING.replace("?", remTimeStr));
                            }
                    });
            }
    }

    /**
     * Called to clean up the Timer and TimerTask and Vibrator. Should only be
     * called once this activity enters the shutdown path.
     */
    private void cleanUp() {
            timer_.cancel();
            countdownTask_.cancel();
            vibrator_.cancel();
    }

    /**
     * Process user input.
     */
    public void onClick(View v) {
            report(v.equals(yesButton_));
    }

    /**
     * The "constructor". Sets up the fields and the layout. This is either
     * called because somebody requested this activity to start up or because
     * the activity is being reloaded due to a keyboard flip.
     */
    @Override
    protected void onCreate(Bundle save) {
            super.onCreate(save);
            setContentView(R.layout.accident_wreck_confirem);
         CreateNewFile();
            yesButton_ = (Button) findViewById(R.id.accident_true);
            yesButton_.setOnClickListener(this);
            noButton_ = (Button) findViewById(R.id.accident_false);
            noButton_.setOnClickListener(this);

            timeRemaining_ = (TextView) findViewById(R.id.time_remaining);
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            vibrator_ = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            maxTime_ =12;
    		timeStamp=  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    		FirstVideoName="VID_"+ timeStamp + ".mp4";

            bar_ = (ProgressBar) findViewById(R.id.progress_bar);
            bar_.getProgressDrawable().setColorFilter(Color.GREEN,Mode.SRC_IN);
            bar_.setMax(maxTime_);

            if (save != null && save.containsKey("Time"))
                    startTime_ = save.getLong("Time");

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
out.append(""+","+""+","+"");
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
	
    
    
    

    /**
     * The "destructor". Calls cleanUp()
     */
    @Override
    protected void onDestroy() {
            super.onDestroy();
            cleanUp();
    }

    /**
     * Used to intercept the back key. If the back key is pressed, report an
     * accident and quit.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                    report(true);
                    return true;
            } else
                    return super.onKeyDown(keyCode, event);
    }

    /**
     * Called before the Activity is killed (due to keyboard flip). Saves the
     * current time so that we can recreate object state later.
     */
    @Override
    public void onSaveInstanceState(Bundle save) {
            super.onSaveInstanceState(save);
            save.putLong("Time", startTime_);
    }

    /**
     * The "main" method. Activate the "dialog" if started with the correct
     * intent. Else, finish the activity.
     */
 
    

    @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new My_orientation().start();
		   if (startTime_ == 0)
               startTime_ = System.currentTimeMillis();

       // Instantiate here so that startTime_ is always valid
       countdownTask_ = new CountdownTimerTask();

               // Schedule the timer task
               timer_.scheduleAtFixedRate(countdownTask_, 1000, 1000);
               long[] pattern = { 100, 200 };
               int repeat = 0;
               vibrator_.vibrate(pattern, repeat);
           	TelephonyManager man = (TelephonyManager) super
                    .getSystemService(Context.TELEPHONY_SERVICE);
                deviceId = man.getDeviceId();
               
               Intent i=this.getIntent();
              Acc=i.getDoubleExtra("acc", 0);
       		speed =i.getFloatExtra("speed", 0);
       		SD=i.getFloatExtra("ssd", 0);
       		lat=i.getDoubleExtra("lat", 0);
       		lon=i.getDoubleExtra("lon", 0);
       		Scenario=i.getStringExtra("scenario");
       		elapsed_period=i.getIntExtra("elpasedPeriod", 0);
       		CanIrecordVideo=i.getBooleanExtra("videoState", true);
       		CanISendSMS=i.getBooleanExtra("smsState", true);
       		AirbageStae=i.getBooleanExtra("airbagState", true);
       		Diff_detection=i.getLongExtra("Diff", 0);
    }

	/**
     * Takes the necessary actions to report whether an accident occurred.
     * Cleans up and finishes the activity.
     * 
     * @param occurred
     */
    
    
    
    private void report(boolean occurred) {
    	
    	
    	
    	
            cleanUp();
            if (occurred)
            
            {
            
            
             mNotificationManager.cancel(NotificationId);
            
            
         	// send SMS 
				
             if (CanISendSMS)
				{
					new Sending_SMS_Message(getApplicationContext(), lat, lon).execute("07705321962");
				}
					
				// start Camera
				
				if (CanIrecordVideo)
			{Intent camera=new Intent(getApplicationContext(), CameraRecorder.class);
			camera.putExtra("time", timeStamp);
			camera.putExtra("orientation", fu);
			startActivityForResult(camera, 100);
			}
				
				new InsertIntoDB().execute(new ApiConnector()); 

				
				
				
				// TODO Auto-generated method stub
	
             
             
             
             /*
             
             stopService(new Intent(getApplicationContext(), Accessing_Accelerometer_Sensor.class));//stop acceleration service
			  stopService(new Intent(getApplicationContext(), Accessing_Microphone.class));//stop sound service
			  stopService(new Intent(getApplicationContext(), Accessing_GPS_Reciever.class));
			  
			  
			  Context context = this;
				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
				if (appWidgetManager != null){
				RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
				ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
				remoteViews.setImageViewResource(R.id.widget_image, R.drawable.widget_off_1);
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);
				}
			  


			  new Thread( new Runnable() {
			 				
			 				@Override
			 				public void run() {
			 					// TODO Auto-generated method stub
			 					//sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			 					 SharedPreferences myPref 
			 					 
			 				        = PreferenceManager.getDefaultSharedPreferences(
			 				 
			 				                ConfirmationScreen.this);
			 				 
			 				       sendVideo=myPref.getBoolean("checkbox", true);
			 				       sndSms=myPref.getBoolean("checkbox_sms_loc", true);
			 					  runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
									Toast.makeText(getApplicationContext(), "video is "+sendVideo+"\n sms is :"+sndSms, Toast.LENGTH_LONG).show();	
									
									
									 if (sndSms)
									  { Intent sms=new Intent(getApplicationContext(),SendSMSMessage.class);
									  sms.putExtra("smsLat", lat);
									  sms.putExtra("smsLon", lon);

									  startService(sms);// start sms service
								//	new Sending_SMS_Message().execute("");
									 
									  }
									
									
									  
									  if (sendVideo){
									  
								  Intent videoREC=new Intent(getApplicationContext(), Video_Recorder.class);
									  videoREC.putExtra("acc", acc);
									  videoREC.putExtra("lat", lat);
									  videoREC.putExtra("lon", lon);
									  videoREC.putExtra("speed", speed);
									 videoREC.putExtra("divice_id", deviceId);
									  startActivity(videoREC);
									  finish();
									  }
									  else
									  {
										  String path=null;
										  new Connect_To_Server(getApplicationContext(), path, lat, lon, acc, speed, deviceId).execute(new ApiConnector());
										  finish();
									  }
									
									
									
									}
								});
			 				}
			 			}).start();
			  
         
            */
            
            }
            else
            {
            	
            	// click No button:

                mNotificationManager.cancel(NotificationId);
            	finish();
            	
            }
			  		
			
    
    }
    

	private class InsertIntoDB extends AsyncTask<ApiConnector, Long, JSONObject>
	{
	        @Override
	        protected JSONObject doInBackground(ApiConnector... params_x) {
	      	
	      
	            // it is executed on Background thread
	        	  // Building Parameters
	        	List<NameValuePair> params = new ArrayList<NameValuePair>();
	        	  String url= "http://www.emergencyservice.hostingsiteforfree.com/add_accident_record.php";
	        	  Date d= new Date();
	        	  start=d.getTime();
	            
	        	params.add(new BasicNameValuePair("Acceleration_Value", String.valueOf(Acc)+"G"));
	              params.add(new BasicNameValuePair("latitude", String.valueOf(lat)));
	              params.add(new BasicNameValuePair("longitude", String.valueOf(lon)));
	              params.add(new BasicNameValuePair("speed", String.valueOf(speed)+"Km/h"));
	              params.add(new BasicNameValuePair("elapsed_time", String.valueOf(elapsed_period)));
	              params.add(new BasicNameValuePair("SSD", String.valueOf(SD)));
	              params.add(new BasicNameValuePair("airbag_flag", String.valueOf(AirbageStae)));
	              params.add(new BasicNameValuePair("Video_Url", FirstVideoName));
	              params.add(new BasicNameValuePair("Show_Post", "true"));
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
	                	Date d= new Date();
	                	end =d.getTime();
	                	diff=end-start;
	                     Toast.makeText(getApplicationContext(), "insert", Toast.LENGTH_SHORT).show();
	                     new Thread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								try {
									out.append(" time is taken from start connection to receive replay from server is"+","+String.valueOf(diff)+"ms"+","+Scenario);
								       out.newLine();
								       
								     
								     		} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								  
							}
						}).start();
	                     
	                // start upload video
//new  UploadVideo().execute(videoPath);	               
	                     
	                
	                  
	                } else {
	                    // failed to create product
	                	Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
	                }
	            } catch (JSONException e) {
	                e.printStackTrace();
	            }
	          
	        	}
	        	else
	        	{
	        		Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
	        	}

	        }
		
		
		
		
	}
	
    
	
	private class My_orientation extends Thread implements SensorEventListener{

		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			if(	isSupported(getApplicationContext(),Sensor.TYPE_ROTATION_VECTOR))
			{ sensorManager.registerListener(this,
		            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
		            SensorManager.SENSOR_DELAY_NORMAL);
			}
			else
			{
				Log.d("sensor message", "sensor is not availabe ");
			}
			
			
		}
		
		
		private boolean isSupported(Context context, int sensorType) {
			// TODO Auto-generated method stub
			sensorManager =
		                (SensorManager) context
		                        .getSystemService(Context.SENSOR_SERVICE);
		        List<Sensor> sensors = sensorManager.getSensorList(sensorType);
		        return sensors.size() > 0;
		}
		
		

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			 float[] rotationMatrix;
			 rotationMatrix = new float[16];
	         SensorManager.getRotationMatrixFromVector(rotationMatrix,
	                 event.values);
	         determineOrientation(rotationMatrix);
			
		}
		
		private void determineOrientation(float[] rotationMatrix) {
			// TODO Auto-generated method stub
			  float[] orientationValues = new float[3];
		        SensorManager.getOrientation(rotationMatrix, orientationValues);
		        double azimuth = Math.toDegrees(orientationValues[0]);
		        double pitch = Math.toDegrees(orientationValues[1]);
		        double roll = Math.toDegrees(orientationValues[2]);
		        if (pitch <= 10)
		        {   
		            if (Math.abs(roll) >= 170)
		            {
		                onFaceDown();
		            }
		            else if (Math.abs(roll) <= 10)
		            {
		                onFaceUp();
		            }
		        }
			
		}
		
		private void onFaceUp() {
			// TODO Auto-generated method stub
			sensorManager.unregisterListener(this);
			fu=true;
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Face UP", Toast.LENGTH_LONG).show();
				}
			});
			
		}

		private void onFaceDown() {
			// TODO Auto-generated method stub
			sensorManager.unregisterListener(this);
			fu=false;
		runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "Face Down", Toast.LENGTH_LONG).show();
				}
			});
			
		}
		
		

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		
		
		
		
		
	}


	
	
	
	 public class UploadVideo extends AsyncTask<String, Integer, String[]>

	 {
		 
	       long start_upload,end_upload,dif_upload;

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			Log.d("cancel", "cancel my task");
		
		}

		@Override
		protected void onPostExecute(String [] result) {
			// TODO Auto-generated method stub
		 //   progressDialog.dismiss();
		
			super.onPostExecute(result);
Date d=new Date();
end_upload=d.getTime();
dif_upload=end_upload-start_upload;
			if (result[2].equalsIgnoreCase("200"))
			{
				Toast.makeText(getApplicationContext(), "video uploading is complete"+"\n during"+dif_upload, Toast.LENGTH_LONG).show();
		
			     new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								out.append(" total time is taken to upload the video is"+","+String.valueOf(dif_upload)+"ms"+","+Scenario);
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
			
			
			
			
			}
			else  {
				
				Toast.makeText(getApplicationContext(), ""+result[1]+"\n"+"\n"+result[2], Toast.LENGTH_LONG).show();
			
			
			
			
			}
			
			
		}

	

	

		@Override
		protected String [] doInBackground(String... params) {
			// TODO Auto-generated method stub
			 
		
			    	  String fileName = params[0];
			 Date d=new Date();
			        start_upload=d.getTime();
			          DataOutputStream dos = null;  
			          String lineEnd = "\r\n";
			          String twoHyphens = "--";
			          String boundary = "*****";
			          int bytesRead, bytesAvailable, bufferSize;
			          byte[] buffer;
			          int maxBufferSize = 1 * 1024 * 1024; 
			          File sourceFile = new File(params[0]); 
			          
			          
			          {
				           try { 
				        	   
				        	      if (isCancelled())
					               {fileInputStream.close();}
				        	      else
				        	      {
				        	   
				        	   
				            	 // open a URL connection to the Servlet
				           fileInputStream = new FileInputStream(sourceFile);
				               URL url = new URL(upLoadServerUri);
				         
				               
				               // Open a HTTP  connection to  the URL
				               conn = (HttpURLConnection) url.openConnection(); 
				               conn.setDoInput(true); // Allow Inputs
				               conn.setDoOutput(true); // Allow Outputs
				               conn.setUseCaches(false); // Don't use a Cached Copy
				               conn.setRequestMethod("POST");
				               conn.setRequestProperty("Connection", "Keep-Alive");
				               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				               conn.setRequestProperty("uploaded_file", fileName); 
				              
				               dos = new DataOutputStream(conn.getOutputStream());
				     
				               dos.writeBytes(twoHyphens + boundary + lineEnd); 
				               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
				            		                     + fileName + "\"" + lineEnd);
				               
				               dos.writeBytes(lineEnd);
				     
				               // create a buffer of  maximum size
				               bytesAvailable = fileInputStream.available(); 
				     
				               bufferSize = Math.min(bytesAvailable, maxBufferSize);
				               buffer = new byte[bufferSize];
				               int totalBytesWritten=0;
				               // read file and write it into form...
				               bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
				                 
				               while (bytesRead > 0) {
				            	   
				                 dos.write(buffer, 0, bufferSize);
				                // sentBytes += bufferSize;
				               // sentBytes += bufferSize;
				                Log.d("bytes Read", ""+bytesRead);
				                totalBytesWritten =totalBytesWritten+bufferSize;
				                
				          
				                 bytesAvailable = fileInputStream.available();
				                 Log.d("bytesAvailable", ""+bytesAvailable);
				                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
				                 Log.d("bufferSize",""+bufferSize);
				                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				                 Log.d("bytesRead", ""+bytesRead);
				                 
				                 
				                }
				     
				               // send multipart form data necesssary after file data...
				               dos.writeBytes(lineEnd);
				               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				     
				               // Responses from the server (code and message)
				               serverResponseCode = conn.getResponseCode();
				               String serverResponseMessage = conn.getResponseMessage();
				               
				                
				               Log.i("uploadFile", "HTTP Response is : " 
				            		   + serverResponseMessage + ": " + serverResponseCode);
				               
				            
				               
				               
				               if(serverResponseCode == 200){
				            	   
				                              
				               }    
				               
				               //close the streams //
				               fileInputStream.close();
				               dos.flush();
				               dos.close();
				        	      }
				          } catch (MalformedURLException ex) {
				        	  
				        Ret[0]=ex.getMessage();
				              Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
				          } catch (Exception e) {
				        	  Ret[1]=e.getMessage();
				            
				              Log.e("Upload file to server Exception", "Exception : " 
				            		                           + e.getMessage(), e);  
				          }
				        Ret[2]=String.valueOf(serverResponseCode);
				          return Ret; 
			          }   
			           } // End e

			
		
		
	 
	 
	 
	 
	 
	 
	 }	
	
	
	
}