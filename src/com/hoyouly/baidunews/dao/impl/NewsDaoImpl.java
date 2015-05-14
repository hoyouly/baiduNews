package com.hoyouly.baidunews.dao.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.hoyouly.baidunews.dao.DBHelper;
import com.hoyouly.baidunews.dao.NewsDao;
import com.hoyouly.baidunews.domain.News;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

public class NewsDaoImpl implements NewsDao {

	private DbUtils db;
	
	public NewsDaoImpl(Context context){
		db=DBHelper.getDBUtils(context);
	}
	
	@Override
	public News selectByArgs(int channelId, String links) {
		try {
			return  db.findFirst(News.class, WhereBuilder.b("channelId", "=", channelId).and("link", "=", links));
		} catch (DbException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void destory() {
	}

	@Override
	public ArrayList<News> selectByChannel(int channelId, int start, int limit) {
		try {
			List<News> newss=db.findAll(Selector.from(News.class).where(WhereBuilder.b("channelId", "=", channelId)).limit(limit).offset(start).orderBy("time", true));
			return  (ArrayList<News>) newss;
			
		} catch (DbException e) {
			e.printStackTrace();
		};
		return null;
	}

	@Override
	public ArrayList<News> selectByChannel(int channelId, int start, int limit, long number) {
		try {
			List<News> newss=db.findAll(Selector.from(News.class).where(WhereBuilder.b("channelId", "=", channelId).and("time","<",number)).limit(limit).offset(start));
			return  (ArrayList<News>) newss;
			
		} catch (DbException e) {
			e.printStackTrace();
		};
		return null;
	}
}
