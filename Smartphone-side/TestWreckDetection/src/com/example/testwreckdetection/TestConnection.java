package com.example.testwreckdetection;

import com.example.testwreckdetection.Second_startup_Activity.ConnectionDetector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestConnection extends Activity implements OnClickListener {
Button BtTestConnection;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_dialog);
		BtTestConnection=(Button)findViewById(R.id.btnTestConn);
		BtTestConnection.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		new ConnectionTester().execute(this);
	}
	
	public class ConnectionTester extends AsyncTask<Context, Integer, Boolean>
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
				//showAlertDialog(Second_startup_Activity.this, "Internet Connection",
				  //      "You don't have internet connection..Please check your connection");
			startActivity( new Intent(getApplicationContext(), TestConnection.class));
			finish();
			
			
			}
			else
			{
			startActivity( new Intent(getApplicationContext(), ImageTextListViewActivity.class));
			overridePendingTransition(R.anim.push_right_in,
			        R.anim.push_right_out);
			finish();
			}
			
			
		}

	
}
	
	

}
