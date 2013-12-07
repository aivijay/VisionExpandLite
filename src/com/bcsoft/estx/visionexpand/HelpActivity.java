package com.bcsoft.estx.visionexpand;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.viewpagerindicator.CirclePageIndicator;

public class HelpActivity extends BaseSampleActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_help);

		super.onCreate(savedInstanceState);
		
		/*
		 * Set the title for the page
		 */
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.activity_help);

		/*
		 * Set the custom title for this page
		 */
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		//		R.layout.standard_window_title);		
		
//		TextView tv = (TextView)findViewById(R.id.windowTitleText);
//		tv.setText(getResources().getString(R.string.help));
		
		mAdapter = new TestFragmentAdapter(getSupportFragmentManager());
		mAdapter.setContent(this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator = indicator;
		indicator.setViewPager(mPager);

		final float density = getResources().getDisplayMetrics().density;
		indicator.setBackgroundColor(0xFFCCCCCC);
		indicator.setRadius(10 * density);
		indicator.setPageColor(0x880000FF);
		indicator.setFillColor(0xFF888888);
		indicator.setStrokeColor(0xFF000000);
		indicator.setStrokeWidth(2 * density);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		// Look up the AdView as a resource and load a request.
		AdView adView = (AdView) this.findViewById(R.id.adViewSmart);
		adView.loadAd(new AdRequest());

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		/*
		 * 
		 * mAdapter = new TestFragmentAdapter(getSupportFragmentManager());
		 * 
		 * mPager = (ViewPager)findViewById(R.id.pager);
		 * mPager.setAdapter(mAdapter);
		 * 
		 * mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
		 * mIndicator.setViewPager(mPager);
		 */
	}

}
