package com.categoty2.cmetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;



public class Start extends Activity {	
	
	SharedPreferences SP ;
	Intent nxtIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
		SP = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE);
		Boolean isLoogedInRes = SP.getBoolean("isLoggedIn", false);
		Log.i("StartActivity : onCreate : isLoggedIn : ", isLoogedInRes.toString());
		if(SP.getBoolean("isLoggedIn", false)){
			nxtIntent = new Intent(Start.this, TestDashActivity.class);
		}else{
			nxtIntent = new Intent(Start.this, LoginActivity.class);
		}
		
		final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(nxtIntent);
                finish();
            }
        }, 1000);

		
	}		
	
}
