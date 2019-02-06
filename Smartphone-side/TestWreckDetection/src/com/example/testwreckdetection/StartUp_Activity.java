package com.example.testwreckdetection;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

@SuppressLint("NewApi")
public class StartUp_Activity extends Activity implements OnClickListener {
Button Next_Button,Cancel_Button;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.startup);
		Next_Button=(Button)findViewById(R.id.btn_next);
		Cancel_Button=(Button)findViewById(R.id.btn_cancel);
		Next_Button.setOnClickListener(this);
		Cancel_Button.setOnClickListener(this);
		startActivity(new Intent(getApplicationContext(), Second_startup_Activity.class));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_next:
			startActivity(new Intent(getApplicationContext(), Second_startup_Activity.class));
			finish();
			break;
		case R.id.btn_cancel:
			finish();
		default:
			break;
		}
		
		
	}

}
