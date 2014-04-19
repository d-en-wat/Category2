package com.categoty2.cmetracker;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

public class CreateTask extends Activity {
	
	/*private Spinner taskType;
	private EditText creditsView;
	private EditText startDateView;
	private EditText endDateView;
	private EditText frequencyView;
	private EditText durationView;
	private String userId  = null;*/
	Button submit;
	Button cancel;
	SharedPreferences SP;
	SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_task);
		SP = getSharedPreferences(RegisterActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		//userId = SP.getString("userid", "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_task, menu);
		return true;
	}

}
