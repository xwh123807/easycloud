package org.easycloud.platform.metadata.define;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.core.domain.EntityAction;
import org.easycloud.platform.core.utils.AssertUtil;
import org.easycloud.platform.metadata.annotation.FilterView;
import org.easycloud.platform.metadata.annotation.ListView;
import org.easycloud.platform.metadata.annotation.OrderView;
import org.easycloud.platform.metadata.internal.EntityMetaData;
import org.easycloud.platform.metadata.internal.EntityMetaDataConstants;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.Assert;

/**
 * 实体列表定义
 * 
 * @author xiangwanhong
 *
 */
public class ListDefinition extends BaseDenifition {
	/**
	 * 标题，显示在Box header上
	 */
	private String title;
	/**
	 * 表格表头，一般用于复杂表头
	 */
	private String header;
	/**
	 * 列表数据源主实体名称
	 */
	private String entityName;
	/**
	 * 子表属性，子表场景才需要赋值
	 */
	private String subTableAttr;
	/**
	 * 字段列表定义
	 */
	private FieldDefinition[] fields;
	/**
	 * 过滤条件定义
	 */
	private FilterDefinition[] filters;
	/**
	 * 排序定义
	 */
	private OrderDefinition[] orders;
	/**
	 * 表格取数是否异步Ajax处理，如果是，则由客户端再次发送请求获取数据，用于大数据量的分页；如果为否，页面直接输出全部数据
	 */
	private boolean serverSideMode;
	/**
	 * 
	 */
	private boolean enableActions;
	/**
	 * 实体列表操作集
	 */
	private EntityAction[] listActions;
	/**
	 * 实体项操作集
	 */
	private EntityAction[] itemActions;
	/**
	 * 
	 * @return
	 */
	private String labelField;
	/**
	 * 列表显示样式
	 */
	private ListStyle listStyle;

	public ListDefinition(final Object parent, final String name, final String title) {
		super(parent);
		setName(name);
		setTitle(title);
	}

	public ListDefinition(FieldDefinition[] fields, String entityName, String subTableAttr) {
		super(null);
		setFields(fields);
		setEntityName(entityName);
		setSubTableAttr(subTableAttr);
	}

	public ListDefinition(EntityMetaData metaData) {
		super(metaData);
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public FilterDefinition[] getFilters() {
		return filters;
	}

	public void setFilters(FilterDefinition[] filters) {
		this.filters = filters;
	}

	public boolean isServerSideMode() {
		return serverSideMode;
	}

	public void setServerSideMode(boolean serverSideMode) {
		this.serverSideMode = serverSideMode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 判断当时是否为子表应用场景
	 * 
	 * @return
	 */
	public boolean isSubTableScene() {
		return StringUtils.isNotBlank(getSubTableAttr());
	}

	public String getSubTableAttr() {
		return subTableAttr;
	}

	public void setSubTableAttr(String subTableAttr) {
		this.subTableAttr = subTableAttr;
	}

	public boolean isEnableActions() {
		return enableActions;
	}

	public void setEnableActions(boolean enableActions) {
		this.enableActions = enableActions;
	}

	public EntityAction[] getListActions() {
		return listActions;
	}

	public void setListActions(EntityAction[] listActions) {
		this.listActions = listActions;
	}

	public EntityAction[] getItemActions() {
		return itemActions;
	}

	public void setItemActions(EntityAction[] itemActions) {
		this.itemActions = itemActions;
	}

	public String getLabelField() {
		return labelField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

	public OrderDefinition[] getOrders() {
		return orders;
	}

	public void setOrders(OrderDefinition[] orders) {
		this.orders = orders;
	}

	/**
	 * 增加排序
	 * 
	 * @param orderDefinition
	 */
	public void addOrder(OrderDefinition orderDefinition) {
		AssertUtil.parameterEmpty(orderDefinition, "orderDefinition");
		if (getOrders() == null) {
			setOrders(new OrderDefinition[] { orderDefinition });
		} else {
			ArrayUtils.add(this.orders, orderDefinition);
		}
	}

	/**
	 * 根据排序定义，构建Sort
	 * 
	 * @return
	 */
	public Sort getSort() {
		if (ArrayUtils.isNotEmpty(getOrders())) {
			List<Order> orders = new ArrayList<>();
			for (OrderDefinition orderDefinition : getOrders()) {
				Direction direction = OrderType.ASC.equals(orderDefinition.getOrderType()) ? Direction.ASC
						: Direction.DESC;
				Order order = new Order(direction, orderDefinition.getName());
				orders.add(order);
			}
			Sort sort = new Sort(orders);
			return sort;
		} else {
			return null;
		}
	}

	public FieldDefinition[] getFields() {
		return fields;
	}

	public void setFields(FieldDefinition[] fields) {
		this.fields = fields;
	}

	public FieldDefinition[] getFields(boolean printMode) {
		if (printMode) {
			return ArrayUtils.remove(fields, fields.length - 1);
		} else {
			return fields;
		}
	}

	public ListStyle getListStyle() {
		return listStyle;
	}

	public void setListStyle(ListStyle listStyle) {
		this.listStyle = listStyle;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@Override
	public String toString() {
		return "name: " + getName() + ", fields: [" + FieldDefinition.getNames(getFields()) + "]";
	}

	/**
	 * 构建实体列表查询定义
	 * 
	 * @param metaData
	 * @param listView
	 * @return
	 */
	public static ListDefinition buildListDefinition(EntityMetaData metaData, ListView listView) {
		ListDefinition listDefinition = new ListDefinition(metaData, listView.name(), listView.title());

		if (StringUtils.isBlank(listDefinition.getName())) {
			listDefinition.setName(EntityMetaDataConstants.DEFAULT_NAME);
		}
		if (StringUtils.isNotBlank(listView.title())) {
			listDefinition.setTitle(listView.title());
		} else {
			listDefinition.setTitle(metaData.getTableDefinition().getTitle());
		}
		if (StringUtils.isNotBlank(listView.labelField())) {
			listDefinition.setLabelField(listView.labelField());
		} else {
			listDefinition.setLabelField(metaData.getTableDefinition().getLabelField());
		}
		listDefinition.setHeader(listView.header());
		listDefinition.setServerSideMode(listView.serverSideMode());
		listDefinition.setEntityName(metaData.getEntityName());
		listDefinition.setListStyle(listView.listStyle());
		listDefinition.setListActions(listView.listActions());
		listDefinition.setItemActions(listView.itemActions());

		// 更新字段定义
		FieldDefinition[] fields;
		if (ArrayUtils.isNotEmpty(listView.fields())) {
			// 有配置ListView注解的，按配置的字段显示
			fields = metaData.getFields(metaData, listView.fields());
			// 增加操作字段
			if (ArrayUtils.isNotEmpty(listDefinition.getItemActions())) {
				//fields = ArrayUtils.add(fields, new EntityActionField(listDefinition.getItemActions()));
			}
		} else {
			// 没有配置ListView注解时，取全部字段
			fields = metaData.getAllFields();
		}
		listDefinition.setFields(fields);

		// 设置过滤条件
		updateMetaDataListViewFilterDefinitions(metaData, listView, listDefinition);
		// 更新排序定义
		updateMetaDataListViewOrderDefinitions(metaData, listView, listDefinition);
		return listDefinition;
	}

	/**
	 * 更新列表过滤器定义
	 * 
	 * @param metaData
	 * @param listView
	 * @param listDefinition
	 */
	private static void updateMetaDataListViewFilterDefinitions(final EntityMetaData metaData, final ListView listView,
			final ListDefinition listDefinition) {
		// 更新过滤条件定义
		List<FilterDefinition> filters = new ArrayList<>();
		if (ArrayUtils.isNotEmpty(listView.filters())) {
			for (FilterView filterView : listView.filters()) {
				FieldDefinition field = metaData.getField(filterView.field());
				Assert.notNull(field);
				FilterDefinition filter = new FilterDefinition(field, filterView.operator());
				filter.setShow(filterView.show());
				filter.setValue(filterView.value());
				filters.add(filter);
			}
		}
		listDefinition.setFilters(filters.toArray(new FilterDefinition[] {}));
	}

	/**
	 * 更新列表排序定义
	 * 
	 * @param metaData
	 * @param listView
	 * @param listDefinition
	 */
	private static void updateMetaDataListViewOrderDefinitions(final EntityMetaData metaData, ListView listView,
			ListDefinition listDefinition) {
		List<OrderDefinition> orders = new ArrayList<>();
		if (ArrayUtils.isNotEmpty(listView.orders())) {
			for (OrderView orderView : listView.orders()) {
				FieldDefinition field = metaData.getField(orderView.field());
				Assert.notNull(field);
				OrderDefinition orderDefinition = new OrderDefinition(listDefinition, orderView.field(),
						orderView.orderType());
				orders.add(orderDefinition);
			}
			listDefinition.setOrders(orders.toArray(new OrderDefinition[] {}));
		} else {
			updateBaseEntityDefaultOrder(listDefinition);
		}
	}

	/**
	 * 添加默认过滤器
	 * 
	 * @param listDefinition
	 */
	private static void updateBaseEntityDefaultOrder(ListDefinition listDefinition) {
		// 增加排序字段,且实体为SBaseEntity，自定添加按最后修改时间降序排序
		/*
		if (((EntityMetaData) listDefinition.getParent()).newEntityInstance() instanceof SBaseEntity) {
			listDefinition.addOrder(new OrderDefinition(listDefinition, "updated", OrderType.DESC));
		}
		*/
	}
	
	/**
	 * 构建默认包含全部字段的ListDefinition
	 * 
	 * @param metaData
	 * @return
	 */
	public static ListDefinition buildDefaultListDefinition(final EntityMetaData metaData, final String name) {
		ListDefinition listDefinition = new ListDefinition(metaData, name, metaData.getTableDefinition().getTitle());
		listDefinition.setName(name);
		listDefinition.setEntityName(metaData.getEntityName());
		listDefinition.setLabelField(metaData.getTableDefinition().getLabelField());
		listDefinition.setFields(metaData.getAllFields());
		listDefinition.setServerSideMode(EntityMetaDataConstants.DEFAULT_SERVER_MODE);
		listDefinition.setListStyle(EntityMetaDataConstants.DEFAULT_LIST_STYLE);
		listDefinition.setListActions(EntityMetaDataConstants.DEFAULT_ENTITY_LIST_ACTIONS);
		listDefinition.setItemActions(EntityMetaDataConstants.DEFAULT_ENTITY_ITEM_ACTIONS);
		List<FilterDefinition> filters = new ArrayList<>();
		for (FieldDefinition field : metaData.getAllFields()) {
			FilterDefinition filter = new FilterDefinition(field, SQLOperator.EQUAL);
			filters.add(filter);
		}
		listDefinition.setFilters(filters.toArray(new FilterDefinition[] {}));
		updateBaseEntityDefaultOrder(listDefinition);
		// 增加操作字段
		if (ArrayUtils.isNotEmpty(listDefinition.getItemActions())) {
//			FieldDefinition[] fields = ArrayUtils.add(listDefinition.getFields(),
//					new EntityActionField(listDefinition.getItemActions()));
//			listDefinition.setFields(fields);
		}
		return listDefinition;
	}
	
	public void validate(){
		Assert.hasLength(getEntityName());
		Assert.notEmpty(getFields());
	}
}
