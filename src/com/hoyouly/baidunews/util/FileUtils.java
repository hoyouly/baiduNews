package com.hoyouly.baidunews.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;

public class FileUtils {

	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();

	public static String mAppPackageName = "baidunews";

	public static File getImageViewpath(int channelId,String url) {
		if (getRootFile(channelId).exists()&&!TextUtils.isEmpty(url)) {
			// 说明sd卡正常挂载
			return new File(getRootFile(channelId),MD5Util.getMD5(url)+".jpg");
		}
		return null;
	}
	
	public static File getRootFile(int channelId){
		if (Environment.getExternalStorageState().endsWith(Environment.MEDIA_MOUNTED)) {
			// 说明sd卡正常挂载
			 File file = new File(mSdRootPath + File.separator + mAppPackageName + File.separator + "channelId_"+channelId);
			 if(!file.exists()){
				 file.mkdirs();
			 }
			 return file;
		}
		return null;
	}
	
	public static void saveBitmap(int channelId, String url, Bitmap bitmap) {
		try {
			File f = getImageViewpath(channelId,url);
			if (!f.exists()) {
				f.createNewFile();
				FileOutputStream fos = new FileOutputStream(f);
				// 把图片压缩成jpeg格式的，图片质量是100，图片质量是0~100，
				bitmap.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			} else {
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static  Bitmap getBitmap(int channelId,String url) {
		File file = getImageViewpath(channelId,url);
		if (file!=null&&file.exists()) {
			try {
				return BitmapFactory.decodeStream(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 递归删除文件目录
	 * 
	 * @param dir
	 *            文件目录
	 */
	public static void deleteFileDir(File dir) {
		try {
			if (dir.exists() && dir.isDirectory()) {// 判断是文件还是目录
				if (dir.listFiles().length == 0) {// 若目录下没有文件则直接删除
					dir.delete();
				} else {// 若有则把文件放进数组，并判断是否有下级目录
					File delFile[] = dir.listFiles();
					int len = dir.listFiles().length;
					for (int j = 0; j < len; j++) {
						if (delFile[j].isDirectory()) {
							deleteFileDir(delFile[j]);// 递归调用deleteFileDir方法并取得子目录路径
						} else {
							delFile[j].delete();// 删除文件
						}
					}
					delFile = null;
				}
				deleteFileDir(dir);// 递归调用
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param dir
	 *            文件目录
	 */
	public static void deleteFile(File file) {
		try {
			if (file != null && file.isFile() && file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
