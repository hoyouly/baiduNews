package com.hoyouly.baidunews.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hoyouly.baidunews.R;
import com.hoyouly.baidunews.domain.News;
import com.hoyouly.baidunews.util.ImageLoader;

import java.util.ArrayList;

public class NewsListAdapter_old extends BaseAdapter {
	private LayoutInflater mInflater = null;
	private ArrayList<News> mItems;
	private ImageLoader mImageLoader;

	public NewsListAdapter_old(Context context) {
		mInflater = LayoutInflater.from(context);
		mImageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return mItems == null ? 0 : mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHodler viewHodler = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.news_item, null);
			viewHodler = new ViewHodler();
			viewHodler.newsImg = (ImageView) convertView.findViewById(R.id.news_img);
			viewHodler.newsTitle = (TextView) convertView.findViewById(R.id.news_title);
			viewHodler.newsSummary = (TextView) convertView.findViewById(R.id.news_summary);
			viewHodler.newsSource = (TextView) convertView.findViewById(R.id.news_source);
			viewHodler.newsPubDateTime = (TextView) convertView.findViewById(R.id.news_pub_date_time);
			convertView.setTag(viewHodler);
		} else {
			viewHodler = (ViewHodler) convertView.getTag();
		}

		News item = mItems.get(position);

		String imageDownloadUrl = item.getImageDownloadUrl();
		viewHodler.newsImg.setTag(imageDownloadUrl);
		viewHodler.newsTitle.setText(item.getTitle());
		viewHodler.newsSummary.setText(Html.fromHtml(item.getDescription().replaceAll("<img", "<a ")));
		viewHodler.newsSource.setText(item.getAuthor());
		viewHodler.newsPubDateTime.setText(item.getPubDate());
		viewHodler.newsImg.setTag(R.id.new_image_link_tag, item.getImageLink());
		if (item.getReadStatus() == 0) {
			viewHodler.newsTitle.setTextColor(0xff000000);
		} else {
			viewHodler.newsTitle.setTextColor(0xff9C9C9C);
		}
		mImageLoader.loadImage(imageDownloadUrl, viewHodler.newsImg, item.getImagePath(), item.getChannelId(), item.get_id());
		return convertView;
	}

	static class ViewHodler {
		ImageView newsImg;
		TextView newsTitle;
		TextView newsSummary;
		TextView newsSource;
		TextView newsPubDateTime;
	}

	/**
	 * 设置数据，下拉刷新也可以使用这个方法
	 * @param items
	 */
	public void setDate(ArrayList<News> items) {
		if (items == null) {
			return;
		}
		if (mItems == null) {
			mItems = items;
		} else {
			int size = items.size();
			if (size == 0) {
				return;
			}
			// 添加到集合最上边。下来刷新就是调用这个
			mItems.addAll(0, items);
		}
	}

	/**
	 * 添加数据，上拉加载更多的时候，调用该方法
	 * @param items
	 */
	public void addDate(ArrayList<News> items) {
		if (mItems == null) {
			return;
		}// 上拉加载更多
		mItems.addAll(items);
	}


	public ArrayList<News> getDate() {
		return mItems;
	}

	public void destory() {
		mImageLoader = null;
		mItems.clear();
		mItems = null;
	}

	/**
	 * 刷新数据
	 * @param listView
	 * @param isCut
	 */
	public void notifyDataSetChanged(ListView listView, boolean isCut) {
		listView.smoothScrollBy(0, 0);// 停止滚动
		notifyDataSetChanged();
	}
	
	public void notifyDataSetChanged(ListView listView) {
		if (getCount() > News.LIMIT_SIZE) {
			listView.smoothScrollBy(0, 0);// 停止滚动
			notifyDataSetChanged();
		}
	}
}
