package com.hoyouly.baidunews.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hoyouly.baidunews.R;
import com.hoyouly.baidunews.domain.News;
import com.hoyouly.baidunews.util.ImageLoader;

import java.util.List;

public class NewsListAdapter extends CommonAdapter<News> {
    private ImageLoader mImageLoader;

    public NewsListAdapter(Context context, List<News> datas, int layoutId) {
        super(context, datas, layoutId);
        mImageLoader=ImageLoader.getInstance();
    }


    @Override
    protected void convert(ViewHolder holder, News news) {
        holder.setText(R.id.news_title, news.getTitle()).setText(R.id.news_source, news.getAuthor()).setText(R.id.news_pub_date_time, news.getPubDate());
        TextView summary = holder.getView(R.id.news_summary);
        summary.setText(Html.fromHtml(news.getDescription().replaceAll("<img", "<a ")));
        ImageView iv=holder.getView(R.id.news_img);
        mImageLoader.loadImage(news.getImageDownloadUrl(), iv, news.getImagePath(), news.getChannelId(), news.get_id());
    }


    /**
     * 刷新数据
     *
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
