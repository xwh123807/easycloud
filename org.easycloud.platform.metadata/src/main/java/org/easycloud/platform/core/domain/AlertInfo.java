package org.easycloud.platform.core.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 警告信息
 * 
 * @author xiangwanhong
 *
 */
public class AlertInfo {
	/**
	 * 警告级别
	 */
	private AlertLevel level;
	/**
	 * 主标题
	 */
	private String title;
	/**
	 * 副标题
	 */
	private String subTitle;
	/**
	 * 详细内容
	 */
	private String[] messages;

	public AlertInfo(final AlertLevel level, final String title, final String subTitle, final String... messages) {
		setLevel(level);
		setTitle(title);
		setSubTitle(subTitle);
		setMessages(messages);
	}

	public AlertInfo(final AlertLevel level, final String title) {
		setLevel(level);
		setTitle(title);
	}
	
	public AlertInfo(final AlertLevel level, final String title, final String[] messages) {
		setLevel(level);
		setTitle(title);
		setMessages(messages);
	}

	/**
	 * 设置错误信息
	 * 
	 * @param title
	 * @param e
	 */
	public AlertInfo(final String title, final Exception e) {
		setLevel(AlertLevel.DANGER);
		setTitle(title);
		setSubTitle(e.getMessage());
		List<String> lines = new ArrayList<>();
		for (StackTraceElement element : e.getStackTrace()) {
			lines.add(element.toString());
		}
		setMessages(lines.toArray(new String[] {}));
	}

	public AlertLevel getLevel() {
		return level;
	}

	public void setLevel(AlertLevel level) {
		this.level = level;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String[] getMessages() {
		return messages;
	}

	public void setMessages(String[] messages) {
		this.messages = messages;
	}

	/**
	 * 使用之后，立即清除警告信息
	 */
	public void clear() {
		this.level = null;
		this.title = null;
		this.subTitle = null;
		this.messages = null;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		if (getLevel() != null) {
			buffer.append("level: " + getLevel().getTitle()).append("\n");
		}
		buffer.append("title: " + getTitle()).append("\n");
		if (StringUtils.isNotBlank(getSubTitle())) {
			buffer.append("subTitle: " + getSubTitle()).append("\n");
		}
		if (ArrayUtils.isNotEmpty(getMessages())) {
			buffer.append("messages: ");
			for (String message : getMessages()) {
				buffer.append("\t").append(message).append("\n");
			}
		}
		return buffer.toString();
	}
}
