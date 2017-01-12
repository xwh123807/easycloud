package org.easycloud.platform.core.metadata.define;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.core.domain.FieldDataType;
import org.easycloud.platform.core.metadata.internal.EntityMetaData;
import org.easycloud.platform.core.utils.AppUtil;
import org.easycloud.platform.core.utils.AssertUtil;
import org.easycloud.platform.core.utils.UUIDUtil;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.Assert;

public class FieldDefinition extends BaseDenifition {
	/**
	 * 标签
	 */
	private String label;

	/**
	 * 字段名称
	 */
	private String fieldName;
	/**
	 * 简档
	 */
	private String description;
	/**
	 * 是否必填
	 */
	private boolean required;
	/**
	 * 数据类型
	 */
	private FieldDataType dataType;
	/**
	 * 控件值
	 */
	private String value;
	/**
	 * 如果是枚举类型，存放枚举类型名；如果是关联关系，存放关联类名
	 */
	private String relationClass;
	/**
	 * 如果是查找关系、主子表关系时，存放关联表名
	 */
	private String relationTable;
	/**
	 * 如果是主子表关系，存放子表对应的字段<br>
	 */
	private FieldDefinition relationField;
	/**
	 * 最小长度
	 */
	private int minLength;
	/**
	 * 最大长度，如果是字符串类型时，取长度
	 */
	private int maxLength;
	/**
	 * 精度
	 */
	private int precision;
	/**
	 * 输入限制
	 */
	private String mask;
	/**
	 * 值不为空时，取值由此函数确定
	 */
	private GetFieldValueHandler getValueHandler;
	/**
	 * 设置实体函数
	 */
	private SetFieldValueHandler setValueHandler;
	/**
	 * 字段值类型
	 */
	private Class<?> type;
	/**
	 * 字段链接配置
	 */
	private String linkUrl;

	/**
	 * 只读，不能更改
	 */
	private boolean readonly;
	/**
	 * 为关联属性时，id转名称显示字段
	 */
	private String labelField;
	/**
	 * 主子表关系时：显示字段定义
	 */
	private FieldDefinition labelFieldDefinition;
	/**
	 * 查找关系时：查找表主键定义
	 */
	private PKFieldDefinition pkFieldDefinition;
	/**
	 * 是否为主键字段，如果是复合主键，则一个实体中有多个属性
	 */
	private boolean isIdField;

	/**
	 * get方法
	 */
	private Method getter;

	/**
	 * set方法
	 */
	private Method setter;
	/**
	 * 值自动生成器,@GenericGenerator
	 */
	private GenericGenerator generator;

	public FieldDefinition() {
		super(null);
	}

	public FieldDefinition(String label, String name) {
		super(null);
		setLabel(label);
		setName(name);
	}

	public FieldDefinition(String label, String name, FieldDataType dataType) {
		super(null);
		setLabel(label);
		setName(name);
		setDataType(dataType);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public FieldDataType getDataType() {
		return dataType;
	}

	public void setDataType(FieldDataType dataType) {
		this.dataType = dataType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRelationClass() {
		return relationClass;
	}

	public void setRelationClass(String relationClass) {
		this.relationClass = relationClass;
	}

	public String getRelationTable() {
		return relationTable;
	}

	public void setRelationTable(String relationTable) {
		this.relationTable = relationTable;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public GetFieldValueHandler getGetValueHandler() {
		return getValueHandler;
	}

	public void setGetValueHandler(GetFieldValueHandler getValueHandler) {
		this.getValueHandler = getValueHandler;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public void setRelationField(FieldDefinition relationField) {
		this.relationField = relationField;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType(Class<?> type) {
		this.type = type;
	}

	public SetFieldValueHandler getSetValueHandler() {
		return setValueHandler;
	}

	public void setSetValueHandler(SetFieldValueHandler setValueHandler) {
		this.setValueHandler = setValueHandler;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

	@Override
	public String toString() {
		return "label: " + getLabel() + "; name: " + getName() + "; dataType: " + getDataType() + "; fieldName: "
				+ getFieldName();
	}

	public String getLabelField() {
		return labelField;
	}

	public void setLabelField(String labelField) {
		this.labelField = labelField;
	}

	public boolean isIdField() {
		return isIdField;
	}

	public void setIdField(boolean isIdField) {
		this.isIdField = isIdField;
	}

	public Method getGetter() {
		return getter;
	}

	public void setGetter(Method getter) {
		this.getter = getter;
	}

	public Method getSetter() {
		return setter;
	}

	public void setSetter(Method setter) {
		this.setter = setter;
	}

	public GenericGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(GenericGenerator generator) {
		this.generator = generator;
	}

	/**
	 * 查找关系时，获取查找表显示字段定义
	 * 
	 * @return
	 */
	public FieldDefinition getLabelFieldDefinition() {
		if (labelFieldDefinition == null) {
			if (FieldDataType.SEARCHRELATION.equals(getDataType())
					|| FieldDataType.FLYSEARCHRELATION.equals(getDataType())
					|| FieldDataType.AUTORELATION.equals(getDataType())
					|| FieldDataType.FLYMDRELATION.equals(getDataType())) {
				if (StringUtils.isBlank(getLabelField())) {
					AssertUtil.parameterInvalide("field", "字段[" + getName() + "]没有配置LabelField字段");
				}
				EntityMetaData metaData = AppUtil.getEntityMataDataService().getEntityMetaData(getRelationClass());
				AssertUtil.parameterEmpty(metaData, "metaData", "没有名称为[" + getRelationClass() + "]的实体元模型");
				labelFieldDefinition = metaData.getField(getLabelField());
				AssertUtil.parameterEmpty(labelFieldDefinition, "labelFieldDefinition", "字段[" + getName()
						+ "]的LabelField属性配置错误，实体[" + getRelationClass() + "]中没有字段[" + getLabelField() + "]");
			} else {
				AssertUtil.parameterInvalide(true,
						"字段[" + getName() + "]不是关联属性，只有[" + FieldDataType.SEARCHRELATION.getTitle() + "和"
								+ FieldDataType.AUTORELATION.getTitle() + "]的字段才支持.");
			}
		}
		return labelFieldDefinition;
	}

	/**
	 * 查找关系时，获取查找表主键定义
	 * 
	 * @return
	 */
	public PKFieldDefinition getPKFieldDefinition() {
		if (pkFieldDefinition == null) {
			AssertUtil.parameterInvalide(!FieldDataType.SEARCHRELATION.equals(getDataType()),
					"字段[" + getName() + "]不是关联属性，只有[" + FieldDataType.SEARCHRELATION.getTitle() + "]的字段才支持.");
			pkFieldDefinition = AppUtil.getEntityMataDataService().getEntityMetaData(getRelationClass())
					.getPKFieldDefinition();
		}
		return pkFieldDefinition;
	}

	/**
	 * 为主子表关联字段时，获取关联属性
	 * 
	 * @return
	 */
	public FieldDefinition getRelationField() {
		if (FieldDataType.MDRELATION.equals(getDataType())) {
			if (relationField != null && relationField.getDataType() == null) {
				// 由于在元模型构造时存在循环调用，更改为需要时获取。 更新关联字段等信息
				AssertUtil.parameterEmpty(getRelationTable(), "getRelationTable()");
				EntityMetaData relEntityMetaData = AppUtil.getEntityMataDataService()
						.getEntityMetaData(getRelationTable());
				relationField = relEntityMetaData.getField(relationField.getName());
			}
		}
		return relationField;
	}

	public String getFieldName() {
		if (FieldDataType.SEARCHRELATION.equals(getDataType())) {
			// 复合外键，wid=wid;did=did；将属性名转换为字段名
			if (fieldName.startsWith("tmp|")) {
				if (fieldName.contains("=")) {
					String searchField = fieldName.substring(4);
					EntityMetaData metaData = getParent();
					EntityMetaData relEntityMetaData = AppUtil.getEntityMataDataService()
							.getEntityMetaData(getRelationTable());
					String[] items = searchField.split(";");
					String result = "";
					for (String item : items) {
						String name = metaData.getField(StringUtils.substringBefore(item, "=")).getFieldName();
						String value = relEntityMetaData.getField(StringUtils.substringAfter(item, "=")).getFieldName();
						result += ";" + name + "=" + value;
					}
					if (StringUtils.isNotBlank(result)) {
						result = result.substring(1);
					}
					fieldName = result;
				}
			}
		}
		return fieldName;
	}

	/**
	 * 生成值
	 * 
	 * @return
	 */
	public void generatedValue(Map<String, Object> entity) {
		if (getGenerator() != null) {
			if ("system-uuid".equals(getGenerator().name())) {
				// UUID
				entity.put(getFieldName(), UUIDUtil.newUUID());
			} else {
				throw new NotImplementedException();
			}
		} else {
			throw new IllegalArgumentException("字段必须有@GenericGenerator注解，才能调用generateValue方法.");
		}
	}

	private Boolean isFlyEntityForRelationClass;

	public Boolean relationClassIsFlyEntity() {
		if (isFlyEntityForRelationClass == null) {
			EntityMetaData relEntityMetaData = AppUtil.getEntityMataDataService().getEntityMetaData(getRelationTable());
			isFlyEntityForRelationClass = relEntityMetaData.isFlyEntity();
		}
		return isFlyEntityForRelationClass;
	}

	public static String getNames(FieldDefinition[] fields) {
		String buffer = "";
		for (FieldDefinition field : fields) {
			buffer += "," + field.getName();
		}
		if (buffer.length() > 0) {
			buffer = buffer.substring(1);
		}
		return buffer;
	}

	public void validate() {
		Assert.hasLength(getName());
		Assert.hasLength(getLabel());
		if (FieldDataType.FLYMDRELATION.equals(getDataType()) || FieldDataType.MDRELATION.equals(getDataType())) {
		} else {
			Assert.hasLength(getFieldName());
		}
		Assert.notNull(getDataType());
		Assert.notNull(getType());
	}
}
