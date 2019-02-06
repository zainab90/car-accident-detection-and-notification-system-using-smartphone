package com.example.testwreckdetection;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;







import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class RecorderService extends Service {


	Intent intent;
	boolean recording;
	 private Camera myCamera;
	 private SurfaceView mSurfaceView;
	  private SurfaceHolder mSurfaceHolder;
	  private static Camera mServiceCamera;
	  private MediaRecorder mediaRecorder;
	  public static final int MEDIA_TYPE_IMAGE = 1;
	    public static final int MEDIA_TYPE_VIDEO = 2;
	    String videoPath="";
	    private static String TAG = "MainActivity";
	   static String timeStamp;
	    private int frontFacingCameraID;
	    int orientation=0;
	   static String absolutePath; 
	    private final Handler Path_handler = new Handler();
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
        super.onCreate();
	}

	  private Camera getCameraInstance( int x) {
	        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

	        int numberOfCameras = Camera.getNumberOfCameras();

	        Log.i(TAG, "number of cameras: " + numberOfCameras);

	        for(int i = 0; i < numberOfCameras; i++) {
	            Camera.getCameraInfo(i, cameraInfo);
	                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && x==0) {
	                    Log.i(TAG, "camera: " + i + " is front facing");
	                    frontFacingCameraID = i;
	                }

	                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK && x==1) {
	                    Log.i(TAG, "camera: " + i + " is back facing");
	                    frontFacingCameraID = i;
	                }
	        }

	        Camera c = null;
	        try {
	            //c = Camera.open(); // Default Rear Camera works
	            c = Camera.open(frontFacingCameraID); // attempt to get a Camera instance

	            // hacks I have read about which do nothing
	            Camera.Parameters params = c.getParameters();
	            params.set("cam-mode", 1);
	            params.set("camera-id", 1);
	            params.set("cam_mode", 1);
	            c.setParameters(params);
	        } catch (Exception e) {
	            // Camera is not available (in use or does not exist)
	        }
	        return c; // returns null if camera is unavailable
	    }

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		 releaseMediaRecorder();       // if you are using MediaRecorder, release it first
	        releaseCamera();
	    
	 
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		
		recording=false;
	orientation=intent.getIntExtra("orientation", 0);
		timeStamp=intent.getStringExtra("time");
	
		   myCamera = getCameraInstance(orientation);
		   if(myCamera == null){
	            Toast.makeText(RecorderService.this, 
	                    "Fail to get Camera", 
	                    Toast.LENGTH_LONG).show(); }
	        mSurfaceView = CameraRecorder.mSurfaceView;
	        mSurfaceHolder = CameraRecorder.mSurfaceHolder;
		
		
		
		
	
Toast.makeText(this,"start Recording",Toast.LENGTH_LONG).show();
if(recording){
    // stop recording and release camera
    mediaRecorder.stop(); 
    // stop the recording
   
    this.stopSelf();
    Toast.makeText(this,"stop Recording",Toast.LENGTH_LONG).show();
}else{
    
    //Release Camera before MediaRecorder start
    releaseCamera();
    
    if(!prepareMediaRecorder()){
        Toast.makeText(this, 
                "Fail in prepareMediaRecorder()!\n - Ended -", 
                Toast.LENGTH_LONG).show();
        //*** finish
        this.stopSelf();
    }
    mediaRecorder.start();
    recording = true;

}

return START_STICKY;
	}
	
	
  
	    
	    
	    
	
	
	  @SuppressLint("InlinedApi")
	private boolean prepareMediaRecorder(){
	        myCamera = getCameraInstance(orientation);
	        mediaRecorder = new MediaRecorder();
		//	zEdit=Prefs.edit();

	        myCamera.unlock();
	        mediaRecorder.setCamera(myCamera);

	        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

	        mediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_QVGA));
	        
	        mediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());
	        
	       

	       
	        mediaRecorder.setMaxDuration(30000); // Set max duration 10 sec.we set this number in milisecond
	        mediaRecorder.setMaxFileSize(10000000); // Set max file size 10M

	        mediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
	        try {
	            mediaRecorder.prepare();
	        } catch (IllegalStateException e) {
	            releaseMediaRecorder();
	            return false;
	        } catch (IOException e) {
	            releaseMediaRecorder();
	            return false;
	        }
	        
	   //     new Thread(new Runnable() {
				
			//	@Override
		//		public void run() {
					// TODO Auto-generated method stub
				//	 zEdit.putString("videoPath",videoPath);
				  //      zEdit.commit();
					
			//	}
		//	}).start();
	        return true;
	        
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

	  
	  
	  
	  private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.
		//timeStamp=  new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
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
	   
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
absolutePath=mediaFile.getPath();
	    return mediaFile;
	}
	  
	  
	  

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}
	 
	
	





	
	
	
	
	
	
	
	
	
	
	
	
	
}
