package com.hoyouly.baidunews;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.hoyouly.baidunews.other.Configure;
import com.hoyouly.baidunews.other.InitDB;
import com.hoyouly.baidunews.util.ImageLoader;
import com.hoyouly.baidunews.util.SharedPreferencesHelper;

public class SplashActivity extends Activity {

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				spai.setVisibility(View.INVISIBLE);
				loadMainUI();
			}
		};
	};
	private SharedPreferencesHelper sp;
	private AnimationDrawable animationDrawable;
	private Message msg;
	private ImageView spai;// 旋转图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		spai = (ImageView) findViewById(R.id.iv_spai);
		spai.setVisibility(View.VISIBLE);

		spai.setImageResource(R.drawable.sapi_loading_blue);
		animationDrawable = (AnimationDrawable) spai.getDrawable();
		animationDrawable.start();

		sp = SharedPreferencesHelper.newInstance(SplashActivity.this);
		boolean hasDB = sp.getBoolean("initdb", false);
		if (hasDB) {// 说明已经初始化频道列表了，
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					loadMainUI();
				}
			}, 2000);
		} else {// 没有初始化过
			new Thread() {
				@Override
				public void run() {
					msg = Message.obtain();
					new InitDB(SplashActivity.this).initChannel();
					sp.putBoolean("initdb", true);
					msg.what = 0;
					Log.i("System.out", "初始化数据库完毕");
					handler.sendMessage(msg);
				}
			}.start();
		}
		//得到该应用程序在该设备中内存限制大小，单位是M
		int size = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
		ImageLoader.getInstance().setLruCacheSize(size);
		Configure.init(SplashActivity.this);
	}

	private void loadMainUI() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();// 把当前的Activity关闭掉
	};
}
