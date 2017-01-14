package org.easycloud.platform.metadata.annotation.entity;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;

/**
 * 字段数据类型
 * 
 * @author xiangwanhong 数据类型 未选取任何内容 请选择以下数据类型之一。
 * 
 *         公式 根据您定义的公式表达式派生其值的只读字段。任何来源字段有所更改，该公式字段均将更新。 累计汇总
 *         在相关列表中显示字段的汇总值、最小值或最大值的只读字段，或在相关列表中列出的所有记录的记录数。 自动编号
 *         系统使用您定义的显示格式生成的序列编号。该编号对于每条新记录会自动递增。
 * 
 *         查找关系 创建一个将此对象链接到另一对象的关系。关系字段允许用户单击查找图标，以从弹出列表中选择值。另一对象是列表中值的源。
 *         主-详细信息关系 创建一个此对象（子级或"详细信息"）与另一对象（父级或"主"）之间的特殊父子关系类型，其中：
 *         所有详细信息记录的关系字段必填。 详细信息记录的所有权和共享由主记录确定。 当用户删除主记录时，将删除所有详细信息记录。
 *         您可以在主记录上创建累计汇总字段以汇总详细信息记录。 关系字段允许用户单击查找图标，以从弹出列表中选择值。主对象是列表中值的源。
 * 
 *         URL 允许用户输入任何有效的网址。当用户单击该字段时，该 URL 将在单独的浏览器窗口中打开。 百分比
 *         允许用户输入百分比数字（例如，"10"），并自动向数字添加百分号。 币种
 *         允许用户输入美元或其他币种金额，并将字段自动转换为币种金额格式。此功能在将数据导出到 Excel 或其它电子表格时非常有用。 文本区（长）
 *         允许用户输入多行文本，最多可输入 131,072 个字符。 地理定位 允许用户定义位置。包括精度和纬度组件，可用于计算距离。 电话
 *         允许用户输入任何电话号码。自动将其转换为电话号码格式。 电子邮件
 *         允许用户输入电子邮件地址，对其进行验证以确保格式正确。如果对于一个联系人和潜在客户指定了此字段，则用户单击“发送电子邮件”时可以选择地址。
 *         注意，自定义电子邮件地址无法用于批量电子邮件。 复选框 允许用户选择"真"（选取）或"假"（不选取）值。 文本区（RTF）
 *         允许用户输入格式化文本、添加图片和链接。最多有 131,072 个字符在分开行上。 加密文本
 *         允许用户输入任何字母和数字组合，并将它们以加密格式存储。 日期 允许用户输入日期或从弹出式日历中选择日期。 日期/时间
 *         允许用户输入日期和时间，或从弹出式日历中选择日期。当用户单击弹出式日历中的某个日期后，该日期和当前时间将输入到"日期/时间"字段。 数字
 *         允许用户输入任何数字。将删除前置零。 文本 允许用户输入任何字母和数字组合。 文本区 允许用户输入多行文本，最多可输入 255 个字符。
 *         选项列表 允许用户从定义的列表中选择值。 选项列表（多项选择） 允许用户从定义的列表中选择多个值。
 */
public enum FieldDataType implements BaseEnum {
	/**
	 * 未选取任何内容
	 */
	NONE("未指定", String.class),
	/**
	 * 32为uuid，全局唯一
	 */
	UUID("唯一", String.class),
	/**
	 * 公式 根据您定义的公式表达式派生其值的只读字段。任何来源字段有所更改，该公式字段均将更新。
	 */
	FORMULA("公式", String.class),
	/**
	 * 累计汇总 在相关列表中显示字段的汇总值、最小值或最大值的只读字段，或在相关列表中列出的所有记录的记录数。
	 */
	SUM("累计汇总", BigDecimal.class),
	/**
	 * 自动编号 系统使用您定义的显示格式生成的序列编号。该编号对于每条新记录会自动递增
	 */
	AUTOINC("自动编号", Long.class),
	/**
	 * 查找关系 创建一个将此对象链接到另一对象的关系。关系字段允许用户单击查找图标，以从弹出列表中选择值。另一对象是列表中值的源
	 */
	SEARCHRELATION("查找关系", String.class),
	/**
	 * 扩展查找关系 属性为关联实体的UID，通过@SearchRelation定义
	 */
	FLYSEARCHRELATION("扩展查找关系", String.class),
	/**
	 * 主-详细信息关系 创建一个此对象（子级或"详细信息"）与另一对象（父级或"主"）之间的特殊父子关系类型，其中： 所有详细信息记录的关系字段必填。
	 * 详细信息记录的所有权和共享由主记录确定。 当用户删除主记录时，将删除所有详细信息记录。 您可以在主记录上创建累计汇总字段以汇总详细信息记录。
	 * 关系字段允许用户单击查找图标，以从弹出列表中选择值。主对象是列表中值的源。
	 */
	MDRELATION("主从关系", List.class),
	/**
	 * 扩展主从关系，用于动态添加子表，如附件、备注
	 */
	FLYMDRELATION("扩展主从关系", List.class),
	/**
	 * URL 允许用户输入任何有效的网址。当用户单击该字段时，该 URL 将在单独的浏览器窗口中打开。
	 */
	URL("URL", String.class),
	/**
	 * 百分比 允许用户输入百分比数字（例如，"10"），并自动向数字添加百分号。
	 */
	PERCENT("百分比", Float.class),
	/**
	 * 币种 允许用户输入美元或其他币种金额，并将字段自动转换为币种金额格式。此功能在将数据导出到 Excel 或其它电子表格时非常有用。
	 */
	MONEY("币种", String.class),
	/**
	 * 文本区（长） 允许用户输入多行文本，最多可输入 131,072 个字符。
	 */
	TEXT("文本区（长）", String.class),
	/**
	 * 文件
	 */
	FILE("文件", String.class),
	/**
	 * 地理定位 允许用户定义位置。包括精度和纬度组件，可用于计算距离。
	 */
	LOCATION("地理定位", String.class),
	/**
	 * 电话 允许用户输入任何电话号码。自动将其转换为电话号码格式。
	 */
	TELPHONE("电话", String.class),
	/**
	 * 传真
	 */
	FAX("传真", String.class),
	/**
	 * 电子邮件
	 * 允许用户输入电子邮件地址，对其进行验证以确保格式正确。如果对于一个联系人和潜在客户指定了此字段，则用户单击“发送电子邮件”时可以选择地址。注意，
	 * 自定义电子邮件地址无法用于批量电子邮件。
	 */
	EMAIL("电子邮件", String.class),
	/**
	 * 复选框 允许用户选择"真"（选取）或"假"（不选取）值。
	 */
	CHECK("是/否", Boolean.class),
	/**
	 * 文本区（RTF） 允许用户输入格式化文本、添加图片和链接。最多有 131,072 个字符在分开行上。
	 */
	RICHTEXT("富文本", String.class),
	/**
	 * 加密文本
	 */
	ENCPYTTEXT("加密文本", String.class),
	/**
	 * 日期 允许用户输入日期或从弹出式日历中选择日期。
	 */
	DATE("日期", Date.class),
	/**
	 * 日期/时间 允许用户输入日期和时间，或从弹出式日历中选择日期。当用户单击弹出式日历中的某个日期后，该日期和当前时间将输入到"日期/时间"字段。
	 */
	DATETIME("日期/时间", Date.class),
	/**
	 * 数字 允许用户输入任何数字。将删除前置零。
	 */
	NUMBER("数字", Double.class),
	/**
	 * 文本 允许用户输入任何字母和数字组合。
	 */
	VARCHAR("文本", String.class),
	/**
	 * 文本区 允许用户输入多行文本，最多可输入 255 个字符。
	 */
	MULTIVARCHAR("多行文本", String.class),
	/**
	 * 选项列表 允许用户从定义的列表中选择值。
	 */
	SINGLEOPTION("选项列表", String.class),
	/**
	 * 选项列表（多项选择） 允许用户从定义的列表中选择多个值。
	 */
	MULTIOPTION("选项列表（多项选择）", String.class),
	/**
	 * 系统枚举，由代码系统
	 */
	SYSENUM("系统枚举", String.class),
	/**
	 * 扩展枚举，从枚举字典中读取
	 */
	FLYENUM("扩展枚举", String.class),
	/**
	 * 操作，比如增删改查
	 */
	ACTIONS("操作", String.class),
	/**
	 * 关联数据，由查找关系带出值
	 */
	AUTORELATION("关联数据", String.class);

	private String title;
	private Class<?> javaType;

	private FieldDataType(final String title, final Class<?> javaType) {
		this.title = title;
		this.javaType = javaType;
	}

	@Override
	public String getTitle() {
		return this.title;
	}
	
	public Class<?> getJavaType(){
		return javaType;
	}

	/**
	 * 
	 * @param type
	 * @return
	 */
	public static FieldAttr fromJavaType(Class<?> type, Column column) {
		FieldAttr attr = new FieldAttr();
		if (column == null)
			column = new Column() {
				@Override
				public Class<? extends Annotation> annotationType() {
					return null;
				}

				@Override
				public boolean updatable() {
					return false;
				}

				@Override
				public boolean unique() {
					return false;
				}

				@Override
				public String table() {
					return null;
				}

				@Override
				public int scale() {
					return 0;
				}

				@Override
				public int precision() {
					return 0;
				}

				@Override
				public boolean nullable() {
					return false;
				}

				@Override
				public String name() {
					return null;
				}

				@Override
				public int length() {
					return 0;
				}

				@Override
				public boolean insertable() {
					return false;
				}

				@Override
				public String columnDefinition() {
					return null;
				}
			};
		switch (type.getName()) {
		case "boolean":
		case "java.lang.Boolean":
			attr.setDataType(FieldDataType.CHECK);
			break;
		case "java.lang.String":
			if (column.length() <= 255) {
				attr.setDataType(FieldDataType.VARCHAR);
			} else {
				attr.setDataType(FieldDataType.TEXT);
			}
			attr.setLength(column.length());
			break;
		case "java.sql.Clob":
			attr.setDataType(FieldDataType.TEXT);
			break;
		case "java.sql.Blob":
		case "[B":
			attr.setDataType(FieldDataType.FILE);
			break;
		case "java.lang.Integer":
			attr.setDataType(FieldDataType.NUMBER);
			attr.setLength(10);
			break;
		case "java.lang.Long":
			attr.setDataType(FieldDataType.NUMBER);
			attr.setLength(10);
			break;
		case "java.lang.Double":
		case "java.lang.Float":
			attr.setDataType(FieldDataType.NUMBER);
			attr.setLength(column.length() == 255 ? 10 : column.length());
			attr.setPrecision(column.precision());
			break;
		case "java.math.BigDecimal":
			attr.setDataType(FieldDataType.NUMBER);
			attr.setLength(19);
			attr.setPrecision(2);
			break;
		case "java.math.BigInteger":
			attr.setDataType(FieldDataType.NUMBER);
			attr.setLength(19);
			attr.setPrecision(2);
			break;
		case "java.util.Date":
		case "java.sql.Date":
		case "java.util.Calendar":
			attr.setDataType(FieldDataType.DATE);
			break;
		case "java.sql.Time":
		case "java.sql.Timestamp":
			attr.setDataType(FieldDataType.DATETIME);
			break;
		case "org.myfly.platform.system.domain.EnumValue":
			attr.setDataType(FieldDataType.FLYENUM);
			break;
		default:
			if (type.isEnum()) {
				attr.setDataType(FieldDataType.SYSENUM);
			} else {
				attr.setDataType(FieldDataType.NONE);
			}
			break;
		}
		return attr;
	}

	public static class FieldAttr {
		private FieldDataType dataType;
		private long length;
		private long precision;

		public FieldDataType getDataType() {
			return dataType;
		}

		public void setDataType(FieldDataType dataType) {
			this.dataType = dataType;
		}

		public long getLength() {
			return length;
		}

		public void setLength(long length) {
			this.length = length;
		}

		public long getPrecision() {
			return precision;
		}

		public void setPrecision(long precision) {
			this.precision = precision;
		}
	}

	/**
	 * 根据sql数据类型返回对应的FieldDataType
	 * @param sqlType
	 * @return
	 */
	public static FieldDataType fromSQLType(int sqlType) {
		switch (sqlType) {
		case Types.BOOLEAN:
			return FieldDataType.CHECK;
		case Types.CHAR:
		case Types.VARCHAR:
			return FieldDataType.VARCHAR;
		case Types.DATE:
		case Types.TIME:
			return FieldDataType.DATE;
		case Types.TIMESTAMP:
			return FieldDataType.DATETIME;
		default:
			return FieldDataType.VARCHAR;
		}
	}
}
