package com.hoyouly.baidunews.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

	private static final String SP_NAME = "baidu";//文件名
	private static SharedPreferences sp;
	private static SharedPreferences.Editor editor;
	private static SharedPreferencesHelper helper;
	private SharedPreferencesHelper(){}
	public static SharedPreferencesHelper newInstance(Context context) {
		if(helper == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
			editor = sp.edit();
			helper = new SharedPreferencesHelper();
		}
		return helper;
	}
	
	public boolean putValue(String key, String value) {
		if (null != key && !key.equals("")) {
			editor = sp.edit();
			editor.putString(key, value);
			return editor.commit();
		} else {
			return false;
		}
	}

	public boolean putBoolean(String key, boolean value) {
		if (null != key && !key.equals("")) {
			editor = sp.edit();
			editor.putBoolean(key, value);
			return editor.commit();
		} else {
			return false;
		}
	}
	
	
	public boolean putInt(String key, int value) {
		if (null != key && !key.equals("")) {
			editor = sp.edit();
			editor.putInt(key, value);
			return editor.commit();
		} else {
			return false;
		}
	}
	public boolean putLong(String key, long value) {
		if (null != key && !key.equals("")) {
			editor = sp.edit();
			editor.putLong(key, value);
			return editor.commit();
		} else {
			return false;
		}
	}

	public long getLong(String key) {
		return sp.getLong(key, 0);
	}
	
	public int getInt(String key) {
		return sp.getInt(key, 0);
	}
	
	public int getInt(String key, int defaultValue) {
		return sp.getInt(key, 0);
	}
	
	public String getValue(String key) {
		return sp.getString(key, null);
	}

	public String getValue(String key, String defaultStr) {
		return sp.getString(key, defaultStr);
	}

	public boolean getBoolean(String key) {
		return sp.getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

}
