package com.categoty2.cmetracker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.R.layout;
import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class TestDashActivity extends Activity {

	SharedPreferences SP;
	SharedPreferences.Editor editor;

	Spinner mActivityView;
	EditText mActivityStartDate;
	EditText mActivityEndDate;
	DatePickerDialog dpDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_dash);

		SP = getSharedPreferences(LoginActivity.MyPREFERENCES,
				Context.MODE_PRIVATE);
		mActivityView = (Spinner) findViewById(R.id.spinner1);
		mActivityStartDate = (EditText) findViewById(R.id.activity_start_dt);
		mActivityEndDate = (EditText) findViewById(R.id.activity_end_dt);
		
		mActivityView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				//dummyTextView.setText(mActivityView.getSelectedItem().toString());
				View lay = findViewById(R.id.inner_dash);
				if(pos == 0)					
					lay.setVisibility(View.GONE);
				else
					lay.setVisibility(View.VISIBLE);					
			}
		});

		mActivityStartDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dpDialog = showDatePicker(v);
				dpDialog.show();
			}
		});

		mActivityEndDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dpDialog = showDatePicker(v);
				dpDialog.show();
			}
		});
	}

	private DatePickerDialog showDatePicker(final View v) {
		final Calendar cal = new GregorianCalendar();
		DatePickerDialog myDatePickerDialog = new DatePickerDialog(TestDashActivity.this,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
						StringBuilder sb = new StringBuilder("");
						sb.append(monthOfYear + 1).append("/").append(dayOfMonth).append("/").append(year);
						((EditText) v).setText(sb.toString());
					}
				}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)); 
		return myDatePickerDialog;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_dash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*
		 * case android.R.id.home: NavUtils.navigateUpFromSameTask(this); return
		 * true;
		 */
		case R.id.action_logout:
			logout();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void logout() {
		editor = SP.edit();
		editor.putBoolean("isLoggedIn", false);
		editor.commit();

		Intent nxtIntent = new Intent(TestDashActivity.this,
				LoginActivity.class);
		finish();
		startActivity(nxtIntent);
	}

	public void onSubmit(View v) {
		// when clicked on submit button
	}

	public void onCancel(View v) {
		// when clicked on cancel button
	}
}
