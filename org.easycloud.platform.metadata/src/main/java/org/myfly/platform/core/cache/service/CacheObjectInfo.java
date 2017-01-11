package org.myfly.platform.core.cache.service;

import java.util.Date;

import org.myfly.platform.core.metadata.annotation.ObjectListView;
import org.myfly.platform.core.metadata.annotation.PropertyView;

@ObjectListView(title = "缓存", linkField = "value", linkUrl = "/vp/template/$!{objitem.value}", fields = {
		@PropertyView(name = "key", title = "KEY"), @PropertyView(name = "value", title = "缓存对象"),
		@PropertyView(name = "hitCount", title = "命中次数"), @PropertyView(name = "creationTime", title = "创建时间"),
		@PropertyView(name = "lastAccessTime", title = "最后访问时间") })
public class CacheObjectInfo {
	/**
	 * 键值
	 */
	private String key;
	private Object value;
	private long hitCount;
	private Date creationTime;
	private Date lastAccessTime;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public long getHitCount() {
		return hitCount;
	}

	public void setHitCount(long hitCount) {
		this.hitCount = hitCount;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
}
