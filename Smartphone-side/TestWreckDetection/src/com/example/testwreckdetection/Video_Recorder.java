package com.example.testwreckdetection;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

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
import android.util.Log;
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

public class Video_Recorder extends Activity implements OnClickListener,SurfaceHolder.Callback{
@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	 	   Toast.makeText(getApplicationContext(), "from camera"+ videoPath+"\n lat :"+ lat+"\n lon:"+lon, Toast.LENGTH_LONG).show();
	 	   
	 	   if (EndService)
	 	   {
	 		   
	 		   // start Upload video file  service..
	           new UploadVideo().execute(uploadFilePath + "" + uploadFileName);
	           // new UploadImg_Vid(uploadFilePath + "" + uploadFileName, getApplicationContext());
	 	   }
	 	   else
	 	   {
	 		   
	 		  Toast.makeText(getApplicationContext(), "error in stop service ", Toast.LENGTH_LONG).show();
	 	   }
	 	   
	 	   
	 	   
	}
android.content.SharedPreferences Prefs;
android.content.SharedPreferences.Editor zEdit;
FileInputStream fileInputStream;

HttpURLConnection conn = null;
String [] Ret = new String[3];
Intent intent;
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
String uploadFilePath,uploadFileName;
double lat,lon,acc,speed;
boolean EndService;
String deviceId; 
int serverResponseCode = 0;
String upLoadServerUri = "http://192.168.2.101//WebService/UploadToServer.php";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Prefs=getApplicationContext().getSharedPreferences("videoPathFile", Context.MODE_PRIVATE);
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
        
        
        
        
        
        new Find_Orientation().execute("");
        
 
       
			     
        
        
        
        
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
			Intent video=this.getIntent();
			 acc=   video.getDoubleExtra("acc", 0.0);
	         lat=  video.getDoubleExtra("lat", 0.0);
	         lon=  video.getDoubleExtra("lon", 0.0);
	          speed= video.getDoubleExtra("speed", 0.0);
	          deviceId=video.getStringExtra("divice_id");
		
			
		}
	

		private class Find_Orientation extends AsyncTask<String, Integer, Boolean> implements SensorEventListener
		{
		boolean fu,fd;
			@Override
			protected Boolean doInBackground(String... arg0) {
				// TODO Auto-generated method stub
				if(	isSupported(getApplicationContext(),Sensor.TYPE_ROTATION_VECTOR))
				{ sensorManager.registerListener(this,
			            sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
			            SensorManager.SENSOR_DELAY_NORMAL);
				
				return true;
				}
				else
				{
				return false;	
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
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
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
				fu=true;
				
			}

			private void onFaceDown() {
				// TODO Auto-generated method stub
				fu=false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (!result)
				{}
				else
				{
					if (fu)
					{Toast.makeText(getApplicationContext(), "faceUp", Toast.LENGTH_SHORT).show();
				sensorManager.unregisterListener(this);
					
					 intent	 = new Intent(Video_Recorder.this, RecorderService.class);
				        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        intent.putExtra("orientation", 0);
				        
				    startService(intent);
						      
				        new CountDownTimer(30000, 1000) {

				            public void onTick(long millisUntilFinished) {
				               // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
				            }

				            public void onFinish() {
				            	
				            	
				            	new Thread(new Runnable() {
									
									@Override
									public void run() {
										// TODO Auto-generated method stub
										videoPath=Prefs.getString("videoPath", "");
										uploadFilePath= videoPath.substring(videoPath.indexOf("/"), videoPath.lastIndexOf("/")+1); 
					                	 uploadFileName=videoPath.substring(videoPath.lastIndexOf("/")+1);
									}
								}).start();
				            	
				          
				            	
				            	
				            	
				                EndService= stopService(intent);
				          finish();
				         
				            }
				         }.start();
					
					
					}
					else
					{Toast.makeText(getApplicationContext(), "faceDown", Toast.LENGTH_SHORT).show();
					
					sensorManager.unregisterListener(this);

					 intent	 = new Intent(Video_Recorder.this, RecorderService.class);
				        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        intent.putExtra("orientation", 1);

				        startService(intent);
					      
				        new CountDownTimer(30000, 1000) {

				            public void onTick(long millisUntilFinished) {
				               // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
				            }

				            public void onFinish() {
				                EndService=      stopService(intent);
				          
				      	
			            	new Thread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									videoPath=Prefs.getString("videoPath", "");
									uploadFilePath= videoPath.substring(videoPath.indexOf("/"), videoPath.lastIndexOf("/")+1); 
				                	 uploadFileName=videoPath.substring(videoPath.lastIndexOf("/")+1);
								}
							}).start();
				          finish();
				            }
				         }.start();
				        
						
					
				}
					
					
				}
			}
			


		}
	 
	 
	 
		 public class UploadVideo extends AsyncTask<String, Integer, String[]>

		 {
			 
			 

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
				Log.d("my /Result is ", ""+result);
				
				if (result[2].equalsIgnoreCase("200"))
				{
					Toast.makeText(getApplicationContext(), "video is uploading", Toast.LENGTH_LONG).show();
					new Connect_To_Server(getApplicationContext(), "http://192.168.2.101/WebService/uploads/"+uploadFileName, lat, lon, acc, speed, deviceId).execute(new ApiConnector());
				}
				else  {
					
					Toast.makeText(getApplicationContext(), ""+result[1]+"\n"+"\n"+result[2], Toast.LENGTH_LONG).show();
				
				
				
				
				}
				
				
			}

		

		

			@Override
			protected String [] doInBackground(String... params) {
				// TODO Auto-generated method stub
				 
			
				    	  String fileName = params[0];
				 
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
