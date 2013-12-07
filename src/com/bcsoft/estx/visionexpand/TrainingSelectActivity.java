package com.bcsoft.estx.visionexpand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class TrainingSelectActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training_select);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.training_select, menu);
		return true;
	}
	
	public void onClickTraining(View view) {
		Intent i = new Intent(this, Training1MainActivity.class);
		startActivity(i);
	}

	public void onClickTraining2(View view) {
		Intent i = new Intent(this, Training2MainActivity.class);
		startActivity(i);
	}
	
	/*
	
	public void onClickTraining(View view) {
		Intent i = new Intent(this, VisionExpandMainActivity.class);
		startActivity(i);
	}

	public void onClickTraining2(View view) {
		Intent i = new Intent(this, VisionExpandTwoMainActivity.class);
		startActivity(i);
	}
	*/
}
