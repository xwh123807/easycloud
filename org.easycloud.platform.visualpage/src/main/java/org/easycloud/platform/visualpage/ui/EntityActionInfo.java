package org.easycloud.platform.visualpage.ui;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.annotation.view.EntityAction;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.define.entity.GetFieldValueHandler;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.ui.control.EntityActionsFieldRender;

/**
 * 实体操作信息结构
 * 
 * @author xiangwanhong
 *
 */
public class EntityActionInfo {
	/**
	 * 实体表名，子表场景下也是主表名
	 */
	public String tableName;
	/**
	 * 主表UID
	 */
	public String uid;
	/**
	 * 用于子表场景， 子表属性名
	 */
	public String subTableAttr;
	/**
	 * 子表UID
	 */
	public String subUid;
	/**
	 * 实体
	 */
	public Object entity;
	/**
	 * 显示名称
	 */
	public String text;
	/**
	 * 视图名称
	 */
	public String view;

	public EntityActionInfo(String tableName, String uid, String subTableAttr, String subUid, String text, String view,
			Object entity) {
		this.tableName = tableName;
		this.uid = uid;
		this.subTableAttr = subTableAttr;
		this.subUid = subUid;
		this.entity = entity;
		this.text = text;
		this.view = view;
	}

	/**
	 * 是否为子表场景
	 * 
	 * @return
	 */
	public boolean isSubTableScene() {
		return StringUtils.isNotBlank(subTableAttr);
	}

	/**
	 * 实体操作工具字段
	 * 
	 * @author xiangwanhong
	 *
	 */
	public static final class EntityActionField extends FieldDefinition {
		public EntityActionField(final EntityAction ...actionDenifitions) {
			setLabel("操作");
			setName("actions");
			setDataType(FieldDataType.ACTIONS);
			setGetValueHandler(new GetFieldValueHandler() {

				@Override
				public Object getFieldValue(Object obj) {
					if (obj instanceof EntityActionInfo) {
						EntityActionInfo entityActionInfo = (EntityActionInfo) obj;
						EntityActionsFieldRender render = new EntityActionsFieldRender(ViewType.VIEW, actionDenifitions,
								entityActionInfo);
						return render.html();
					} else {
						if (obj != null) {
							AssertUtil.parameterInvalide("(EntityActionInfo) obj",
									"参数必须是EntityActionInfo类型，实际是" + obj.getClass().getName());
						}
					}
					return obj;
				}
			});
		}
	}
}
