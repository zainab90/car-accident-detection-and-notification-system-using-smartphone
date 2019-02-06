package com.example.testwreckdetection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;





import android.app.Activity;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class ImageTextListViewActivity extends Activity implements
OnItemClickListener, OnItemSelectedListener {

public static final String[] titles = new String[] {  "Activate/Deactivate Accident detection ","Upload Image/Video ",
     "Choose Emergency contacts","Setting", };

CustomListViewAdapter adapter;
String classes[]={"Activate_Accident_Detection","Uploads","AddEmergencyContacts","Setting"};

public static final Integer[] images = {  R.drawable.accident_dervice_1,R.drawable.attachment_01,
	
  
    R.drawable.emergency_contact_1,R.drawable.settings,R.drawable.settings,R.drawable.ic_action_about};
SharedPreferences prefs;
SharedPreferences.Editor editor;
String IndexFile="ListIndex";
ListView listView;
List<RowItem> rowItems;
int index=0;
SharedPreferences Prefs;
SharedPreferences.Editor zEdit;
/** Called when the activity is first created. */
@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

setContentView(R.layout.list_activity);
Prefs=getApplicationContext().getSharedPreferences("videoPathFile", Context.MODE_PRIVATE);
zEdit=Prefs.edit();
zEdit.clear();
zEdit.commit();


rowItems = new ArrayList<RowItem>();
for (int i = 0; i < titles.length; i++) {
    RowItem item = new RowItem(images[i], titles[i]);
    rowItems.add(item);
}
new SaveIndex().execute(index);
listView = (ListView) findViewById(R.id.listView1);
 adapter = new CustomListViewAdapter(this,
        R.layout.list_item, rowItems);
listView.setAdapter(adapter);
listView.setOnItemClickListener(this);


}


@Override
public void onItemClick(AdapterView<?> parent, View view, final int position,
    long id) {
	Toast.makeText(getApplicationContext(),"selected", Toast.LENGTH_SHORT).show();
	new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String mypos=classes[position];

			try{
				
				Class myclass=Class.forName("com.example.testwreckdetection."+mypos);
			Intent intent=new Intent(ImageTextListViewActivity.this,myclass);
			startActivity(intent);
			}
			catch(ClassNotFoundException e){ e.printStackTrace();}
		}
	}).start();
	
}
	


@Override
public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	
}


@Override
public void onAttachedToWindow() {
	// TODO Auto-generated method stub
	super.onAttachedToWindow();
	View view = getWindow().getDecorView();
	   //view.setBackgroundColor(Color.TRANSPARENT);
	   WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view.getLayoutParams();
	  lp.gravity = Gravity.LEFT;
	//
	   lp.width = 830;
	    lp.height = 2000;
	    getWindowManager().updateViewLayout(view, lp);
}


@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub	
}


@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        finish();}
    if (keyCode== KeyEvent.KEYCODE_DPAD_CENTER)
    {overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    finish();}
    return super.onKeyDown(keyCode, event);
}

private class SaveIndex extends AsyncTask<Integer, Void, Boolean>
{
int saved_index;
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result)
		{Toast.makeText(getApplicationContext(), "saved"+saved_index, Toast.LENGTH_LONG).show();
		}
		else
		{Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected Boolean doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		saved_index=params[0];
		prefs=getSharedPreferences(IndexFile, Activity.MODE_PRIVATE);
		editor=prefs.edit();
		editor.putInt("index", params[0]);
		if(editor.commit())
	
		return true;
		else
			return false;
	}
	
	
}


	
}