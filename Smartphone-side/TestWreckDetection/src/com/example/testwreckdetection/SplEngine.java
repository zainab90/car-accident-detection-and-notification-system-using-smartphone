package com.example.testwreckdetection;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

/**
 * 
 * @author Hashir N A <hashir@mobware4u.com>
 * 
 */
public class SplEngine extends Thread {
    private static final int FREQUENCY = 44100;
    private static final int CHANNEL = 16;
    private static final int ENCODING = 2;
    
    private static final int MY_MSG = 1;
    private static final int MAXOVER_MSG = 2;
    private static final int ERROR_MSG = -1;
    
    private volatile int BUFFSIZE = 0;
    private static final double P0 = 2.0E-006D;

    private static final int CALIB_INCREMENT = 3;
    private static final int CALIB_DEFAULT = -80;
    private int mCaliberationValue = -80;

    private volatile boolean mIsRunning = false;
    private Handler mHandle = null;

    private double mMaxValue = 0.0D;
    private volatile boolean mShowMaxValue = false;

    private FileWriter mSplLog = null;
    private volatile boolean mIsLogging = false;
    private static String LOGPATH = "/sdcard/splmeter_";
    private int LOGLIMIT = 50;
    private int logCount = 0;

    private volatile String mode = "FAST";

    AudioRecord mRecordInstance = null;
    Context mContext = null;
    String PREFS_NAME = "SPLMETER";
    
    File PathFile,mYFile=null;
	boolean CanR,CanW;
	BufferedWriter out;
	 File root,gpxfile;
	 FileWriter gpxwriter;
	 String state, strDate;
	 float MS2S = 1.0f / 1000.0f;
	
    
    public SplEngine(Handler handle, Context context) {
    	
            this.mHandle = handle;
            this.mContext = context;
            this.mCaliberationValue = readCalibValue();
            this.mode = "FAST";
            this.mIsLogging = false;
            this.mIsRunning = false;
            this.mMaxValue = 0.0;
            this.mShowMaxValue = false;
            
            BUFFSIZE = AudioRecord.getMinBufferSize(
                                                            FREQUENCY, 
                                                            CHANNEL, 
                                                            ENCODING);
            
            mRecordInstance = new AudioRecord(
                                                            MediaRecorder.AudioSource.MIC,
                                                            FREQUENCY, CHANNEL, 
                                                            ENCODING, BUFFSIZE*2);
            
            // this is for the fast mode
            BUFFSIZE = AudioRecord.getMinBufferSize(
                            FREQUENCY, 
                            CHANNEL, 
                            ENCODING)*2;
            
            
            
            // create file to save values:
            
            
    }
    
    
    
	
private void CreateNewFile() {
		// TODO Auto-generated method stub
	checkState();
	
	if (CanR=CanW=true)
	{
		
		try {
		     root = Environment.getExternalStorageDirectory();
		     
		    if (root.canWrite()){
		        gpxfile = new File(root, "Sound_test.csv");
		        gpxwriter = new FileWriter(gpxfile,true);
		       out = new BufferedWriter(gpxwriter);
out.append("dB_level"+","+"time");
		       out.newLine();
		    }
		} catch (IOException e) {
		    //Log.e(, "Could not write file " + e.getMessage());
e.printStackTrace();
		
	}
		
		
		
		
	}
	
		
	}

private void checkState() {
	// TODO Auto-generated method stub
	state=Environment.getExternalStorageState();
    // now cheak what state is if we can access to sd card or not
    if (state.equals(Environment.MEDIA_MOUNTED))//MEDIA_MOUNTED it mean we can read and write
    {	
	     CanR=CanW=true;
    }
    else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
    { // read but not write
    	
		CanW=false;
		CanR=true;}
		else 
			//it can't read or write
		{
		CanR=CanW=false;}
	
}
    
    

    /**
     * Reset the Engine by deleting all calibration details 
     * and resetting it to a default value
     */
    public void reset() {
            SharedPreferences settings = PreferenceManager
                            .getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("CalibSlow", CALIB_DEFAULT);

            editor.putInt("CalibFast", CALIB_DEFAULT);
            editor.commit();
            mCaliberationValue = CALIB_DEFAULT;
    }

    
    
    /**
     * starts the engine.
     */
    public void start_engine() {
            this.mIsRunning = true;
            if (this.getState()== Thread.State.NEW)
            {  this.start();
            CreateNewFile();
            }
    
    }

    
    
    /**
     * stops the engine
     */
    public void stop_engine() {
            this.mIsRunning = false;
            try {
				out.flush();
				 out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

    
    
    /**
     * sets mode as fast or slow     
     * @param mode
     */
    public void setMode(String mode) {
            this.mode = mode;
            setCalibValue(readCalibValue());
            
            if ("SLOW".equals(mode)) {
                    BUFFSIZE = AudioRecord.getMinBufferSize(
                                    FREQUENCY, 
                                    CHANNEL, 
                                    ENCODING)*30;                   
                    LOGLIMIT = 10;
            } else {
                    BUFFSIZE = AudioRecord.getMinBufferSize(
                                    FREQUENCY, 
                                    CHANNEL, 
                                    ENCODING)*2;                    
                    LOGLIMIT = 50;
            }
            
            
    }

    
    
    /**
     * Returns the current calibration value         
     * @return
     */
    public int getCalibValue() {
            return mCaliberationValue;
    }

    
    
    /**
     * Sets the calibration value to a value passed.
     * @param value
     */
    public void setCalibValue(int value) {
            mCaliberationValue = value;
    }

    
    
    /**
     * Read the calibration values 
     * @return
     */
    public int readCalibValue() {           
            SharedPreferences settings = mContext
                                                                            .getSharedPreferences(
                                                                                            PREFS_NAME, 
                                                                                            Context.MODE_WORLD_READABLE);   
            return settings.getInt(mode, CALIB_DEFAULT);
    }

    
    
    /**
     * Stores the current calibration value to a file separate calibration for
     * SLOW and FAST modes
     */
    public void storeCalibvalue() {
            SharedPreferences settings = mContext
                                                                            .getSharedPreferences(
                                                                                            PREFS_NAME, 
                                                                                            Context.MODE_WORLD_WRITEABLE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(mode, mCaliberationValue);
            editor.commit();
    }

    
    
    /**
     * Increase the calibration by an fixed increment
     */
    public void calibUp() {
            mCaliberationValue = mCaliberationValue + CALIB_INCREMENT;
            if (mCaliberationValue == 0) {
                    mCaliberationValue = mCaliberationValue + 1;
            }
    }

    
    
    /**
     * Decrease the calibration by a fixed value
     */
    public void calibDown() {
            mCaliberationValue = mCaliberationValue - CALIB_INCREMENT;
            if (mCaliberationValue == 0) {
                    mCaliberationValue = mCaliberationValue - 1;
            }
    }

    
    /**
     *  Display max value for 2 seconds and then resume 
     */
    public double showMaxValue() {
            mShowMaxValue = true;
            return mMaxValue;
    }

    
    /**
     * Get the maximum value recorded so far         
     * @return
     */
    public double getMaxValue() {
            return mMaxValue;
    }
    

    /**
     * Sets the max value of SPL
     */
    public void setMaxValue(double max) {
            mMaxValue = max;
    }

    
    /**
     * Start logging the values to a log file
     */
    public void startLogging() {
            mIsLogging = true;
    }

    
    
    /**
     * Stop the logging
     */
    public void stopLogging() {
            mIsLogging = false;
    }

    
    
    /**
     * If logging, then store the spl values to a log file. 
     * separate log file for each day.
     */
    private void writeLog(double value) {
            if (mIsLogging) {
                    if (logCount++ > LOGLIMIT) {
                            try {
                                    Date now = new Date();

                                    mSplLog = new FileWriter(LOGPATH + now.getDate() + "_"
                                                    + now.getMonth() + "_" + (now.getYear() + 1900)
                                                    + ".xls", true);
                                    mSplLog.append(value + "\n");
                                    mSplLog.close();

                            } catch (Exception e) {
                                            e.printStackTrace();
                            }
                            logCount = 0;
                    }
            }
    }

    
    /**
     * The main thread. Records audio and calculates the SPL The heart of the
     * Engine.
     */
    public void run() {
            try {
                    mRecordInstance.startRecording();

                    double splValue = 0.0;
                    double rmsValue = 0.0;
                    

                    while (this.mIsRunning) {
                            
                            // creating these variables here so that
                            // the mode change can be handled
                            int SIZE = BUFFSIZE;
                            short[] tempBuffer = new short[SIZE];
                            
                            mRecordInstance.read(tempBuffer, 0, SIZE);
                            
                            
                            for (int i = 0; i < SIZE - 1; i++) {
                                    rmsValue += tempBuffer[i] * tempBuffer[i];
                            }
                            rmsValue = rmsValue / SIZE;
                            rmsValue = Math.sqrt(rmsValue);

                            splValue = 20 * Math.log10(rmsValue / P0);
                            splValue = splValue + mCaliberationValue;
                            splValue = round(splValue, 2);

                           if (splValue >=100)
                           {splValue=splValue+33;}
                            
                            if (mMaxValue < splValue) {
                                    mMaxValue = splValue;
                            }

                            if (!mShowMaxValue) {
                                    Message msg = mHandle.obtainMessage(MY_MSG, splValue);
                                    mHandle.sendMessage(msg);
                            } else {
                                    Message msg = mHandle.obtainMessage(MY_MSG, mMaxValue);
                                    mHandle.sendMessage(msg);
                                    Thread.sleep(2000);
                                    msg = mHandle.obtainMessage(MAXOVER_MSG, mMaxValue);
                                    mHandle.sendMessage(msg);
                                    mShowMaxValue = false;
                            }
                            
                            
                            
                        
                            

                       //     writeLog(splValue);
                            
                        	try {
                        		 Calendar calendar = Calendar.getInstance();
                        			
                        			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss:SS a");
                        		
                        		strDate = simpleDateFormat.format(calendar.getTime());
        						out.append(String.valueOf(splValue)+","+strDate);
        					       out.newLine();
        					       
        					     
        					     		} catch (IOException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
                            
                            
                            
                            
                    }

            } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = mHandle.obtainMessage(ERROR_MSG, 
                                                                    e.getLocalizedMessage()+"");
                    mHandle.sendMessage(msg);
            }
            if(mRecordInstance != null){
                    mRecordInstance.stop();
                    mRecordInstance.release();
                    mRecordInstance = null;
            }
    }

    
    /**
     * Utility function for rounding decimal values
     */
    public double round(double d, int decimalPlace) {
            // see the Javadoc about why we use a String in the constructor
            // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
            BigDecimal bd = new BigDecimal(Double.toString(d));
            bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
            return bd.doubleValue();
    }

}