package com.categoty2.cmetracker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class TestDashActivity extends Activity {

	SharedPreferences SP;
	SharedPreferences.Editor editor;
	
	Spinner mActivityView;
	EditText mActivityStartDateView;
	EditText mActivityEndDateView;
	EditText mCreditsView;
	View mInnerLayoutView;
	
	DatePickerDialog dpDialog;
	String userId;
	DataBaseHandler dbHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_dash);

		SP = getSharedPreferences(LoginActivity.MyPREFERENCES,Context.MODE_PRIVATE);
		userId = SP.getString("userid", "");
		Log.i("TestDashActivity : onCreate : userId from preferences : ", userId);
		mActivityView = (Spinner) findViewById(R.id.spinner1);
		mActivityStartDateView = (EditText) findViewById(R.id.activity_start_dt);
		mActivityEndDateView = (EditText) findViewById(R.id.activity_end_dt);
		mCreditsView = (EditText)findViewById(R.id.enter_credits);
		mInnerLayoutView = findViewById(R.id.inner_dash);
		
		mActivityView.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				if(pos == 0)					
					mInnerLayoutView.setVisibility(View.GONE);
				else
					mInnerLayoutView.setVisibility(View.VISIBLE);			
			}
		});

		mActivityStartDateView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dpDialog = showDatePicker(v);
				dpDialog.show();
			}
		});

		mActivityEndDateView.setOnClickListener(new View.OnClickListener() {
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
		mCreditsView.setError(null);
		mActivityStartDateView.setError(null);
		mActivityEndDateView.setError(null);
		Log.i("TestDashActivity : onSubmit : ", "Entered");
		String activityType = String.valueOf(mActivityView.getSelectedItem());
		Log.i("TestDashActivity : onSubmit : activity type selected : ", activityType);
		Log.i("TestDashActivity : onSubmit : userId : ", userId);
		Log.i("TestDashActivity : onSubmit : StartDate Selected : ", mActivityStartDateView.getText().toString());
		Log.i("TestDashActivity : onSubmit : EndDate Selected : ", mActivityEndDateView.getText().toString());
		Log.i("TestDashActivity : onSubmit : Credits Selected : ", mCreditsView.getText().toString());
		try{
			String mCredits = mCreditsView.getText().toString();
			String mStartDt = mActivityStartDateView.getText().toString();
			String mEndDt = mActivityEndDateView.getText().toString();
			boolean cancel = false;
			View focusView = null;
			dbHandler = new DataBaseHandler(getBaseContext());
			if (activityType.equalsIgnoreCase("SELECT ACTIVITY")) { 
				Toast.makeText(TestDashActivity.this,"Please Select Activity !!", Toast.LENGTH_LONG).show();
				return;
			}else if (TextUtils.isEmpty(mCredits)){
				mCreditsView.setError(getString(R.string.error_empty_credits));
				focusView = mCreditsView;
				cancel = true;
			}else if (TextUtils.isEmpty(mStartDt)){
				mActivityStartDateView.setError(getString(R.string.error_empty_task_startdt));
				focusView = mActivityStartDateView;
				cancel = true;
			}else if (TextUtils.isEmpty(mEndDt)){
				mActivityEndDateView.setError(getString(R.string.error_empty_task_enddt));
				focusView = mActivityEndDateView;
				cancel = true;
			}
			if (cancel) {
				focusView.requestFocus();
			}else{
				long noOfRowsInserted = dbHandler.createTask(activityType, mCredits, userId, "", "", mStartDt, mEndDt);
				if (noOfRowsInserted > 0) {
					Log.i("TestDashActivity : onSubmit : RowId Created : ", String.valueOf(noOfRowsInserted));
					Toast.makeText(getBaseContext(),"Activity Created Successfully", Toast.LENGTH_LONG).show();
					mActivityView.setSelection(0);
					reset();
				} else {
					Toast.makeText(getBaseContext(), "Error in Activity Creation",Toast.LENGTH_LONG).show();
				}
			}
		}catch (SQLException exception) {
			exception.printStackTrace();
			Log.e("TestDashActivity : onSubmit : SQL Exception : ", "SQL Exception",exception);
		}catch (Exception exception) {
			exception.printStackTrace();
			Log.e("TestDashActivity : onSubmit : Other Exception : ", "SOME OTHER EXCEPTION",exception);
		} finally {
			if (dbHandler != null) {
				dbHandler.close();
			}			
		}
		Log.i("TestDashActivity : onSubmit : ", "Exit");
	}

	public void onCancel(View v) {
		// when clicked on cancel button
		mCreditsView.setError(null);
		mActivityStartDateView.setError(null);
		mActivityEndDateView.setError(null);
		reset();
	}
	
	private void reset(){
		mCreditsView.setText("");
		mActivityStartDateView.setText("");
		mActivityEndDateView.setText("");
		mActivityView.setSelection(0);
	}
}



