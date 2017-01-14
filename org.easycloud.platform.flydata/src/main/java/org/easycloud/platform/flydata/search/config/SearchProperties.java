package org.easycloud.platform.flydata.search.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "org.myfly.platform.search")
public class SearchProperties {
	private String attachmentIndexPath = "/tmp/attachementindex";

	public String getAttachmentIndexPath() {
		return attachmentIndexPath;
	}

	public void setAttachmentIndexPath(String attachmentIndexPath) {
		this.attachmentIndexPath = attachmentIndexPath;
	}
}
