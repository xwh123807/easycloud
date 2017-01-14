package org.easycloud.platform.metadata.define.view;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.metadata.annotation.view.SubTableView;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.springframework.util.Assert;

/**
 * 子表视图定义，暂时只支持引用
 * 
 * @author xiangwanhong
 *
 */
public class SubTableDefinition extends ListDefinition {
	/**
	 * 引用名称
	 */
	private String refName;

	/**
	 * 
	 * @param parent
	 *            父对象
	 * @param title
	 *            标题
	 */
	public SubTableDefinition(Object parent, String title) {
		super(parent, null, title);
	}

	/**
	 * 实体子表直接引用子实体列表视图定义
	 * 
	 * @param metaData
	 * @param subTableField
	 * @param refName
	 * @return
	 */
	public static SubTableDefinition buildSubTable(EntityMetaData metaData, FieldDefinition subTableField,
			String refName) {
		Assert.notNull(subTableField);
		Assert.notNull(refName);
		EntityMetaData subEntityMetaData = null;
		if (subTableField.getRelationClass() != null
				&& subTableField.getRelationClass().equals(metaData.getEntityClass().getName())) {
			// 自关联时
			subEntityMetaData = metaData;
		} else {
			subEntityMetaData = metaData.getSubEntityMetaData(subTableField.getName());
		}
		// 从子表实体listviews定义中获取listview定义，并复制到当前子表定义中
		ListDefinition listDefinition = subEntityMetaData.getListDefinition(refName);

		SubTableDefinition subTable = new SubTableDefinition(metaData, subTableField.getLabel());
		subTable.setRefName(refName);
		subTable.setEntityName(metaData.getEntityName());
		subTable.setSubTableAttr(subTableField.getName());
		subTable.setLabelField(subEntityMetaData.getTableDefinition().getLabelField());
		subTable.setTitle(listDefinition.getTitle());
		subTable.setHeader(listDefinition.getHeader());
		subTable.setServerSideMode(listDefinition.isServerSideMode());
		subTable.setFields(listDefinition.getFields());
		subTable.setFilters(listDefinition.getFilters());
		subTable.setListStyle(listDefinition.getListStyle());
		subTable.setListActions(listDefinition.getListActions());
		subTable.setItemActions(listDefinition.getItemActions());
		return subTable;
	}

	/**
	 * 实体子表引用子实体列表视图定义，再覆盖自定义内容
	 * 
	 * @param metaData
	 *            主实体元模型
	 * @param subTableField
	 *            子表字段
	 * @param refName
	 *            视图名称
	 * @return
	 */
	public static SubTableDefinition buildSubTable(EntityMetaData metaData, SubTableView subTableView) {
		FieldDefinition subTableField = metaData.getField(subTableView.tableAttr());
		Assert.notNull(subTableField, "实体[" + metaData.getEntityName() + "]不存在子表属性[" + subTableView.tableAttr() + "]");
		SubTableDefinition subTable = buildSubTable(metaData, subTableField, subTableView.refName());

		// 处理可由子表定义覆盖的部分
		if (!ListStyle.NONE.equals(subTableView.listStyle())) {
			subTable.setListStyle(subTableView.listStyle());
		}

		return subTable;
	}

	/**
	 * 按SubTableView注解构建SubTableDefinition
	 * 
	 * @param metaData
	 * @param subTableView
	 * @return
	 */
	private static SubTableDefinition buildSubTable2(EntityMetaData metaData, SubTableView subTableView,
			String viewName) {
		AssertUtil.parameterNotEmpty(null, metaData, subTableView);
		SubTableDefinition subTable = new SubTableDefinition(metaData, subTableView.title());
		subTable.setEntityName(metaData.getEntityName());
		subTable.setHeader(subTableView.header());
		subTable.setServerSideMode(subTableView.serverSideMode());
		subTable.setSubTableAttr(subTableView.tableAttr());
		AssertUtil.parameterEmpty(subTable.getSubTableAttr(), "subTableView.tableAttr", "必须制定子表视图tableAttr属性");

		subTable.setListStyle(subTableView.listStyle());
		subTable.setListActions(subTableView.listActions());
		subTable.setItemActions(subTableView.itemActions());

		FieldDefinition subTableField = metaData.getField(subTableView.tableAttr());
		AssertUtil.parameterEmpty(subTableField, "subTableField",
				"实体[" + metaData.getEntityClass().getName() + "]不存在属性[" + subTableView.tableAttr() + "]");
		if (StringUtils.isBlank(subTable.getTitle())) {
			subTable.setTitle(subTableField.getLabel());
		}
		EntityMetaData subEntityMetaData = null;
		if (subTableField.getRelationClass().equals(metaData.getEntityClass().getName())) {
			subEntityMetaData = metaData;
		} else {
			subEntityMetaData = metaData.getSubEntityMetaData(subTableField.getName());
		}
		AssertUtil.parameterEmpty(subEntityMetaData, "subEntityMetaData");
		if (StringUtils.isNotBlank(subTableView.labelField())) {
			subTable.setLabelField(subTableView.labelField());
		} else {
			subTable.setLabelField(subEntityMetaData.getTableDefinition().getLabelField());
		}

		// 更新字段集
		FieldDefinition[] subFields;
		if (ArrayUtils.isNotEmpty(subTableView.fields())) {
			// 指定子表显示字段
			subFields = subEntityMetaData.getFields(subTableView.fields());
			// 增加操作字段
			if (ArrayUtils.isNotEmpty(subTable.getItemActions())) {
				//subFields = ArrayUtils.add(subFields, new EntityActionField(subTable.getItemActions()));
			}
		} else {
			// 先判断子表是否有定义ListView
			if (subEntityMetaData.getListDefinitions() != null
					&& ArrayUtils.isNotEmpty(subEntityMetaData.getListDefinition(viewName).getFields())) {
				// 如果有定义ListView，则取
				subFields = subEntityMetaData.getListDefinition(viewName).getFields();
			} else {
				// 如果没有定义ListView，则取子表全部字段
				subFields = subEntityMetaData.getAllFields();
				// 增加操作字段
				if (ArrayUtils.isNotEmpty(subTable.getItemActions())) {
					//subFields = ArrayUtils.add(subFields, new EntityActionField(subTable.getItemActions()));
				}
			}
		}
		subTable.setFields(subFields);
		return subTable;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	@Override
	public void validate() {
		super.validate();
		Assert.hasLength(getSubTableAttr());
	}
}
