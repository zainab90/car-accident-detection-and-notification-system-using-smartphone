package com.example.testwreckdetection;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlayVideo extends Activity  {
    
   
   public static final int MEDIA_TYPE_VIDEO = 2;
   
   public static PlayVideo ActivityContext =null; 
   public static TextView output;
  
   Uri output_vd;
    
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.video);

       ActivityContext = this;
        
       Button buttonRecording = (Button)findViewById(R.id.recording);
       output = (TextView)findViewById(R.id.output);
        
       buttonRecording.setOnClickListener(new Button.OnClickListener(){

           @Override
           public void onClick(View arg0) {
        	output_vd = getOutputMediaFile(MEDIA_TYPE_VIDEO);  // create a file to save the video in specific folder (this works for video only)
        	 Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        	//Add extra to save video
        	  //  intent.putExtra(MediaStore.EXTRA_OUTPUT, output_vd);
        	    startActivityForResult(intent, 55);
                
           }

		private Uri getOutputMediaFile(int type) {
			// TODO Auto-generated method stub
			  if(Environment.getExternalStorageState() != null) {
		            // this works for Android 2.2 and above
		            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "SMW_VIDEO");

		            // This location works best if you want the created images to be shared
		            // between applications and persist after your app has been uninstalled.

		            // Create the storage directory if it does not exist
		            if (! mediaStorageDir.exists()) {
		                if (! mediaStorageDir.mkdirs()) {
		                    Log.d("", "failed to create directory");
		                    Toast.makeText(getApplicationContext(), "failed to create directory", Toast.LENGTH_LONG).show();
		                    return null;
		                }
		            }

		            // Create a media file name
		            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		            File mediaFile;
		           if(type == MEDIA_TYPE_VIDEO) {
		                mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		                "VID_"+ timeStamp + ".mp4");
		            } else {
		                return null;
		            }

		            return Uri.fromFile(mediaFile);
		        }

		        return null;
		}});
        
        
   
   }
   
    
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {

       if (resultCode == Activity.RESULT_OK) {
           switch (requestCode) {
   case  55:
	   if (data.getData()!=null)
	   {Toast.makeText(getApplicationContext(), "returen from vid"+getLastPhotoOrVideo(), Toast.LENGTH_LONG).show(); }
	   else
	   {   //this.videoFromCameraNexus(resultCode, data);
		   Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
	   }
   break;

default:
               break;
           }


       }
       else 
       {Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();}
   }


private void videoFromCameraNexus(int resultCode, Intent data) {
	// TODO Auto-generated method stub
	// if(output_vd != null) {
      //   Log.d("c1", "Video saved to:\n" + output_vd);
        // 
        // Log.d("c2", "Video path:\n" + output_vd.getPath());
        
 // use uri.getLastPathSegment() if store in folder
 //use the file Uri.
     //}
	
	Toast.makeText(getApplicationContext(), "finally return"+getLastPhotoOrVideo(), Toast.LENGTH_LONG).show();
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

}
