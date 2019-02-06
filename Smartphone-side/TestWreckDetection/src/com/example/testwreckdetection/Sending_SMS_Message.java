package com.example.testwreckdetection;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class Sending_SMS_Message extends AsyncTask<String, Void, Boolean>{

	Context ctx;
int index=0;
Intent intent;
double latitude,longitude;
public Sending_SMS_Message(Context ctx, double lat, double lon){
	this.ctx=ctx;
	this.latitude=lat;
	this.longitude=lon;
}


		@Override
		protected Boolean doInBackground(String... arg0) {
			


		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(arg0[0], null, "http://maps.google.com/maps?q="+String.valueOf(latitude)+","+String.valueOf(longitude), null, null);
			return true; }
					
					catch (Exception e) {
				    
				         e.printStackTrace();
				     	return false;}

	    
	
	
	
	
	
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result)
			{Toast.makeText(ctx, "SMS was sent", Toast.LENGTH_SHORT).show();
			}
			else
			{Toast.makeText(ctx, "error in sending message", Toast.LENGTH_SHORT).show();
		}

			
		}

	
	
	
	
	
	
}
