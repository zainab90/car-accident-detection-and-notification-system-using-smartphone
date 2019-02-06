package com.example.testwreckdetection;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Recording_Video extends Activity{
    
	android.content.SharedPreferences Prefs;
	android.content.SharedPreferences.Editor zEdit;
	private Camera myCamera;
    private CameraSurfaceView myCameraSurfaceView;
    private MediaRecorder mediaRecorder;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    Button myButton;
    SurfaceHolder surfaceHolder;
    boolean recording;
   
	 static File mediaFile=null;
	 boolean pre;
	 String path;
	 TextView timerValue,sizeValue,recStatus;
	 View view;
		 
	 private long startTime = 0L;
	 
		
		private Handler customHandler = new Handler();
		
		long timeInMilliseconds = 0L;
		long timeSwapBuff = 0L;
		long updatedTime = 0L;
		boolean state;
	
		Animation anim;
	 File f;
	

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Prefs=getApplicationContext().getSharedPreferences("videoPathFile", Context.MODE_PRIVATE);
        zEdit=Prefs.edit();
        recording = false;
        
        setContentView(R.layout.videoapp);
        timerValue=(TextView)findViewById(R.id.timeRec);
        sizeValue=(TextView)findViewById(R.id.videoSize);    
       recStatus=(TextView)findViewById(R.id.recStatus);
       view =(View)findViewById(R.id.circle);
       
        anim = new AlphaAnimation(0.0f, 1.0f);
    	anim.setDuration(70); //You can manage the time of the blink with this parameter
    	anim.setStartOffset(20);
    	anim.setRepeatMode(Animation.REVERSE);
    	anim.setRepeatCount(Animation.INFINITE);
    	
        	  
        //Get Camera for preview
        myCamera = getCameraInstance();
        if(myCamera == null){
            Toast.makeText(Recording_Video.this, 
                    "Fail to get Camera", 
                    Toast.LENGTH_LONG).show();
        }

        myCameraSurfaceView = new CameraSurfaceView(this, myCamera);
        FrameLayout myCameraPreview = (FrameLayout)findViewById(R.id.videoview);
        myCameraPreview.addView(myCameraSurfaceView);
   
       
        myButton = (Button)findViewById(R.id.mybutton);
        myButton.setOnClickListener(myButtonOnClickListener);
      
    }
    
    Button.OnClickListener myButtonOnClickListener
    = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(recording){
            	
            	  myButton.setBackgroundResource(R.drawable.onscreen_button_1);
            	
                // stop recording and release camera
                mediaRecorder.stop();  // stop the recording
                releaseMediaRecorder(); // release the MediaRecorder object
              SendVideoPath();
             timeSwapBuff += timeInMilliseconds;
				customHandler.removeCallbacks(updateTimerThread);
			//	new CheckSize(AndroidVideoCapture.this).cancel(true);
                //Exit after saved
                finish();
            }else{
                
            	
                path=getOutputMediaFile(MEDIA_TYPE_VIDEO).toString();
            	Toast.makeText(getApplicationContext(), ""+path, Toast.LENGTH_LONG).show();
            	
                //Release Camera before MediaRecorder start
                releaseCamera();
                
                if(!prepareMediaRecorder()){
                    Toast.makeText(Recording_Video.this, 
                            "Fail in prepareMediaRecorder()!\n - Ended -", 
                            Toast.LENGTH_LONG).show();
                    finish();
                }
                
              
                mediaRecorder.start();
                startTime = SystemClock.uptimeMillis();
                myButton.setBackgroundResource(R.drawable.stop_recording_01);
			recStatus.setText(getResources().getString(R.string.rec_));
			view.setVisibility(View.VISIBLE);
				recStatus.startAnimation(anim);
			       view.startAnimation(anim);
					customHandler.postDelayed(updateTimerThread, 0);
                recording = true;
             
         
              
           
               
            	
	
              
            	
            }
            
            
            
        }

};
   
        
		private Runnable updateTimerThread = new Runnable() {

			String ShowingSize;
    		public void run() {
    			
    			if (new File (path).length() < 12000000)
    			{
    			timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
    			
    			updatedTime = timeSwapBuff + timeInMilliseconds;

    			int secs = (int) (updatedTime / 1000);
    			int mins = secs / 60;
    			secs = secs % 60;
    			
    			timerValue.setText("" + mins + ":"
    					+ String.format("%02d", secs) 
    					);
    			long size=new File (path).length();
    			if (size > 1024)
    				{
    				size=size/1024;
    				ShowingSize=""+size+"K";
    				}
    			else if (size > (1024 *1024))
    				{
    				size= size / (1024 *1024);
    				ShowingSize=""+size+"M";
    				}
    			
    			
    	sizeValue.setText(ShowingSize);
    			customHandler.postDelayed(this, 0);
    			
    			}
    			
    			else
    			{
    				timerValue.setText("done");
    				timeSwapBuff += timeInMilliseconds;
					customHandler.removeCallbacks(updateTimerThread);
    				 mediaRecorder.stop();  // stop the recording
    	                releaseMediaRecorder(); // release the MediaRecorder object
      	              SendVideoPath();

    			}
    			
    			
    		}

    	};

    private Camera getCameraInstance(){
        // TODO Auto-generated method stub
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    
    public void SendVideoPath() {
		// TODO Auto-generated method stub
    	Intent output = new Intent();
		output.putExtra("video_path", path);
	//	output.putExtra(ActivityOne.Number2Code, num2);
		setResult(RESULT_OK, output);
		//finish();
		finish();
		
	}

	private boolean prepareMediaRecorder(){
  	  
  			// TODO Auto-generated method stub
  		      myCamera = getCameraInstance();
  		        mediaRecorder = new MediaRecorder();
  				zEdit=Prefs.edit();

  		        myCamera.unlock();
  		        mediaRecorder.setCamera(myCamera);

  		        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
  		        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
  		        mediaRecorder.setOrientationHint(0);

  		        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));
  		       // mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
  		        //mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
  		        //mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
  		        zEdit.putString("videoPathZ",path);
  	            zEdit.commit();
  		        mediaRecorder.setOutputFile(path);
  		        
  		    //    mediaRecorder.setMaxDuration(30000); // Set max duration 10 sec.we set this number in milisecond
  		    // mediaRecorder.setMaxDuration(60000);
  		        mediaRecorder.setMaxFileSize(12000000); // Set max file size 10M

  		        mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder().getSurface());

  		        try {
  		            mediaRecorder.prepare();
  		          
  		       
  		        } catch (IllegalStateException e) {
  		            releaseMediaRecorder();
  		           pre= false;
  		        } catch (IOException e) {
  		            releaseMediaRecorder();
  		            pre= false;
  		        }
  		        pre= true;
  		

  	        return pre;
}
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mediaRecorder != null) {
            mediaRecorder.reset();   // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (myCamera != null){
            myCamera.release();        // release the camera for other applications
            myCamera = null;
        }
    }
   

  /** Create a File for saving an image or video */
  private static File getOutputMediaFile(int type){
      // To be safe, you should check that the SDCard is mounted
      // using Environment.getExternalStorageState() before doing this.

      File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
      // This location works best if you want the created images to be shared
      // between applications and persist after your app has been uninstalled.

      // Create the storage directory if it does not exist
      if (! mediaStorageDir.exists()){
          if (! mediaStorageDir.mkdirs()){
              Log.d("MyCameraApp", "failed to create directory");
              return null;
          }
      }

      // Create a media file name
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
     
      if (type == MEDIA_TYPE_IMAGE){
          mediaFile = new File(mediaStorageDir.getPath() + File.separator +
          "IMG_"+ timeStamp + ".jpg");
      } else if(type == MEDIA_TYPE_VIDEO) {
          mediaFile = new File(mediaStorageDir.getPath() + File.separator +
          "VID_"+ timeStamp + ".mp4");
      } else {
          return null;
      }

      return mediaFile;
  }
    
    
    public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

        private SurfaceHolder mHolder;
        private Camera mCamera;
        
        public CameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int weight,
                int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
              // preview surface does not exist
              return;
            }

            // stop preview before making changes
            try {
                mCamera.stopPreview();
            } catch (Exception e){
              // ignore: tried to stop a non-existent preview
            }

            // make any resize, rotate or reformatting changes here

            // start preview with new settings
            try {
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e){
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            
        }
    }
    
    
    
 
  	}

    
    
    
    

