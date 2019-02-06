package com.example.testwreckdetection;

import java.util.ArrayList;


import com.example.testwreckdetection.util.Utils;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;


public class ContactsListActivity extends FragmentActivity implements
ContactsListFragment.OnContactsInteractionListener {

// Defines a tag for identifying log entries
private static final String TAG = "ContactsListActivity";

//private ContactDetailFragment mContactDetailFragment;

// If true, this is a larger screen device which fits two panes
private boolean isTwoPaneLayout;
int i=0;
// True if this activity instance is a search result view (used on pre-HC devices that load
// search results in a separate instance of the activity rather than loading results in-line
// as the query is typed.
private boolean isSearchResultView = false;
public String[] Contact_Number=new String[10];
public String[]Contact_Name=new String[10];
int Contact_index;
boolean[] itemsChecked;
CharSequence[]returendNumber;
SaveContactDetail mSaveContactDetail;
String FileName="Contact_Detail";
SharedPreferences Prefs_contact;
SharedPreferences.Editor Edit_contact;
String NumberChoosed,NameChoosed;
LoadIndex mLoadIndex;
String IndexFileName="contact_index";
SaveIndex mSaveIndex;
ArrayList<String>ToatalContactsNames=new ArrayList<String>();
ArrayList<String>ToatalContactsNumbers=new ArrayList<String>(); 
int pos;
@Override
protected void onCreate(Bundle savedInstanceState) {
if (BuildConfig.DEBUG) {
    Utils.enableStrictMode();
}
super.onCreate(savedInstanceState);

// Set main content view. On smaller screen devices this is a single pane view with one
// fragment. One larger screen devices this is a two pane view with two fragments.
setContentView(R.layout.activity_main);

//initially load index
mLoadIndex=new LoadIndex();
mLoadIndex.execute(IndexFileName);

//Contact_Name.add(null);
//Contact_Number.add(null);
// Check if two pane bool is set based on resource directories
isTwoPaneLayout = getResources().getBoolean(R.bool.has_two_panes);

// Check if this activity instance has been triggered as a result of a search query. This
// will only happen on pre-HC OS versions as from HC onward search is carried out using
// an ActionBar SearchView which carries out the search in-line without loading a new
// Activity.
if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {

    // Fetch query from intent and notify the fragment that it should display search
    // results instead of all contacts.
    String searchQuery = getIntent().getStringExtra(SearchManager.QUERY);
    ContactsListFragment mContactsListFragment = (ContactsListFragment)
            getSupportFragmentManager().findFragmentById(R.id.contact_list);

    // This flag notes that the Activity is doing a search, and so the result will be
    // search results rather than all contacts. This prevents the Activity and Fragment
    // from trying to a search on search results.
    isSearchResultView = true;
    mContactsListFragment.setSearchQuery(searchQuery);

    // Set special title for search results
    String title = getString(R.string.contacts_list_search_results_title, searchQuery);
    setTitle(title);
}

if (isTwoPaneLayout) {
    // If two pane layout, locate the contact detail fragment
 //   mContactDetailFragment = (ContactDetailFragment)
   //         getSupportFragmentManager().findFragmentById(R.id.contact_detail);
}
}


public void onContactSelected(String name,final ArrayList<CharSequence>  number,Integer position) {
	

	if (number.size() !=0)
	
	{
		
	 itemsChecked = new boolean [number.size()];
	 returendNumber=new CharSequence[number.size()];
	 NameChoosed=name;
	 pos=position;
	 Log.d("chhosing name is ", NameChoosed);
	 Contact_Name[position]=name;
for (int i=0;i<number.size();i++)
{Log .i("number is ",(String) number.get(i));
returendNumber[i]=number.get(i);
}
Log.d("array size",""+ number.size());
 if (number.size()>1)
{showDialog(0);}
 else
 { 
 NumberChoosed=number.get(0).toString();
 Log.d("choosing number is :",NumberChoosed);
 Contact_Number[position]=NumberChoosed;

 }

}
}
/**
* This interface callback lets the main contacts list fragment notify
* this activity that a contact is no longer selected.
*/
@Override
public void onSelectionCleared() {
//if (isTwoPaneLayout && mContactDetailFragment != null) {
   // mContactDetailFragment.setContact(null);
//}
}

@Override
public boolean onSearchRequested() {
// Don't allow another search if this activity instance is already showing
// search results. Only used pre-HC.
return !isSearchResultView && super.onSearchRequested();
}


@Override

protected Dialog onCreateDialog(int id) {
	// TODO Auto-generated method stub
	switch (id) {
	case 0:
		new AlertDialog.Builder(this)
		.setIcon(R.drawable.emergency_contact_1)
		.setTitle("Choose Number")
		.setPositiveButton("OK", 
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,	int whichButton)
				{
					Toast.makeText(getBaseContext(),
							"OK clicked!", Toast.LENGTH_SHORT).show();
				
				}
		    }
		)
		.setNegativeButton("Cancel", 
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{
					Toast.makeText(getBaseContext(),
							"Cancel clicked!", Toast.LENGTH_SHORT).show();
				}
			}
		)
		.setSingleChoiceItems(returendNumber, -1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), ""+returendNumber[which], Toast.LENGTH_SHORT).show();
				
				NumberChoosed=returendNumber[which].toString();
				Log.d("choosing number from dialoge", NumberChoosed);
				Contact_Number[pos]= NumberChoosed;
				
			
			}
		})
		
		
		
		.create().show();
		
		break;

	default:
		break;
	}
	return null;
}


@SuppressWarnings("unchecked")
@Override
public void onDoneIsClicked() {

	Log.d("finish", "finish");
	 mSaveContactDetail=new SaveContactDetail();
	 mSaveContactDetail.execute(FileName);

	
}


@Override
public void finish() {
	// TODO Auto-generated method stub

	super.finish();

}

private class SaveContactDetail extends AsyncTask<String, Integer, Boolean>

{
boolean edite;
	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		Prefs_contact=getSharedPreferences(params[0], Activity.MODE_PRIVATE);
        Edit_contact=Prefs_contact.edit();
       for (int y=0;y< Contact_Name.length;y++)
       {   if (Contact_Name[y]!=null && Contact_Number[y]!=null)
       { Edit_contact.putString("name"+Contact_index,Contact_Name[y]);
        Edit_contact.putString("number"+Contact_index, Contact_Number[y]);
       Edit_contact.commit();
       Contact_index++;}
       
       
       }
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result)
		{ 
			Toast.makeText(getApplicationContext(), "your contacts have been saved", Toast.LENGTH_LONG).show();
		mSaveIndex=new SaveIndex();
		mSaveIndex.execute(Contact_index);
		}
		else
		{Toast.makeText(getApplicationContext(), "your contacts have not been saved", Toast.LENGTH_LONG).show(); }
		
	
	}
}

public class LoadIndex extends AsyncTask<String, Integer, Integer>
{
int index;
	@Override
	protected Integer doInBackground(String... params) {
		// TODO Auto-generated method stub
		Prefs_contact=getSharedPreferences(params[0], Activity.MODE_PRIVATE);
	     index=	Prefs_contact.getInt("index", 0);
		if (index!=0)
			return index;
		else
		return 0;
	}
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result !=0)
			{Contact_index=result;

Log.d("my index loded from file in contactlistactivity ", ""+Contact_index);
			}
		else
			Contact_index=0;

Log.d("my index loded from file in contactlistactivity ", ""+Contact_index);
	}
}

public class SaveIndex extends AsyncTask<Integer, Integer, Boolean>
{ int my_index; 

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result)
		{
		Log.d(" index to be saved after co. selec",""+my_index);		
	
		startActivity(new Intent(getApplicationContext(), AddEmergencyContacts.class));
		finish();
		}
		else
		{
			Toast.makeText(getApplicationContext(), "error in saving new index", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected Boolean doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		Prefs_contact=getSharedPreferences(IndexFileName, Activity.MODE_PRIVATE);
		Edit_contact=Prefs_contact.edit();
		Edit_contact.putInt("index", params[0]);
		my_index=params[0];
		 if (Edit_contact.commit())
			 return true;
		 else
			 return false;
		
	}
	





}

@Override
public void RemoveFromList(int id) {
	// 
Log.d("removed position", ""+id);
Contact_Name[id]=null;
Contact_Number[id]=null;
	
}



}
