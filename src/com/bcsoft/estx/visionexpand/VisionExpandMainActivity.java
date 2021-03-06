package com.bcsoft.estx.visionexpand;

import java.util.ArrayList;
import java.util.Random;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
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
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VisionExpandMainActivity extends Activity {
	private LayoutParams params;
	private TextView statusBar;
	private String receivedData[];
	private String expanderTestData[];
	public static Button dataRow[][];
	// private TextView progressStatusText;
	// private TextView scoreStatusText;
	private MenuItem progressStatusText;
	private MenuItem scoreStatusText;
	private Menu menu;

	private ArrayList<String> testData = new ArrayList<String>();

	private int cid = 1; // component id to position relatively.

	private int currentTest = 1;
	private int successCount = 0;
	private int failureCount = 0;
	private int flashPeriod = Constants.DEFAULT_FLASH_PERIOD; // in milliseconds
	private int charactersCount = Constants.DEFAULT_NO_OF_CHARACTERS;
	private int rowsCount = Constants.DEFAULT_NO_OF_ROWS;
	private String fontName = Constants.DEFAULT_FONT_NAME;
	private int fontSize = Constants.DEFAULT_FONT_SIZE;

	private int currentDataPosition = 0;
	private String blankText = "_";

	private boolean entryAllowed = false;

	String tag = "Lifecycle";

	private boolean onCreateCompleted = false;

	int dataElementWidth;
	int dataElementHeight;
	int dataTextSize;

	private static boolean timedDataLock = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int npButtonWidth = 60;
		int npButtonHeight = 40;

		dataElementWidth = Integer.valueOf(getResources().getString(
				R.string.landscapeExplorerDataElementWidth));
		dataElementHeight = Integer.valueOf(getResources().getString(
				R.string.landscapeExplorerDataElementHeight));
		dataTextSize = Integer.valueOf(getResources().getString(
				R.string.landscapeExplorerDataTextSize));

		try {
			super.onCreate(savedInstanceState);

			/*
			 * Set the title for the page
			 */
			// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

			loadPreferences();

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				setContentView(R.layout.portrait_expander1_activity_main);

				dataElementWidth = Integer.valueOf(getResources().getString(
						R.string.portraitDataElementWidth));
				dataElementHeight = Integer.valueOf(getResources().getString(
						R.string.portraitDataElementHeight));
				dataTextSize = Integer.valueOf(getResources().getString(
						R.string.portraitDataTextSize));

				npButtonWidth = 60;
				npButtonHeight = 40;

			} else {
				if (getRowsCount() == 2) {
					setContentView(R.layout.portrait_expander1_activity_main);
				} else {
					// In Landscape we are not showing the title to save
					// horizontal
					// space
					setContentView(R.layout.portrait_expander1_activity_main);
				}
			}

			LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
			LinearLayout mainContentLayout = new LinearLayout(this);
			mainContentLayout.setOrientation(LinearLayout.VERTICAL);

			this.params = params;

			initializeExpanderUI();

			//
			// setup status bar
			//

			// Remove notification bar
			this.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.expander1_window_title);

		// progressStatusText = (TextView)findViewById(R.id.progressStatusText);
		// scoreStatusText = (TextView)findViewById(R.id.scoreStatusText);

		// refreshData();

		onCreateCompleted = true;

		// Look up the AdView as a resource and load a request.
		 AdView adView = (AdView)
		 this.findViewById(R.id.adViewSmartforExpander1);
		 adView.loadAd(new AdRequest());
	}

	private void initializeDataRows() {

		LinearLayout.LayoutParams testTextParams = new LinearLayout.LayoutParams(
				dipToPixels(dataElementWidth), dipToPixels(dataElementHeight));
		int left = 0;
		int top = 5;
		int right = 0;
		int bottom = 5;
		testTextParams.setMargins(left, top, right, bottom);

		for (int row = 0; row < getRowsCount(); row++) {
			//
			// By default we keep the first row
			//
			LinearLayout rowLayout = (LinearLayout) findViewById(R.id.expanderRow1);

			//
			// if the is the 2nd row then get the 2nd rows layout
			//
			if (row == 1) {
				rowLayout = (LinearLayout) findViewById(R.id.expanderRow2);
			}

			//
			// Proceed with positions the columns for each row
			//
			for (int i = 0; i < getCharactersCount(); i++) {
				Button testTextButton = (Button) getLayoutInflater().inflate(
						R.layout.expander_text_button_template, null);

				testTextButton.setText(getBlankText());
				testTextButton.setTextSize(dataTextSize);

				dataRow[row][i] = testTextButton;

				testTextButton.setLayoutParams(testTextParams);

				rowLayout.addView(dataRow[row][i]);
			}
		}

	}

	private void showTimedTestData() {
		int waitPeriod = Constants.TEST_WAIT; // standard test wait which is 1
												// sec

		if (getTimedDataLock() == false) {
			resetCurrentDataPosition();
			/*
			 * if (getCurrentTest() == 1) { waitPeriod = getFlashPeriod() +
			 * Constants.FIRST_TEST_WAIT; } else { waitPeriod =
			 * getFlashPeriod(); }
			 */
			generateTestData();

			// Just wait for 3 seconds before presenting

			try {
				if (getCurrentTest() == 1) {
					if (getFlashPeriod() <= 300) {
						waitPeriod = Constants.FIRST_TEST_WAIT_200MS;
					}
					new CountDownTimer(waitPeriod, 50) {

						public void onTick(long millisUntilFinished) {
						}

						public void onFinish() {
							runTimedDataPresentation(getFlashPeriod());
						}

					}.start();
				} else {
					//
					// Just wait for a second before displaying anything
					//
					new CountDownTimer(1000, 50) {
						public void onTick(long millisUntilFinished) {
							resetVisionData();
						}

						public void onFinish() {
							runTimedDataPresentation(getFlashPeriod());
							// setTimedDataLock(false);
						}
					}.start();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
			}
		}

	}

	private void runTimedDataPresentation(int period) {
		new CountDownTimer(period, 50) {
			public void onTick(long millisUntilFinished) {
				showTestData();
			}

			public void onFinish() {
				unlockDataEntry();
				resetVisionData();
			}
		}.start();
	}

	public void onClickCheck(View view) {

	}

	private OnClickListener buttonListener = new OnClickListener() {
		public void onClick(View v) {
		}
	};

	private void resetData() {
		testData = new ArrayList<String>();
	}

	private String createStatusText() {
		String st = "Progress: <font color='blue'>" + getCurrentTest() + " of "
				+ Constants.MAX_TESTS + "</font>" + Constants.STATUS_SEP
				+ Constants.STATUS_SEP + "Score: <b><font color='green'>"
				+ getSuccessCount() + "</font></b>";

		return st;
	}

	/**
	 * Check if the entered result is the same as the one which was displayed
	 */
	private void performCheck() {
		boolean success = true;
		for (int i = 0; i < getRowsCount(); i++) {
			String s1 = getTestData().get(i);
			String s2 = "";

			for (int j = 0; j < getCharactersCount(); j++) {
				s2 += dataRow[i][j].getText();
			}

			if (!getTestData().get(i).equals(s2)) {
				success = false;
			}
		}

		Toast toast = new Toast(getBaseContext());
		toast.setDuration(Toast.LENGTH_LONG);

		if (success == true) {
			incrementSuccessCount();

			//
			// Flash green background for successful test completion
			//
			new CountDownTimer(500, 50) {
				public void onTick(long millisUntilFinished) {
					flashSuccess();
				}

				public void onFinish() {
					resetStateFlash();
				}
			}.start();

		} else {
			incrementFailureCount();

			//
			// Flash red background for failed test
			//
			new CountDownTimer(500, 50) {
				public void onTick(long millisUntilFinished) {
					flashFailure();
				}

				public void onFinish() {
					resetStateFlash();

				}
			}.start();

		}
		incrementCurrentTest();

		if (getCurrentTest() <= Constants.MAX_TESTS) {
			refreshData();
		} else { // Tests have been completed
			visionTestComplete();
		}
	}

	private void flashSuccess() {
		for (int i = 0; i < getRowsCount(); i++) {
			for (int j = 0; j < getCharactersCount(); j++) {
				dataRow[i][j].setBackgroundColor(Color
						.parseColor(Constants.EXPANDER1_SUCCESS_COLOR));
			}
		}
	}

	private void flashFailure() {
		for (int i = 0; i < getRowsCount(); i++) {
			for (int j = 0; j < getCharactersCount(); j++) {
				dataRow[i][j].setBackgroundColor(Color
						.parseColor(Constants.EXPANDER1_FAILURE_COLOR));
			}
		}
	}

	private void resetStateFlash() {
		for (int i = 0; i < getRowsCount(); i++) {
			for (int j = 0; j < getCharactersCount(); j++) {
				dataRow[i][j].setBackgroundColor(Color
						.parseColor(Constants.STANDARD_EXPANDER1_BG_COLOR));
			}
		}
	}

	private String printableFlashPeriod() {
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
	}

	private int getCid() {
		return this.cid++;
	}

	private void showTestData() {
		// initialize test data
		// generateTestData();

		for (int i = 0; i < getRowsCount(); i++) {
			String cdtString = getTestData().get(i);
			if (dataRow == null) {
			}

			for (int j = 0; j < getCharactersCount(); j++) {
				if (dataRow[i][j] == null) {
				}
				dataRow[i][j].setText(Character.toString(cdtString.charAt(j)));
			}
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

	private ArrayList<String> getTestData() {
		return this.testData;
	}

	private final void generateTestData() {
		try {
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
		} catch (Exception ex) {
			ex.printStackTrace();
		}
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
		// return this.layout;
		return null;
	}

	private void refresh() {
		// getLayout().postInvalidate();
		getLayout().invalidate();
	}

	private void resetVisionData() {
		for (int i = 0; i < getRowsCount(); i++) {
			// / DELETE visionTexts[i].setText("");
			for (int j = 0; j < getCharactersCount(); j++) {
				dataRow[i][j].setText(getBlankText());
			}
		}
	}

	private void reset() {
		progressStatusText = menu.findItem(R.id.itemProgressText);
		scoreStatusText = menu.findItem(R.id.itemScoreText);
		try {
			testData = new ArrayList<String>();
			progressStatusText.setTitle(Html.fromHtml("Progress: "
					+ getCurrentTest()));
			scoreStatusText.setTitle(Html
					.fromHtml("<font color='#0AF77D'><b>Score: "
							+ getSuccessCount() + "</b></font>"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private final int dpsToPixels(int dps) {
		final float scale = getBaseContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}

	/** Called when the max number of tests are complete */
	public void visionTestComplete() {

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
		this.menu = menu;
		refreshData();

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
	}

	private void loadPreferences() {
		try {
			SharedPreferences appPrefs = getSharedPreferences(
					"visionExpandPreferences", MODE_PRIVATE);
			setFontName(appPrefs.getString("prefFontName", ""));
			setFontSize((Integer.parseInt(appPrefs.getString("prefFontSize",
					Integer.toString(Constants.DEFAULT_FONT_SIZE)))));
			setFlashPeriod((Integer.parseInt(appPrefs.getString(
					"prefFlashPeriod",
					Integer.toString(Constants.DEFAULT_FLASH_PERIOD)))));
			setCharactersCount((Integer.parseInt(appPrefs.getString(
					"prefCharactersCount",
					Integer.toString(Constants.DEFAULT_NO_OF_CHARACTERS)))));
			setRowsCount((Integer.parseInt(appPrefs.getString("prefRowsCount",
					Integer.toString(Constants.DEFAULT_NO_OF_ROWS)))));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onStart() {
		try {
			super.onStart();
			loadPreferences();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void onRestart() {
		super.onRestart();
		reInitializeApp();
	}

	@Override
	public void onResume() {
		try {
			super.onResume();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

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

	public void onRestore(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Brought in newer version of the layout and helpers
	 */

	private String getBlankText() {
		return blankText;
	}

	private int dipToPixels(int dip) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dip, getResources().getDisplayMetrics());
	}

	private void initializeExpanderUI() {
		loadPreferences();

		dataRow = new Button[getRowsCount()][getCharactersCount()];

		initializeDataRows();

		expanderTestData = new String[getTotalDataElements()];
		receivedData = new String[getTotalDataElements()];
		for (int i = 0; i < getTotalDataElements(); i++) {
			expanderTestData[i] = "";
			receivedData[i] = "";
		}
	}

	private int getCurrentDataPosition() {
		return currentDataPosition;
	}

	private int getRowOnPosition() {
		return getCurrentDataPosition() / getCharactersCount();
	}

	private int getColumnOnPosition() {
		return getCurrentDataPosition() % getCharactersCount();
	}

	private int getTotalDataElements() {
		return getRowsCount() * getCharactersCount();
	}

	private void incrementCurrentDataPosition() {
		if (getCurrentDataPosition() < getTotalDataElements() - 1) {
			currentDataPosition++;
		} else if (getCurrentDataPosition() == getTotalDataElements() - 1) {
			resetCurrentDataPosition();
			lockDataEntry();
			performCheck();
		}
	}

	private void resetCurrentDataPosition() {
		currentDataPosition = 0;
	}

	private void decrementCurrentDataPosition() {
		if (getCurrentDataPosition() > 0) {
			currentDataPosition--;
		}
	}

	private Button[][] getDataRow() {
		return dataRow;
	}

	public void onClickNum1(View view) {
		if (isEntryUnlocked()) {
			String numberOne = "1";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickNum2(View view) {
		if (isEntryUnlocked()) {
			String numberOne = "2";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickNum3(View view) {
		if (isEntryUnlocked()) {
			String numberOne = "3";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickNum4(View view) {
		if (isEntryUnlocked()) {

			String numberOne = "4";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickNum5(View view) {
		if (isEntryUnlocked()) {

			String numberOne = "5";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickNum6(View view) {
		if (isEntryUnlocked()) {

			String numberOne = "6";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickNum7(View view) {
		if (isEntryUnlocked()) {

			String numberOne = "7";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickNum8(View view) {
		if (isEntryUnlocked()) {

			String numberOne = "8";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickNum9(View view) {
		if (isEntryUnlocked()) {

			String numberOne = "9";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickNum0(View view) {
		if (isEntryUnlocked()) {

			String numberOne = "0";
			receivedData[getCurrentDataPosition()] = numberOne;
			getDataRow()[getRowOnPosition()][getColumnOnPosition()]
					.setText(numberOne);
			incrementCurrentDataPosition();
		}
	}

	public void onClickBackSpace(View view) {
		if (isEntryUnlocked()) {
			//
			// dont allow to go to the first position in the array which is 0
			// we allow till the second element in the array with the index of 1
			//
			if (getCurrentDataPosition() >= 1) {
				receivedData[getCurrentDataPosition() - 1] = getBlankText();

				if (getRowOnPosition() == 1 && getColumnOnPosition() == 0) {
					//
					// just move the index to the previous row and set to the
					// last
					// element in the row
					//
					getDataRow()[getRowOnPosition() - 1][getCharactersCount() - 1]
							.setText(getBlankText());
				} else {
					// backspace with ref to the prev char on the nth row
					getDataRow()[getRowOnPosition()][getColumnOnPosition() - 1]
							.setText(getBlankText());
				}

				decrementCurrentDataPosition();
			}
		}
	}

	private void disableNumPadButtons() {

	}

	private void enableNumPadButtons() {

	}

	private boolean getEntryAllowed() {
		return entryAllowed;
	}

	private void setEntryAllowed(boolean state) {
		this.entryAllowed = state;
	}

	private void lockDataEntry() {
		setEntryAllowed(false);
	}

	private void unlockDataEntry() {
		setEntryAllowed(true);
	}

	private boolean isEntryLocked() {
		return getEntryAllowed();
	}

	private boolean isEntryUnlocked() {
		return getEntryAllowed();
	}

	private boolean getTimedDataLock() {
		return this.timedDataLock;
	}

	private void setTimedDataLock(boolean lock) {
		this.timedDataLock = lock;
	}

}
