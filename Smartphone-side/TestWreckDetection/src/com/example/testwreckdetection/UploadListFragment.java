package com.example.testwreckdetection;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import java.net.HttpURLConnection;

import com.google.android.gms.internal.ep;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class UploadListFragment  extends Fragment
{
	UploadListViewAdapter uploao_Apapter;
	List<RowItem> upload_rowItems;
	//public static final String[] upload_titles = new String[] {null};
	ArrayList<String>upload_titles= new ArrayList<String>();
	public static final Integer[] upload_image = new Integer[] {0};
	int upload_index;
	ListView listview;
	String Tiltle=null;
	int mImageId=0;
	
	Intent camera_intent,Video_intent,upload_intant;
	int Camera_res,Video_res,upload_res=8;
	String pic_video="";

	 ArrayList<String> upload_show_title=new ArrayList<String>();
	 static String fileName="MySharedString";
	 SharedPreferences someDate;
		TextView empty_text;
		List<RowItem> rowItems;
		ArrayList<String> myAList=new ArrayList<String>();
		private SharedPreferences preferences;
		String Lunch;
		  ArrayList<String>  list = new ArrayList<String>();
		  String timeStamp;
	  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	myAList.add("z");
	upload_titles.add( "z");
	
Log.d("hi again", "ok");
		View v;
		v= inflater.inflate(  R.layout.upload_list_fragment, container, false);
	
	listview=(ListView)v.findViewById(R.id.upload_list_image_video);
	empty_text=(TextView)v.findViewById(R.id.upload_empty_text);
rowItems = new ArrayList<RowItem>();
empty_text=(TextView)v.findViewById(R.id.upload_empty_text);

	return v;
	}
	
	
	

	

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

		Uploads h=(Uploads) getActivity();
		if (h != null)
		//load from perefrence file
		{	
			// 
			
	//    Location_place.setOnClickListener(this);
	 
	}
		else 
		{Log.d("hi", "not selected");}
	}


 public void displayList (int ImgeId,String pic,int pos,String status,Boolean mode, int Visible, HttpURLConnection URL, DataOutputStream DO)
 {
	 RowItem item = new RowItem(ImgeId,pic,status,0,mode,Visible,URL,DO);
	 rowItems.add(pos, item);
	 uploao_Apapter = new UploadListViewAdapter(getActivity(),  R.layout.upload_list_item, rowItems );
   	 listview.setAdapter(uploao_Apapter);
	 
 }

 public void display_List (Bitmap ImgeId,String pic)
 {
	 RowItem item = new RowItem(ImgeId,pic);
	 rowItems.add(0, item);
	 uploao_Apapter = new UploadListViewAdapter(getActivity(),  R.layout.upload_list_item, rowItems);
   	 listview.setAdapter(uploao_Apapter);
	 
 }
 
public void checkFirstLunch(String lunch_activity){
	
	    preferences = this.getActivity().getSharedPreferences("ProfileNames", Activity.MODE_PRIVATE);
	    SharedPreferences.Editor preferencesEditor = preferences.edit();
	    preferencesEditor.putString("name" , lunch_activity);
	    preferencesEditor.commit();
	    empty_text.setText(lunch_activity);
	 
}


private String load() {
	// TODO Auto-generated method stub
    preferences = this.getActivity().getSharedPreferences("ProfileNames", Activity.MODE_PRIVATE);
    String ReurnData=preferences.getString("name","this");
    empty_text.setText(ReurnData+"zainab");
    return ReurnData;
	
}
public void saveIndex(int index)
{
	
preferences = this.getActivity().getSharedPreferences("upload_ListIndex", Activity.MODE_PRIVATE);
SharedPreferences.Editor preferencesEditor = preferences.edit();
preferencesEditor.putInt("index", index);
Log.d("comming index is :", ""+index);
preferencesEditor.commit();}

public int returnIndex()
{preferences = this.getActivity().getSharedPreferences("upload_ListIndex", Activity.MODE_PRIVATE);
int x=preferences.getInt("index", 0);
return x;}

public void saveIntoAdapter(int img_video_Id,String title,int index)
{  
//upload_titles.add(index, title);
preferences = this.getActivity().getSharedPreferences("upload_ListName", Activity.MODE_PRIVATE);
SharedPreferences.Editor preferencesEditor = preferences.edit();
preferencesEditor.putString("value"+index, title);
preferencesEditor.putInt("pic_video_id", img_video_Id);
Log.d("save in prefre.",title);
preferencesEditor.putInt("listSize",index);
preferencesEditor.commit();

}
public void LoadAdapter()
{preferences = this.getActivity().getSharedPreferences("upload_ListName", Activity.MODE_PRIVATE);
int pref_size=preferences.getInt("listSize", 0);
for (int j=pref_size;j>=0;j--)
{
Log.d("loaded from pref",preferences.getString("value"+j, "" ));
RowItem item = new RowItem(preferences.getInt("pic_video_id"+j,0), preferences.getString("value"+j, ""));
rowItems.add(item);
uploao_Apapter = new UploadListViewAdapter((Uploads)getActivity(),
        R.layout.upload_list_item, rowItems);
listview.setAdapter(uploao_Apapter);}
}
public String splitName(String pic_video_Path) {
		// TODO Auto-generated method stub
  	String result = pic_video_Path.substring(pic_video_Path.lastIndexOf("/") +1);
  	Log.d("", result);
  	return result;



}









private void saveInPrefAgain(int pos) {
	// TODO Auto-generated method stub
	preferences = this.getActivity().getSharedPreferences("upload_ListName", Activity.MODE_PRIVATE);
	SharedPreferences.Editor preferencesEditor = preferences.edit();
	int pref_new_size=preferences.getInt("listSize", 0);
	for (int x=pos; x<= pref_new_size;x++)
	{ String str_title=preferences.getString("value"+(x+1), "");
	Log.d("loaded", ""+str_title);
	preferencesEditor.putString("value"+x, str_title);
	Log.d("saved", ""+str_title);
	}
}


}
