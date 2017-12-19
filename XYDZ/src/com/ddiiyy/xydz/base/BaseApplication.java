package com.ddiiyy.xydz.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class BaseApplication extends Application {

	private static Context mContext;
	private static boolean mIsAtLeastGB;
	private static String PREF_NAME = "creativelocker.pref";
	private static String LAST_REFRESH_TIME = "last_refresh_time.pref";

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		
	}

	public static synchronized BaseApplication context() {
		return (BaseApplication) mContext;
	}

	/***
	 * 记录列表上次刷新时间
	 * 
	 * @return void
	 * @param key
	 * @param value
	 */
	public static void putToLastRefreshTime(String key, String value) {
		SharedPreferences preferences = getPreferences(LAST_REFRESH_TIME);
		Editor editor = preferences.edit();
		editor.putString(key, value);
		apply(editor);
	}

	@SuppressLint("NewApi")
	public static void apply(SharedPreferences.Editor editor) {
		if (mIsAtLeastGB) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

	public static void set(String key, boolean value) {
		Editor editor = getPreferences().edit();
		editor.putBoolean(key, value);
		apply(editor);
	}

	public static void set(String key, String value) {
		Editor editor = getPreferences().edit();
		editor.putString(key, value);
		apply(editor);
	}

	public static boolean get(String key, boolean defValue) {
		return getPreferences().getBoolean(key, defValue);
	}

	public static String get(String key, String defValue) {
		return getPreferences().getString(key, defValue);
	}

	public static int get(String key, int defValue) {
		return getPreferences().getInt(key, defValue);
	}

	public static long get(String key, long defValue) {
		return getPreferences().getLong(key, defValue);
	}

	public static float get(String key, float defValue) {
		return getPreferences().getFloat(key, defValue);
	}

	@SuppressLint("InlinedApi")
	public static SharedPreferences getPreferences() {
		SharedPreferences pre = context().getSharedPreferences(PREF_NAME,
				Context.MODE_MULTI_PROCESS);
		return pre;
	}

	@SuppressLint("InlinedApi")
	public static SharedPreferences getPreferences(String prefName) {
		return context().getSharedPreferences(prefName,
				Context.MODE_MULTI_PROCESS);
	}


}
