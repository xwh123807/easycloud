package org.easycloud.platform.core.flydata.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.myfly.platform.core.domain.DataImportInfo;
import org.myfly.platform.core.domain.DataImportState;
import org.myfly.platform.core.domain.FieldDataType;
import org.myfly.platform.core.domain.ImportInfo;
import org.myfly.platform.core.flydata.service.EntityMap;
import org.myfly.platform.core.flydata.service.EntityQueryMap;
import org.myfly.platform.core.flydata.service.FlyEntityMap;
import org.myfly.platform.core.flydata.service.IFlyDataAccessService;
import org.myfly.platform.core.flydata.service.IJpaAccessService;
import org.myfly.platform.core.flydata.service.Search;
import org.myfly.platform.core.message.service.IMessageQueueService;
import org.myfly.platform.core.message.service.MessageQueueConstants;
import org.myfly.platform.core.metadata.define.FieldDefinition;
import org.myfly.platform.core.metadata.define.ListDefinition;
import org.myfly.platform.core.metadata.define.PKFieldDefinition;
import org.myfly.platform.core.metadata.define.SetFieldValueHandler;
import org.myfly.platform.core.metadata.internal.EntityMetaData;
import org.myfly.platform.core.metadata.internal.EntityMetaDataConstants;
import org.myfly.platform.core.metadata.service.IEntityMetaDataService;
import org.myfly.platform.core.search.service.IFullTextSearchService;
import org.myfly.platform.core.utils.AssertUtil;
import org.myfly.platform.core.utils.ClassUtil;
import org.myfly.platform.core.utils.DateUtil;
import org.myfly.platform.core.utils.EntityLinkUtil;
import org.myfly.platform.core.utils.ExcelUtils;
import org.myfly.platform.core.utils.csv.CsvReader;
import org.myfly.platform.core.visualpage.ui.EntityAction;
import org.myfly.platform.core.visualpage.ui.EntityActionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.Base64Utils;

/**
 * 
 * @author xiangwanhong
 *
 */
@Transactional
public abstract class AbstractFlyDataAccessService implements IFlyDataAccessService {
	private static Log log = LogFactory.getLog(AbstractFlyDataAccessService.class);

	@Autowired
	private IEntityMetaDataService entityMetaDataService;
	@Autowired
	@Qualifier("defaultConversionService")
	private ConversionService conversionService;
	@Autowired
	private IFullTextSearchService fullTextSearchService;
	@Autowired
	private IJpaAccessService jpaDataAccessService;
	@Autowired
	private IMessageQueueService messageService;

	/**
	 * 最大导出行
	 */
	// @Value("${org.myfly.platform.data.maxExportSize}")
	private int maxExportSize = 10000;

	public int getMaxExportSize() {
		return maxExportSize;
	}

	public void setMaxExportSize(int maxExportSize) {
		this.maxExportSize = maxExportSize;
	}

	public IEntityMetaDataService getEntityMetaDataService() {
		return entityMetaDataService;
	}

	public ConversionService getConversionService() {
		return conversionService;
	}

	public IFullTextSearchService getFullTextSearchService() {
		return fullTextSearchService;
	}

	public IJpaAccessService getJpaDataAccessService() {
		return jpaDataAccessService;
	}

	/**
	 * 获取实体元模型
	 * 
	 * @param entityName
	 * @return
	 */
	public EntityMetaData getEntityMetaData(String entityName) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		return entityMetaDataService.getEntityMetaData(entityName);
	}

	@Override
	@Transactional
	public void exportToExcel(String entityName, String uid, String subTableAttr, String view, EntityQueryMap params,
			String excelFileName) {
		// 一次一万条，分批处理
		DataTablesResponse tableResponse = findAllToDataTables(entityName, uid, subTableAttr, view, params, 0,
				getMaxExportSize(), true);
		try {
			// 写文件
			ExcelUtils.writeToFile(excelFileName, tableResponse.getMetaData().getFieldTitles(),
					tableResponse.getData());
			if (log.isInfoEnabled()) {
				log.info("表[" + entityName + "]数据写入文件：" + excelFileName);
			}
		} catch (Exception e) {
			log.error("生成Excel文件失败，" + excelFileName + ".\t" + e.getMessage());
		}
	}

	@Override
	@Transactional
	public void exportToExcel(String entityName, String view, EntityQueryMap params, String excelFileName) {
		// 一次一万条，分批处理
		DataTablesResponse tableResponse = findAllToDataTables(entityName, view, params, 0, getMaxExportSize(), true);
		try {
			// 写文件
			ExcelUtils.writeToFile(excelFileName, tableResponse.getMetaData().getFieldTitles(),
					tableResponse.getData());
			if (log.isInfoEnabled()) {
				log.info("表[" + entityName + "]数据[" + tableResponse.getRecordsTotal() + "]行写入文件：" + excelFileName);
			}
		} catch (Exception e) {
			log.error("生成Excel文件失败，" + excelFileName + ".\t" + e.getMessage());
		}
	}

	public interface ImportActions {
		public void before(Object entity);
	}

	@Override
	@Transactional
	public String importExcel(String table, String view, String uploadDir, String actionUrl) {
		return importExcel(table, view, uploadDir, actionUrl, null);
	}

	private String importExcel(String table, String view, String uploadDir, String actionUrl,
			ImportActions importActions) {
		AssertUtil.parameterEmpty(table, "table");
		AssertUtil.parameterEmpty(uploadDir, "uploadDir");
		File file = new File(uploadDir);
		if (!file.exists() || file.isFile()) {
			AssertUtil.parameterInvalide(uploadDir, "上传文件目录参数[uploadDir]不是有效地目录");
		}
		Collection<File> files = FileUtils.listFiles(file, new String[] { "csv", "txt" }, false);
		String groupUid = StringUtils.substringAfterLast(uploadDir, String.valueOf(File.separator));
		for (File excelFile : files) {
			DataImportInfo entity = new DataImportInfo();
			entity.setActionUrl(actionUrl);
			entity.setGroupUid(groupUid);
			entity.setCreated(DateUtil.nowSqlTimestamp());
			entity.setImportFile(excelFile.getName());
			try {
				ImportInfo importInfo = importExcelFile(table, view, excelFile, importActions);
				updateDataImportLog(entity, importInfo);
			} catch (Exception e) {
				entity.setMessage(e.getMessage());
			}
			if (entity.getFaultCount() == 0) {
				entity.setState(DataImportState.SUCCESS);
			} else if (entity.getTotalCount() == entity.getFaultCount()) {
				entity.setState(DataImportState.FAULT);
			} else {
				entity.setState(DataImportState.PARTIAL_SUCCESS);
			}
			messageService.sendMessageToQueue(MessageQueueConstants.QUEUE_DATAIMPORT_LOG, entity);
		}
		return null;
	}

	@Override
	public String importExcel(String table, final String uid, String subTableAttr, String listViewName,
			String uploadDir, String actionUrl) {
		EntityMetaData entityMetaData = getEntityMetaDataService().getEntityMetaData(table);
		FieldDefinition subField = entityMetaData.getField(subTableAttr);
		final SetFieldValueHandler setHandler = subField.getRelationField().getSetValueHandler();
		return importExcel(subField.getRelationTable(), listViewName, uploadDir, actionUrl, new ImportActions() {
			@Override
			public void before(Object entity) {
				setHandler.setFieldValue(entity, uid);
			}

		});
	}

	@Override
	public ImportInfo importExcelFile(String table, String view, File excelFile) {
		return importExcelFile(table, view, excelFile, null);
	}

	private ImportInfo importExcelFile(String table, String view, File excelFile, ImportActions importActions) {
		log.info("读取文件：" + excelFile.getAbsolutePath());
		EntityMetaData entityMetaData = getEntityMetaDataService().getEntityMetaData(table);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		ListDefinition listDefinition = entityMetaData.getListDefinition(view);

		ImportInfo importInfo = new ImportInfo();
		long start = System.currentTimeMillis();
		try {
			CsvReader reader = new CsvReader(excelFile.getAbsolutePath());
			reader.readHeaders();
			TransNameToIDCache transNameToIDCache = new TransNameToIDCache(getJpaDataAccessService());
			List<Object> batchList = new ArrayList<>();
			while (reader.readRecord()) {
				Object entity = entityMetaData.newEntityInstance();
				int columnIndex = 0;
				try {
					for (FieldDefinition field : listDefinition.getFields()) {
						Object value = reader.get(columnIndex);
						try {
							if (StringUtils.isNotBlank((String) value)) {
								if (FieldDataType.ACTIONS.equals(field.getDataType())
										|| FieldDataType.AUTORELATION.equals(field.getDataType())) {
									// 导入时不处理空值字段、操作字段、自动关联属性字段
								} else {
									if (FieldDataType.SEARCHRELATION.equals(field.getDataType())) {
										// 名称向ID转换
										value = transNameToIDCache.transNameToUID(field.getRelationTable(),
												field.getName(), (String) value);
									} else if (FieldDataType.SYSENUM.equals(field.getDataType())) {
										// 系统枚举类型，将label转换成name
										value = ClassUtil.getEnumByTitle(field.getType(), (String) value);
									}
									value = getConversionService().convert(value, field.getType());
									field.getSetValueHandler().setFieldValue(entity, value);
								}
							}
						} catch (Exception e) {
							throw new Exception("设置属性[" + field.getName() + "]为值[" + value + "], " + e.getMessage());
						}
						columnIndex++;
					}
					if (importActions != null) {
						importActions.before(entity);
					}
					importInfo.incSuccess();
				} catch (Exception e) {
					importInfo.addErrorLine(StringUtils.join(reader.getValues(), reader.getRecordDelimiter()),
							e.getMessage());
				}
				batchList.add(entity);
			}
			saveEntity(batchList);
			// getJpaDataAccessService().batchSaveEntity(table, batchList);
		} catch (Exception e) {
			importInfo.setMessage(e.getMessage());
		}
		importInfo.setUsedTime((int) (System.currentTimeMillis() - start));
		importInfo.setTotalCount(importInfo.getSuccessCount() + importInfo.getErrorCount());
		return importInfo;
	}

	private void updateDataImportLog(DataImportInfo entity, ImportInfo importInfo) {
		entity.setFaultCount(importInfo.getErrorCount());
		entity.setSuccessCount(importInfo.getSuccessCount());
		entity.setTotalCount(importInfo.getTotalCount());
		entity.setUsedtime(importInfo.getUsedTime());
	}

	@Override
	public String transNameToUID(String entityName, String name) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(name, "name");
		EntityMetaData metaData = getEntityMetaData(entityName);
		Map<String, Object> keyParams = new LinkedHashMap<>();
		FieldDefinition labelField = metaData.getLabelField();
		keyParams.put(labelField.getFieldName(), name);
		Object entity = findOne(entityName, keyParams);
		if (entity != null) {
			return metaData.getPKFieldDefinition().getPKValue(entity);
		} else {
			return null;
		}
	}

	@Override
	public String transUIDToName(String entityName, String uid) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(uid, "uid");
		Object entity = findOne(entityName, uid);
		if (entity != null) {
			// 取出实体显示字段的值
			Object value = getEntityMetaData(entityName).getLabelField().getGetValueHandler().getFieldValue(entity);
			if (value != null && !(value instanceof String)) {
				value = getConversionService().convert(value, String.class);
			}
			return (String) value;
		} else {
			return null;
		}
	}

	@Override
	public DataTablesResponse findAllForSelect(String entityName, Search search, PageRequest pageable) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		Map<String, Object> params = new LinkedHashMap<>();
		List<?> page = findAll2(entityName, params);
		DataTablesResponse tableResponse = new DataTablesResponse();
		tableResponse.setRecordsTotal((int) page.size());
		tableResponse.setRecordsFiltered(tableResponse.getRecordsTotal());
		// 将分页查询结果转换成String[][]类型，数据类型都转为字符串
		String[][] data = new String[page.size()][2];
		EntityMetaData metaData = getEntityMetaDataService().getEntityMetaData(entityName);
		PKFieldDefinition pkFieldDefinition = metaData.getPKFieldDefinition();
		FieldDefinition labelFieldDefinition = metaData.getLabelField();
		for (int row = 0; row < page.size(); row++) {
			final Object entity = page.get(row);
			String pkValue = pkFieldDefinition.getPKValue(entity);
			String labelValue = "";
			if (labelFieldDefinition != null) {
				Object value = labelFieldDefinition.getGetValueHandler().getFieldValue(entity);
				if (value != null && !(value instanceof String)) {
					labelValue = ClassUtil.convertValueToString(value);
				} else {
					labelValue = (String) value;
				}
			} else {
				labelValue = pkValue;
			}
			data[row][0] = pkValue;
			data[row][1] = labelValue;
		}
		tableResponse.setData(data);
		return tableResponse;
	}

	@Override
	public DataTablesResponse findAllForSearchDataTables(String entityName, String listViewName, String searchText,
			int page, int size) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData entityMetaData = getEntityMetaDataService().getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		Page<?> pageData = getFullTextSearchService().search(entityName, searchText, page, size);
		ListDefinition listDefinition = entityMetaData.getListDefinition(listViewName);
		DataTablesResponse response = buildDataTableReponse(pageData, listDefinition.getFields(), true);
		response.setMetaData(new DataTableMetaData(listDefinition, false));
		response.setPage(page);
		response.setSize(size);
		return response;
	}

	@Override
	public Page<FlyEntityMap> findAllForSearch(String entityName, String listViewName, String searchText, int page,
			int size) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData entityMetaData = getEntityMetaDataService().getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		Page<?> pageData = getFullTextSearchService().search(entityName, searchText, page, size);
		List<FlyEntityMap> list = new ArrayList<>();
		ListDefinition listDefinition = entityMetaData.getListDefinition(listViewName);
		for (Object entity : pageData.getContent()) {
			String pkValue = entityMetaData.getPKFieldDefinition().getPKValue(entity);
			list.add(convertToViewMap(entityName, null, null, entity, listDefinition.getFields(),
					listDefinition.getLabelField(), pkValue, listViewName, false));
		}
		PageRequest pageable = new PageRequest(page, size);
		PageImpl<FlyEntityMap> pageImpl = new PageImpl<FlyEntityMap>(list, pageable, pageData.getTotalElements());
		return pageImpl;
	}

	@Override
	public DataTablesResponse findAllToDataTables(String entityName, String uid, String subTableAttr, String view,
			EntityQueryMap params, int page, int size, boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		AssertUtil.parameterEmpty(uid, "uid");
		AssertUtil.parameterEmpty(subTableAttr, "subTableAttr");

		EntityMetaData entityMetaData = entityMetaDataService.getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		Sort sort = entityMetaData.getListDefinition(view).getSort();
		PageRequest pageable = new PageRequest(page, size, sort);
		// 查询返回分页数据
		Page<?> pageData = findAllForSubEntityWithPage(entityName, uid, subTableAttr, view, params, pageable,
				printMode);
		ListDefinition listDefinition = entityMetaData.getFormDefinition(view).getSubTableDefinition(subTableAttr);
		DataTablesResponse dataTablesResponse = buildDataTableReponse(pageData, listDefinition.getFields(), false);
		dataTablesResponse.setMetaData(new DataTableMetaData(listDefinition, printMode));
		return dataTablesResponse;
	}

	@Override
	public DataTablesResponse findAllToDataTables(String entityName, String listViewName, EntityQueryMap params,
			int page, int size, boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		EntityMetaData entityMetaData = entityMetaDataService.getEntityMetaData(entityName);
		AssertUtil.parameterEmpty(entityMetaData, "entityMetaData");
		// 查询数据
		Sort sort = entityMetaData.getListDefinition(listViewName).getSort();
		PageRequest pageable = new PageRequest(page, size, sort);
		Page<?> pageData = findAll(entityName, listViewName, params, pageable, printMode);
		ListDefinition listDefinition = entityMetaData.getListDefinition(listViewName);
		DataTablesResponse dataTablesResponse = buildDataTableReponse(pageData, listDefinition.getFields(), false);
		dataTablesResponse.setMetaData(new DataTableMetaData(listDefinition, printMode));
		return dataTablesResponse;
	}

	/**
	 * 构造结构集
	 * 
	 * @param pageData
	 *            分页查询结果
	 * @param fields
	 *            字段定义
	 * @param resultIsMap
	 *            结果是否为Map[]，否则为String[][]
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private DataTablesResponse buildDataTableReponse(Page<?> pageData, FieldDefinition[] fields, boolean resultIsMap) {
		DataTablesResponse dataTablesResponse = new DataTablesResponse();
		dataTablesResponse.setPage(pageData.getNumber());
		dataTablesResponse.setSize(pageData.getSize());
		dataTablesResponse.setTotalPages(pageData.getTotalPages());
		dataTablesResponse.setRecordsTotal(pageData.getTotalElements());
		dataTablesResponse.setRecordsFiltered(pageData.getTotalElements());
		dataTablesResponse.setResultIsMap(resultIsMap);
		// 设置结果集
		if (CollectionUtils.isNotEmpty(pageData.getContent())) {
			if (pageData.getContent().get(0) instanceof Map) {
				if (resultIsMap) {
					try {
						dataTablesResponse.setMapData((pageData.getContent().toArray(new HashMap[] {})));
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// 将Map转换为数组
					String[][] datas = convertEntityMapsToStringArray(pageData.getContent(), fields);
					dataTablesResponse.setData(datas);
				}
			} else {
				// 结果集是对象列表
				if (resultIsMap) {
				} else {

				}
			}
		} else {
			dataTablesResponse.setData(new String[0][]);
			dataTablesResponse.setMapData(new HashMap[0]);
		}
		return dataTablesResponse;
	}

	/**
	 * 将Map类型结果集转换为二维字符串数组
	 * 
	 * @param content
	 * @param fields
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String[][] convertEntityMapsToStringArray(List<?> content, FieldDefinition[] fields) {
		String[][] datas = new String[content.size()][fields.length];
		int row = 0;
		for (Object item : content) {
			Map<String, String> entity = (Map<String, String>) item;
			int col = 0;
			for (FieldDefinition field : fields) {
				String name = field.getName();
				if (FieldDataType.SEARCHRELATION.equals(field.getDataType())) {
					name += "__link";
				} else if (FieldDataType.SYSENUM.equals(field.getDataType())) {
					name += "__label";
				}
				String value = entity.get(name);
				datas[row][col] = value;
				col++;
			}
			row++;
		}
		return datas;
	}

	@Override
	public List<FlyEntityMap> findAllForSubEntity(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, boolean printMode) {
		return findAllForSubEntity(entityName, uid, subTableAttr, formViewName, params, 0, 0, printMode);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<FlyEntityMap> findAllForSubEntityWithPage(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, Pageable pageable, boolean printMode) {
		// 获取子表记录数
		long total = count(entityName, uid, subTableAttr, formViewName, null);
		if (total > 0) {
			List<?> content = findAllForSubEntity(entityName, uid, subTableAttr, formViewName, params,
					pageable.getPageNumber(), pageable.getPageSize(), printMode);
			return new PageImpl(content, pageable, total);
		} else {
			return new PageImpl(new ArrayList(), pageable, 0);
		}
	}

	@Override
	public Page<FlyEntityMap> findAllForSubEntityWithPage(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, int page, int size, boolean printMode) {
		Pageable pageable = new PageRequest(page, size);
		return findAllForSubEntityWithPage(entityName, uid, subTableAttr, formViewName, params, pageable, printMode);
	}

	/**
	 * 将DB查询结果转换为显示模型，只处理key调整为属性名称，不增加任何扩展字段
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param entity
	 * @param pkValue
	 * @return
	 */
	public Map<String, String> convertToViewMap(String entityName, String uid, String subTableAttr, Object entity,
			String pkValue) {
		Map<String, String> result = new LinkedHashMap<>();
		for (FieldDefinition fieldDefinition : getEntityMetaData(entityName).getAllFields()) {
			// 先取出字段值，再转换为字符串
			String value = null;
			Object tmp = null;
			if (entity instanceof Map) {
				tmp = ((Map<?, ?>) entity).get(fieldDefinition.getFieldName());
			} else if (!FieldDataType.ACTIONS.equals(fieldDefinition.getDataType())) {
				tmp = fieldDefinition.getGetValueHandler().getFieldValue(entity);
			}
			if (tmp != null && !(tmp instanceof String)) {
				value = getConversionService().convert(tmp, String.class);
			} else {
				value = (String) tmp;
			}
			if (value != null) {
				result.put(fieldDefinition.getName(), value);
			}
		}
		if (!result.containsKey("uid")) {
			result.put("uid", pkValue);
		}
		return result;
	}

	/**
	 * 将结果转换成可直接显示的内容，增加显示列链接，查找关系列链接，操作集链接
	 * 
	 * @param subTableAttr
	 * @param uid
	 * @param entityName2
	 * 
	 * @param entity
	 *            以map存放的实体值
	 * @param fields
	 *            字段列表
	 * @param labelField
	 *            显示字段
	 * @param entityName
	 *            表名
	 * @param pkValue
	 *            主键值
	 * @param formViewName
	 *            视图名
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public FlyEntityMap convertToViewMap(String entityName, String uid, String subTableAttr, Object entity,
			FieldDefinition[] fields, String labelFieldName, String pkValue, String formViewName, boolean printMode) {
		FlyEntityMap result = new FlyEntityMap();
		for (FieldDefinition fieldDefinition : fields) {
			// 先取出字段值，再转换为字符串
			String value = null;
			Object tmp = null;
			if (entity instanceof Map) {
				// jdbc实现，entity为字段名值对
				tmp = ((Map<String, Object>) entity).get(fieldDefinition.getFieldName());
			} else if (FieldDataType.MDRELATION.equals(fieldDefinition.getDataType())
					|| FieldDataType.FLYMDRELATION.equals(fieldDefinition.getDataType())) {
				// 不输出子表数据，子表数据由应用场景按需取数
				continue;
			} else if (printMode && FieldDataType.ACTIONS.equals(fieldDefinition.getDataType())) {
				// 打印模式下，不输出操作字段
				continue;
			} else if (!FieldDataType.ACTIONS.equals(fieldDefinition.getDataType())) {
				tmp = fieldDefinition.getGetValueHandler().getFieldValue(entity);
			}
			if (tmp != null) {
				if (tmp instanceof byte[]) {

				} else if (!(tmp instanceof String)) {
					try{
						value = getConversionService().convert(tmp, String.class);
					}catch(Exception e){
						value = "错误：" + e.getMessage();
					}
				} else {
					value = (String) tmp;
				}
			} else {
			}

			// Link字段
			String linkField = fieldDefinition.getName() + "__link";
			String linkValue = "";
			if (fieldDefinition.getName().equals(labelFieldName)) {
				// 如果是Label字段，则增加两个字段，{name}字段表示数据库原始值；{name}__link字段表示超链接显示
				if (!printMode) {
					// 显示字段增加查看自己的超链接
					if (StringUtils.isBlank(subTableAttr)) {
						// 非子表
						linkValue = EntityLinkUtil.getEntityActionLinkHtml(EntityAction.VIEW, entityName, pkValue,
								value, formViewName, false, false);
					} else {
						// 子表
						linkValue = EntityLinkUtil.getSubEntityActionLinkHtml(EntityAction.VIEW, entityName, uid,
								subTableAttr, pkValue, value, formViewName, false, false);
					}
				} else {
					linkValue = value;
				}
				if (!result.containsKey(linkField)) {
					result.put(linkField, linkValue);
				}
			} else {
				switch (fieldDefinition.getDataType()) {
				case URL:
					value = "<a href='" + value + "'>" + value + "</a>";
					break;
				case ACTIONS:
					EntityActionInfo actionInfo = null;
					if (StringUtils.isBlank(subTableAttr)) {
						actionInfo = new EntityActionInfo(entityName, pkValue, null, null, null, formViewName, null);
					} else {
						actionInfo = new EntityActionInfo(entityName, uid, subTableAttr, pkValue, null, formViewName,
								null);
					}
					value = (String) fieldDefinition.getGetValueHandler().getFieldValue(actionInfo);
					break;
				case SYSENUM:
					// 增加两个字段，{name}字段表示数据库原始值；{name}__label字段表示显示值
					String label = ClassUtil.getEnumTitleByName(fieldDefinition.getRelationClass(), value);
					result.put(fieldDefinition.getName() + "__label", label);
					break;
				case AUTORELATION:
					// 增加三个字段，{name}字段表示数据库原始值；{name}__label字段表示显示名称；{name}__link字段表示超链接显示
					String[] values2 = (String[]) fieldDefinition.getGetValueHandler().getFieldValue(entity);
					if (ArrayUtils.isNotEmpty(values2) && values2.length == 2) {
						// 原始值
						value = values2[1];
					}
					break;
				case FLYSEARCHRELATION:
				case SEARCHRELATION:
					// 增加三个字段，{name}字段表示数据库原始值；{name}__label字段表示显示名称；{name}__link字段表示超链接显示
					String[] values = (String[]) fieldDefinition.getGetValueHandler().getFieldValue(entity);
					if (ArrayUtils.isNotEmpty(values) && values.length == 2) {
						// 原始值
						value = values[0];
						// Label字段
						String labelField = fieldDefinition.getName() + "__label";
						if (!result.containsKey(labelField)) {
							result.put(labelField, values[1]);
						}
						if (!printMode && !FieldDataType.AUTORELATION.equals(fieldDefinition.getDataType())) {
							// 为查找关系实体增加超链接
							linkValue = EntityLinkUtil.getEntityActionLinkHtml(EntityAction.VIEW,
									fieldDefinition.getRelationTable(), values[0], values[1], formViewName, false,
									false);
						} else {
							linkValue = values[1];
						}
						if (!result.containsKey(linkField)) {
							result.put(linkField, linkValue);
						}
					}
					break;
				case MDRELATION:
					// 子表，取出子表数据
					break;
				case FILE:
					value = Base64Utils.encodeToString((byte[]) tmp);
					break;
				default:
					break;
				}
			}
			if (value != null) {
				result.put(fieldDefinition.getName(), value);
			}
		}
		if (!result.containsKey("uid")) {
			result.put("uid", pkValue);
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.data.service.IFlyDataAccessService#findAll2(
	 * java.lang.String, java.util.Map)
	 */
	@Override
	public <T> List<T> findAll(String entityName, Map<String, String> params) {
		return null;
	}

	@Transactional
	@Override
	public void mergeEntity(String entityName, String uid, String formViewName, EntityMap values) {
		Object entity = findOne(entityName, uid);
		values.mergeEntity(entityName, entity);
		updateEntity(entity);
	}

	@Transactional
	@Override
	public void mergeEntity(String entityName, String uid, String subTableAttr, String subUid, String formViewName,
			EntityMap values) {
		EntityMetaData subMetaData = getEntityMetaData(entityName).getSubEntityMetaData(subTableAttr);
		Object entity = findOne(subMetaData.getEntityName(), subUid);
		values.mergeEntity(entityName, entity);
		updateEntity(entity);
	}

	@Override
	public List<FlyEntityMap> findAll(String entityName, String listViewName, EntityQueryMap params,
			boolean printMode) {
		AssertUtil.parameterEmpty(entityName, "entityName");
		return findAll(entityName, listViewName, params, null, printMode).getContent();
	}

	@Override
	public List<FlyEntityMap> findAll(String entityName, String listViewName, EntityQueryMap params, int page, int size,
			boolean printMode) {
		Pageable pageable = new PageRequest(page, size);
		return findAll(entityName, listViewName, params, pageable, printMode).getContent();
	}

	@Override
	public Page<FlyEntityMap> findAllWithPage(String entityName, String listViewName, EntityQueryMap params, int page,
			int size, boolean printMode) {
		Pageable pageable = new PageRequest(page, size);
		return findAll(entityName, listViewName, params, pageable, printMode);
	}

	@Override
	public int delSubEntity(String entityName, String uid, String subTableAttr, String subUid) {
		EntityMetaData metaData = getEntityMetaData(entityName).getSubEntityMetaData(subTableAttr);
		return delOne(metaData.getEntityName(), subUid);
	}

	@Override
	public void saveEntity(String entityName, List<EntityMap> entities) {
		for (EntityMap entity : entities) {
			saveEntity(entityName, EntityMetaDataConstants.DEFAULT_ALL_NAME, entity);
		}
	}
}
