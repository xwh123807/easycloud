package org.myfly.platform.core.visualpage.ui.control.component;

import org.myfly.platform.core.visualpage.ui.ViewType;

/**
 * 社交化组件，和实体绑定使用。可以记录电话、发送邮件、创建任务
 * 
 * @author xiangwanhong
 *
 */
public class SocialRender extends BaseComponentRender {

	public SocialRender(ViewType viewType) {
		super(viewType);
	}

	@Override
	public String getComponentName() {
		return "social";
	}

}
