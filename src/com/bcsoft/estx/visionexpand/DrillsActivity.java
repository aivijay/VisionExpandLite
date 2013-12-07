package com.bcsoft.estx.visionexpand;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.viewpagerindicator.CirclePageIndicator;

public class DrillsActivity extends BaseSampleActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drills);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public void onClickDrillA(View view) {
		Intent i = new Intent(this, DrillAActivity.class);
		startActivity(i);
	}

	public void onClickDrillB(View view) {
		Intent i = new Intent(this, DrillBActivity.class);
		startActivity(i);
	}

	public void onClickDrillC(View view) {
		Intent i = new Intent(this, DrillCActivity.class);
		startActivity(i);
	}

	public void onClickDrillD(View view) {
		Intent i = new Intent(this, DrillDActivity.class);
		startActivity(i);
	}

	public void onClickDrillE(View view) {
		Intent i = new Intent(this, DrillEActivity.class);
		startActivity(i);
	}

	public void onClickDrillsHelp(View view) {
		// Intent i = new Intent(this, DrillsHelpActivity.class);
		// startActivity(i);
	}
}
