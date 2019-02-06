package com.example.testwreckdetection;

import java.util.ArrayList;
import java.util.List;

import com.example.testwreckdetection.AddEmergencyContacts.LoadIndex;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class DeleteEmergencyContacts extends Activity{
ListView delete_contacts_list;
SharedPreferences prefs;
SharedPreferences.Editor prefs_Editor;
String IndexFileName="contact_index";
String FileName="Contact_Detail";
int ImgeId=R.drawable.ic_contact_picture_holo_light;
List<RowItem> rowItems;
DeleteEmergencyContactsAdapter mdeDeleteEmergencyContactsAdapter;
int index;
LoadIndex mLoadData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		rowItems = new ArrayList<RowItem>();
		setContentView(R.layout.delete_emergency_contacts);

        delete_contacts_list=(ListView)findViewById(R.id.DeletelistView1);
        mLoadData=new LoadIndex();
		 mLoadData.execute(IndexFileName);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.delete_contacts_menu, menu);
		return true;}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.contact_delete_menu_done:
			mdeDeleteEmergencyContactsAdapter.DeleteContacts();
			
			break;
		case R.id.contact_delete_menu_cancel:
			finish();
			startActivity(new Intent(getApplicationContext(),AddEmergencyContacts.class));
			break;

		default:
			break;
		}
		
		
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	
	
	
	
	
	public class LoadIndex extends AsyncTask<String, Integer,Boolean>
{
		ArrayList<String>Name,Number;
	
       @Override
		protected Boolean doInBackground(String... params) {
		
			// TODO Auto-generated method stub
			prefs=getSharedPreferences(	IndexFileName, Activity.MODE_PRIVATE);
			 index=prefs.getInt("index", 0);
			
			Name= new ArrayList<String>(index+1);
			Number=new ArrayList<String>(index+1);
			if (index!=0)
			{
				Log .d("returen index", ""+index);
				prefs=getSharedPreferences(	FileName, Activity.MODE_PRIVATE);
				for (int i=0;i<index ;i++)
				{
				Log.d("name"+i, prefs.getString("name"+i, "this"));
		        Name.add( i,prefs.getString("name"+i, "this"));
				Log.d("number"+i, prefs.getString("number"+i, "this"));
				Number.add(i, prefs.getString("number"+i, "this"));
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
				//ReturnNumeAndNumber(Name, Number);
				
			//	Log.d("ret", "true"+index+Name.get(1)+Number.get(1));
				index--;// why ??? check this 
				ReturnNumeAndNumberList(Name, Number);
				
				}
			else
			{
				Log.d("retu", "error");
		}
		}

}

	public void ReturnNumeAndNumberList(ArrayList<String> name,
			ArrayList<String> number) {
		// TODO Auto-generated method stub
		
		for (int i=0;i<name.size();i++)
		{
	displayContactList(ImgeId, name.get(i), number.get(i),i);
		
		}
	}

	private void displayContactList(int imgeId2, String title, String number,
			int i) {
		// TODO Auto-generated method stub
		RowItem item = new RowItem(ImgeId, title, number);
		 rowItems.add(i, item);
		 mdeDeleteEmergencyContactsAdapter = new DeleteEmergencyContactsAdapter(this,  R.layout.add_emergency_contact_item, rowItems);
	  
		 delete_contacts_list.setAdapter(mdeDeleteEmergencyContactsAdapter);
		
	}


}
