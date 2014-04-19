package com.categoty2.cmetracker;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.GetChars;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DashBoard extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	SharedPreferences SP;
	SharedPreferences.Editor editor;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	static Context cont;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		SP = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		
		cont = getApplicationContext();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dash_board, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		/*case android.R.id.home:			
			NavUtils.navigateUpFromSameTask(this);
			return true;*/
		case R.id.action_logout:
			logout();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void logout(){
		editor = SP.edit();
		editor.putBoolean("isLoggedIn", false);
		editor.commit();
		
		Intent nxtIntent = new Intent(DashBoard.this, LoginActivity.class);
		finish();
		startActivity(nxtIntent);
	}
	
	/*private static DatePickerDialog showDatePicker(final View v){
		final Calendar cal = new GregorianCalendar();
		DatePickerDialog myDatePickerDialog = new DatePickerDialog(cont, new DatePickerDialog.OnDateSetListener()  {
		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		    	StringBuilder sb = new StringBuilder("");
		    	sb.append(monthOfYear+1).append("/").append(dayOfMonth).append("/").append(year);
		    	((EditText)v).setText(sb.toString());
		    }
		} , cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		return myDatePickerDialog;
	}*/
	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
 			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		Spinner mActivityView;
		EditText mActivityStartDate;
		EditText mActivityEndDate;
		DatePickerDialog dpDialog;
		
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}
		
		void showDatePicker(final View v){
			//new DatePickerFragment((EditText) v).showDatePickerDialog(v);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_dash_board_dummy, container, false);
			//final TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
			mActivityView = (Spinner)rootView.findViewById(R.id.spinner1);
			mActivityStartDate = (EditText) rootView.findViewById(R.id.activity_start_dt);
			mActivityEndDate = (EditText) rootView.findViewById(R.id.activity_end_dt);
			
			mActivityStartDate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					/*dpDialog = showDatePicker(v);
					dpDialog.show();*/
					showDatePicker(v);
				}
			});
			
			mActivityEndDate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					/*dpDialog = showDatePicker(v);
					dpDialog.show();*/
				}
			});			
			/*mActivityView.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
					// TODO Auto-generated method stub
					//super.onItemSelected(parent, view, pos, id);
					dummyTextView.setText(mActivityView.getSelectedItem().toString());
				}
			});*/
			
			//dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}			
	}

}
