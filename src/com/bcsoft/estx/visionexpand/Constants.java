package com.bcsoft.estx.visionexpand;

public class Constants {

	/*
	 * Type of data to be presented 1 - numeric data 2 - Alpha numeric data will
	 * be presented as a string
	 */
	public static final int NUMERIC_TYPE = 1;
	public static final int STRING_TYPE = 2;
	
	// VE_TYPE identifies whether its
	// vision expand Lite (1) or Full (2)
	public static final int VE_TYPE = 1;

	public static final String SEPERATOR = "         ";
	public static final int MAX_LINES = 1;
	public static final int DEFAULT_FONT_SIZE = 24;
	public static final int DEFAULT_NO_OF_CHARACTERS = 4;
	public static final int DEFAULT_NO_OF_ROWS = 1;
	public static final int MAX_TESTS = 25;
	public static final String STATUS_SEP = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	public static final int STATUS_FONT_SIZE = 14;
	public static final String APP_NAME = "Vision Expand";
	public static final String DEFAULT_FONT_NAME = "ARIAL";
	public static final int DEFAULT_FLASH_PERIOD = 1000; // 1 s
	public static final int FIRST_TEST_WAIT = 2000; // 2 seconds wait
	public static final int FIRST_TEST_WAIT_200MS = 3000; // 3 seconds wait
	public static final int TEST_WAIT = 1000; // 1 second wait before flash
	public static final int FONT_SPACES_UPTO_24 = 9; // 9 spaces
	public static final int FONT_SPACES_GT_24 = 6; // spaces
	public static final int FONT_SPACES_SMALL = 9; // font size 18
	public static final int FONT_SPACES_NORMAL = 8; // font size 24
	public static final int FONT_SPACES_LARGE = 7; // font size 30
	public static final int FONT_SPACES_HUGE = 6; // font size 34;
	public static final int FONT_SMALL = 18;
	public static final int FONT_NORMAL = 24;
	public static final int FONT_LARGE = 30;
	public static final int FONT_HUGE = 34;
	public static final int FONT_SIZE_24 = 24;


	public final static String VT_SUCCESS_COUNT = "com.bcsoft.estx.visionexpand.VT_SUCCESS_COUNT";
	public final static String VT_FAILURE_COUNT = "com.bcsoft.estx.visionexpand.VT_FAILURE_COUNT";
	public final static String VT_ROWS = "com.bcsoft.estx.visionexpand.VT_ROWS";
	public final static String VT_DIGITS = "com.bcsoft.estx.visionexpand.VT_DIGITS";
	public final static String VT_FLASH_RATE = "com.bcsoft.extx.visionexpand.VT_FLASH_RATE";
	
    public static final int RESULT_SETTINGS = 1;

    public static final String STANDARD_EXPANDER1_BG_COLOR = "#C8EFFA";
    public static final String EXPANDER1_SUCCESS_COLOR = "#B9FACA";
    public static final String EXPANDER1_FAILURE_COLOR = "#FCB8B8";
    
    public static final String STATUS_BAR_COLOR = "#DEDEDE";
    
    // Expander 2 specific
    public static final int DATA_CHARACTERS_PORTRAIT = 8;
    public static final int DATA_CHARACTERS_LANDSCAPE = 10;
    public static final int DATA_PAIR_COUNT = 2;
    public static final int DEFAULT_EXPANDER2_DIGITS = 3;
    public static final int EXPANDER_TWO_MAX_CORRECT_ANSWERS = 3;
    public static final int EXPANDER_TWO_MAX_INCORRECT_ANSWERS = 3;
    public static final int DEFAULT_PAIR_DIGITS_COUNT = 2;
    
    // ADMOB specific
    public final static String VISION_EXPAND_ADMOB_AD_UNIT_ID = "ca-app-pub-2608575944155753/1608124029";
}
