package com.hoyouly.baidunews.util;

import android.content.Context;

import com.hoyouly.baidunews.dao.DBHelper;
import com.hoyouly.baidunews.dao.impl.NewsDaoImpl;
import com.hoyouly.baidunews.domain.News;
import com.lidroid.xutils.exception.DbException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class XmlUtils {

	public static ArrayList<News> pullParseXML(String rssXml, int channelId, Context context) {

		// 构建XmlPullParserFactory
		try {
			XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
			// 获取XmlPullParser的实例
			XmlPullParser xmlPullParser = pullParserFactory.newPullParser();
			// 设置输入流 xml文件
			xmlPullParser.setInput(new ByteArrayInputStream(rssXml.getBytes("gbk")), "gbk");
			// 开始
            return ParserXML(channelId, context, xmlPullParser);
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (DbException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

    public static ArrayList<News> ParserXML(int channelId, Context context, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException, DbException {
        ArrayList<News> list = null;
        News item = null;
        int eventType = xmlPullParser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String nodeName = xmlPullParser.getName();
            switch (eventType) {
            // 文档开始
            case XmlPullParser.START_DOCUMENT:
                list = new ArrayList<News>();
                break;
            // 开始节点
            case XmlPullParser.START_TAG:
                // 判断如果其实节点为item
                if ("item".equals(nodeName)) {
                    // 实例化NewsItem对象
                    item = new News();
                    item.setChannelId(channelId);// 设置该新闻的频道id
                } else if (item != null) {
                    if ("title".equals(nodeName)) {
                        String str = xmlPullParser.nextText();
                        item.setTitle(str);
                    } else if ("link".equals(nodeName)) {
                        item.setLink(xmlPullParser.nextText());
                        News nitem = new NewsDaoImpl(context).selectByArgs(channelId, item.getLink());
                        if (nitem != null) {
                            list.add(nitem);
                            item = null;
                        }
                    } else if ("pubDate".equals(nodeName)) {
                        item.setPubDate(xmlPullParser.nextText());
                        item.setTime();
                    } else if ("author".equals(nodeName)) {
                        item.setAuthor(xmlPullParser.nextText());
                    } else if ("description".equals(nodeName)) {
                        item.setDescription(xmlPullParser.nextText());
                    }
                }
                break;
            // 结束节点
            case XmlPullParser.END_TAG:
                if ("item".equals(nodeName) && item != null) {
                    DBHelper.getDBUtils(context).save(item);
                    list.add(item);
                    item = null;
                }
                break;
            }
            eventType = xmlPullParser.next();
        }
        return list;
    }

}
