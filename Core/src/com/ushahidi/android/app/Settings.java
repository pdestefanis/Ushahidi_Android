/** 
 ** Copyright (c) 2010 Ushahidi Inc
 ** All rights reserved
 ** Contact: team@ushahidi.com
 ** Website: http://www.ushahidi.com
 ** 
 ** GNU Lesser General Public License Usage
 ** This file may be used under the terms of the GNU Lesser
 ** General Public License version 3 as published by the Free Software
 ** Foundation and appearing in the file LICENSE.LGPL included in the
 ** packaging of this file. Please review the following information to
 ** ensure the GNU Lesser General Public License version 3 requirements
 ** will be met: http://www.gnu.org/licenses/lgpl.html.	
 **	
 **
 ** If you have questions regarding the use of this file, please contact
 ** Ushahidi developers at team@ushahidi.com.
 ** 
 **/

package com.ushahidi.android.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.text.InputType;
import android.util.Log;

import com.ushahidi.android.app.ui.SeekBarPreference;
import com.ushahidi.android.app.util.ApiUtils;
import com.ushahidi.android.app.util.Util;

public class Settings extends PreferenceActivity implements
		OnSharedPreferenceChangeListener, OnPreferenceChangeListener {

	private EditTextPreference firstNamePref;

	private EditTextPreference lastNamePref;

	private EditTextPreference emailAddressPref;

	private EditTextPreference phoneNumberPref;

	private CheckBoxPreference autoFetchCheckBoxPref;

	private EditTextPreference gpsTimeoutPref;

	private EditTextPreference reportImagesCount;

	private EditTextPreference locationTolerancePref;

	private EditTextPreference defaultLatitudePref;

	private EditTextPreference defaultLongitudePref;

	private SeekBarPreference defaultZoomLevelPref;

	private CheckBoxPreference vibrateCheckBoxPref;

	private CheckBoxPreference ringtoneCheckBoxPref;

	private CheckBoxPreference flashLedCheckBoxPref;

	private DialogPreference clearCacheCheckBoxPref;

	private ListPreference autoUpdateTimePref;

	private ListPreference saveItemsPref;

	private ListPreference totalReportsPref;

	private SeekBarPreference photoSizePref;

	private SharedPreferences settings;

	private SharedPreferences.Editor editor;

	private boolean validUrl = false;

	private final Handler mHandler = new Handler();

	public static final String AUTO_FETCH_PREFERENCE = "auto_fetch_preference";

	public static final String SMS_PREFERENCE = "sms_preference";

	public static final String VIBRATE_PREFERENCE = "vibrate_preference";

	public static final String RINGTONE_PREFERENCE = "ringtone_preference";

	public static final String FLASH_LED_PREFERENCE = "flash_led_preference";

	public static final String USHAHIDI_DEPLOYMENT_PREFERENCE = "ushahidi_instance_preference";

	public static final String EMAIL_ADDRESS_PREFERENCE = "email_address_preference";

	public static final String PHONE_NUMBER_PREFERENCE = "phone_number_preference";

	public static final String CHECKIN_PREFERENCE = "checkin_preference";

	public static final String PHOTO_SIZE_PREFERENCE = "photo_size_preference";

	public static final String ZOOM_LEVEL_PREFERENCE = "default_zoom_level_preference";

	private boolean checkin = false;

	private String recentReports = "";

	private String onPhone = "";

	private String onSdCard = "";

	private String minutes = "";

	private static final String CLASS_TAG = Settings.class.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		firstNamePref = new EditTextPreference(this);

		lastNamePref = new EditTextPreference(this);

		emailAddressPref = new EditTextPreference(this);

		phoneNumberPref = new EditTextPreference(this);

		autoFetchCheckBoxPref = new CheckBoxPreference(this);
		vibrateCheckBoxPref = new CheckBoxPreference(this);
		ringtoneCheckBoxPref = new CheckBoxPreference(this);
		flashLedCheckBoxPref = new CheckBoxPreference(this);

		photoSizePref = (SeekBarPreference) getPreferenceScreen()
				.findPreference(PHOTO_SIZE_PREFERENCE);
		photoSizePref.setOnPreferenceChangeListener(this);

		recentReports = getString(R.string.recent_reports);
		onPhone = getString(R.string.on_phone);
		onSdCard = getString(R.string.on_sd_card);
		minutes = getString(R.string.minutes);

		clearCacheCheckBoxPref = (DialogPreference) getPreferenceScreen()
				.findPreference("clear_cache_preference");
		autoUpdateTimePref = new ListPreference(this);
		saveItemsPref = new ListPreference(this);
		totalReportsPref = new ListPreference(this);
		gpsTimeoutPref = new EditTextPreference(this);
		reportImagesCount = new EditTextPreference(this);
		locationTolerancePref = new EditTextPreference(this);
		defaultLatitudePref = new EditTextPreference(this);
		defaultLongitudePref = new EditTextPreference(this);

		defaultZoomLevelPref = (SeekBarPreference) getPreferenceScreen()
				.findPreference(ZOOM_LEVEL_PREFERENCE);
		defaultZoomLevelPref.setOnPreferenceChangeListener(this);

		new ListPreference(this);

		setPreferenceScreen(createPreferenceHierarchy());

		this.saveSettings();
	}

	private PreferenceScreen createPreferenceHierarchy() {
		// ROOT element
		PreferenceScreen root = getPreferenceManager().createPreferenceScreen(
				this);

		// Basic preferences
		PreferenceCategory basicPrefCat = new PreferenceCategory(this);
		basicPrefCat.setTitle(R.string.basic_settings);
		root.addPreference(basicPrefCat);

		// Total reports to fetch at a time
		// set list values
		// TODO:// need to look it how to properly handle this. It looks ugly
		// but it works.
		CharSequence[] totalReportsEntries = { "20 ".concat(recentReports),
				"40 ".concat(recentReports), "60 ".concat(recentReports),
				"80 ".concat(recentReports), "100 ".concat(recentReports),
				"250 ".concat(recentReports), "500 ".concat(recentReports),
				"1000 ".concat(recentReports) };

		CharSequence[] totalReportsValues = { "20", "40", "60", "80", "100",
				"250", "500", "1000" };

		totalReportsPref.setEntries(totalReportsEntries);
		totalReportsPref.setEntryValues(totalReportsValues);
		totalReportsPref.setDefaultValue(totalReportsValues[0]);
		totalReportsPref.setDialogTitle(R.string.total_reports);
		totalReportsPref.setKey("total_reports_preference");
		totalReportsPref.setTitle(R.string.total_reports);
		totalReportsPref.setSummary(R.string.hint_total_reports);
		basicPrefCat.addPreference(totalReportsPref);

		// Photo resize seekbar
		basicPrefCat.addPreference(photoSizePref);

		// Aman Maximum number of images in report Preferences
		reportImagesCount.setDialogTitle(R.string.txt_location_tolerance_in_km);
		reportImagesCount.setKey("report_image_count");
		reportImagesCount.setTitle(R.string.txt_maximum_images);
		reportImagesCount.setSummary(R.string.txt_maximum_images_report);
		reportImagesCount.setDefaultValue("6");
		reportImagesCount.getEditText()
				.setInputType(InputType.TYPE_CLASS_PHONE);
		basicPrefCat.addPreference(reportImagesCount);

		// User ID Preferences
		PreferenceCategory userIdPrefCat = new PreferenceCategory(this);
		userIdPrefCat.setTitle(R.string.userid_settings);
		root.addPreference(userIdPrefCat);

		// First name entry field
		firstNamePref.setDialogTitle(R.string.txt_first_name);
		firstNamePref.setKey("first_name_preference");
		firstNamePref.setTitle(R.string.txt_first_name);
		firstNamePref.setSummary(R.string.hint_first_name);
		firstNamePref.getEditText().setInputType(
				InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		userIdPrefCat.addPreference(firstNamePref);

		// Last name entry field
		lastNamePref.setDialogTitle(R.string.txt_last_name);
		lastNamePref.setKey("last_name_preference");
		lastNamePref.setTitle(R.string.txt_last_name);
		lastNamePref.setSummary(R.string.hint_last_name);
		lastNamePref.getEditText().setInputType(
				InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		userIdPrefCat.addPreference(lastNamePref);

		// Email name entry field
		emailAddressPref.setDialogTitle(R.string.txt_email);
		emailAddressPref.setKey("email_address_preference");
		emailAddressPref.setTitle(R.string.txt_email);
		emailAddressPref.setSummary(R.string.hint_email);
		emailAddressPref.getEditText().setInputType(
				InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		userIdPrefCat.addPreference(emailAddressPref);

		// phone number entry field
		phoneNumberPref.setDialogTitle(R.string.txt_phonenumber);
		phoneNumberPref.setKey("phone_number_preference");
		phoneNumberPref.setTitle(R.string.txt_phonenumber);
		phoneNumberPref.setSummary(R.string.hint_phonenumber);
		phoneNumberPref.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);

		/**
		 * Commenting out this code so it doesn't prompt users for opengeoSMS
		 * basicPrefCat.addPreference(phoneNumberPref); TODO:// re-enable this
		 * when I'm happy with opengeoSMS integration with the Ushahidi
		 * platform.
		 */

		// Advanced Preferences
		PreferenceCategory advancedPrefCat = new PreferenceCategory(this);
		advancedPrefCat.setTitle(R.string.advanced_settings);
		root.addPreference(advancedPrefCat);

		PreferenceScreen advancedScreenPref = getPreferenceManager()
				.createPreferenceScreen(this);
		advancedScreenPref.setKey("advanced_screen_preference");
		advancedScreenPref.setTitle(R.string.advanced_settings);
		advancedScreenPref.setSummary(R.string.hint_advanced_settings);
		advancedPrefCat.addPreference(advancedScreenPref);

		// Auto fetch reports
		autoFetchCheckBoxPref.setKey("auto_fetch_preference");
		autoFetchCheckBoxPref.setTitle(R.string.chk_auto_fetch);
		autoFetchCheckBoxPref.setSummary(R.string.hint_auto_fetch);
		advancedScreenPref.addPreference(autoFetchCheckBoxPref);
		
		// location of storage
		// set list values
		CharSequence[] saveItemsEntries = { onPhone, onSdCard };

		CharSequence[] saveItemsValues = { "phone", "card" };

		saveItemsPref.setEntries(saveItemsEntries);
		saveItemsPref.setEntryValues(saveItemsValues);
		saveItemsPref.setDefaultValue(saveItemsValues[0]);
		saveItemsPref.setDialogTitle(R.string.option_location);
		saveItemsPref.setKey("save_items_preference");
		saveItemsPref.setTitle(R.string.option_location);
		saveItemsPref.setSummary(R.string.hint_option_location);
		advancedScreenPref.addPreference(saveItemsPref);

		// Mapping Preferences
		PreferenceCategory mappingPrefCat = new PreferenceCategory(this);
		mappingPrefCat.setTitle(R.string.mapping_settings);
		advancedScreenPref.addPreference(mappingPrefCat);

		// Default Latitude Preferences for report view map
		defaultLatitudePref
				.setDialogTitle("Default Latitude (from -90 to +90)");
		defaultLatitudePref.setKey("default_latitude_preference");
		defaultLatitudePref.setTitle("Default Latitude");
		defaultLatitudePref
				.setSummary("Default Latitude value(from -90 to +90) for map view in View Reports ");
		defaultLatitudePref.setDefaultValue("5");
		defaultLatitudePref.getEditText().setInputType(
				InputType.TYPE_CLASS_PHONE);
		mappingPrefCat.addPreference(defaultLatitudePref);

		// Default Longitude Preferences for report view map
		defaultLongitudePref
				.setDialogTitle("Default Longitude (from -90 to +90)");
		defaultLongitudePref.setKey("default_longitude_preference");
		defaultLongitudePref.setTitle("Default Longitude");
		defaultLongitudePref
				.setSummary("Default Longitude value(from -90 to +90) for map view in View Reports ");
		defaultLongitudePref.setDefaultValue("5");
		defaultLongitudePref.getEditText().setInputType(
				InputType.TYPE_CLASS_PHONE);
		mappingPrefCat.addPreference(defaultLongitudePref);

		// Default map zoom level prefrence
		mappingPrefCat.addPreference(defaultZoomLevelPref);

		// location tolerance Preferences
		locationTolerancePref
				.setDialogTitle(R.string.txt_location_tolerance_in_km);
		locationTolerancePref.setKey("location_tolerance_preference");
		locationTolerancePref.setTitle(R.string.txt_location_tolerance);
		locationTolerancePref.setSummary(R.string.txt_location_tolerance_in_km);
		locationTolerancePref.setDefaultValue("5");
		locationTolerancePref.getEditText().setInputType(
				InputType.TYPE_CLASS_PHONE);
		mappingPrefCat.addPreference(locationTolerancePref);

		// gps timeout Preferences
		gpsTimeoutPref.setDialogTitle(R.string.txt_gps_timeout);
		gpsTimeoutPref.setKey("gps_timeout_preference");
		gpsTimeoutPref.setTitle(R.string.txt_gps_timeout);
		gpsTimeoutPref.setSummary(R.string.txt_gps_timeout_in_sec);
		gpsTimeoutPref.setDefaultValue("60");
		gpsTimeoutPref.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
		mappingPrefCat.addPreference(gpsTimeoutPref);

		// notification Preferences
		PreferenceCategory notificationPrefCat = new PreferenceCategory(this);
		notificationPrefCat.setTitle(R.string.bg_notification);
		advancedScreenPref.addPreference(notificationPrefCat);

		
		// Auto update reports time interval
		// set list values
		CharSequence[] autoUpdateEntries = { "5 ".concat(minutes),
				"10 ".concat(minutes), "15 ".concat(minutes),
				"30 ".concat(minutes), "60 ".concat(minutes) };
		CharSequence[] autoUpdateValues = { "0", "5", "10", "15", "30", "60" };
		autoUpdateTimePref.setEntries(autoUpdateEntries);
		autoUpdateTimePref.setEntryValues(autoUpdateValues);
		autoUpdateTimePref.setDefaultValue(autoUpdateValues[0]);
		autoUpdateTimePref.setDialogTitle(R.string.txt_auto_update_delay);
		autoUpdateTimePref.setKey("auto_update_time_preference");
		autoUpdateTimePref.setTitle(R.string.txt_auto_update_delay);
		autoUpdateTimePref.setSummary(R.string.hint_auto_update_delay);
		notificationPrefCat.addPreference(autoUpdateTimePref);

		// clear cache
		notificationPrefCat.addPreference(clearCacheCheckBoxPref);

		// vibrate
		vibrateCheckBoxPref.setKey("vibrate_preference");
		vibrateCheckBoxPref.setTitle(R.string.vibrate);
		vibrateCheckBoxPref.setSummary(R.string.hint_vibrate);
		notificationPrefCat.addPreference(vibrateCheckBoxPref);

		// ringtone
		ringtoneCheckBoxPref.setKey("ringtone_preference");
		ringtoneCheckBoxPref.setTitle(R.string.ringtone);
		ringtoneCheckBoxPref.setSummary(R.string.hint_ringtone);
		notificationPrefCat.addPreference(ringtoneCheckBoxPref);

		// flash led
		flashLedCheckBoxPref.setKey("flash_led_preference");
		flashLedCheckBoxPref.setTitle(R.string.flash_led);
		flashLedCheckBoxPref.setSummary(R.string.hint_flash_led);
		notificationPrefCat.addPreference(flashLedCheckBoxPref);

		return root;
	}

	protected void saveSettings() {

		settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
		editor = settings.edit();

		String autoUpdate = autoUpdateTimePref.getValue();
		String saveItems = saveItemsPref.getValue();
		String totalReports = totalReportsPref.getValue();
		String newSavePath;
		int autoUdateDelay = 0;

		// "5 Minutes", "10 Minutes", "15 Minutes", "c", "60 Minutes"
		if (autoUpdate.matches("5")) {
			autoUdateDelay = 5;
		} else if (autoUpdate.matches("10")) {
			autoUdateDelay = 10;
		} else if (autoUpdate.matches("15")) {
			autoUdateDelay = 15;
		} else if (autoUpdate.matches("30")) {
			autoUdateDelay = 30;
		} else if (autoUpdate.matches("60")) {
			autoUdateDelay = 60;
		}

		if (saveItems.equalsIgnoreCase("phone")) {
			newSavePath = this.getDir("",
					MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE).toString()
					+ "/";

		} else { // means on sd is checked
			newSavePath = Environment.getExternalStorageDirectory().toString()
					+ "ushahidi/";
		}

		editor.putString("Domain", Preferences.domain);
		editor.putString("Firstname", firstNamePref.getText());
		editor.putString("Lastname", lastNamePref.getText());
		editor.putString("Email", emailAddressPref.getText());
		editor.putString("Phonenumber", phoneNumberPref.getText());
		editor.putString("savePath", newSavePath);
		editor.putInt("AutoUpdateDelay", autoUdateDelay);
		editor.putBoolean("AutoFetch", autoFetchCheckBoxPref.isChecked());
		editor.putString("TotalReports", totalReports);
		editor.putInt("CheckinEnabled", Preferences.isCheckinEnabled);
		editor.putString("gps_timeout_preference", gpsTimeoutPref.getText());
		editor.putString("report_image_count", reportImagesCount.getText());
		editor.putString("location_tolerance_preference",
				locationTolerancePref.getText());
		editor.commit();

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		this.saveSettings();

	}

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);

	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// Let's do something when a preference value changes

		if (sharedPreferences.getBoolean(AUTO_FETCH_PREFERENCE, false)) {

			// Enable background service.
			startService(new Intent(Settings.this, BackgroundService.class));

		} else {
			stopService(new Intent(Settings.this, BackgroundService.class));
		}

		// Reset vibrate
		if (sharedPreferences.getBoolean(VIBRATE_PREFERENCE, false)) {
			Preferences.vibrate = true;
		} else {
			Preferences.vibrate = false;
		}

		// Reset ringtone
		if (sharedPreferences.getBoolean(RINGTONE_PREFERENCE, false)) {

			Preferences.ringtone = true;
		} else {

			Preferences.ringtone = false;
		}

		// Reset flash led
		if (sharedPreferences.getBoolean(FLASH_LED_PREFERENCE, false)) {

			Preferences.flashLed = true;
		} else {
			Preferences.flashLed = false;
		}

		// cache
		if (key.equals(USHAHIDI_DEPLOYMENT_PREFERENCE)) {
			if (!sharedPreferences
					.getString(USHAHIDI_DEPLOYMENT_PREFERENCE, "").equals(
							Preferences.domain)) {
				validateUrl(sharedPreferences.getString(
						USHAHIDI_DEPLOYMENT_PREFERENCE, ""));

			}
		}

		/*
		 * //photo size if (key.equals(PHOTO_SIZE_PREFERENCE)) { int size =
		 * sharedPreferences.getInt(PHOTO_SIZE_PREFERENCE, 200);
		 * 
		 * Log.d(CLASS_TAG,
		 * "Photo Size: "+size+", Preferences.photoWidth: "+Preferences
		 * .photoWidth);
		 * 
		 * if (size > Preferences.photoWidth) { //Preferences.photoWidth = size;
		 * } }
		 */

		// validate email address
		if (key.equals(EMAIL_ADDRESS_PREFERENCE)) {
			if (!Util.validateEmail(sharedPreferences.getString(
					EMAIL_ADDRESS_PREFERENCE, ""))) {
				Util.showToast(this, R.string.invalid_email_address);
			}
		}

		// save changes
		this.saveSettings();

	}

	// thread class
	private class ReportsTask extends AsyncTask<Void, Void, Integer> {

		protected Integer status;

		private ProgressDialog dialog;

		protected Context appContext;

		@Override
		protected void onPreExecute() {

			Preferences.loadSettings(appContext);
			this.dialog = ProgressDialog.show(appContext,
					getString(R.string.please_wait),
					getString(R.string.fetching_new_reports), true);

		}

		@Override
		protected Integer doInBackground(Void... params) {
			// clear previous data
			MainApplication.mDb.clearData();
			status = ApiUtils.processReports(appContext);
			isCheckinsEnabled();
			return status;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == 4) {
				Util.showToast(appContext, R.string.internet_connection);
			} else if (result == 3) {
				Util.showToast(appContext, R.string.invalid_ushahidi_instance);
			} else if (result == 2) {
				Util.showToast(appContext, R.string.could_not_fetch_reports);
			} else if (result == 1) {
				Util.showToast(appContext, R.string.could_not_fetch_reports);
			} else {
				Util.showToast(appContext,
						R.string.reports_successfully_fetched);
			}
			this.dialog.cancel();
		}

	}

	/**
	 * Create runnable for validating callback URL.
	 */
	Runnable mValidateUrl = new Runnable() {
		public void run() {

			if (!validUrl) {

				// reset whatever was entered in that field.
				Preferences.loadSettings(Settings.this);
				Util.showToast(Settings.this,
						R.string.invalid_ushahidi_instance);
			} else {

				ReportsTask reportsTask = new ReportsTask();
				reportsTask.appContext = Settings.this;
				reportsTask.execute();
			}
		}
	};

	/**
	 * Create a child thread and validate the callback URL in it when enabling
	 * 
	 * @param String
	 *            Url - The Callback Url to be validated.
	 * @return void
	 */
	public void validateUrl(final String Url) {

		Thread t = new Thread() {
			public void run() {

				validUrl = ApiUtils.validateUshahidiInstance(Url);

				mHandler.post(mValidateUrl);
			}
		};
		t.start();
	}

	Runnable mIsCheckinsEnabled = new Runnable() {
		public void run() {

			if (checkin) {
				Preferences.isCheckinEnabled = 1;
			} else {
				Preferences.isCheckinEnabled = 0;
			}

			Preferences.saveSettings(Settings.this);

		}
	};

	/**
	 * Checks if checkins is enabled on the configured Ushahidi deployment.
	 */
	public void isCheckinsEnabled() {
		if (ApiUtils.isCheckinEnabled(Settings.this)) {
			Preferences.isCheckinEnabled = 1;
		} else {
			Preferences.isCheckinEnabled = 0;
		}
		Preferences.saveSettings(Settings.this);
	}

	public boolean onPreferenceChange(Preference preference, Object newValue) {

		if (preference.getKey().equalsIgnoreCase(PHOTO_SIZE_PREFERENCE)) {
			Log.d(CLASS_TAG, "Preferences.photoWidth: " + newValue);
			Preferences.photoWidth = (Integer) newValue;
			editor.putInt("PhotoWidth", Preferences.photoWidth);
			editor.commit();
		} else if (preference.getKey().equalsIgnoreCase(ZOOM_LEVEL_PREFERENCE)) {
			Log.d(CLASS_TAG, "Preferences.Max Zoom: " + newValue);
			Preferences.mapZoom = (Integer) newValue;
			editor.putInt("mapZoom", Preferences.mapZoom);
			editor.commit();
		}

		return true;
	}

}
