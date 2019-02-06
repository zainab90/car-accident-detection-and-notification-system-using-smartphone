package com.example.testwreckdetection;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.HttpURLConnection;

import android.graphics.Bitmap;

public class RowItem {
    private int imageId;
    private String title;
    private boolean selected;
    Bitmap bt;
    private String Number;
    private String status;
    boolean mode;
    HttpURLConnection conn;
    DataOutputStream DOS;
 int visible;
    public RowItem(int imageId, String title) {
        this.imageId = imageId;
        this.title = title;
      
    }
    public RowItem(Bitmap bt, String pic) {
		// TODO Auto-generated constructor stub
    	this.bt=bt;
    	this.title=pic;
	}
    
    public RowItem(int imageId, String title ,String Number)
    {
    	this.imageId=imageId;
    	this.title=title;
    	this.Number=Number;
    }
    
    
    public RowItem(int imageId, String title ,String Status, int x,Boolean Mode ,int Visible, HttpURLConnection URL,DataOutputStream DO)
    {
    	this.imageId=imageId;
    	this.title=title;
    	this.status=Status;
    	this.mode=Mode;
    	this.visible=Visible;
    	this.conn=URL;
    	this.DOS=DO;
    }
    
    
    public void GetConnection( HttpURLConnection URL)
    
    {
    	URL.disconnect();
    	
    }
   public DataOutputStream GetStream()
    
    {
    return DOS;	
    	
    }
    
    
	public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
  
   
    public String getTitle() {
        return title;
    }
    
    public String getUploadStatus() {
        return status;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    
    
    public boolean isSelected() {
        return selected;
    }
 
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    
    
    public void setNumber(String Number)
    {
    	this.Number=Number;
    }
    public String getNumber()
    {return Number;}
    
    
    
    
    public void setDownloadStatus(String Status)
    {
    	this.status=Status;
    }
    
    
    public String getDownloadStatus()
    {return status;}
    
    
    public boolean getIntermediateMode()
    {
    	return mode;
    }
    
    
    public void setIntermediateMode(Boolean mod)
    {
    	this. mode=mod;
    } 
    
    public void setProgressVisiblity(int Visible){
    	this.visible=Visible;
    	
    }
   
    public int getProgressVisiblity(){
    	return visible;
    	
    }
    
    
    
    public void SetConnection(HttpURLConnection url)
    {
    	this.conn=url;
    }
    
    public HttpURLConnection GetConnection()
    {
    	return conn;
    	
    }
    
    
}