/** 
 ** Copyright (c) 2011 Ushahidi Inc
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

package com.ushahidi.android.app.ui;

import com.ushahidi.android.app.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SeekBarPreference extends DialogPreference implements
		SeekBar.OnSeekBarChangeListener {

	private static final String androidns = "http://schemas.android.com/apk/res/android";

	private SeekBar mSeekBar;

	private TextView mSplashText, mValueText;

	private Context mContext;

	private String mDialogMessage, mSuffix;

	private int mMax = 0;

	private int mValue = 0;

	private int mDefault = 0;

	private int mMin = 200;

	private static final String CLASS_TAG = SeekBarPreference.class
			.getCanonicalName();

	private SharedPreferences settings;

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);

		settings = context.getSharedPreferences(Preferences.PREFS_NAME, 0);

		getmMin();

		mContext = context;

		mDialogMessage = attrs.getAttributeValue(androidns, "dialogMessage");
		mSuffix = attrs.getAttributeValue(androidns, "text");
		mDefault = attrs.getAttributeIntValue(androidns, "defaultValue", mMin);
		mMax = attrs.getAttributeIntValue(androidns, "max", 100);

	}

	@Override
	protected View onCreateDialogView() {

		LinearLayout.LayoutParams params;
		LinearLayout layout = new LinearLayout(mContext);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(6, 6, 6, 6);

		mSplashText = new TextView(mContext);
		if (mDialogMessage != null)
			mSplashText.setText(mDialogMessage);
		layout.addView(mSplashText);

		mValueText = new TextView(mContext);
		mValueText.setGravity(Gravity.CENTER_HORIZONTAL);
		mValueText.setTextSize(32);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.addView(mValueText, params);

		mSeekBar = new SeekBar(mContext);
		mSeekBar.setOnSeekBarChangeListener(this);
		layout.addView(mSeekBar, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));

		if (shouldPersist())
			mValue = getPersistedInt(mDefault);

		mSeekBar.setMax(mMax);
		if (mValue < mMin)
			mSeekBar.setProgress(mMin);
		else
			mSeekBar.setProgress(mValue);
		return layout;
	}

	@Override
	protected void onBindDialogView(View v) {
		super.onBindDialogView(v);
		mSeekBar.setMax(mMax);
		mSeekBar.setProgress(mValue);
	}

	@Override
	protected void onSetInitialValue(boolean restore, Object defaultValue) {
		super.onSetInitialValue(restore, defaultValue);
		if (restore)
			mValue = shouldPersist() ? getPersistedInt(mDefault) : mMin;
		else
			mValue = ((Integer) defaultValue > mMin) ? (Integer) defaultValue
					: mMin;
	}

	public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {

		int increment = getIncrement(); // Aman for a jump

		value = (value / increment) * increment;

		callChangeListener(new Integer(value));

		getmMin();
		String t = "";
		if (value < mMin) {
			t = String.valueOf(mMin);
		} else {
			t = String.valueOf(value);
		}

		Log.i(CLASS_TAG, "SeekBar current value: " + value);

		mValueText.setText(mSuffix == null ? t : t.concat(mSuffix));
		if (shouldPersist()) {
			Log.i(CLASS_TAG, "SeekBar is persisting " + value);
			persistInt(value);
		}

		callChangeListener(new Integer(value));
	}

	public void onStartTrackingTouch(SeekBar seek) {
	}

	public void onStopTrackingTouch(SeekBar seek) {
	}

	public void setMax(int max) {
		mMax = max;
	}

	public int getMax() {
		return mMax;
	}

	public void setProgress(int progress) {
		mValue = progress;
		if (mSeekBar != null)
			mSeekBar.setProgress(progress);
	}

	public int getProgress() {
		return mValue;
	}

	public void getmMin() {

		Log.d(CLASS_TAG, "log2:" + settings.getString("type", "photo"));
		String type = settings.getString("type", "photo");
		if (type.equals("zoom"))
			mMin = 1;
		else if (type.equals("report"))
			mMin = 10;
		else if (type.equals("location"))
			mMin = 1;
		else if (type.equals("gps"))
			mMin = 15;
		else if (type.equals("update"))
			mMin = 10;
		else if (type.equals("reportImage"))
			mMin = 0;
		else if (type.equals("totalReports"))
			mMin = 10;
		else
			mMin = 20;

		Log.d(CLASS_TAG, "min2:" + mMin);

	}

	private int getIncrement() {
		String type = settings.getString("type", "photo");
		int inc = 1;

		if (type.equals("update"))
			inc = 10;
		else if (type.equals("totalReports"))
			inc = 10;
		else if (type.equals("photo"))
			inc = 10;

		Log.d(CLASS_TAG, "Inc" + inc);
		return inc;
	}

}
