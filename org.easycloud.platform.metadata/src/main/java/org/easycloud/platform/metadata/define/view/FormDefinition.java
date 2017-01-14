package org.easycloud.platform.metadata.define.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.metadata.annotation.entity.FieldDataType;
import org.easycloud.platform.metadata.annotation.view.Div1View;
import org.easycloud.platform.metadata.annotation.view.Div2View;
import org.easycloud.platform.metadata.annotation.view.Div3View;
import org.easycloud.platform.metadata.annotation.view.FieldSetView;
import org.easycloud.platform.metadata.annotation.view.FormView;
import org.easycloud.platform.metadata.annotation.view.SectionView;
import org.easycloud.platform.metadata.annotation.view.SubTableView;
import org.easycloud.platform.metadata.define.entity.BaseDenifition;
import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.internal.EntityMetaDataConstants;
import org.easycloud.platform.metadata.service.EntityMetaData;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.hibernate.action.internal.EntityAction;
import org.springframework.util.Assert;

/**
 * 表单视图定义，用于单个对象查看、编辑、打印等
 * 
 * @author xiangwanhong
 *
 */
public class FormDefinition extends BaseDenifition {
	private static Log log = LogFactory.getLog(FormDefinition.class);
	/**
	 * 布局定义
	 */
	private DivDefinition[] divs;
	/**
	 * 显示内容定义
	 */
	private SectionDefinition[] sections;

	/**
	 * 实体操作集
	 */
	private EntityAction[] actions;

	public FormDefinition(EntityMetaData parent, String name) {
		super(parent);
		setName(name);
	}

	public SectionDefinition[] getSections() {
		return sections;
	}

	public void setSections(SectionDefinition[] sections) {
		this.sections = sections;
	}

	/**
	 * 获取子表定义信息
	 * 
	 * @param subTableAttr
	 * @return
	 */
	public SubTableDefinition getSubTableDefinition(String subTableAttr) {
		SubTableDefinition result = null;
		if (ArrayUtils.isNotEmpty(getSections())) {
			for (SectionDefinition section : getSections()) {
				if (ArrayUtils.isNotEmpty(section.getSubTables())) {
					for (SubTableDefinition subtable : section.getSubTables()) {
						if (subtable.getSubTableAttr().equals(subTableAttr)) {
							result = subtable;
							break;
						}
					}
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EntityMetaData getParent() {
		return super.getParent();
	}

	/**
	 * 获取Form定义中用到的字段
	 * 
	 * @return
	 */
	public FieldDefinition[] getFields() {
		Map<String, FieldDefinition> fields = new LinkedHashMap<>();
		if (ArrayUtils.isNotEmpty(getSections())) {
			for (SectionDefinition sectionDefinition : getSections()) {
				if (ArrayUtils.isNotEmpty(sectionDefinition.getFieldSets())) {
					for (FieldSetDefinition fieldSetDefinition : sectionDefinition.getFieldSets()) {
						if (ArrayUtils.isNotEmpty(fieldSetDefinition.getFields())) {
							for (FieldDefinition fieldDefinition : fieldSetDefinition.getFields()) {
								if (!fields.containsKey(fieldDefinition.getName())) {
									fields.put(fieldDefinition.getName(), fieldDefinition);
								} else {
								}
							}
						}
					}
				}
			}
		}
		// 自动添加键值字段
		for (FieldDefinition field : getParent().getPKFieldDefinition().getIdFields()) {
			if (!fields.containsKey(field.getName())) {
				fields.put(field.getName(), field);
			}
		}
		return fields.values().toArray(new FieldDefinition[] {});
	}

	@Override
	public String toString() {
		return "name: " + getName();
	}

	public EntityAction[] getActions() {
		return actions;
	}

	public void setActions(EntityAction[] actions) {
		this.actions = actions;
	}

	public DivDefinition[] getDivs() {
		return divs;
	}

	public void setDivs(DivDefinition[] divs) {
		this.divs = divs;
	}

	/**
	 * 构建实体表单定义
	 * 
	 * @param metaData
	 * @param formView
	 * @return
	 */
	public static FormDefinition buildFormDefinition(EntityMetaData metaData, FormView formView) {
		FormDefinition formDefinition = new FormDefinition(metaData, formView.name());
		if (StringUtils.isBlank(formDefinition.getName())) {
			formDefinition.setName(EntityMetaDataConstants.DEFAULT_NAME);
		}
		List<SectionDefinition> sections = new ArrayList<>();
		// 布局定义
		if (ArrayUtils.isNotEmpty(formView.divs())) {
			List<DivDefinition> divs = new ArrayList<>();
			for (Div1View divView : formView.divs()) {
				DivDefinition divDefinition = new DivDefinition(formDefinition);
				divDefinition.setName(divView.name());
				divDefinition.setDivType(divView.divType());
				divDefinition.setExtClass(divView.extClass());
				divDefinition.setWidth(divView.width());
				if (ArrayUtils.isNotEmpty(divView.subs())) {
					List<DivDefinition> divs2 = new ArrayList<>();
					for (Div2View div2View : divView.subs()) {
						DivDefinition div2Definition = new DivDefinition(divDefinition);
						div2Definition.setName(div2View.name());
						div2Definition.setDivType(div2View.divType());
						div2Definition.setExtClass(div2View.extClass());
						div2Definition.setWidth(div2View.width());
						if (ArrayUtils.isNotEmpty(div2View.subs())) {
							List<DivDefinition> divs3 = new ArrayList<>();
							for (Div3View div3View : div2View.subs()) {
								DivDefinition div3Definition = new DivDefinition(div2Definition);
								div3Definition.setName(div3View.name());
								div3Definition.setDivType(div3View.divType());
								div3Definition.setExtClass(div3View.extClass());
								div3Definition.setWidth(div3View.width());
								divs3.add(div3Definition);
							}
							div2Definition.setSubs(divs3.toArray(new DivDefinition[] {}));
						}
						divs2.add(div2Definition);
					}
					divDefinition.setSubs(divs2.toArray(new DivDefinition[] {}));
				}
				divs.add(divDefinition);
			}
			formDefinition.setDivs(divs.toArray(new DivDefinition[] {}));
		} else {
			// 没有定义布局时，生成默认布局
			if (ArrayUtils.isNotEmpty(formView.sections())) {
				int index = 0;
				DivDefinition[] divs0 = new DivDefinition[2];
				divs0[0] = new DivDefinition(formDefinition);
				divs0[0].setWidth(8);
				DivDefinition[] divs = new DivDefinition[formView.sections().length];
				for (SectionView sectionView : formView.sections()) {
					DivDefinition divDefinition = new DivDefinition(divs0[0]);
					if (StringUtils.isBlank(sectionView.name())) {
						divDefinition.setName("div" + index);
					} else {
						divDefinition.setName(sectionView.name());
					}
					divs[index] = divDefinition;
					index++;
				}
				divs0[0].setSubs(divs);

				divs0[1] = new DivDefinition(formDefinition);
				divs0[1].setWidth(4);
				divs0[1].setName("div" + formView.sections().length);
				SectionDefinition social = new SectionDefinition(formDefinition, "活动");
				social.setName(divs0[1].getName());
				social.setType(SectionType.SOCIAL);
				sections.add(social);
				formDefinition.setDivs(divs0);
			}
		}
		// 显示内容定义
		if (ArrayUtils.isNotEmpty(formView.sections())) {
			int index = 0;
			for (SectionView sectionView : formView.sections()) {
				SectionDefinition sectionDefinition = new SectionDefinition(formDefinition, sectionView.title());
				if (StringUtils.isBlank(sectionView.name())) {
					sectionDefinition.setName("div" + index);
				} else {
					sectionDefinition.setName(sectionView.name());
				}
				sectionDefinition.setType(sectionView.type());
				// 设置FieldSet
				if (ArrayUtils.isNotEmpty(sectionView.fieldSets())) {
					List<FieldSetDefinition> fieldSets = new ArrayList<>();
					for (FieldSetView fieldSetView : sectionView.fieldSets()) {
						FieldSetDefinition fieldSet = new FieldSetDefinition(fieldSetView.title());
						fieldSet.setFields(metaData.getFields(metaData, fieldSetView.fields()));
						fieldSets.add(fieldSet);
					}
					sectionDefinition.setFieldSets(fieldSets.toArray(new FieldSetDefinition[] {}));
				}
				List<SubTableDefinition> subTables = new ArrayList<>();
				// 设置SubTable
				if (ArrayUtils.isNotEmpty(sectionView.subTables())) {
					for (SubTableView subTableView : sectionView.subTables()) {
						AssertUtil.parameterEmpty(subTableView.tableAttr(),
								"subTableView.tableAttr=" + subTableView.tableAttr());
						SubTableDefinition subTable = SubTableDefinition.buildSubTable(metaData, subTableView);
						subTable.setName(formDefinition.getName());
						subTables.add(subTable);
					}
				}
				// 设置公共子表
				if (!SectionType.CUSTOM.equals(sectionView.type())) {
					CommonSubTableType commonSubTableType = CommonSubTableType.valueOf(sectionView.type().name());
					Assert.notNull(commonSubTableType,
							"在公共子表定义[CommonSubTableType]中找不到名称为[" + sectionView.type().name() + "]的子表");
					try {
						FormDefinition.class.forName(commonSubTableType.getTableClass());
						SubTableDefinition subTable = SubTableDefinition.buildSubTable(metaData,
								metaData.getField(commonSubTableType.getAttrName()), EntityMetaDataConstants.DEFAULT_NAME);
						subTables.add(subTable);
					} catch (ClassNotFoundException e) {
						log.warn("找不到子表实现类[" + commonSubTableType.getTableClass() + "]，将忽略");
					}
				}
				sectionDefinition.setSubTables(subTables.toArray(new SubTableDefinition[] {}));
				sections.add(sectionDefinition);
				index++;
			}
		} else {
			formDefinition = buildDefaultFormViewDefinition(metaData, formDefinition.getName());
		}
		formDefinition.setSections(sections.toArray(new SectionDefinition[] {}));
		return formDefinition;
	}

	/**
	 * 构建默认的formView。将用默认格式显示.默认显示时，所有主记录字段在一个FieldSet中显示，每个子表也独立显示
	 * 
	 * @param metaData
	 * @param name
	 *            为formView的名称
	 */
	public static FormDefinition buildDefaultFormViewDefinition(EntityMetaData metaData, String name) {
		FormDefinition formDefinition = new FormDefinition(metaData, name);
		List<SectionDefinition> sections = new ArrayList<>();
		// 主记录所有属性建立一个Section和二个FieldSet
		SectionDefinition sectionDefinition = new SectionDefinition(formDefinition, "基本信息");
		FieldSetDefinition fieldSetDefinition1 = new FieldSetDefinition("基本信息一");
		FieldDefinition[] fieldSet1 = ArrayUtils.subarray(metaData.getAllFields(), 0,
				metaData.getAllFields().length / 2);
		fieldSetDefinition1.setFields(fieldSet1);
		FieldSetDefinition fieldSetDefinition2 = new FieldSetDefinition("基本信息二");
		FieldDefinition[] fieldSet2 = ArrayUtils.subarray(metaData.getAllFields(), metaData.getAllFields().length / 2,
				metaData.getAllFields().length);
		fieldSetDefinition2.setFields(fieldSet2);
		sectionDefinition.setFieldSets(new FieldSetDefinition[] { fieldSetDefinition1, fieldSetDefinition2 });
		sections.add(sectionDefinition);

		// 每一个子表建立一个Section
		FieldDefinition[] subTableFields = metaData
				.getFieldsByDataType(new FieldDataType[] { FieldDataType.MDRELATION, FieldDataType.FLYMDRELATION });
		for (FieldDefinition subTableField : subTableFields) {
			SubTableDefinition subTable = SubTableDefinition.buildSubTable(metaData, subTableField,
					EntityMetaDataConstants.DEFAULT_NAME);
			if (subTable != null) {
				subTable.setName(formDefinition.getName());

				sectionDefinition = new SectionDefinition(formDefinition, subTable.getTitle());
				sectionDefinition.addSubTable(subTable);
				sections.add(sectionDefinition);
			}
		}
		formDefinition.setSections(sections.toArray(new SectionDefinition[] {}));

		// 没有定义布局时，生成默认布局
		if (ArrayUtils.isNotEmpty(formDefinition.getSections())) {
			/**
			 * 默认布局： <div width=8> </div>
			 * <div width=4> <div name='' class='SOCIAL'> </div>
			 */
			DivDefinition[] divs = new DivDefinition[2];
			divs[0] = new DivDefinition(formDefinition);
			divs[0].setWidth(8);
			int index = 0;
			DivDefinition[] div1s = new DivDefinition[formDefinition.getSections().length];
			for (SectionDefinition sectionDefinition2 : formDefinition.getSections()) {
				sectionDefinition2.setName("div" + index);
				DivDefinition divDefinition = new DivDefinition(divs[0]);
				divDefinition.setName(sectionDefinition2.getName());
				div1s[index] = divDefinition;
				index++;
			}
			divs[0].setSubs(div1s);

			// 增加特性Section
			SectionDefinition socialSection = new SectionDefinition(formDefinition, "活动");
			socialSection.setName("div" + sections.size());
			socialSection.setType(SectionType.SOCIAL);

			divs[1] = new DivDefinition(formDefinition);
			divs[1].setWidth(4);
			divs[1].setName(socialSection.getName());

			formDefinition.setSections(ArrayUtils.add(formDefinition.getSections(), socialSection));
			formDefinition.setDivs(divs);
		}

		return formDefinition;
	}

	public void validate() {
		Assert.notEmpty(getDivs());
		Assert.notEmpty(getSections());
		for (SectionDefinition section : getSections()) {
			section.validate();
		}
		for (DivDefinition div : getDivs()) {
			div.validate();
		}
	}
}
