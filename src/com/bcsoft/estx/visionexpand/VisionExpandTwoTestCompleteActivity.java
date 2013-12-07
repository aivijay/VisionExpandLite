package com.bcsoft.estx.visionexpand;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VisionExpandTwoTestCompleteActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		/*
		 * Set the title for the page
		 */
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        
		Intent intent = getIntent();
		String successCount = intent.getStringExtra(Constants.VT_SUCCESS_COUNT);
		String failureCount = intent.getStringExtra(Constants.VT_FAILURE_COUNT);
		String rows = intent.getStringExtra(Constants.VT_ROWS);
		String digits = intent.getStringExtra(Constants.VT_DIGITS);

		RelativeLayout relativeLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams relLayout = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		/*
		 * // Bottom RIGHT RelativeLayout.LayoutParams rlb = new
		 * RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * rlb.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 * rlb.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); Button cb = new
		 * Button(this); cb.setText("BOTTOM RIGHT");
		 * 
		 * 
		 * relativeLayout.addView(cb, rlb); setContentView(relativeLayout);
		 */

		// Bottom CENTER
		RelativeLayout.LayoutParams rlb2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlb2.addRule(RelativeLayout.CENTER_VERTICAL);
		rlb2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlb2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlb2.bottomMargin = 10;

		/*
		 * Button cb2 = new Button(this); cb2.setText("BOTTOM CENTER");
		 * 
		 * 
		 * relativeLayout.addView(cb2, rlb2); setContentView(relativeLayout);
		 */

		// MIDDLE of the page
		RelativeLayout.LayoutParams rlb3 = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rlb3.addRule(RelativeLayout.CENTER_VERTICAL);
		rlb3.addRule(RelativeLayout.CENTER_HORIZONTAL);

		/*
		 * // Bottom TOP LEFT of the page RelativeLayout.LayoutParams rlb4 = new
		 * RelativeLayout.LayoutParams( LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * rlb4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		 * rlb4.addRule(RelativeLayout.ALIGN_PARENT_TOP); Button cb4 = new
		 * Button(this); cb4.setText("TOP LEFT");
		 * 
		 * relativeLayout.addView(cb4, rlb4);
		 * ////setContentView(relativeLayout);
		 */
		LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		LinearLayout mainContentLayout = new LinearLayout(this);
		mainContentLayout.setOrientation(LinearLayout.VERTICAL);
		mainContentLayout.setGravity(Gravity.CENTER);

		try {
			TextView infoText = (TextView)getLayoutInflater().inflate(R.layout.info_on_results_page_text_template, null);
			
			if (successCount == null) {
				successCount = "0";
			}
			if (failureCount == null) {
				failureCount = "0";
			}

			int sc = Integer.parseInt(successCount);
			int fc = Integer.parseInt(failureCount);
			infoText.setText("Your Performance is");
			infoText.setGravity(Gravity.CENTER);
			
			int scoreMessageTextSize = Integer.valueOf(getResources().getString(R.string.scoreMessageTextSize));
			infoText.setTextSize(scoreMessageTextSize);

			// Set the text view as the activity layout
			
			TextView percentText = (TextView)getLayoutInflater().inflate(R.layout.percent_performance_text_template, null);
			percentText.setText(calculatePercentage(sc, fc) + " %");
			percentText.setGravity(Gravity.CENTER);

			mainContentLayout.addView(infoText);
			mainContentLayout.addView(percentText);

			LinearLayout buttonContentLayout = new LinearLayout(this);
			buttonContentLayout.setOrientation(LinearLayout.HORIZONTAL);
			buttonContentLayout.setGravity(Gravity.CENTER);
			
			Button tryAgainButton = (Button)getLayoutInflater().inflate(R.layout.try_again_button_template, null);
			tryAgainButton.setOnClickListener(tryAgainButtonListener);
			
			mainContentLayout.addView(tryAgainButton, params);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		relativeLayout.addView(mainContentLayout, rlb3);

		setContentView(relativeLayout);

        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.expander2_window_title);
        
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			//getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	private OnClickListener tryAgainButtonListener = new OnClickListener() {
		public void onClick(View v) {
			tryAgain();
		}
	};

	private OnClickListener finishedButtonListener = new OnClickListener() {
		public void onClick(View v) {
			tryAgain();
		}
	};
	
	private void tryAgain() {
		finish();
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

	private int calculatePercentage(int sc, int fc) {
		int percent = 0;
		if (sc > 0) {
			float per = (float) sc / (sc + fc);
			percent = (int) (per * 100);
		}
		return percent;
	}
}