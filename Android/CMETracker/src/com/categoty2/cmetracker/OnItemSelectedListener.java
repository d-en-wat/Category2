package com.categoty2.cmetracker;
import android.view.View;
import android.widget.AdapterView;
public class OnItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener{

	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		//Toast.makeText(parent.getContext(), "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		// implemented based on class requirements
	}

}
