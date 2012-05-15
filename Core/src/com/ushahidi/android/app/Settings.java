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

	private EditTextPreference password;

	private CheckBoxPreference autoFetchCheckBoxPref;

	private SeekBarPreference reportImagesCount;

	private EditTextPreference defaultLatitudePref;

	private EditTextPreference defaultLongitudePref;

	private SeekBarPreference defaultZoomLevelPref;

	private SeekBarPreference locationTolerancePref;

	private SeekBarPreference gpsTimeoutPref;

	private CheckBoxPreference vibrateCheckBoxPref;

	private CheckBoxPreference ringtoneCheckBoxPref;

	private CheckBoxPreference flashLedCheckBoxPref;

	private DialogPreference clearCacheCheckBoxPref;

	private SeekBarPreference autoUpdateTimePref;

	private ListPreference saveItemsPref;

	private SeekBarPreference totalReportsPref;

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

	public static final String ZOOM_LEVEL_PREFERENCE = "zoom_level_preference";

	public static final String LOCATION_TOLERENCE_PREFERENCE = "location_tolerance";

	public static final String GPS_TIMEOUT_PREFERENCE = "gps_timeout_preference_temp";

	public static final String AUTO_UPDATE_TIME_PREFERENCE = "AutoUpdateDelay";

	public static final String REPORT_IMAGE_COUNT_PREFERENCE = "report_image_count_temp";

	public static final String TOTAL_REPORTS_PREFERENCE = "TotalReports_temp";

	private boolean checkin = false;

	// private String recentReports = "";

	private String onPhone = "";

	private String onSdCard = "";

	// private String minutes = "";

	private static final String CLASS_TAG = Settings.class.getCanonicalName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);

		firstNamePref = new EditTextPreference(this);

		lastNamePref = new EditTextPreference(this);

		emailAddressPref = new EditTextPreference(this);

		phoneNumberPref = new EditTextPreference(this);

		password = new EditTextPreference(this);

		autoFetchCheckBoxPref = new CheckBoxPreference(this);
		vibrateCheckBoxPref = new CheckBoxPreference(this);
		ringtoneCheckBoxPref = new CheckBoxPreference(this);
		flashLedCheckBoxPref = new CheckBoxPreference(this);

		photoSizePref = (SeekBarPreference) getPreferenceScreen()
				.findPreference(PHOTO_SIZE_PREFERENCE);
		photoSizePref.setOnPreferenceChangeListener(this);

		// recentReports = getString(R.string.recent_reports);
		onPhone = getString(R.string.on_phone);
		onSdCard = getString(R.string.on_sd_card);
		// minutes = getString(R.string.minutes);

		clearCacheCheckBoxPref = (DialogPreference) getPreferenceScreen()
				.findPreference("clear_cache_preference");
		// autoUpdateTimePref = new ListPreference(this);
		autoUpdateTimePref = (SeekBarPreference) getPreferenceScreen()
				.findPreference(AUTO_UPDATE_TIME_PREFERENCE);
		autoUpdateTimePref.setOnPreferenceChangeListener(this);

		saveItemsPref = new ListPreference(this);
		// totalReportsPref = new ListPreference(this);
		totalReportsPref = (SeekBarPreference) getPreferenceScreen()
				.findPreference(TOTAL_REPORTS_PREFERENCE);
		totalReportsPref.setOnPreferenceChangeListener(this);

		// gpsTimeoutPref = new EditTextPreference(this);
		gpsTimeoutPref = (SeekBarPreference) getPreferenceScreen()
				.findPreference(GPS_TIMEOUT_PREFERENCE);
		gpsTimeoutPref.setOnPreferenceChangeListener(this);

		// reportImagesCount = new EditTextPreference(this);
		reportImagesCount = (SeekBarPreference) getPreferenceScreen()
				.findPreference(REPORT_IMAGE_COUNT_PREFERENCE);
		reportImagesCount.setOnPreferenceChangeListener(this);

		// locationTolerancePref = new EditTextPreference(this);
		defaultLatitudePref = new EditTextPreference(this);
		defaultLongitudePref = new EditTextPreference(this);

		defaultZoomLevelPref = (SeekBarPreference) getPreferenceScreen()
				.findPreference(ZOOM_LEVEL_PREFERENCE);
		defaultZoomLevelPref.setOnPreferenceChangeListener(this);

		locationTolerancePref = (SeekBarPreference) getPreferenceScreen()
				.findPreference(LOCATION_TOLERENCE_PREFERENCE);
		locationTolerancePref.setOnPreferenceChangeListener(this);

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

		totalReportsPref.setOrder(1);
		basicPrefCat.addPreference(totalReportsPref);

		// Photo settings
		PreferenceCategory photoPrefCat = new PreferenceCategory(this);
		photoPrefCat.setTitle(R.string.photo_settings);
		root.addPreference(photoPrefCat);

		// Photo resize seekbar
		photoSizePref.setOrder(1);
		photoPrefCat.addPreference(photoSizePref);

		// Aman Maximum number of images in report Preferences
		// reportImagesCount.setDialogTitle(R.string.txt_location_tolerance_in_km);
		// reportImagesCount.setKey("report_image_count");
		// reportImagesCount.setTitle(R.string.txt_maximum_images);
		// reportImagesCount.setSummary(R.string.txt_maximum_images_report);
		// reportImagesCount.setDefaultValue("6");
		// reportImagesCount.getEditText()
		// .setInputType(InputType.TYPE_CLASS_PHONE);
		reportImagesCount.setOrder(2);
		photoPrefCat.addPreference(reportImagesCount);

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

		// password entry field
		password.setDialogTitle("Password");
		password.setKey("password_prefrence");
		password.setTitle("Password");
		password.setSummary("Enter Password to view Advance settings");
		password.getEditText().setInputType(
				InputType.TYPE_TEXT_VARIATION_PASSWORD);
		userIdPrefCat.addPreference(password);

		/**
		 * Commenting out this code so it doesn't prompt users for opengeoSMS
		 * basicPrefCat.addPreference(phoneNumberPref); TODO:// re-enable this
		 * when I'm happy with opengeoSMS integration with the Ushahidi
		 * platform.
		 */

		try {
			// to set password on first run.
			password.getText().toString();
		} catch (Exception e) {
			password.setText("");
		}

		if ((password.getText().toString()).equals("si2sv2012")) {

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
			// saveItemsPref.setOrder(2);
			advancedScreenPref.addPreference(saveItemsPref);

			// clear cache
			// clearCacheCheckBoxPref.setOrder(1);
			advancedScreenPref.addPreference(clearCacheCheckBoxPref);

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
			defaultLatitudePref.setDefaultValue("13.69947");
			defaultLatitudePref.getEditText().setInputType(
					InputType.TYPE_CLASS_PHONE);
			defaultLatitudePref.setOrder(0);
			mappingPrefCat.addPreference(defaultLatitudePref);

			// Default Longitude Preferences for report view map
			defaultLongitudePref
					.setDialogTitle("Default Longitude (from -90 to +90)");
			defaultLongitudePref.setKey("default_longitude_preference");
			defaultLongitudePref.setTitle("Default Longitude");
			defaultLongitudePref
					.setSummary("Default Longitude value(from -90 to +90) for map view in View Reports ");
			defaultLongitudePref.setDefaultValue("-89.2216");
			defaultLongitudePref.getEditText().setInputType(
					InputType.TYPE_CLASS_PHONE);
			defaultLongitudePref.setOrder(1);
			mappingPrefCat.addPreference(defaultLongitudePref);

			// Default map zoom level prefrence
			defaultLatitudePref.setOrder(2);
			mappingPrefCat.addPreference(defaultZoomLevelPref);

			// location tolerance Preferences
			locationTolerancePref.setOrder(3);
			mappingPrefCat.addPreference(locationTolerancePref);

			// gps timeout Preferences
			// gpsTimeoutPref.setDialogTitle(R.string.txt_gps_timeout);
			// gpsTimeoutPref.setKey("gps_timeout_preference");
			// gpsTimeoutPref.setTitle(R.string.txt_gps_timeout);
			// gpsTimeoutPref.setSummary(R.string.txt_gps_timeout_in_sec);
			// gpsTimeoutPref.setDefaultValue("60");
			// gpsTimeoutPref.getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
			gpsTimeoutPref.setOrder(4);
			mappingPrefCat.addPreference(gpsTimeoutPref);

			// notification Preferences
			PreferenceCategory notificationPrefCat = new PreferenceCategory(
					this);
			notificationPrefCat.setTitle(R.string.bg_notification);
			advancedScreenPref.addPreference(notificationPrefCat);

			// Auto fetch reports
			autoFetchCheckBoxPref.setKey("auto_fetch_preference");
			autoFetchCheckBoxPref.setTitle(R.string.chk_auto_fetch);
			autoFetchCheckBoxPref.setSummary(R.string.hint_auto_fetch);
			autoFetchCheckBoxPref.setOrder(1);
			notificationPrefCat.addPreference(autoFetchCheckBoxPref);

			// Auto update reports time interval
			// set list values
			// CharSequence[] autoUpdateEntries = { "5 ".concat(minutes),
			// "10 ".concat(minutes), "15 ".concat(minutes),
			// "30 ".concat(minutes), "60 ".concat(minutes) };
			// CharSequence[] autoUpdateValues = { "0", "5", "10", "15", "30",
			// "60"
			// };
			// autoUpdateTimePref.setEntries(autoUpdateEntries);
			// autoUpdateTimePref.setEntryValues(autoUpdateValues);
			// autoUpdateTimePref.setDefaultValue(autoUpdateValues[0]);
			// autoUpdateTimePref.setDialogTitle(R.string.txt_auto_update_delay);
			// autoUpdateTimePref.setKey("auto_update_time_preference");
			// autoUpdateTimePref.setTitle(R.string.txt_auto_update_delay);
			// autoUpdateTimePref.setSummary(R.string.hint_auto_update_delay);
			autoFetchCheckBoxPref.setOrder(2);
			notificationPrefCat.addPreference(autoUpdateTimePref);

			// vibrate
			vibrateCheckBoxPref.setKey("vibrate_preference");
			vibrateCheckBoxPref.setTitle(R.string.vibrate);
			vibrateCheckBoxPref.setSummary(R.string.hint_vibrate);
			vibrateCheckBoxPref.setOrder(3);
			notificationPrefCat.addPreference(vibrateCheckBoxPref);

			// ringtone
			ringtoneCheckBoxPref.setKey("ringtone_preference");
			ringtoneCheckBoxPref.setTitle(R.string.ringtone);
			ringtoneCheckBoxPref.setSummary(R.string.hint_ringtone);
			ringtoneCheckBoxPref.setOrder(4);
			notificationPrefCat.addPreference(ringtoneCheckBoxPref);

			// flash led
			flashLedCheckBoxPref.setKey("flash_led_preference");
			flashLedCheckBoxPref.setTitle(R.string.flash_led);
			flashLedCheckBoxPref.setSummary(R.string.hint_flash_led);
			flashLedCheckBoxPref.setOrder(5);
			notificationPrefCat.addPreference(flashLedCheckBoxPref);
		}
		return root;
	}

	protected void saveSettings() {

		settings = getSharedPreferences(Preferences.PREFS_NAME, 0);
		editor = settings.edit();

		// String autoUpdate = autoUpdateTimePref.getValue();
		String saveItems = saveItemsPref.getValue();
		// String totalReports = totalReportsPref.getValue();
		String newSavePath;
		// int autoUdateDelay = 0;
		try {
			// try to protect from first time saving.
			if ((password.getText().toString()).equals("si2sv2012")) {
				if (saveItems.equalsIgnoreCase("phone")) {
					newSavePath = this.getDir("",
							MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE)
							.toString()
							+ "/";

				} else { // means on sd is checked
					newSavePath = Environment.getExternalStorageDirectory()
							.toString() + "ushahidi/";
				}

				editor.putString("Domain", Preferences.domain);
				editor.putString("Firstname", firstNamePref.getText());
				editor.putString("Lastname", lastNamePref.getText());
				editor.putString("Email", emailAddressPref.getText());
				editor.putString("Phonenumber", phoneNumberPref.getText());
				editor.putString("default_latitude_preference",
						defaultLatitudePref.getText());
				editor.putString("default_longitude_preference",
						defaultLongitudePref.getText());
				editor.putString("savePath", newSavePath);
				// editor.putInt("AutoUpdateDelay", autoUdateDelay);
				editor.putBoolean("AutoFetch",
						autoFetchCheckBoxPref.isChecked());
				// editor.putString("TotalReports", totalReports);
				editor.putInt("CheckinEnabled", Preferences.isCheckinEnabled);
				// editor.putString("gps_timeout_preference",
				// gpsTimeoutPref.getText());
				// editor.putString("report_image_count",
				// reportImagesCount.getText());
				// editor.putString("location_tolerance_preference",
				// locationTolerancePref.getText());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
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

		Log.d(CLASS_TAG, "Preference change");
		if (preference.getKey().equalsIgnoreCase(PHOTO_SIZE_PREFERENCE)) {
			Preferences.photoWidth = (Integer) newValue;
			editor.putInt("PhotoWidth", Preferences.photoWidth);
			editor.putString("type", "photo");
			editor.commit();
		} else if (preference.getKey().equalsIgnoreCase(ZOOM_LEVEL_PREFERENCE)) {
			Preferences.mapZoom = (Integer) newValue;
			editor.putString("type", "zoom");
			editor.putInt("mapZoom", Preferences.mapZoom);
			editor.commit();
		} else if (preference.getKey().equalsIgnoreCase(
				LOCATION_TOLERENCE_PREFERENCE)) {
			Preferences.locationTolerance = (Integer) newValue;
			editor.putString("type", "location");
			// Setting the preference for slider
			editor.putString("location_tolerance_preference",
					Preferences.locationTolerance + "");
			// Setting the main preference for app
			editor.putInt("locationTolerence", Preferences.locationTolerance);
			editor.commit();
		} else if (preference.getKey().equalsIgnoreCase(GPS_TIMEOUT_PREFERENCE)) {
			Preferences.gpsTimeout = (Integer) newValue;
			editor.putString("type", "gps");
			editor.putString("gps_timeout_preference", Preferences.gpsTimeout
					+ "");
			editor.putInt("gps_timeout_preference_temp", Preferences.gpsTimeout);
			editor.commit();
		} else if (preference.getKey().equalsIgnoreCase(
				AUTO_UPDATE_TIME_PREFERENCE)) {
			Preferences.AutoUpdateDelay = (Integer) newValue;
			editor.putString("type", "update");
			editor.putInt("AutoUpdateDelay", Preferences.AutoUpdateDelay);
			editor.commit();
		} else if (preference.getKey().equalsIgnoreCase(
				REPORT_IMAGE_COUNT_PREFERENCE)) {
			Preferences.reportImageCount = (Integer) newValue;
			editor.putString("type", "reportImage");
			editor.putInt("report_image_count_temp",
					Preferences.reportImageCount);
			editor.putString("report_image_count", Preferences.reportImageCount
					+ "");
			editor.commit();
		} else if (preference.getKey().equalsIgnoreCase(
				TOTAL_REPORTS_PREFERENCE)) {
			Preferences.totalReports = (Integer) newValue;
			editor.putString("type", "totalReports");
			editor.putInt("TotalReports_temp", Preferences.totalReports);
			editor.putString("TotalReports", Preferences.totalReports + "");
			editor.commit();
		}

		return true;
	}

}
