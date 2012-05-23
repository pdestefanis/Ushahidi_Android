package com.ushahidi.android.app;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.telephony.TelephonyManager;

public class Preferences {
	public static boolean httpRunning = false;

	public static boolean AutoFetch = false;

	public static boolean vibrate = false;

	public static boolean ringtone = false;

	public static boolean flashLed = false;

	public static int countries = 0;

	public static int AutoUpdateDelay = 60;

	public static int gpsTimeout = 60;

	public static final int NOTIFICATION_ID = 1;

	public static final String PREFS_NAME = "UshahidiService";

	public static String incidentsResponse = "";

	public static String categoriesResponse = "";

	public static String savePath = "";

	public static String domain = "";

	public static String firstname = "";

	public static String lastname = "";

	public static String email = "";

	public static int totalReports = 30;

	// public static String fileName = "";

	public static ArrayList<String> fileName = new ArrayList<String>();

	public static int isCheckinEnabled = 0;

	public static int appRunsFirstTime = 0;

	public static int activeDeployment = 0;

	public static int photoWidth = 200;

	public static int reportImageCount = 6;

	public static int mapZoom = 11;

	public static int locationTolerance = 5;

	public static String deploymentLatitude = "13.69947";

	public static String deploymentLongitude = "-89.2216";

	private static SharedPreferences settings;

	private static SharedPreferences.Editor editor;

	public static String totalReportsFetched = "";

	public static String USIM = "";

	public static String DeviceID = "";

	public static boolean isRecord = false;
	public static String audiofile = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/audio1.3gp";

	public static String username = "";
	public static String password = "";
	public static String prePassword = "si2sv2012";

	public static String phonenumber;

	public static void loadSettings(Context context) {
		final SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);

		final String path = context.getDir("",
				Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE).toString();

		savePath = settings.getString("savePath", path);

		domain = settings.getString("Domain", Preferences.domain);
		firstname = settings.getString("Firstname", "");
		lastname = settings.getString("Lastname", "");
		email = settings.getString("Email", "");
		countries = settings.getInt("Countries", 0);
		AutoUpdateDelay = settings.getInt("AutoUpdateDelay", 60);
		AutoFetch = settings.getBoolean("AutoFetch", false);
		totalReports = settings.getInt("TotalReports_temp", 30);
		isCheckinEnabled = settings.getInt("CheckinEnabled", isCheckinEnabled);
		activeDeployment = settings.getInt("ActiveDeployment", 0);
		deploymentLatitude = settings.getString("default_latitude_preference", "13.69947");
		deploymentLongitude = settings.getString("default_longitude_preference", "-89.2216");
		photoWidth = settings.getInt("PhotoWidth", 200);
		mapZoom = settings.getInt("mapZoom", 11);
		reportImageCount = settings.getInt("report_image_count_temp", 6);
		appRunsFirstTime = settings.getInt("AppRunsFirstTime", appRunsFirstTime);
		username = settings.getString("username", "");
		password = settings.getString("password", "");

		locationTolerance = settings.getInt("locationTolerance", 5);
		gpsTimeout = settings.getInt("gps_timeout_preference_temp", 60);

		// Aman
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		USIM = phoneManager.getSimSerialNumber();
		DeviceID = phoneManager.getDeviceId();

		// @inoran
		phonenumber = settings.getString("Phonenumber", "");

		// make sure folder exists
		final File dir = new File(Preferences.savePath);
		dir.mkdirs();

	}

	public static void saveSettings(Context context) {
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		editor = settings.edit();
		editor.putString("Domain", domain);
		editor.putInt("CheckinEnabled", isCheckinEnabled);
		editor.putInt("ActiveDeployment", activeDeployment);
		editor.putString("default_latitude_preference", "13.69947");// deploymentLatitude);
		editor.putString("default_longitude_preference", "-89.2216");// deploymentLongitude);
		editor.putInt("AppRunsFirstTime", appRunsFirstTime);
		editor.commit();
	}
}
