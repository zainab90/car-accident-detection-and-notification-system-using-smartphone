package com.example.testwreckdetection;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteEmergencyContactsAdapter extends ArrayAdapter<RowItem>{
	static RowItem rowItem; Context context;
	UpdateFile mUpdateFile;
	List<RowItem>ri;
	String IndexFileName="contact_index";
	String FileName="Contact_Detail";
	SharedPreferences prefs;
	SharedPreferences.Editor editor;
	SaveNewIndex mSaveNewIndex;
	  Set<Integer> set = new HashSet<Integer>();
	public DeleteEmergencyContactsAdapter(Context context, int resourceId,
            List<RowItem> items) {
		super(context, resourceId, items);
        this.context = context;
        this.ri=items;
		// TODO Auto-generated constructor stub
     
	}
	
	 private class ViewHolder {
	        ImageView imageView;
	        TextView txtTitle,txtNum;
	       CheckBox MyCheckBox;
	       
	   }
	
	 public View getView(final int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        rowItem = getItem(position);
	 
	        LayoutInflater mInflater = (LayoutInflater) context
	                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.delete_emergency_contact_item, null);
	            holder = new ViewHolder();
	           
	          
	            holder.txtTitle = (TextView) convertView.findViewById(R.id.delete_emergency_contact_name);
	            holder.imageView = (ImageView) convertView.findViewById(R.id.delete_emergency_contact_icon);
	            holder.txtNum=(TextView)convertView.findViewById(R.id.delete_emergency_contact_number);
	            holder.MyCheckBox=(CheckBox)convertView.findViewById(R.id.icon_remove);
	            holder.MyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
						// TODO Auto-generated method stub
				          final int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
		                    ri.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
		     new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
			        if (isChecked)
		             { 
		             set.add(getPosition);
		            	Log.d("check from pos ", ""+getPosition+" "+set.size());
		    
		             
		             }
		             else
		             { 
		            	set.remove(getPosition);
		     Log.d("removed pos ", ""+getPosition+" "+set.size());
		             }
				}
			}).start();
						
					}
				});
	            
	          convertView.setTag(holder);
	          convertView.setTag(R.id.delete_emergency_contact_name, holder.txtTitle);
	            convertView.setTag(R.id.delete_emergency_contact_icon, holder.imageView);
	            convertView.setTag(R.id.delete_emergency_contact_number,holder.txtNum);
	            convertView.setTag(R.id.icon_remove,holder.MyCheckBox);
   
	        } else
	          
	        	holder = (ViewHolder) convertView.getTag();
	        holder.MyCheckBox.setTag(position);
	          holder.txtTitle.setText(rowItem.getTitle());
	          holder.imageView.setImageResource(rowItem.getImageId());
	          holder.txtNum.setText(rowItem.getNumber());
	          holder.MyCheckBox.setChecked(ri.get(position).isSelected());
	       
				
			
	     return convertView;
	 }
	
	 
	 @SuppressWarnings("unchecked")
	public void DeleteContacts()
	 {
		 Log.d("set size", ""+set);
		 mUpdateFile=new UpdateFile();
		 if (set .size() !=0)
			{mUpdateFile.execute(set);
			}
	 }
	 
	 
	 
	 
	 
	 
	 
	 
private class UpdateFile extends AsyncTask<Set<Integer>, Integer, Boolean>
{
	int index;

	@Override
	protected Boolean doInBackground(Set<Integer>... params) {
		// TODO Auto-generated method stub
		Log.d("removed position from set is/are :", ""+params[0]);
		prefs=context.getSharedPreferences(IndexFileName, Activity.MODE_PRIVATE);
	
		 index=prefs.getInt("index", 0);
		 if (index!=0)
			{
			 
			 // it is wrong
			 
		//	 index--; // check i think the index should be decrimented by the set size.
			  
			 Integer[] strArr = params[0].toArray(new Integer[set.size()]);
			 index= index- strArr.length; 
			 
			 
	          //    for (int i = 0; i < strArr.length; i++) {
	            //        Log.d("ss",""+strArr[i]);
	             
	          
			 
			 prefs=context.getSharedPreferences(FileName, Activity.MODE_PRIVATE);
				editor=prefs.edit();
			 for (int j=strArr[0];j<=strArr.length;j++) // check this loop
			 {
			String nam=prefs.getString("name"+(j+1), "this");
			Log.d("name to be removed", ""+ nam);
			String num=prefs.getString("number"+(j+1), "this");
			editor.putString("name"+j, nam).commit();
			editor.putString("number"+j, num).commit();
			
			 }
			 
	              
	              //}
		 return true;}
		 else
			 return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result)
		{Toast.makeText(context, "transform is done ", Toast.LENGTH_SHORT).show();

		Log.d("new index is after delete ", ""+index);
		mSaveNewIndex=new SaveNewIndex();
		mSaveNewIndex.execute(index);
		
		}
		else
		{Toast.makeText(context, "error ", Toast.LENGTH_SHORT).show();}
	}
}

private class SaveNewIndex extends AsyncTask<Integer, Integer, Boolean>
{

	@Override
	protected Boolean doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		prefs=context.getSharedPreferences(IndexFileName, Activity.MODE_PRIVATE);
		editor=prefs.edit();
		editor.putInt("index", params[0]);
		Log.d("index has been saved is ", ""+params[0]);
		editor.commit();
		if (editor.commit())
		return true;
		else
			return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result)
		{Toast.makeText(context, "save new index after delete", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(context, AddEmergencyContacts.class);
		  
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
		((Activity)(context)).finish();

	
		}
		else
		{Toast.makeText(context, "error in saving new index", Toast.LENGTH_SHORT).show();}

	}
	




}



}