package com.hoyouly.baidunews.domain;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="channel")
public class Channel {

	@Column(column="id")
	private int id;
	
	@Column(column="name")
	private String name;
	
	@Column(column="rssurl")
	private String rssurl;
	
	@Column(column="parentId")
	private int parentId;
	
	public String getRssurl() {
		return rssurl;
	}

	public void setRssurl(String rssurl) {
		this.rssurl = rssurl;
	}

	public Channel() {
		super();
	}

	public Channel(int id, String name, String rssurl, int parentId) {
		super();
		this.id = id;
		this.name = name;
		this.rssurl = rssurl;
		this.parentId = parentId;
	}
	
	public Channel(String name, String rssurl, int parentId) {
		super();
		this.name = name;
		this.parentId = parentId;
		this.rssurl = rssurl;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	@Override
	public String toString() {
		return "Channel [id=" + id + ", name=" + name + ", rssurl=" + rssurl + ", parentId=" + parentId + "]";
	}

}
