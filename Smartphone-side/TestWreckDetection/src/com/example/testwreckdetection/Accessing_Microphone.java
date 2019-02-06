package com.example.testwreckdetection;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Accessing_Microphone  extends Service{
	  static final int MY_MSG = 1;
	    static final int MAXOVER_MSG = 2;
	    static final int ERROR_MSG = -1;
	    Boolean mMode = false; // false -> fast , true -> slow
	    Boolean mCalib = false;
	    Boolean mLog = false;
	    Boolean mMax = false;
	SplEngine mEngin=null;
	Intent intent;
	public static final String BROADCAST_ACTION = "com.example.testwreckdetection.displaySondevent";
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();

    	intent = new Intent(BROADCAST_ACTION);	
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mEngin=new SplEngine(mh, getApplicationContext());
		mEngin.start_engine();
		
		return START_STICKY;
	}
	Handler mh=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
		        switch (msg.what) {
		                case MY_MSG :
		                	
		                	intent.putExtra("sound", msg.obj.toString());
		                   sendBroadcast(intent);
		               
		                        break;
		                case MAXOVER_MSG :
		                        mMax = false;
		                        handle_mode_display();
		                      
		                        break;
		                case ERROR_MSG:
		                 
		                        stop_meter();
		                    	mEngin.start_engine();
		                        break;
		                default :
		                        super.handleMessage(msg);
		                        break;
		        }
		}

		private void handle_mode_display() {
			// TODO Auto-generated method stub
			
		}

		private void stop_meter() {
			// TODO Auto-generated method stub
			 mEngin.stop_engine();
			
		}

		};
		
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			mEngin.stop_engine();
		}
		
		
}
