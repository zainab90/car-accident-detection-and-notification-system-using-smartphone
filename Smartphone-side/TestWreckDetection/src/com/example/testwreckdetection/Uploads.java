package com.example.testwreckdetection;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class Uploads extends FragmentActivity implements OnClickListener, OnItemClickListener{
@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StartGPSService(); 
	}


@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
	 if (keyCode == KeyEvent.KEYCODE_BACK) {
		 unregisterReceiver(broadcastReceiver);
		 stopService(new Intent(getApplicationContext(),Accessing_GPS_Reciever.class));
	 }	
	
	return super.onKeyDown(keyCode, event);
		
	}
String videoPath;
Button Camera_pic,Video_Rec;
Intent camera_intent,Video_intent,upload_intant;
int Camera_res,Video_res,Location_res,upload_res;
Bitmap camera_image;
ImageView my_Image;
String pic_video="";
private Uri mImageCaptureUri;
UploadListFragment mUploadListFragment;
ListView listView;
public static final int MEDIA_TYPE_VIDEO = 2;
public static final int MEDIA_TYPE_IMAGE = 3;
Uri output_vd;
String state="";
int listPostion,serverResponseCode,uploadedIndex;
SharedPreferences prefs,pref;
SharedPreferences.Editor editor;
String IndexFile="ListIndex";
String upLoadServerUri,deviceId;
String uploadFilePath;
String uploadFileName;
Boolean mode=true;
int Visible=View.VISIBLE;
private BroadcastReceiver broadcastReceiver;
RowItem r;
double lat,lon;
DataOutputStream dos = null; 
HttpURLConnection   conn;


String [] Ret = new String[3];

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uploads);
		initailization();
		
	
}
	

private void StartGPSService() {
		// TODO Auto-generated method stub
	new Thread(new Runnable() {
    	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			startService(new Intent(getApplicationContext(), Accessing_GPS_Reciever.class));
			 broadcastReceiver = new BroadcastReceiver() {
			        @Override
			        public void onReceive(Context context, Intent intent) {
			        	updateUI(intent);       
			        }

					protected void updateUI(Intent intent2) {
			// TODO Auto-generated method stub
			  
		      lat=intent2.getDoubleExtra("lat", 0.0);
		        lon=intent2.getDoubleExtra("lon", 0.0);
		        
			 } }; 
			 registerReceiver(broadcastReceiver, new IntentFilter(Accessing_GPS_Reciever.BROADCAST_ACTION));
		            	
		}
	}).start();
	}


private void initailization() {
		// TODO Auto-generated method stub

		mUploadListFragment= (UploadListFragment)getSupportFragmentManager().findFragmentById(R.id.upload_list_fragment);
		Camera_pic=(Button)findViewById(R.id.upload_camera_btn);
		Video_Rec=(Button)findViewById(R.id.upload_video_btn);

	//	upLoadServerUri = "http://192.168.0.101/WebService/UploadToServer.php";
		upLoadServerUri="http://emergencyservice.hostingsiteforfree.com/UploadToServer.php";
		
		Camera_pic.setOnClickListener(this);
	   Video_Rec.setOnClickListener(this);
	   Ret[0]=Ret[1]=Ret[2]="";
new Thread(new Runnable() {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		prefs=getSharedPreferences("videoPathFile", Context.MODE_PRIVATE);
		editor=prefs.edit();
		editor.clear();
		editor.commit();
	}
}).start();
	
	}




	@Override
	public void onClick(View v) {
		Toast.makeText(getApplicationContext(), "sss", Toast.LENGTH_SHORT).show();
		new LoadIndex().execute(IndexFile);
		Log .d("returned index ", ""+listPostion);
	switch (v.getId()) {

	case R.id.upload_camera_btn:
new Thread(new Runnable() {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		mImageCaptureUri=getOutputMediaFile(MEDIA_TYPE_IMAGE);
		 Intent intent    = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
       try {
           intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
           intent.putExtra("return-data", true);
           

           if (intent.resolveActivity(getPackageManager()) != null) {
               startActivityForResult(intent, 55);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
	}
}).start();
		break;
		
		
	
	case R.id.upload_video_btn:
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			Video_intent= new Intent(getApplicationContext(), Recording_Video.class);
			 if (Video_intent.resolveActivity(getPackageManager()) != null) {
	               startActivityForResult(Video_intent, 66);
	           }
				
		}
	}).start();
		 break;
	
	default:
		break;
	}	
}
	
private Uri getOutputMediaFile(int type) {
		// TODO Auto-generated method stub
		if(Environment.getExternalStorageState() != null) {
            // this works for Android 2.2 and above
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyCameraApp");

            if (! mediaStorageDir.exists()) {
                if (! mediaStorageDir.mkdirs()) {
                    Log.d("", "failed to create directory");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile=null;
            if (type == MEDIA_TYPE_IMAGE){
                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");
            }
            

            return Uri.fromFile(mediaFile);
        }

		else
		{
			return null;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
	
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode,resultCode,data);
		 if (resultCode == Activity.RESULT_OK) {
			 
			 uploadFilePath="";
			 uploadFileName="";
	           switch (requestCode) {
	   
	         case  66:  
		   if (data.hasExtra("video_path")){
			   videoPath=data.getStringExtra("video_path");
			   Toast.makeText(getApplicationContext(), ""+videoPath, Toast.LENGTH_SHORT).show();
			   conn=null;
			   dos=null;
			   mUploadListFragment.displayList(R.drawable.ic_action_video, mUploadListFragment.splitName(videoPath),listPostion,state,mode,Visible, conn,dos);
				uploadedIndex=listPostion;
				  new Thread(new Runnable() {
		              public void run() {
		              	uploadFilePath= videoPath.substring(videoPath.indexOf("/"), videoPath.lastIndexOf("/")+1); 
		              	 uploadFileName=videoPath.substring(videoPath.lastIndexOf("/")+1);
		            
		              runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							new Upload_Img_VID_File(uploadedIndex,"1",uploadFileName,conn,dos).execute(uploadFilePath + "" + uploadFileName);
							
							
							
							listPostion++;
							   new SaveIndex().execute(listPostion);
							
						}
					});
		              
		              }
		            }).start(); 
				  
				  
			   
		   }
		   else
		   {			   Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_SHORT).show();
}
		   
		
		  break;
	    
	       
	    case 55:
		Toast.makeText(getApplicationContext(), "retured from camera", Toast.LENGTH_SHORT).show();
		   pic_video    = mImageCaptureUri.getPath();
		   conn=null;
		   dos=null;
		   mUploadListFragment.displayList(R.drawable.ic_action_picture, pic_video,listPostion,state,mode,Visible ,conn, dos);
		   uploadedIndex=listPostion;
		  new Thread(new Runnable() {
               public void run() {
            	   
                	uploadFilePath= pic_video.substring(pic_video.indexOf("/"), pic_video.lastIndexOf("/")+1); 
                	 uploadFileName=pic_video.substring(pic_video.lastIndexOf("/")+1);
                	 runOnUiThread(new Runnable() {
             			
             			@Override
             			public void run() {
             				// TODO Auto-generated method stub
             				 runOnUiThread(new Runnable() {
             					
             					@Override
             					public void run() {
             						// TODO Auto-generated method stub
             						new Upload_Img_VID_File(uploadedIndex,"0",uploadFileName,conn, dos).execute(uploadFilePath + "" + uploadFileName);
             					
             						// for setting pause action
             						
             						
        							// end setting
             					
    
      							  listPostion++;
      							   new SaveIndex().execute(listPostion);
             						
             					}
             				});
             			}
             		});                     
              }
              }).start(); 
		
		  break;
		  

		   
	default:
	  break;
	           } 
	           
		 }
		 else if(resultCode==Activity.RESULT_CANCELED){
			 Toast.makeText(getApplicationContext(), "cancel..try again", Toast.LENGTH_LONG).show(); 
		 }
	       else 
	  {Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();}
		
}
	
	




	public Uri getLastPhotoOrVideo()
	{ String[] columns = { MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DATE_ADDED };

	Cursor cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null,
	        MediaStore.MediaColumns.DATE_ADDED + " DESC");

	int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	cursor.moveToFirst();
	String path = cursor.getString(columnIndex);

	cursor.close();

	return Uri.fromFile(new File(path));}


	
	
	private class LoadIndex extends AsyncTask<String, Void, Integer>
	{ 
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			listPostion=result;
		}
		int index=0;
      @Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
    	  prefs=getSharedPreferences(params[0], Activity.MODE_PRIVATE);
    	index=prefs.getInt("index", 0);
			return index;
		}
		
   }
	
	private class SaveIndex extends AsyncTask<Integer, Void, Boolean>
	{
int saved_index;
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result)
			{Toast.makeText(getApplicationContext(), "saved"+saved_index+"\n"+lat, Toast.LENGTH_LONG).show();
			}
			else
			{Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			saved_index=params[0];
			prefs=getSharedPreferences(IndexFile, Activity.MODE_PRIVATE);
			editor=prefs.edit();
			editor.putInt("index", params[0]);
			if(editor.commit())
		
			return true;
			else
				return false;
		}
		
		
	}
	
	
	
	
	
	public class Upload_Img_VID_File extends AsyncTask<String, Integer, String[]>

	 {
		int uploadIndex;
		String fileName;
		String repType;
		String ImgVidfileName;
		HttpURLConnection conn;
		DataOutputStream dos;
		
		 public Upload_Img_VID_File ( int uploadIndex,String repType, String ImgVidfileName, HttpURLConnection conn, DataOutputStream dos){
			this.uploadIndex=uploadIndex; 
			this.repType=repType;
			this.ImgVidfileName=ImgVidfileName;
			this.conn=conn;
			this.dos=dos;
		 }
// repType=0.... pic
//repType=1.. video		 
		 
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		
		
		}

		@Override
		protected void onPostExecute(String [] result) {
			// TODO Auto-generated method stub
		 //   progressDialog.dismiss();
		
			super.onPostExecute(result);
			
			if (result[2].equalsIgnoreCase("200"))
			{
				
			
	new AttachLoctionToImg_Vid(getApplicationContext(), ImgVidfileName, lat, lon,r,mUploadListFragment,uploadedIndex,repType).execute(new ApiConnector());						 
				
				}
			
			else  {
				
				Toast.makeText(getApplicationContext(), ""+result[1]+"\n"+"\n"+result[2], Toast.LENGTH_LONG).show();
				 r=mUploadListFragment.uploao_Apapter.getItem(uploadedIndex);
        		 r.setProgressVisiblity(View.INVISIBLE);
        		  r.setDownloadStatus("faild");
        		  mUploadListFragment.uploao_Apapter.notifyDataSetChanged();
			
		}
			
			
		}

	


		@Override
		protected String [] doInBackground(String... params) {
			// TODO Auto-generated method stub
			 

			
	    	  fileName = params[0];
	 
	          
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
			               {}
		        	      else
		        	      {
		        	   
		        	   
		            	 // open a URL connection to the Servlet
		        	    	    FileInputStream     fileInputStream = new FileInputStream(sourceFile);
		               URL url = new URL(upLoadServerUri);
		         
		               
		               // Open a HTTP  connection to  the URL
		               
		               conn = (HttpURLConnection) url.openConnection();
		          runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					     r=mUploadListFragment.uploao_Apapter.getItem(uploadedIndex);
				       		r.SetConnection(conn);
			        		  mUploadListFragment.uploao_Apapter.notifyDataSetChanged();
					}
				});

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
		                
		                publishProgress(totalBytesWritten * 100 / bytesAvailable);
		                   
		                 
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
		               
		                
		           
		               
		               
		               if(serverResponseCode == 200){
		            	   
		                   Log.d("status", "File Upload Completed.\n\n See uploaded file here : \n\n"
		                        		          +" http://www.androidexample.com/media/uploads/"
		                        		         );                
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



