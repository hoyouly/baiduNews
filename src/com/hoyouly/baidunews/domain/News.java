package com.hoyouly.baidunews.domain;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.text.TextUtils;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 
 <item> <title><![CDATA[父母接力捐肾救子遭拒 母亲的肾已经在儿体内坏死]]></title>
 * <link><![CDATA[http:
 * //society.people.com.cn/n/2014/0409/c136657-24856076.html]]></link>
 * <pubDate><![CDATA[2014-04-09T06:02:27.000Z]]></pubDate>
 * <source><![CDATA[人民网]]></source> <author><![CDATA[人民网]]></author>
 * <description> //省略 </description> </item>
 * 
 */

@SuppressWarnings("serial")
@Table(name = "news")
public class News implements Serializable {

	public static final int LIMIT_SIZE = 10;
	public static final String LIMIT = "0,10";

	@Column(column = "_id")
	private long _id;

	@Column(column = "title")
	private String title;// 标题

	@Column(column = "link")
	private String link;// 连接

	@Column(column = "pubDate")
	private String pubDate;// 发布日期

	@Column(column = "source")
	private String source;// 来源

	@Column(column = "author")
	private String author;// 作者

	@Column(column = "description")
	private String description;// 描述

	@Column(column = "imageDownloadUrl")
	private String imageDownloadUrl;// 下载图片地址

	@Column(column = "imageLink")
	private String imageLink;// 点击图片的链接

	@Column(column = "imagePath")
	private String imagePath;// 本地存放路径

	@Column(column = "channelId")
	private int channelId;// 新闻类型

	@Column(column = "readStatus")
	private int readStatus;// 0 未读，1 已读
	
	@Column(column = "time")
	private long time ;
	
	public long getTime() {
		return time;
	}
	
	public void setTime() {
		String str=getPubDate().replace("-", "").replace(" ", "").replace(":", "").replace(".", "");
		this.time = Long.valueOf(str);;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getPubDate() {
		pubDate = pubDate.replaceAll("T", " ").replaceAll("000Z", "");
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		if (description != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(description);
			description = m.replaceAll("");
		}
		return description.replaceAll("<a", "<a ").replaceAll("<font", "<font ").replaceAll("<img", "<img ");
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageDownloadUrl() {
		String des = getDescription();
		Document doc = Jsoup.parseBodyFragment(des);
		Element body = doc.body();
		Elements elements = body.getElementsByTag("img");

		for (Element element : elements) {
			String src = element.attr("src");
			if (!TextUtils.isEmpty(src)) {
				return src;
			}
		}
		return "";
	}

	public void setImageDownloadUrl(String imageDownloadUrl) {
		this.imageDownloadUrl = imageDownloadUrl;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getReadStatus() {
		return readStatus;
	}

	public void setReadStatus(int readStatus) {
		this.readStatus = readStatus;
	}

	@Override
	public String toString() {
		return "News [_id=" + _id + ", title=" + title + ", link=" + link + ", " + //
				"pubDate=" + pubDate + ", source=" + source + ", author=" + author//
				+ ", description=" + description + ", imageDownloadUrl=" + imageDownloadUrl + //
				", imageLink=" + imageLink + ", imagePath=" + imagePath  + ", time=" + time + ", channelId=" + channelId //
				+ ", readStatus=" + readStatus + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + ((source == null) ? 0 : source.hashCode()) + ((title == null) ? 0 : title.hashCode());
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (o instanceof News) {
			News item = (News) o;
			if (item.channelId == this.channelId && (item.title.equals(this.title) || item.link.equals(this.link))) {
				return true;
			}
		}
		return false;
	}
}
