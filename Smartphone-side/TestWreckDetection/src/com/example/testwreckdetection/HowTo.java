package com.example.testwreckdetection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class HowTo extends Activity {

Button test;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.how_to);
		test=(Button)findViewById(R.id.button1);
		test.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			//	new AttachLoctionToImg_Vid(getApplicationContext(), "zainab", 33.6548494, 44.986876, "8765443211").execute(new ApiConnector());
		startActivity(new Intent(getApplicationContext(), Video_Recorder.class));
			
			}
		});

	}
	
	

}
