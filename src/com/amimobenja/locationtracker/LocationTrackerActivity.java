package com.amimobenja.locationtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class LocationTrackerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		//-- hides the title bar---
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.main);
	}

}
