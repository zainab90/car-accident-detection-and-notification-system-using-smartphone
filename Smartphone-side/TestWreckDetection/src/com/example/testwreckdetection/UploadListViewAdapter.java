package com.example.testwreckdetection;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;








import com.example.testwreckdetection.Uploads.Upload_Img_VID_File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UploadListViewAdapter extends ArrayAdapter<RowItem>{
	static RowItem rowItem;
	Context context;
    FragmentActivity activity;
    ViewHolder holder = null;
    UploadListFragment ls;
    SharedPreferences preferences;
    SharedPreferences.Editor pref_Editor;
List<RowItem>ri;
private ArrayList<Integer> itemChecked = new ArrayList<Integer>();
private ArrayList<Integer> isRemovecheck = new ArrayList<Integer>();
UploadImg_Vid muUploadImg_Vid;
int index,responce=0;
String IndexFile="ListIndex";
Uploads up=null;
Upload_Img_VID_File uuu;
public UploadListViewAdapter(  Context  context, int listItem,
			List<RowItem> rowItems) {
		// TODO Auto-generated constructor stub
		    super(context, listItem, rowItems);
	        this.context = context;
	   
	        this.ri=rowItems;
	        for (int i = 0; i < this.getCount(); i++) {
	            itemChecked.add(i, View.VISIBLE); 
	            isRemovecheck.add(i, View.GONE);
	        }
}

	private class ViewHolder {
	        ImageView imageView,pause,loc;
	        TextView txtTitle,upload_status;
	ProgressBar UploadProg;
	 HttpURLConnection conn;
	    DataOutputStream DOS;
	}
	 public View getView(final int position, View convertView, ViewGroup parent) {
	      
	        rowItem = getItem(position);
	        new LoadIndex().execute(IndexFile);
	        LayoutInflater mInflater = (LayoutInflater) context
	                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.upload_list_item, null);
	            holder = new ViewHolder();
	           
	          
	            holder.txtTitle = (TextView) convertView.findViewById(R.id.upload_title);
	            holder.upload_status=(TextView)convertView.findViewById(R.id.upload_status);
	            holder.imageView = (ImageView) convertView.findViewById(R.id.upload_icon);
	           holder.loc=(ImageView)convertView.findViewById(R.id.upload_loc);
	           
	            holder.pause=(ImageView)convertView.findViewById(R.id.upload_icon_remove);
	            holder.UploadProg=(ProgressBar)convertView.findViewById(R.id.progressBar1);
	            convertView.setTag(holder);
	        } else
	            holder = (ViewHolder) convertView.getTag();
	 
	     
	        holder.txtTitle.setText(rowItem.getTitle().substring(rowItem.getTitle().lastIndexOf("/")+1));
	        holder.upload_status.setText(rowItem.getUploadStatus());
	        holder.imageView.setImageResource(rowItem.getImageId());
	        holder.pause.setVisibility(itemChecked.get(position));
	        holder.UploadProg.setVisibility(rowItem.getProgressVisiblity());
	        holder.UploadProg.setProgress(0);
	        holder.UploadProg.setMax(0);
	       holder.conn=rowItem.GetConnection();
	        holder.UploadProg.setIndeterminate(rowItem.getIntermediateMode());
	        
	        
	        
	    //    holder.remove.setVisibility(isRemovecheck.get(position));
	      //  holder.refresh.setVisibility(isRemovecheck.get(position));
	//         holder.remove.setOnClickListener(new OnClickListener() {
				
	//			@Override
//				public void onClick(View v) {
					// TODO Auto-generated method stub
//					Log.d("cilck", "selected");
//					ri.remove(position);
//				itemChecked.set(position, View.VISIBLE);
//					isRemovecheck.set(position, View.GONE);
//			     notifyDataSetChanged();
//				index--;
//				new SaveIndex().execute(index);
//				}
	//		});
	        
	      /* 
	        holder.refresh.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				{ holder.pause.setVisibility(ImageView.VISIBLE);
				holder.refresh.setVisibility(ImageView.GONE);
					v.setVisibility(View.GONE);
				itemChecked.set(position, View.VISIBLE);
				isRemovecheck.set(position, View.GONE);
				notifyDataSetChanged();
				Log.d("cilck", "selected");
				
			}
					
					
				}
			});
		*/
	        holder.pause.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				//	ri.remove(position);
			//		itemChecked.set(position, View.VISIBLE);
				//		isRemovecheck.set(position, View.GONE);
				  //   notifyDataSetChanged();
			//		index--;
			//		new SaveIndex().execute(index);
				if (holder.conn != null)
				{
					
					holder.conn.disconnect();
					Toast.makeText(context, "not null connection", Toast.LENGTH_LONG).show();

				}
				else
				{
					Toast.makeText(context, "null connection", Toast.LENGTH_LONG).show();
				}
					
		
					}
			});
	       
	        holder.loc.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					context.startActivity(new Intent(context, MyLocationDemoActivity.class));
				}
			});
	        
	    
	        return convertView;
	    }

	@Override
		public RowItem getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
		}

		public static RowItem get(int position) {
			// TODO Auto-generated method stub
		rowItem=(RowItem)get(position);
		return rowItem;

		}

	private class LoadIndex extends AsyncTask<String, Void, Integer>
	{
      @Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
    	  preferences=context.getSharedPreferences(params[0], Activity.MODE_PRIVATE);
    	  index=preferences.getInt("index", 0);
			return null;
		}
		
   }
	
	private class SaveIndex extends AsyncTask<Integer, Void, Boolean>
	{

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result)
			{Toast.makeText(context, "saved", Toast.LENGTH_LONG).show();
			}
			else
			{Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			 preferences=context.getSharedPreferences(IndexFile, Activity.MODE_PRIVATE);
			pref_Editor=preferences.edit();
			pref_Editor.putInt("index", params[0]);
			if(pref_Editor.commit())
		
			return true;
			else
				return false;
		}
		
		
	}
		
		
		
		
}
