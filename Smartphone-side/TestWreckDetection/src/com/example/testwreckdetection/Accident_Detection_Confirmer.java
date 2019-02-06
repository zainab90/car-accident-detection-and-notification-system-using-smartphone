package com.example.testwreckdetection;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


public class Accident_Detection_Confirmer {
	private static Accident_Detection_Confirmer InstanceOfWreck = new Accident_Detection_Confirmer();
	private Context context;	
	//acceleration
    private double phi_ = 0;
     
    //sound
    private int rho_ = 0;
    
  //distance traveled since 
    //dropping below speed threshold
    private int epsilon_ = 0;
    
    //acceleration min value
    //to detect accident with no
    //sound
    private double phiM_ = 30.6;
    private double LargestAcclelration=0.0;
    
    //minimum decibel level to
    //set the sound event variable
    private double rhoM_ = 140.0;
    
    
 
    
    //time before resetting the
    //max acceleration variable
    private int phiS_ = 3000;
    
    //time before resetting the
    //sound event variable
    private int rhoS_ = 3000;

    //minimum speed for enabling
    //detection in meters per second
    private double mBeta_ = 6.705;
    private double Speed=0.0;
    
    //are we at the min speed?
    private boolean beta_ = false;
    
  //last location where beta was
    //true
    private Location betaL_ = null;
    
    //the accident detection threshold
    private double mTr_ = 1.9;
    
    private long lastPhiUpdate_ = 0;
    private long lastRhoUpdate_ = 0;
    private boolean firedConfirmation_ = false;
    private double SentLocationLat=0.0;
    private double SentLocationLong=0.0;
 
    double speed_unit=3.6F;
    final Handler handler = new Handler();
    final Handler handler2 = new Handler();
    List<Float>speedValues= new ArrayList<Float>();
    List <Float> previous_speed= new ArrayList<Float>();
    float Speed_threshold=(float) 10.667;
    float mean=0F;
    float summation,sub,sum,SD=0F;
    int No_minutes=0;
    Timer timer;
    TimerTask timerTask;
     String strDate,TotalSpeed;
     String Total="[";
     int period, counter,SSDIsgreaterSSP=0;
     boolean start=true;
    Thread t= new Thread();
    boolean speedState=false;
    int elapsedPeriod=0;
    boolean period_timer_state, timer_state, MP, CanIrecordVideo,CanISendSMS,AirbagStae,HighSpeedFlag=false;
    
    CountDownTimer PeriodTimer= new CountDownTimer(30000,1000) {
    	
    	@Override
    	public void onTick(long millisUntilFinished) {
    		// TODO Auto-generated method stub
    		period_timer_state=true;
    		MP=true;
    		elapsedPeriod++;
    		//SpeedStatus.setText("speed is changed from high to low during "+elapsedPeriod);
    	}
    	
    	@Override
    	public void onFinish() {
    		// TODO Auto-generated method stub
    		elapsedPeriod=29;// finish 30s
    		period_timer_state=false;
    		speedState=false;
    		calculateSD();
    		
    	}
    };
    
    
    
    Thread timer2 = new Thread (){

    	@Override
    	public void run() {
    		// TODO Auto-generated method stub
  
    		
    		try {
    			sleep(30000);
    		} catch (InterruptedException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
    		finally{
    			
    			handler2.post(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						if (!speedState)
				    	{
				    // still low speed
				    		Toast.makeText(context, "wake up and speed is low", Toast.LENGTH_SHORT).show();

				    		Calendar calendar = Calendar.getInstance();
				    		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
				    		strDate = simpleDateFormat.format(calendar.getTime());
				    	calculateSD();
				    	period=15000;
				    	startTimer();
				    	}
				    	
				    	// speed state = true
				    	else
				    	{
				    		Toast.makeText(context, "wake up and speed is high", Toast.LENGTH_SHORT).show();

				    	}
						
					}
				});
    			
  	
    		}	
    	}
    };
    
    private void startTimer() {
		// TODO Auto-generated method stub
		//set a new Timer
		timer = new Timer();
		
		//initialize the TimerTask's job
		initializeTimerTask();
		
		//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
		timer.schedule(timerTask, 0, period); //
		
	}
    
	public void initializeTimerTask() {
		
		timerTask = new TimerTask() {
			public void run() {
				
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
				strDate = simpleDateFormat.format(calendar.getTime());
				//use a handler to run a toast that shows the current timestamp
				handler.post(new Runnable() {
					public void run() {
						//get the current timeStamp
			CalculateSD15Sec();	
					}
				});
			}
		};
	}
    
    
	public void stoptimertask() {
		//stop the timer, if it's not already null
		if (timer != null) {
			timer.cancel();
			timer = null;
			
		      
		}
	}
    
    
	private void calculateSD() {
		// TODO Auto-generated method stub
		context.stopService(new Intent(context, Accessing_GPS_Reciever.class));
		
		new Thread (new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
			elapsedPeriod=0;	
				summation=0;
				mean=0;
				sub=0;
				sum=0;
				SD=0;

				if ( !speedValues.isEmpty() )
				{
				
					
                     // all speed values are in km/h
					// keep the the values of speed samples in array for next period of 15 sec
					
				for (int k=speedValues.size()/2; k < speedValues.size();k++ )
				{
					previous_speed.add(speedValues.get(k));
				     
				}
					
					
					
					
				for (int i=0; i< speedValues.size();i++)
				{
					summation+=speedValues.get(i);
						
				}
				mean=summation/speedValues.size();
				
				for (int j=0; j< speedValues.size();j++)
				{
					sub=(float) Math.pow(speedValues.get(j)-mean, 2);
					sum+=sub;
					
				}
				
				SD=(float) Math.sqrt(sum/speedValues.size());
				TotalSpeed=String.valueOf(speedValues);
				speedValues.clear();
				if (SD > 2)
				{
					SSDIsgreaterSSP=1;
				}
				
				
				else
				{

					SSDIsgreaterSSP=0;
					
				}
					
				}
				
			}
		}).start();
		
	context.startService(new Intent(context,Accessing_GPS_Reciever.class));
		

		
	}
	private void CalculateSD15Sec()
	{
		
		context.stopService(new Intent(context, Accessing_GPS_Reciever.class));
		
		new Thread (new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				elapsedPeriod=0;
				summation=0;
				mean=0;
				sub=0;
				sum=0;
				SD=0;
				counter=0;
				if ( !speedValues.isEmpty() )
				{
					
					// read samples from previous period
					for (int y=0; y< previous_speed.size();y++)
					{
						speedValues.add(previous_speed.get(y));
					}
					
					
					for (int i=0; i< speedValues.size();i++)
				{
					summation+=speedValues.get(i);
					
					
				}
				mean=summation/speedValues.size();
				
				for (int j=0; j< speedValues.size();j++)
				{
					sub=(float) Math.pow(speedValues.get(j)-mean, 2);
					sum+=sub;
					
				}
				
				SD=(float) Math.sqrt(sum/speedValues.size());
				
				// keep the values for next period but before, clear all last values
				previous_speed.clear();
				
				for (int k=0; k < speedValues.size()/2;k++ )
				{
					previous_speed.add(speedValues.get(k));
				     
				}
				 
			
				
				// save in right way
			Total=String.valueOf(speedValues);
				
			if (SD > 2)
			{
				SSDIsgreaterSSP=1;
			}
			
			
			else
			{

				SSDIsgreaterSSP=0;
				
			}	
							
				speedValues.clear(); // now, just the previous_speed list has values and speedValues is cleared to save new speed values for next period.
		
				}
				
			}
		}).start();
		
		context.startService(new Intent(context,Accessing_GPS_Reciever.class));
		

		
		
		
	}
	
	
  

	public static Accident_Detection_Confirmer get (Context cntx)
	{ 
		InstanceOfWreck.context=cntx;
		return InstanceOfWreck;
	}
	
	  public void checkTheAcceleration(double v){
	       long time = System.currentTimeMillis();
           if(time - lastPhiUpdate_ > phiS_){
                   phi_ = v;
                   SD=new FindSD().RetSD();
           }
           else if(v > phi_){
                   phi_ = v;
                   lastPhiUpdate_ = time;
           }
           
           checkForAccident();
   }
	  public void checkSoundLevel(double v){
          if(v > rhoM_){
                  rho_ = 1;
                  lastRhoUpdate_ = System.currentTimeMillis();
          }
          
  }
	  
	  public void checkTheSpeed(double s, double lat, double lon){
		  
        SentLocationLat= lat;
       SentLocationLong  =lon;
         Speed=s* speed_unit;
         
         // low speed
		 if (s < Speed_threshold && !speedState)
		 {
			 
			 HighSpeedFlag=false;
			 
		 speedValues.add((float) Speed); 
		 
		
		 // start calculate sd.
		 if ( ! timer2.isAlive())
		 {
			 timer2.start();
		 }
		 
		 }
		 
		// high speed
		 if (s > Speed_threshold) 
		 {
			
			 speedState=true;
			 SD=0;
			 elapsedPeriod=0;
               HighSpeedFlag=true;
			 
			 // check if there is count down timer in the background
			 if (period_timer_state)
			 {
				 PeriodTimer.cancel();
				 
				 // new addition
				 period_timer_state=false;
			 }
			 
			 // new addition
			 if (speedValues.size() !=0)
			 {
				 speedValues.clear();
			 }

		 }
		 
		
		 // travelling in high speed and then reducing the speed
		 if (s < Speed_threshold && speedState)
		 {
			 speedValues.add((float) Speed); 
			 
			// SpeedStatus.setText("speed is changed from high to low");
			 HighSpeedFlag=false;
			 
			 if (!period_timer_state) // for first start only 
		
			{
			elapsedPeriod=0;
			PeriodTimer.start();
		 	}
			
		 
		 }
		 
		 
  }
	  
	  public boolean isAccident(){
          if(System.currentTimeMillis() - lastRhoUpdate_ > rhoS_){
                  rho_ = 0;
          }
          
          double gamma = (phi_ / phiM_) + rho_;
          if ((gamma > mTr_  &&beta_)||(phi_ >=phiM_))
          {
        	  LargestAcclelration=phi_;
             return true;
          }
          
          double gammax = (phi_ / phiM_)+SSDIsgreaterSSP+rho_;
          if (gammax > 2.9){
        	  
        	  LargestAcclelration=phi_;
              return true; 
          }
          
          else if (gamma > mTr_  && period_timer_state) // low speed accident with sever acceleration
          {return true;}
          else
          { return false;
  }
          }
	  
	  public void checkForAccident(){
if (isAccident())
	  fireAccidentConfirmation();
	  
	  }
  
	public void fireAccidentConfirmation() {
		// TODO Auto-generated method stub
		if (firedConfirmation_)
		return;

		
		// stop count down timer
		if (period_timer_state)
		{PeriodTimer.cancel();}
		
	// stop timer
		if (timer != null)
	{timer.cancel();}
		
		
		     firedConfirmation_ = true;
		Intent intent = new Intent(context,
           com.example.testwreckdetection.ConfirmationScreen.class);
            intent.putExtra("ShowDialog", true);
            intent.putExtra("acc", LargestAcclelration);
            intent.putExtra("lat", SentLocationLat);
            intent.putExtra("lon", SentLocationLong);
            intent.putExtra("speed", Speed);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
         
			return;
		
	}

}
