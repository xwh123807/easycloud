package org.easycloud.platform.visualpage.ui.control;

import org.apache.commons.lang3.ArrayUtils;
import org.easycloud.platform.metadata.annotation.view.EntityAction;
import org.easycloud.platform.visualpage.ui.EntityActionInfo;
import org.easycloud.platform.visualpage.ui.ViewType;
import org.easycloud.platform.visualpage.ui.view.BaseRender;

/**
 * 实体操作工具条
 * 
 * @author xiangwanhong
 *
 */
public class EntityActionsFieldRender extends BaseRender {

	private EntityAction[] actionDenifitions;
	private EntityActionInfo actionInfo;

	public EntityActionsFieldRender(ViewType viewType, EntityAction[] actionDenifitions, EntityActionInfo actionInfo) {
		super(viewType);
		this.setActionDenifitions(actionDenifitions);
		this.setActionInfo(actionInfo);
	}

	@Override
	public String html() {
		return getActionInfo().isSubTableScene() ? getSubEntityActionsBar() : getEntityActionsBar();
	}

	/**
	 * 实体主表操作工具条
	 * 
	 * @return
	 */
	public String getEntityActionsBar() {
		StringBuffer buffer = new StringBuffer();
		if (ArrayUtils.isNotEmpty(getActionDenifitions())) {
			for (EntityAction acitonDenifition : getActionDenifitions()) {
				buffer.append(EntityLinkUtil.getEntityActionLinkHtml(acitonDenifition, getActionInfo().tableName,
						getActionInfo().uid, null, getActionInfo().view, true, false));
			}
		}
		return buffer.toString();
	}

	/**
	 * 实体子表操作工具条
	 * 
	 * @return
	 */
	public String getSubEntityActionsBar() {
		StringBuffer buffer = new StringBuffer();
		if (ArrayUtils.isNotEmpty(getActionDenifitions())) {
			for (EntityAction acitonDenifition : getActionDenifitions()) {
				buffer.append(EntityLinkUtil.getSubEntityActionLinkHtml(acitonDenifition, getActionInfo().tableName,
						getActionInfo().uid, getActionInfo().subTableAttr, getActionInfo().subUid, null,
						getActionInfo().view, true, false));
			}
		}
		return buffer.toString();
	}

	public EntityAction[] getActionDenifitions() {
		return actionDenifitions;
	}

	public void setActionDenifitions(EntityAction[] actionDenifitions) {
		this.actionDenifitions = actionDenifitions;
	}

	public EntityActionInfo getActionInfo() {
		return actionInfo;
	}

	public void setActionInfo(EntityActionInfo actionInfo) {
		this.actionInfo = actionInfo;
	}

}
