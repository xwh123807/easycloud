package org.easycloud.platform.metadata.define;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.common.utils.AssertUtil;
import org.easycloud.platform.common.utils.ClassUtil;
import org.easycloud.platform.metadata.internal.EntityMetaData;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 实体主键定义
 * 
 * @author xiangwanhong
 *
 */
public class PKFieldDefinition extends BaseDenifition {
	/**
	 * 值不为空时，取值由此函数确定
	 */
	@JsonIgnore
	private GetFieldValueHandler getValueHandler;
	/**
	 * 设置实体函数
	 */
	@JsonIgnore
	private SetFieldValueHandler setValueHandler;
	/**
	 * 主键字段，已经按名称排好了顺序
	 */
	private FieldDefinition[] idFields;
	/**
	 * 复合主键类
	 */
	private Class<?> idClass;
	/**
	 * 主键类属性
	 */
	@JsonIgnore
	private Field[] idClassFields;
	@JsonIgnore
	private Method[] idClassGetMethods;
	@JsonIgnore
	private Method[] idClassSetMethods;

	/**
	 * 主键类型
	 */
	private KeyType keyType = KeyType.SINGLE;

	public enum KeyType {
		// 单@Id,单主键
		SINGLE,
		// @Embeddable，复合主键
		EMBED,
		// 多@Id,指定@IdClass，复合主键
		MULTIID
	}

	public PKFieldDefinition(Object owner) {
		super(owner);
	}

	public GetFieldValueHandler getGetValueHandler() {
		return getValueHandler;
	}

	public void setGetValueHandler(GetFieldValueHandler getValueHandler) {
		this.getValueHandler = getValueHandler;
	}

	public SetFieldValueHandler getSetValueHandler() {
		return setValueHandler;
	}

	public void setSetValueHandler(SetFieldValueHandler setValueHandler) {
		this.setValueHandler = setValueHandler;
	}

	public FieldDefinition[] getIdFields() {
		return idFields;
	}

	public void setIdFields(FieldDefinition[] idFields) {
		this.idFields = idFields;
		Arrays.sort(this.idFields, new Comparator<FieldDefinition>() {

			@Override
			public int compare(FieldDefinition o1, FieldDefinition o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
	}

	public Class<?> getIdClass() {
		return idClass;
	}

	public void setIdClass(Class<?> idClass) {
		this.idClass = idClass;
		List<Field> fields = new ArrayList<>();
		for (Field field : idClass.getDeclaredFields()) {
			// 属性以$开头的是代码覆盖工具添加的，需要排除
			if (!field.getName().equals("serialVersionUID") && !field.getName().startsWith("$")) {
				fields.add(field);
			}
		}
		idClassFields = fields.toArray(new Field[] {});
		Arrays.sort(idClassFields, new Comparator<Field>() {

			@Override
			public int compare(Field o1, Field o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		idClassGetMethods = new Method[idClassFields.length];
		idClassSetMethods = new Method[idClassFields.length];
		int index = 0;
		try {
			for (Field field : idClassFields) {
				String name = field.getName();
				name = name.substring(0, 1).toUpperCase() + name.substring(1);
				Method getMethod = idClass.getDeclaredMethod("get" + name);
				if (getMethod != null) {
					idClassGetMethods[index] = getMethod;
				}
				Method setMethod = idClass.getDeclaredMethod("set" + name, field.getType());
				if (setMethod != null) {
					idClassSetMethods[index] = setMethod;
				}
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO 代码覆盖测试报错
			// throw new IllegalArgumentException("获取主键类方法失败，错误信息：" +
			// e.getMessage());
		}
	}

	/**
	 * 判断是否联合主键
	 * 
	 * @return
	 */
	public boolean isComplexKey() {
		return KeyType.EMBED.equals(getKeyType()) || KeyType.MULTIID.equals(getKeyType());
	}

	public KeyType getKeyType() {
		return keyType;
	}

	public void setKeyType(KeyType keyType) {
		this.keyType = keyType;
	}

	public void updateKeyType() {
		if (idClass == null) {
			setKeyType(KeyType.SINGLE);
		} else if (getIdFields().length == 1) {
			setKeyType(KeyType.EMBED);
		} else {
			setKeyType(KeyType.MULTIID);
		}
	}

	/**
	 * 构建主键
	 * 
	 * @param uid
	 * @return
	 */
	public Serializable buildPK(String uid) {
		if (KeyType.SINGLE.equals(getKeyType())) {
			return (Serializable) ClassUtil.convert(uid, getIdFields()[0].getType());
		} else {
			return buildComplexPk(uid);
		}
	}

	/**
	 * 构建主键Map
	 * 
	 * @param uid
	 * @return
	 */
	public Map<String, Object> buildPKMap(String uid) {
		Map<String, Object> pkMap = new LinkedHashMap<>();
		if (KeyType.SINGLE.equals(getKeyType())) {
			FieldDefinition keyField = getIdFields()[0];
			pkMap.put(keyField.getName(), ClassUtil.convert(uid, keyField.getType()));
		} else {
			// 复合主键
			String[] keys = uid.split("_");
			AssertUtil.parameterInvalide(keys.length != getIdFields().length,
					"复合主键[" + uid + "]参数个数[" + getIdFields().length + "]不一致");
			int index = 0;
			for (FieldDefinition fieldDefinition : getIdFields()) {
				Object value = ClassUtil.convert(keys[index], fieldDefinition.getType());
				pkMap.put(fieldDefinition.getName(), value);
				index++;
			}
		}
		return pkMap;
	}

	/**
	 * TODO
	 * 
	 * @param keyParams
	 * @return
	 */
	public Serializable buildPK(Map<String, Object> keyParams) {
		if (keyParams.size() == 1) {
			return (Serializable) keyParams.values().toArray(new Object[] {})[0];
		} else {
			for (FieldDefinition field : idFields) {

			}
		}
		return null;
	}

	/**
	 * 构建复合主键
	 * 
	 * @param uid
	 * @return
	 */
	private Serializable buildComplexPk(String uid) {
		try {
			// 复合主键
			String[] keys = uid.split("_");
			Serializable pk = (Serializable) idClass.newInstance();
			int index = 0;
			AssertUtil.parameterInvalide(keys.length != idClassSetMethods.length,
					"复合主键[" + uid + "]参数个数[" + idClassSetMethods.length + "]不一致");
			for (Method method : idClassSetMethods) {
				Object value = ClassUtil.convert(keys[index], idClassFields[index].getType());
				method.invoke(pk, value);
				index++;
			}
			return pk;
		} catch (Exception e) {
			throw new IllegalArgumentException("构建复合主键失败，错误信息：" + e.getMessage());
		}
	}

	/**
	 * 获取主键值，并将结果转换为字符串，如果主键有多个属性，属性间用-符号分割
	 * 
	 * @param entity
	 * @return
	 */
	public String getPKValue(Object entity) {
		AssertUtil.parameterEmpty(entity, "entity");
		try {
			Object value = getFieldValue(entity);
			if (KeyType.MULTIID.equals(getKeyType())) {
				return (String) value;
			} else {
				return convertPKToString((Serializable) value);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("获取主键失败，错误信息：" + e.getMessage());
		}
	}

	public String getPKValueByFieldName(Map<String, Object> entity) {
		String pkValue = "";
		for (FieldDefinition field : getIdFields()) {
			pkValue += "_" + entity.get(field.getFieldName());
		}
		return pkValue.substring(1);
	}

	public Map<String, Object> getPKMapValue(Object entity) {
		AssertUtil.parameterEmpty(entity, "entity");
		Map<String, Object> result = new LinkedHashMap<>();
		if (getIdFields().length > 1) {
			// 在实体中@Id标示多个主键
			for (FieldDefinition field : getIdFields()) {
				Object value = field.getGetValueHandler().getFieldValue(entity);
				result.put(field.getName(), value);
			}
		} else {
			result.put(getIdFields()[0].getName(), getIdFields()[0].getGetValueHandler().getFieldValue(entity));
		}
		return result;
	}

	/**
	 * 为实体设置主键值，如果是复合主键，uid格式为key1_key2_key3
	 * 
	 * @param entity
	 * @param uid
	 */
	public void setPKValue(Object entity, String uid) {
		Serializable pkValue = buildPK(uid);
		setPKValue(entity, pkValue);
	}

	/**
	 * 设置实体主键值
	 * 
	 * @param entity
	 * @param pkValue
	 */
	public void setPKValue(Object entity, Serializable pkValue) {
		setFieldValue(entity, pkValue);
	}

	/**
	 * 实体主键转换为字符串
	 * 
	 * @param entityPk
	 * @return
	 */
	public String convertPKToString(Serializable entityPk) {
		try {
			if (KeyType.SINGLE.equals(getKeyType())) {
				return ClassUtil.convertValueToString(entityPk);
			} else {
				String result = "";
				for (Method method : idClassGetMethods) {
					result += "_" + ClassUtil.convertValueToString(method.invoke(entityPk));
				}
				return result.substring(1);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("实体主键转换为字符串失败，错误信息：" + e.getMessage());
		}
	}

	/**
	 * 获取实体主键值，由ComplexGetPKFieldValueHandler调用
	 * 
	 * @param entity
	 * @return
	 */
	public Object getFieldValue(Object entity) {
		if (getIdFields().length > 1) {
			// 在实体中@Id标示多个主键
			String result = "";
			for (FieldDefinition field : getIdFields()) {
				Object value = field.getGetValueHandler().getFieldValue(entity);
				result += "_" + ClassUtil.convertValueToString(value);
			}
			return result.substring(1);
		} else {
			return getIdFields()[0].getGetValueHandler().getFieldValue(entity);
		}
	}

	/**
	 * 设置实体主键值，由ComplexSetPKFieldValueHandler调用
	 * 
	 * @param entity
	 * @param value
	 */
	public void setFieldValue(Object entity, Object value) {
		try {
			if (getIdFields().length == 1) {
				// @Embeddable模式或Single模式
				getIdFields()[0].getSetValueHandler().setFieldValue(entity, value);
			} else {
				if (value instanceof String) {
					// 实体中多个@Id属性模式
					String[] values = ((String) value).split("_");
					int index = 0;
					for (FieldDefinition field : getIdFields()){
						field.getSetValueHandler().setFieldValue(entity, values[index]);
						index ++;
					}
				} else {
					// 主键为主键类时
					int index = 0;
					for (FieldDefinition field : getIdFields()) {
						Object value2 = idClassGetMethods[index].invoke(value);
						field.getSetValueHandler().setFieldValue(entity, value2);
						index++;
					}
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("设置主键失败，错误信息：" + e.getMessage());
		}
	}

	/**
	 * 创建新实体，并为主键赋值
	 * 
	 * @param uid
	 * @return
	 */
	public <T> T newEntity(String uid) {
		T entity = ((EntityMetaData) getParent()).newEntityInstance();
		setPKValue(entity, uid);
		return entity;
	}

	/**
	 * 创建新实体，并为主键赋值
	 * 
	 * @param uid
	 * @return
	 */
	public <T> T newEntity(Serializable pkValue) {
		T entity = ((EntityMetaData) getParent()).newEntityInstance();
		setPKValue(entity, pkValue);
		return entity;
	}

	public String[] getIdFieldNames() {
		String[] result = new String[getIdFields().length];
		for (int i = 0; i < result.length; i++) {
			result[i] = getIdFields()[i].getName();
		}
		return result;
	}

	@Override
	public String toString() {
		return "idFields: " + StringUtils.join(getIdFieldNames(), ",");
	}
	
	public void validate(){
		Assert.notEmpty(getIdFields());
		Assert.notNull(getKeyType());
	}
}
