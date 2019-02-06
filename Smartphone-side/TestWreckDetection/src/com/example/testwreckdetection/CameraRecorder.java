package com.example.testwreckdetection;

import java.io.ObjectInputStream.GetField;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class CameraRecorder extends Activity implements OnClickListener,SurfaceHolder.Callback{
@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();		
}

private void SendVideoBack()
{
Intent output= new Intent ();
videoPath="/storage/emulated/0/Pictures/MyCameraApp/VID_"+ timeStamp + ".mp4";
output.putExtra("video_path", videoPath);
setResult(Activity.RESULT_OK,output);
finish();
}



android.content.SharedPreferences Prefs;
Intent intent,intent_path;
public Camera myCamera;
public static SurfaceView mSurfaceView;
public static SurfaceHolder mSurfaceHolder;
private MediaRecorder mediaRecorder;
public static final int MEDIA_TYPE_IMAGE = 1;
public static final int MEDIA_TYPE_VIDEO = 2;
private static final int GONE = 0;
SurfaceHolder surfaceHolder;
boolean recording;
String videoPath,img_vid_uri="";
TextView startCapture;
SensorManager sensorManager;
String timeStamp="";
String uploadFilePath ="";
String uploadFileName="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.myapp);
startCapture=(TextView)findViewById(R.id.bstartRec);
startCapture.setTextAppearance(getApplicationContext(),
		R.style.blinkText);
Animation anim = new AlphaAnimation(0.0f, 1.0f);
anim.setDuration(70); //You can manage the time of the blink with this parameter
anim.setStartOffset(20);
anim.setRepeatMode(Animation.REVERSE);
anim.setRepeatCount(Animation.INFINITE);
startCapture.startAnimation(anim);
		   FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
        mSurfaceView = (SurfaceView) findViewById(R.id.sview);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		//timeStamp=  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

      //  videoPath="/storage/emulated/0/Pictures/MyCameraApp/VID_"+ timeStamp + ".mp4";
     
     StartRecording(this.getIntent());   
      
	}
	
	private void StartRecording(Intent intent2) {
		
		// TODO Auto-generated method stub
		
		
	
		if (intent2 != null)
		{
			Toast.makeText(getApplicationContext(), ""+intent2.getBooleanExtra("orientation", true), Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_LONG).show();

		}
		
		 intent	 = new Intent(getApplicationContext(), RecorderService.class);
		 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    
		      if ( intent2.getBooleanExtra("orientation", true))
		      {
		    	  // front face camera
		    	  
			   timeStamp=intent2.getStringExtra("time");
			        intent.putExtra("orientation", 0);
			        intent.putExtra("time", timeStamp);
			        startService(intent);
		           countDownForRecordingService();
		      
		      }
		      else
		      {
		    	  // back camera
		    	 
				   timeStamp=intent2.getStringExtra("time");
			        intent.putExtra("orientation", 1);
			        intent.putExtra("time", timeStamp);
			       startService(intent);
		           countDownForRecordingService();
		      }
		
	
	
	
	}

	private void countDownForRecordingService() {
		// TODO Auto-generated method stub
		  new CountDownTimer(20000, 1000) {

	            public void onTick(long millisUntilFinished) {
	               // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
	            }

	            public void onFinish() {
	            	
	          
	            stopService(intent);
	        SendVideoBack();
	         
	            }
	}.start();
		  }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
	
		
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}
	 @Override
		public void onResume() {
			super.onResume();		
		
			
		}
	

		
	 
	 
	 
	 
}
