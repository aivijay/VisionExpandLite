package com.bcsoft.estx.visionexpand;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class Training2MainActivity extends Activity {
	private WebView contentText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Set the title for the page
		 */
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		setContentView(R.layout.activity_training2_main);

		/*
		 * Set the custom title for this page
		 */
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.expander2_window_title);

		/*
		 * contentText = (TextView) findViewById(R.id.training2ContentText);
		 * contentText.setText(Html.fromHtml(getResources().getString(R.string.
		 * training2HelpContent)));
		 */

		contentText = (WebView) findViewById(R.id.training2ContentText);
		contentText.setBackgroundColor(0); // transparent
		WebSettings webSettings = contentText.getSettings();
		webSettings.setDefaultFontSize(Integer.valueOf(getResources()
				.getString(R.string.textContentDefaultFontSize)));

		String mimeType = "text/html";
		String encoding = "utf-8";
		contentText.loadData(
				getResources().getString(R.string.training2HelpContent),
				mimeType, encoding);
		contentText.reload();
		contentText.setVerticalScrollBarEnabled(true);

		// Look up the AdView as a resource and load a request.
		// AdView adView = (AdView) this.findViewById(R.id.adViewSmart);
		// adView.loadAd(new AdRequest());

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	public void onClickTraining2(View view) {
		Intent i = new Intent(this, VisionExpandTwoMainActivity.class);
		startActivity(i);
	}
}
