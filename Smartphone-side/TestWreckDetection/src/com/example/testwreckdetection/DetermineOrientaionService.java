package com.example.testwreckdetection;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

public class DetermineOrientaionService extends Service  {
SensorManager sensorManager;
FindOrientation mFindOrientation;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mFindOrientation=new FindOrientation();
		mFindOrientation.execute("");
		return START_STICKY;
	}

private class FindOrientation extends AsyncTask<String, Integer, Boolean> implements SensorEventListener
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
		{stopSelf();}
		else
		{
			if (fu)
			{Toast.makeText(getApplicationContext(), "faceUp", Toast.LENGTH_SHORT).show();}
			else
			{Toast.makeText(getApplicationContext(), "faceDown", Toast.LENGTH_SHORT).show();}
			
			
		}
	}
	




}





	

	
	

}
