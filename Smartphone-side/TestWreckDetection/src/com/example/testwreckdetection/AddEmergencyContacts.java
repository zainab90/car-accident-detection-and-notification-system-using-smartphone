package com.example.testwreckdetection;

import java.util.ArrayList;
import java.util.List;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AddEmergencyContacts extends Activity implements OnClickListener {
Button AddContact;
ListView  emergencyContactsName;
AddEmergencyContactsAdapter mAddEmergencyContactsAdapter;
String contactName,ContactNumber;
int ImgeId=R.drawable.ic_contact_picture_holo_light;
List<RowItem> rowItems;
LoadIndex mLoadData;
String FileName="Contact_Detail";
SharedPreferences Prefs_contact;
SharedPreferences.Editor prefs_contact_Editor;
String FirstLunch;
TextView empty_txt;
boolean choosed;
IsThisFirstLunch mIsThisfirstLunch;
String IndexFileName="contact_index";
int index;
int IndexList;
boolean x=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		rowItems = new ArrayList<RowItem>();
		setContentView(R.layout.add_emergency_contacts);
		AddContact=(Button)findViewById(R.id.btn_add_contact);
		emergencyContactsName=(ListView)findViewById(R.id.AddlistView1);
		AddContact.setOnClickListener(this);
		empty_txt=(TextView)findViewById(R.id.empty_contact);

		 x=true;
		if (choosed)
	 {
			Log.d("chhose",String.valueOf(choosed));
		
	}}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		finish();
		startActivity(new Intent(getApplicationContext(), ContactsListActivity.class));
		
		
	}
	

	

	public void displayContactList (int ImgeId,String title,String number,int index)
	 {
	RowItem item = new RowItem(ImgeId, title, number);
		 rowItems.add(index, item);
		 mAddEmergencyContactsAdapter = new AddEmergencyContactsAdapter(getApplicationContext(),  R.layout.add_emergency_contact_item, rowItems);
	   Log.d("hiii", title+number);
		 emergencyContactsName.setAdapter(mAddEmergencyContactsAdapter);
		 
	 }
	
	public class LoadIndex extends AsyncTask<String, Integer,Boolean>

	{
		
	ArrayList<String>Name,Number;
	

	
		@Override
		protected Boolean doInBackground(String... params) {
		
			// TODO Auto-generated method stub

			empty_txt.setText("");
			Prefs_contact=getSharedPreferences(	IndexFileName, Activity.MODE_PRIVATE);
			 index=Prefs_contact.getInt("index", 0);
			
			Name= new ArrayList<String>();
			Number=new ArrayList<String>();
			if (index!=0)
			{
				Log .d("returen index", ""+index);
				Prefs_contact=getSharedPreferences(	FileName, Activity.MODE_PRIVATE);
				for (int i=0;i<index ;i++)
				{
				Log.d("name"+i, Prefs_contact.getString("name"+i, "this"));
		        Name.add( i,Prefs_contact.getString("name"+i, "this"));
				Log.d("number"+i, Prefs_contact.getString("number"+i, "this"));
				Number.add(i, Prefs_contact.getString("number"+i, "this"));
}
			
				return true;
			}
			else
				{Log .d("returen index from file in add activity:", ""+index);
				return false;}
		}
		@Override
		protected void onPostExecute( Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result )
			{ 
				
				index--;
		Log.d("index after dec",""+ index);
				ReturnNumeAndNumberList(Name, Number);
				
				}
			else
			{
				
				empty_txt.setText("No Contacts");
				Log.d("retu", "error or first lunch");
		}
		}
		
		
	
}
	
	public void IsNotFirstLunch()
	{
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Prefs_contact=getSharedPreferences("FirstLunch", Activity.MODE_PRIVATE);
				prefs_contact_Editor=Prefs_contact.edit();
				prefs_contact_Editor.putString("FirstLunch", "firstLunch");
				prefs_contact_Editor.commit();
				
			}
		}).start();
	}
	

	
	
public void ReturnNumeAndNumberList(ArrayList<String> name,
			ArrayList<String> number) {
		// TODO Auto-generated method stub
	for (int i=0;i<name.size();i++)
	{
		displayContactList(ImgeId, name.get(i), number.get(i),i);
	
	}
}
private class IsThisFirstLunch extends AsyncTask<String , Integer, String>
{
String ReturenString;
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		Prefs_contact=getSharedPreferences(params[0], Activity.MODE_PRIVATE);
		ReturenString=Prefs_contact.getString("FirstLunch", "this");
		if (ReturenString !=null)
			return ReturenString;
			else
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	if (result.equalsIgnoreCase("this"))
	{	empty_txt.setText("No Contacts");
	choosed=false;
	}
	else
	{empty_txt.setText("");
	choosed=true;
	Log.d("chhose",String.valueOf(choosed));
	
	}
	}

}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// TODO Auto-generated method stub
	getMenuInflater().inflate(R.menu.add_emergency_contact, menu);
	return true;}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
	// TODO Auto-generated method stub
	switch (item.getItemId()) {
	case R.id.delete_contact:
	startActivity(new Intent(getApplicationContext(), DeleteEmergencyContacts.class));
finish();
		break;

	default:
		break;
	}
	
	
	return super.onOptionsItemSelected(item);
}

public class LoadIndexForButton extends AsyncTask<String, Integer, Integer>
{

	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		Prefs_contact=getSharedPreferences(	IndexFileName, Activity.MODE_PRIVATE);
		 index=Prefs_contact.getInt("index", 0);
		 if (index !=0)
		 {return index;}
		 
		 else
		 {return 0;}
	}

	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result >0)
		{IndexList=result-1;
		Log.d("indexlist to add ", ""+IndexList);}
		else
		{IndexList=0;
		Log.d("indexlist to add ", ""+IndexList);}
		
	}
	
}

@Override
protected void onResume() {
	// TODO Auto-generated method stub
	super.onResume();
	mLoadData=new LoadIndex();
	 mLoadData.execute(IndexFileName);
}
@Override
protected void onPause() {
	// TODO Auto-generated method stub
	super.onPause();
	x=false;
}



	
}
