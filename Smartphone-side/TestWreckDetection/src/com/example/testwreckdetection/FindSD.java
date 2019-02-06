package com.example.testwreckdetection;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;

public class FindSD {
	int MILISECOND=1000;
	int NO_MINUTE=3;
	int MIN_SEC=60;
	int total=MILISECOND*NO_MINUTE*MIN_SEC;
	List<Float>speedValues= new ArrayList<Float>();
	
	float mean=0F;
	float summation,sub,sum,SD=0F;
	Timer timer;
	TimerTask timerTask;
	
	//we are going to use a handler to be able to run in our TimerTask
		final Handler handler = new Handler();
		
		public void FillTheList(float speed)
		{
			speedValues.add((float) 0);
			speedValues.add((float) speed);
		}
		
		
		
		
		public void StartTimer(){
			
			timer = new Timer();
			
			//initialize the TimerTask's job
			initializeTimerTask();
			
			//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
			timer.schedule(timerTask, 5000, total); //
		}
		
		
		public void stoptimertask() {
			//stop the timer, if it's not already null
			if (timer != null) {
				timer.cancel();
				timer = null;
				
			}
		}
		public void initializeTimerTask() {
			
			timerTask = new TimerTask() {
				public void run() {
					
					//use a handler to run a toast that shows the current timestamp
					handler.post(new Runnable() {
						public void run() {
							//get the current timeStamp
							SD=0;
							CalculateSD();
							speedValues.clear();
							speedValues.add((float) 0);
						}
					});
				}
			};
		}
		
		protected void CalculateSD() {
			
			// TODO Auto-generated method stub
			//Toast.makeText(getApplicationContext(), ""+speedValues.size(), Toast.LENGTH_LONG).show();

		if ( !speedValues.isEmpty() )
		{
			
			
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
		}
		
		
		
		}
		
		public float RetSD()
		{
			if (!speedValues.isEmpty() && SD==0)
			{CalculateSD();
			return SD;}
			else
			{
				return SD;
			}
		}
		
		
		
}
