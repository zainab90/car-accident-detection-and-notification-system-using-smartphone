package com.example.testwreckdetection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Second_startup_Activity extends Activity implements OnClickListener{
Button StartupList;

boolean check= false;
Intent intent ;
CheckForGps mCheck;
ConnectionDetector mConnecte;
LocationManager lm;
String IndexFileName="contact_index";
SharedPreferences prefs;
CheckOnContacts mCheckonContact;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_startup_activity);
		StartupList=(Button)findViewById(R.id.button_second_startup);
		
		StartupList.setOnClickListener(this);
		

	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mCheckonContact= new CheckOnContacts();
		mCheckonContact.execute(IndexFileName);
	
	}

	
	
	
	public class ConnectionDetector extends AsyncTask<Context, Integer, Boolean>
	{

		@Override
		protected Boolean doInBackground(Context... params) {
			// TODO Auto-generated method stub
			
			
			ConnectivityManager connectivity = (ConnectivityManager) params[0].getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null) 
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null) 
	                  for (int i = 0; i < info.length; i++) 
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
	  }
	          return false;}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if ( ! result)
			{
			
			startActivity( new Intent(getApplicationContext(), TestConnection.class));
			
	         
			
			}
			else
			{
			startActivity( new Intent(getApplicationContext(), ImageTextListViewActivity.class));
			overridePendingTransition(R.anim.push_right_in,
			        R.anim.push_right_out);
			}
			
			
		}

	
}
 public class CheckForGps extends AsyncTask<Context, Integer, Boolean>
 {

	@Override
	protected Boolean doInBackground(Context... params) {
		// TODO Auto-generated method stub
		lm=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    
			if (! lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			{
				return false;
			}
			else
				return true;
		
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (! result)
		{ Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		  startActivity(intent);
	}
		else
		{
			// check for network connection
			mConnecte=new ConnectionDetector();
			mConnecte.execute(Second_startup_Activity.this);
			
		}
	}
}

 public class CheckOnContacts extends AsyncTask<String, Integer, Boolean>
 {

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		prefs=getSharedPreferences(params[0], Activity.MODE_PRIVATE);
		int index=prefs.getInt("index", -1);
		if (index == -1)
		{
		return false;
		}
		else
		{return true;}
		
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if ( !result )
			startActivity( new Intent(Second_startup_Activity.this, AddEmergencyContacts.class));
		else
		{
			// check for gps
			mCheck= new CheckForGps();
			mCheck.execute(Second_startup_Activity.this);
		}
		
	}
}
 
 
}
