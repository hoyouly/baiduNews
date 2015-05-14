package com.hoyouly.baidunews.test;

import android.test.AndroidTestCase;
import android.util.Log;

import com.hoyouly.baidunews.dao.DBHelper;
import com.hoyouly.baidunews.domain.News;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public class TestNewsDb extends AndroidTestCase {
	public void testDB(){
		DbUtils dbUtils = DbUtils.create(getContext(), "baidu.db");
		News news = new News();
		
		try {
			dbUtils.save(news);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	public void testDelete(){
		DbUtils dbUtils = DbUtils.create(getContext(), "baidu.db");
		try {
			dbUtils.dropTable(News.class);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	public void testSelectBychannelIdAndLink(){
		DbUtils db = DBHelper.getDBUtils(getContext());
		try {
			News news = db.findFirst(News.class, WhereBuilder.b("channelId", "=", 0).and("link", "=", "http://sn.people.com.cn/n/2014/0409/c356312-20959596.html"));
			Log.i("System.out", "标题："+news.getTitle());
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
}
