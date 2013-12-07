package com.bcsoft.estx.visionexpand;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;

public class Training1MainActivity extends Activity {
	private WebView contentText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Set the title for the page
		 */
		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.activity_training1_main);

		/*
		 * Set the custom title for this page
		 */
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		//		R.layout.expander1_window_title);		

		/*
		contentText = (TextView) findViewById(R.id.training1ContentText);
		contentText.setText(Html.fromHtml(getResources().getString(R.string.training1HelpContent)));
		
		*/
		contentText = (WebView) findViewById(R.id.training1ContentText);
	
		WebSettings webSettings = contentText.getSettings();
		webSettings.setDefaultFontSize(Integer.valueOf(getResources().getString(R.string.textContentDefaultFontSize)));
		
		String mimeType = "text/html";
		String encoding = "utf-8";
		contentText.loadData(getResources().getString(R.string.training1HelpContent), mimeType, encoding);
		contentText.reload();
		contentText.setBackgroundColor(Color.TRANSPARENT); // transparent
		contentText.setVerticalScrollBarEnabled(true);
		
		// Look up the AdView as a resource and load a request.
//		AdView adView = (AdView) this.findViewById(R.id.adViewSmart);
//		adView.loadAd(new AdRequest());

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}


	public void onClickTraining(View view) {
		Intent i = new Intent(this, VisionExpandMainActivity.class);
		startActivity(i);
	}
}
