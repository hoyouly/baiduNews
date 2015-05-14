package com.hoyouly.baidunews.dao;

import java.util.ArrayList;

import com.hoyouly.baidunews.domain.News;

public interface NewsDao {

	/**
	 * 根据频道的id和link 查询相应的新闻
	 * @param channelId 频道id
	 * @param links 新闻link
	 * @return
	 */
	public News selectByArgs(int channelId, String links);
	
	/**
	 * 根据频道id 分批加载，从start开始，加载limit条数据
	 * @param channelId
	 * @param start 起始位置
	 * @param limit 限制
	 * @return
	 */
	public ArrayList<News> selectByChannel(int channelId, int start,int  limit);
	
	public ArrayList<News> selectByChannel(int channelId, int start,int  limit,long number);
	
	/**
	 * 关闭数据库
	 */
	public void destory();
	
}
