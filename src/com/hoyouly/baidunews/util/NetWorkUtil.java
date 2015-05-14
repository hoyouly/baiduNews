package com.hoyouly.baidunews.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.hoyouly.baidunews.R;
/**
 * 检查网络是否有网
 * @author duan-zhangpeng
 *
 */
public class NetWorkUtil {
	/**
	 * 网络为wrap时候的代理ip
	 */
	public static String PROXY_IP="";
	
	/**
	 * 网络为wrap时候的代理端口
	 */
	public static int PROXY_PORT=0;
	
	
	/**
	 * 检查网络
	 */
	public static boolean checkNetwork(Context context) {
		// 判断手机端利用的通信渠道

		// ①判断WIFI可以连接
		boolean isWIFI = isWIFICon(context);
		// ②判断MOBILE可以连接
		boolean isMOBILE = isMOBILECon(context);

		// 如果都无法使用——提示用户
		if (!isWIFI && !isMOBILE) {
			return false;
		}

		// 如果有可以利用的通信渠道，是不是MOBILE
		if (isMOBILE) {
			// 如果是，是否是wap方式
			// 读取APN配置信息，如果发现代理信息非空
			readAPN(context);// 读取联系人信息
		}
		return true;
	}

	/**
	 * 读取APN配置信息
	 * @param context
	 */
	private static void readAPN(Context context) {
		Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");//4.0模拟器屏蔽掉该权限
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(PREFERRED_APN_URI, null, null, null, null);// 只有一条
		if (cursor != null && cursor.moveToFirst()) {
			NetWorkUtil.PROXY_IP = cursor.getString(cursor.getColumnIndex("proxy"));
			NetWorkUtil.PROXY_PORT = cursor.getInt(cursor.getColumnIndex("port"));
		}
	}

	/**
	 * 判断MOBILE可以连接
	 * @param context
	 * @return
	 */
	private static boolean isMOBILECon(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * 判断WIFI可以连接
	 * @param context
	 * @return
	 */
	private static boolean isWIFICon(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}
	
	/**
	 * 当判断当前手机没有网络时使用
	 * 
	 * @param context
	 */
	public static void showNoNetWork(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setIcon(R.drawable.ic_launcher)//
				.setTitle(R.string.app_name)//
				.setMessage("当前无网络").setPositiveButton("设置", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 跳转到系统的网络设置界面
						Intent intent = null;
						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						} else {
							intent = new Intent();
							ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
							intent.setComponent(component);
							intent.setAction("android.intent.action.VIEW");
						}
						context.startActivity(intent);
						
					}
				}).setNegativeButton("知道了", null).show();
	}
}
