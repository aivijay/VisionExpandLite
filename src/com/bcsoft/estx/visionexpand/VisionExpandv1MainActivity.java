package com.bcsoft.estx.visionexpand;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VisionExpandv1MainActivity extends Activity {
	private AdView adView;
	private LayoutParams params;
	private LinearLayout layout;

	private ArrayList<String> testData;

	private EditText[] et;
	private TextView[] visionTexts;
	private TextView statusBar;

	private int cid = 1; // component id to position relatively.

	private int currentTest = 1;
	private int successCount = 0;
	private int failureCount = 0;
	private int flashPeriod = Constants.DEFAULT_FLASH_PERIOD; // in milliseconds
	private int charactersCount = Constants.DEFAULT_NO_OF_CHARACTERS;
	private int rowsCount = Constants.DEFAULT_NO_OF_ROWS;
	private String fontName = Constants.DEFAULT_FONT_NAME;
	private int fontSize = Constants.DEFAULT_FONT_SIZE;

	private Timer timer;

	String tag = "Lifecycle";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		RelativeLayout relativeLayout = new RelativeLayout(this);
		RelativeLayout.LayoutParams relLayout = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		// Bottom CENTER
		RelativeLayout.LayoutParams rlb2 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlb2.addRule(RelativeLayout.CENTER_VERTICAL);
		rlb2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlb2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rlb2.bottomMargin = 10;
		//Button checkButton = new Button(this);
		Button checkButton = (Button)getLayoutInflater().inflate(R.layout.check_button_template, null);
		checkButton.setOnClickListener(buttonListener);
	
		relativeLayout.addView(checkButton, rlb2);

		// MIDDLE of the page
		RelativeLayout.LayoutParams rlb3 = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlb3.addRule(RelativeLayout.CENTER_VERTICAL);
		rlb3.addRule(RelativeLayout.CENTER_HORIZONTAL);
		if (getRowsCount() > 2) {
			rlb3.addRule(RelativeLayout.BELOW, 1); // below status bar
		}
		// TOP CENTER of the page
		RelativeLayout.LayoutParams rlb4 = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rlb4.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlb4.addRule(RelativeLayout.CENTER_VERTICAL);
		rlb4.addRule(RelativeLayout.ALIGN_PARENT_TOP);

		// LinearLayout container to hold the test data and text field to check
		LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout mainContentLayout = new LinearLayout(this);
		mainContentLayout.setOrientation(LinearLayout.VERTICAL);

		this.layout = mainContentLayout;
		this.params = params;

		relativeLayout.addView(mainContentLayout, rlb3);

		setContentView(relativeLayout);

		initialize();

		// add the status bar to the top
		statusBar = new TextView(this);
		statusBar.setGravity(Gravity.CENTER);
		statusBar.setText(createStatusText());
		statusBar.setLayoutParams(this.params);
		statusBar.setTextSize(Constants.STATUS_FONT_SIZE);
		statusBar.setBackgroundColor(Color.DKGRAY);
		statusBar.setTextColor(Color.WHITE);
		statusBar.setId(1);
		relativeLayout.addView(statusBar, rlb4);

		showTimedTestData();

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
	}

	private void showTimedTestData() {
		int waitPeriod = Constants.TEST_WAIT; // standard test wait which is 1
												// sec

		/*
		 * if (getCurrentTest() == 1) { waitPeriod = getFlashPeriod() +
		 * Constants.FIRST_TEST_WAIT; } else { waitPeriod = getFlashPeriod(); }
		 */
		generateTestData();

		// Just wait for 3 seconds before presenting

		if (getCurrentTest() == 1) {

			if (getFlashPeriod() <= 300) {
				waitPeriod = Constants.FIRST_TEST_WAIT_200MS;
			}

			new CountDownTimer(waitPeriod, 10) {

				public void onTick(long millisUntilFinished) {
					Log.d("debug: --->> waiting tick at time = 10 ms",
							"WAIT TIME");
				}

				public void onFinish() {
					runTimedDataPresentation(getFlashPeriod());
				}

			}.start();

		} else {
			runTimedDataPresentation(getFlashPeriod());
		}
	}

	private void runTimedDataPresentation(int period) {
		new CountDownTimer(period, 50) {
			public void onTick(long millisUntilFinished) {
				showTestData();
			}

			public void onFinish() {
				resetVisionData();
			}
		}.start();
	}

	public void onClickCheck(View view) {

	}
	
	private OnClickListener buttonListener = new OnClickListener() {
		public void onClick(View v) {
			boolean success = true;
			for (int i = 0; i < getRowsCount(); i++) {
				String s1 = getTestData().get(i);
				String s2 = et[i].getText().toString();
				if (!getTestData().get(i).equals(et[i].getText().toString())) {
					success = false;
				}
			}

			Toast toast = new Toast(getBaseContext());
			toast.setDuration(Toast.LENGTH_LONG);

			if (success == true) {
				incrementSuccessCount();
			} else {
				incrementFailureCount();
			}
			incrementCurrentTest();
			if (getCurrentTest() <= Constants.MAX_TESTS) {
				refreshData();
			} else { // Tests have been completed
				visionTestComplete(v);
			}
		}

	};

	private void resetData() {
		testData.clear();
	}

	private String createStatusText() {
		String st = "Test: " + getCurrentTest() + " / "
				+ Constants.MAX_TESTS + Constants.STATUS_SEP + "Success: "
				+ getSuccessCount() + Constants.STATUS_SEP + "Failure: "
				+ getFailureCount() + Constants.STATUS_SEP + "Rows: "
				+ getRowsCount() + Constants.STATUS_SEP + "Chars: "
				+ getCharactersCount() + Constants.STATUS_SEP + "Period: "
				+ printableFlashPeriod();

		return st;
	}
	
	private String printableFlashPeriod () {
		String printString = "";
		int flashPeriod = getFlashPeriod();
		
		if (flashPeriod >= 1000) {
			int secs = flashPeriod / 1000;
			if (secs == 1) {
				printString = secs + " second";
			} else {
				printString = secs + " seconds";
			}
		} else {
			printString = flashPeriod + " milli seconds";
		}
		
		return printString;
	}

	private void incrementCurrentTest() {
		this.currentTest++;
	}

	private int getCurrentTest() {
		return this.currentTest;
	}

	private void incrementSuccessCount() {
		this.successCount++;
	}

	private void incrementFailureCount() {
		this.failureCount++;
	}

	public int getSuccessCount() {
		return this.successCount;
	}

	private final int getFailureCount() {
		return this.failureCount;
	}

	private final int getFlashPeriod() {
		return this.flashPeriod;
	}

	private void setFlashPeriod(int flashPeriod) {
		this.flashPeriod = flashPeriod;
	}

	private final int getCharactersCount() {
		return this.charactersCount;
	}

	private void setCharactersCount(int charactersCount) {
		this.charactersCount = charactersCount;
	}

	private final int getRowsCount() {
		return this.rowsCount;
	}

	private void setRowsCount(int rowsCount) {
		this.rowsCount = rowsCount;
	}

	private final String getFontName() {
		return this.fontName;
	}

	private void setFontName(String fontName) {
		this.fontName = fontName;
	}

	private final int getFontSize() {
		return this.fontSize;
	}

	private void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	private final int getType() {
		// for now only numeric
		// TODO - to implement logic to read from config and identify 1 -
		// numeric, 2 - string
		return 1;
	}

	private void initialize() {
		// initialize the current settings
		loadPreferences();

		initializeVisionTexts();
		// Show editable fields for feedback check
		showCheckFields();
	}

	private void initializeVisionTexts() {
		visionTexts = new TextView[getRowsCount()];
		// Initialize Vision test text views
		for (int i = 0; i < getRowsCount(); i++) {
			visionTexts[i] = new TextView(this);
			visionTexts[i].setTextSize(getFontSize());

			visionTexts[i].setGravity(Gravity.CENTER);
			// / visionTexts[i].setText(createDataForPresentation(getTestData()
			// / .get(i)));
			visionTexts[i].setLayoutParams(this.params);
			visionTexts[i].setId(getCid());
			this.layout.addView(visionTexts[i]);

		}
	}

	private int getCid() {
		return this.cid++;
	}

	private void showTestData() {
		// initialize test data
		// generateTestData();

		for (int i = 0; i < getRowsCount(); i++) {
			// if (visionTexts.length > 0 && visionTexts[i] != null) {
			visionTexts[i].setText(createDataForPresentation(getTestData().get(
					i)));
			et[i].setVisibility(View.VISIBLE);
		}
	}

	private void showCheckFields() {
		// initialize the edit containers
		et = new EditText[getRowsCount()];

		for (int i = 0; i < getRowsCount(); i++) {
			et[i] = new EditText(this);
			et[i].setGravity(Gravity.CENTER);
			et[i].setMaxLines(Constants.MAX_LINES);
			et[i].setLayoutParams(params);
			et[i].setId(getCid());
			et[i].setVisibility(View.INVISIBLE);
			this.layout.addView(et[i]);
		}
	}

	private final String generateSpaces(int spaces) {
		return new String(new char[spaces]).replace("\0", " ");
	}

	private String createDataForPresentation(String data) {
		String pData = "";
		String spaces = new String(new char[Constants.FONT_SPACES_NORMAL])
				.replace("\0", " "); // standard spacing between chars

		switch (getFontSize()) {
		case Constants.FONT_SMALL:
			spaces = generateSpaces(Constants.FONT_SPACES_SMALL);
			break;
		case Constants.FONT_LARGE:
			spaces = generateSpaces(Constants.FONT_SPACES_LARGE);
			break;
		case Constants.FONT_HUGE:
			spaces = generateSpaces(Constants.FONT_SPACES_HUGE);
			break;
		}

		for (int i = 0; i < data.length(); i++) {
			pData += data.charAt(i) + spaces;
		}
		pData = pData.replaceAll(spaces + "$", "");
		return pData;
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.vision_expand_main, menu);
	// return true;
	// }

	private ArrayList<String> getTestData() {
		return this.testData;
	}

	private final void generateTestData() {
		String testText = "";
		ArrayList<String> testArray = new ArrayList<String>();

		for (int i = 0; i < getRowsCount(); i++) {
			for (int j = 0; j < getCharactersCount(); j++) {
				testText += randomData(getType());
			}
			testArray.add(testText);
			testText = "";
		}

		this.testData = testArray;
	}

	/**
	 * randomData - generates random one char numeric or string data according
	 * to the type
	 * 
	 * @param type
	 *            int - identifies type of data to be generated 1 - Numeric only
	 *            2 - String (which can have numeric data as well)
	 * @return
	 */
	private String randomData(int type) {
		Random rand = new Random();
		String randomData = "342"; // some default
		if (type == Constants.NUMERIC_TYPE) {
			randomData = Integer.toString(rand.nextInt(10));
		} else if (type == Constants.STRING_TYPE) {

		}
		return randomData;
	}

	private LinearLayout getLayout() {
		return this.layout;
	}

	private void refresh() {
		// getLayout().postInvalidate();
		getLayout().invalidate();
	}

	private void resetVisionData() {
		for (int i = 0; i < getRowsCount(); i++) {
			visionTexts[i].setText("");
		}
	}

	private void reset() {
		for (int i = 0; i < getRowsCount(); i++) {
			if (et[i] != null) {
				et[i].setText("");
				visionTexts[i].setText("");
			}
		}
		testData.clear();
		statusBar.setText(createStatusText());
	}

	private final int dpsToPixels(int dps) {
		final float scale = getBaseContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}

	/** Called when the max number of tests are complete */
	public void visionTestComplete(View view) {

		// Wait for 2 seconds before proceeding to show the final results
		new CountDownTimer(1000, 100) {

			public void onTick(long millisUntilFinished) {
			}

			public void onFinish() {
			}
		}.start();

		Intent intent = new Intent(this, VisionTestCompleteActivity.class);
		intent.putExtra(Constants.VT_SUCCESS_COUNT, getSuccessCount() + "");
		intent.putExtra(Constants.VT_FAILURE_COUNT, getFailureCount() + "");
		intent.putExtra(Constants.VT_ROWS, getRowsCount() + "");
		intent.putExtra(Constants.VT_DIGITS, getCharactersCount() + "");
		intent.putExtra(Constants.VT_FLASH_RATE, getFlashPeriod() + "");
		startActivity(intent);
	}

	/**
	 * Settings menu specific
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent i = new Intent(this, UserSettingActivity.class);
			startActivityForResult(i, Constants.RESULT_SETTINGS);
			break;

		}

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case Constants.RESULT_SETTINGS:
			showUserSettings();
			break;

		}

		SharedPreferences appPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		/**
		 * Handle settings with saving the new settings and updating refreshing
		 * them
		 */
		setFontName(appPrefs.getString("prefFontName", ""));
		setFontSize((Integer.parseInt(appPrefs.getString("prefFontSize",
				Integer.toString(Constants.DEFAULT_FONT_SIZE)))));
		setFlashPeriod((Integer.parseInt(appPrefs.getString("prefFlashPeriod",
				Integer.toString(Constants.DEFAULT_FLASH_PERIOD)))));
		setCharactersCount((Integer.parseInt(appPrefs.getString(
				"prefCharactersCount",
				Integer.toString(Constants.DEFAULT_NO_OF_CHARACTERS)))));
		setRowsCount((Integer.parseInt(appPrefs.getString("prefRowsCount",
				Integer.toString(Constants.DEFAULT_NO_OF_ROWS)))));

		// Refresh the app with new settings
		reInitializeApp();
	}

	private void refreshData() {
		reset();
		showTimedTestData();
	}

	private void reInitializeApp() {
		this.recreate();
	}

	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		builder.append("\n Username: "
				+ sharedPrefs.getString("prefUsername", "NULL"));

		builder.append("\n Send report:"
				+ sharedPrefs.getBoolean("prefSendReport", false));

		builder.append("\n Sync Frequency: "
				+ sharedPrefs.getString("prefSyncFrequency", "NULL"));

		// TextView settingsTextView = (TextView)
		// findViewById(R.id.textUserSettings);

		// settingsTextView.setText(builder.toString());
	}

	private void loadPreferences() {
		SharedPreferences appPrefs = getSharedPreferences(
				"visionExpandPreferences", MODE_PRIVATE);
		setFontName(appPrefs.getString("prefFontName", ""));
		setFontSize((Integer.parseInt(appPrefs.getString("prefFontSize",
				Integer.toString(Constants.DEFAULT_FONT_SIZE)))));
		setFlashPeriod((Integer.parseInt(appPrefs.getString("prefFlashPeriod",
				Integer.toString(Constants.DEFAULT_FLASH_PERIOD)))));
		setCharactersCount((Integer.parseInt(appPrefs.getString(
				"prefCharactersCount",
				Integer.toString(Constants.DEFAULT_NO_OF_CHARACTERS)))));
		setRowsCount((Integer.parseInt(appPrefs.getString("prefRowsCount",
				Integer.toString(Constants.DEFAULT_NO_OF_ROWS)))));

	}

	public void onStart() {
		super.onStart();
		refreshData();
	}

	public void onRestart() {
		super.onRestart();
		reInitializeApp();
	}

	public void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	public void onStop() {
		super.onStop();
	}

	public void onDestroy() {
		super.onDestroy();
	}

}
