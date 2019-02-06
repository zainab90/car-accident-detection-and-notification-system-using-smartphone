package com.example.testwreckdetection;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class UploadImg_Vid {
	
	String img_vid_path;
	 int serverResponseCode,res = 0;
	String upLoadServerUri = null;
	String uploadFilePath,uploadFileName;
	Context context;
	public UploadImg_Vid(String img_vid_path,Context ctx)
	{
		this.img_vid_path=img_vid_path;
		this.context=ctx;
	}
	
	public int  StartUploading ()
	{
		new Thread(new Runnable() {
            public void run() {
         
            	 uploadFilePath= img_vid_path.substring(img_vid_path.indexOf("/"), img_vid_path.lastIndexOf("/")+1);	
           uploadFileName=img_vid_path.substring(img_vid_path.lastIndexOf("/")+1);
           Log.d("path is :", uploadFilePath);
           Log.d("nzme", uploadFileName);
   		upLoadServerUri="http://emergencyservice.hostingsiteforfree.com/UploadToServer.php";
       serverResponseCode=   uploadFile(uploadFilePath + "" + uploadFileName);

          if (serverResponseCode==200)
          {
        res=serverResponseCode;
       // new Connect_To_Server(context, "", 3.4, 4.5, 6.6, 6.6, "").execute(new ApiConnector());
          }
          else
        	  res=0;
            }
          }).start();        
	return res;
	}

	 public int uploadFile(String sourceFileUri) {
          
    	  
    	  String fileName = sourceFileUri;
 
          HttpURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 10 * 1024 * 1024; 
          File sourceFile = new File(sourceFileUri); 
          
          if (!sourceFile.isFile()) {
        	  
	         Log.e("uploadFile", "Source File not exist :"
	        		               +uploadFilePath + "" + uploadFileName);
	       //  Toast.makeText(context, "Source File not exist :"
		     //          +uploadFilePath + "" + uploadFileName, Toast.LENGTH_LONG).show();
	       return 0;
           
          }
          else
          {
	           try { 
	        	   
	            	 // open a URL connection to the Servlet
	               FileInputStream fileInputStream = new FileInputStream(sourceFile);
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
	     
	               // read file and write it into form...
	               bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
	                 
	               while (bytesRead > 0) {
	            	   
	                 dos.write(buffer, 0, bufferSize);
	                 bytesAvailable = fileInputStream.available();
	                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
	                 
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
	            	   

                   	String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                   		          +" http://1ocalhost/WebService/uploads/"
                   		          +uploadFileName;  
               //    	Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	               }    
	               
	               //close the streams //
	               fileInputStream.close();
	               dos.flush();
	               dos.close();
	                
	          } catch (MalformedURLException ex) {
	        	  
	               
	              ex.printStackTrace();
	              
	             // Toast.makeText(context, "MalformedURLException", Toast.LENGTH_SHORT).show();
	              
	              Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
	          } catch (Exception e) {
	        	  
	              
	              e.printStackTrace();
	              
	          //    Toast.makeText(context, "Upload file to server Exception" 
	            	//	                           + e.getMessage() , 
                //		  Toast.LENGTH_LONG).show();
	              Log.e("Upload file to server Exception", "Exception : " 
	            		                           + e.getMessage(), e);  
	             
	          }
	               
	          return serverResponseCode; 
	          
           } // End else block 
         } 
	

}
