package com.categoty2.cmetracker;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private static final String PREFRENCES_NAME = "myPrefs";
	private UserRegistrationTask mAuthTask = null;
	Button submit;
	Button reset;
	DataHandler dbHandler;
	// Values for email and password at the time of the login attempt.
	private String userRegistered = new String("");

	SharedPreferences SP;

	private String mFirstName;
	private String mMiddleName;
	private String mLastName;
	private String mProfession;
	private String mStateLicNum;
	private String mState;
	private String mLicIssueDt;
	private String mLicExpDt;
	private String mTelephone;
	private String mEmail;
	private String mUserName;
	private String mPassword;

	private int atCount;
	private int dotCount;
	// UI references.
	private EditText mFirstNameView;
	private EditText mMiddleNameView;
	private EditText mLastNameView;
	private EditText mStateLicNumView;
	private EditText mLicIssueDtView;
	private EditText mLicExpDtView;
	private EditText mTelephoneView;
	private EditText mEmailView;
	private EditText mUserNameView;
	private EditText mPasswordView;

	private Spinner mProfessionSpinner;
	private Spinner mStateSpinner;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private void nextActivity() {
		// Code to change to go to next page
		// Intent intent = new Intent(this, class);
		// startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setupActionBar();
		
		SP = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);

		mEmailView = (EditText) findViewById(R.id.reg_email);
		mEmailView.setText(SP.getString("email", ""));
		mUserNameView = (EditText) findViewById(R.id.username);
		mPasswordView = (EditText) findViewById(R.id.reg_password);
		mPasswordView.setText(SP.getString("pwd", ""));
		mFirstNameView = (EditText) findViewById(R.id.firstname);
		mMiddleNameView = (EditText) findViewById(R.id.middlename);
		mLastNameView = (EditText) findViewById(R.id.lastname);
		mStateLicNumView = (EditText) findViewById(R.id.medical_license_num);
		addStatesSpinner();
		mLicIssueDtView = (EditText) findViewById(R.id.license_issue_dt);
		mLicExpDtView = (EditText) findViewById(R.id.license_expiry_dt);
		mTelephoneView = (EditText) findViewById(R.id.telephone);

		submit = (Button) findViewById(R.id.submit_button);
		reset = (Button) findViewById(R.id.reset_button);
		addListenerOnSpinnerItemSelection();
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

	}

	public void reset(View view) {
		Log.i("Register Acticity : Reset", "Entered");
		mUserNameView.setText("");
		mPasswordView.setText("");
		mTelephoneView.setText("");
		mFirstNameView.setText("");
		mMiddleNameView.setText("");
		mLastNameView.setText("");
		mStateLicNumView.setText("");
		mLicIssueDtView.setText("");
		mLicExpDtView.setText("");
		mEmailView.setText("");

		mUserNameView.setError(null);
		mPasswordView.setError(null);
		mTelephoneView.setError(null);
		mFirstNameView.setError(null);
		mMiddleNameView.setError(null);
		mLastNameView.setError(null);
		mStateLicNumView.setError(null);
		mLicIssueDtView.setError(null);
		mLicExpDtView.setError(null);
		mEmailView.setError(null);
		mProfessionSpinner.setSelection(0);
		mStateSpinner.setSelection(0);
		Log.i("Register Acticity : Reset", "Exit");

	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		// getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@SuppressWarnings("rawtypes")
	public void addStatesSpinner() {
		mStateSpinner = (Spinner) findViewById(R.id.state);
		List<String> stateList = new ArrayList<String>();
		stateList.add("-SELECT STATE-");
		stateList.add("ARIZONA");
		stateList.add("CALIFORNIA");
		stateList.add("GEORGIA");
		stateList.add("FLORIDA");
		stateList.add("TEXAS");
		ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, stateList);
		stateAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mStateSpinner.setAdapter(stateAdapter);

	}

	public void addListenerOnSpinnerItemSelection() {
		mProfessionSpinner = (Spinner) findViewById(R.id.profession);
		mProfessionSpinner
				.setOnItemSelectedListener(new OnItemSelectedListener());
		mStateSpinner = (Spinner) findViewById(R.id.state);
		mStateSpinner.setOnItemSelectedListener(new OnItemSelectedListener());
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void submit(View view) {
		try {
			Log.i("RegisterActivity : submit : ", "Entered ");
			// Reset errors
			mEmailView.setError(null);
			mUserNameView.setError(null);
			mPasswordView.setError(null);
			mFirstNameView.setError(null);
			mMiddleNameView.setError(null);
			mLastNameView.setError(null);
			mStateLicNumView.setError(null);
			mLicIssueDtView.setError(null);
			mLicExpDtView.setError(null);
			mTelephoneView.setError(null);
			

			// Reset Form
			mEmail = null;
			mPassword = null;
			mUserName = null;
			mFirstName = null;
			mMiddleName = null;
			mLastName = null;
			mStateLicNum = null;
			mLicIssueDt = null;
			mLicExpDt = null;
			mTelephone = null;
			

			atCount = 0;
			dotCount = 0;
			if (mAuthTask != null) {
				return;
			}
			mFirstName = mFirstNameView.getText().toString();
			mFirstName = mFirstName.toLowerCase();
			mMiddleName = mMiddleNameView.getText().toString();
			mMiddleName = mMiddleName.toLowerCase();
			mLastName = mLastNameView.getText().toString();
			mLastName = mLastName.toLowerCase();
			mStateLicNum = mStateLicNumView.getText().toString();
			mLicIssueDt = mLicIssueDtView.getText().toString();
			mLicExpDt = mLicExpDtView.getText().toString();
			mTelephone = mTelephoneView.getText().toString();
			mEmail = mEmailView.getText().toString();
			mEmail = mEmail.toLowerCase();
			mPassword = mPasswordView.getText().toString();
			mUserName = mUserNameView.getText().toString();
			mUserName = mUserName.toLowerCase();

			mProfession = String.valueOf(mProfessionSpinner.getSelectedItem());
			mState = String.valueOf(mStateSpinner.getSelectedItem());

			dbHandler = new DataHandler(getBaseContext());
			dbHandler.open();
			String userRegistered = null;
			if (!TextUtils.isEmpty(mUserName)) {
				userRegistered = dbHandler.validateUserName(mUserName);
				Log.i("Is user already registered : ", userRegistered);
			} 

			boolean cancel = false;
			View focusView = null;
			for (int i = 0; i < mEmail.length(); i++) {
				if (mEmail.charAt(i) == '@') {
					atCount++;
				}
				if (mEmail.charAt(i) == '.') {
					dotCount++;
				}
			}
			if (TextUtils.isEmpty(mEmail)) { // check if the email is
				// null or not
				mEmailView.setError(getString(R.string.reg_error_empty_email));
				focusView = mEmailView;
				cancel = true;
			} else if (mEmail.length() < 5) { // check if the email is null or
				// not
				mEmailView
						.setError(getString(R.string.reg_error_min_length_email));
				focusView = mEmailView;
				cancel = true;
			} else if (mEmail.length() > 100) {
				mEmailView
						.setError(getString(R.string.reg_error_field_max_email_length));
				focusView = mEmailView;
				cancel = true;
			} else if (atCount == 0) {
				mEmailView.setError(getString(R.string.reg_error_noAtTheRate));
				focusView = mEmailView;
				cancel = true;
			} else if (dotCount == 0) {
				mEmailView.setError(getString(R.string.reg_error_noDot));
				focusView = mEmailView;
				cancel = true;
			} else if (atCount > 1) {
				mEmailView.setError(getString(R.string.reg_error_field_more_at_rate));
				focusView = mEmailView;
				cancel = true;
			} else if (mEmail.length() == 2) {
				if (atCount == 1 && dotCount == 1) {
					mEmailView.setError(getString(R.string.reg_error_justdotatrate));
					focusView = mEmailView;
					cancel = true;
				}
			} else if (TextUtils.isEmpty(mUserName)) { // check if the username
				// is null or not
				System.out.println("Username is empty");
				mUserNameView.setError(getString(R.string.reg_error_empty_username));
				focusView = mUserNameView;
				cancel = true;
			} else if (userRegistered.equalsIgnoreCase("User Present")) { // Check
				// if
				// user 
				// has
				// already
				// registered
				// or not .
				mUserNameView
						.setError(getString(R.string.reg_error_user_registered));
				focusView = mUserNameView;
				cancel = true;
			} else if (mPassword == null & mPassword.length() == 0) { // check if the password
				// is empty or not
				mPasswordView
						.setError(getString(R.string.reg_error_empty_password));
				focusView = mPasswordView;
				cancel = true;
			} else if (mPassword.length() < 4) {
				mPasswordView.setError(getString(R.string.reg_error_pwd_short));
				focusView = mPasswordView;
				cancel = true;
			} else if (mPassword.length() > 50) {
				mPasswordView
						.setError(getString(R.string.reg_error_max_length_pwd));
				focusView = mPasswordView;
				cancel = true;
			} else if (mFirstName == null & mFirstName.length() == 0) { // check if the first
														// name is
														// empty or not
				mFirstNameView
						.setError(getString(R.string.reg_error_empty_firstname));
				focusView = mFirstNameView;
				cancel = true;
			} else if (mLastName == null & mLastName.length() == 0) { // check if the Last Name
														// is empty or not
				mLastNameView
						.setError(getString(R.string.reg_error_empty_lastname));
				focusView = mLastNameView;
				cancel = true;
			} else if (mProfession.equalsIgnoreCase("-SELECT PROFESSION-")) { // Check
																				// if
																				// profession
																				// is
																				// selected
																				// or
																				// not
				Toast.makeText(RegisterActivity.this,
						"Please Select your Profession !!", Toast.LENGTH_LONG)
						.show();
				return;
			} else if (mStateLicNum == null & mStateLicNum.length() == 0) { // check if the state
															// license number is
															// empty or not
				mStateLicNumView
						.setError(getString(R.string.reg_error_empty_stlicense));
				focusView = mStateLicNumView;
				cancel = true;
			} else if (mState.equalsIgnoreCase("-SELECT STATE-")) { // check if
																	// the state
																	// is
																	// selected
																	// or not
				Toast.makeText(RegisterActivity.this,
						"Please Select the State !!", Toast.LENGTH_LONG).show();
				return;
			} else if (mLicIssueDt == null & mLicIssueDt.length() == 0) { // check if the License
															// Issue Date is
															// empty or not
				mLicIssueDtView
						.setError(getString(R.string.reg_error_empty_issuedt));
				focusView = mLicIssueDtView;
				cancel = true;
			} else if (mLicExpDt == null & mLicExpDt.length() == 0 ) { // check if the License
														// Issue Date is empty
														// or not
				mLicExpDtView
						.setError(getString(R.string.reg_error_empty_expirydt));
				focusView = mLicExpDtView;
				cancel = true;
			}

			if (cancel) {
				focusView.requestFocus();
			} else {
				// Show a progress spinner, and kick off a background task to
				// perform the user registration attempt.
				mLoginStatusMessageView
						.setText(R.string.login_progress_signing_up);
				// showProgress(true);

				long rowsInserted = dbHandler.registerUser(mFirstName,
						mMiddleName, mLastName, mProfession, mStateLicNum,
						mState, mLicIssueDt, mLicExpDt, mTelephone, mEmail,
						mUserName, mPassword);

				if (rowsInserted > 0) {
					/*
					 * SharedPreferences settings =
					 * getSharedPreferences(PREFRENCES_NAME,
					 * Context.MODE_PRIVATE); SharedPreferences.Editor editor =
					 * settings.edit(); editor.putString("userName", mUserName);
					 * editor.putBoolean("isRegistered", true);
					 * editor.putBoolean("isLoggedin", true); editor.commit();
					 */Toast.makeText(getBaseContext(),
							"Registered Successfully", Toast.LENGTH_LONG)
							.show();
					// mAuthTask = new UserRegistrationTask();
					// mAuthTask.execute((Void) null);
				} else {
					Toast.makeText(getBaseContext(), "Error in Registration",
							Toast.LENGTH_LONG).show();
				}
			}
			Log.i("RegisterActivity : submit : ", "End of try block ");
		} catch (Exception exception) {
			exception.printStackTrace();
			Log.e("RegisterActivity", exception.getMessage().toString());
		} finally {
			if (dbHandler != null) {
				dbHandler.close();
			}
		}
		Log.i("RegisterActivity : submit : ", "Exit ");
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserRegistrationTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}
			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			if (success) {
				nextActivity();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

}
