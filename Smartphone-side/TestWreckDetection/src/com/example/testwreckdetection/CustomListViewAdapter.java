package com.example.testwreckdetection;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.Telephony.Sms.Conversations;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ResourceAsColor")
public class CustomListViewAdapter extends ArrayAdapter<RowItem>
 { static RowItem rowItem;
	private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
    Context context;
List<RowItem>ri;
    public CustomListViewAdapter(Context context, int resourceId,
            List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
        this.ri=items;
        for (int i = 0; i < this.getCount(); i++) {
            itemChecked.add(i, false); // initializes all items value with false
        }
    }
 
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
  
      
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        rowItem = getItem(position);
 
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
           
          
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
  
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
 
     
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageResource(rowItem.getImageId());
   //     holder.ch.setChecked(false);
       // holder.ch.setOnCheckedChangeListener(this);
     //   holder.ch.setChecked(ri.get(position).isSelected());
   //     holder.   cBox.setChecked(itemChecked.get(position));
       
    
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


}