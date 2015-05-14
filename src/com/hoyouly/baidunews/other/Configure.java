package com.hoyouly.baidunews.other;

import android.app.Activity;
import android.util.DisplayMetrics;

public class Configure {
	public static boolean isMove = false;
	public static boolean isChangingPage = false;
	public static boolean isDelDark = false;
	public static int screenHeight = 0;//屏幕高度
	public static int screenWidth = 0; //屏幕宽度
	public static float screenDensity = 0;

	public static int currentPage = 0;
	public static int countPages = 0;

	public static void init(Activity context) {
		if (screenDensity == 0 || screenWidth == 0 || screenHeight == 0) {
			//得到屏幕的宽高
			DisplayMetrics dm = new DisplayMetrics();
			context.getWindowManager().getDefaultDisplay().getMetrics(dm);
			Configure.screenDensity = dm.density;//屏幕密度
			Configure.screenHeight = dm.heightPixels;
			Configure.screenWidth = dm.widthPixels;
		}
		currentPage = 0;//当前页
		countPages = 0;//总页数是0
	}
}