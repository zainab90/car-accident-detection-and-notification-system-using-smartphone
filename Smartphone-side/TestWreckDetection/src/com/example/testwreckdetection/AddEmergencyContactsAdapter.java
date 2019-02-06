package com.example.testwreckdetection;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddEmergencyContactsAdapter extends ArrayAdapter<RowItem> {
	
	
	static RowItem rowItem; Context context;
	List<RowItem>ri;
	 public AddEmergencyContactsAdapter (Context context, int resourceId,
	            List<RowItem> items) {
	        super(context, resourceId, items);
	        this.context = context;
	        this.ri=items;}
	 
	    private class ViewHolder {
	        ImageView imageView;
	        TextView txtTitle,txtNum;
	  
	      
	    }
	  public View getView(final int position, View convertView, ViewGroup parent) {
	        ViewHolder holder = null;
	        rowItem = getItem(position);
	 
	        LayoutInflater mInflater = (LayoutInflater) context
	                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	        if (convertView == null) {
	            convertView = mInflater.inflate(R.layout.add_emergency_contact_item, null);
	            holder = new ViewHolder();
	           
	          
	            holder.txtTitle = (TextView) convertView.findViewById(R.id.emergency_contact_name);
	            holder.imageView = (ImageView) convertView.findViewById(R.id.emergency_contact_icon);
	            holder.txtNum=(TextView)convertView.findViewById(R.id.emergency_contact_number);
	             convertView.setTag(holder);
	        } else
	          
	        	holder = (ViewHolder) convertView.getTag();
	          holder.txtTitle.setText(rowItem.getTitle());
	          holder.imageView.setImageResource(rowItem.getImageId());
	          holder.txtNum.setText(rowItem.getNumber());
	             return convertView;
}

		@Override
		public RowItem getItem(int position) {
			// TODO Auto-generated method stub
			return super.getItem(position);
		}


}