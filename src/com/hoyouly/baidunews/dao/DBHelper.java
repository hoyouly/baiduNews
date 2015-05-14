package com.hoyouly.baidunews.dao;

import android.content.Context;

import com.lidroid.xutils.DbUtils;

public class DBHelper {

	private DBHelper() {
	}

	private static DbUtils db;

	public static DbUtils getDBUtils(Context context) {
		if (db == null) {
			db = DbUtils.create(context, "baidu.db");
		}
		return db;
	}
}
