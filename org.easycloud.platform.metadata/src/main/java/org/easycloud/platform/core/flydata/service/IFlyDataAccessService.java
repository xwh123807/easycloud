package org.easycloud.platform.core.flydata.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.easycloud.platform.core.domain.ImportInfo;
import org.easycloud.platform.core.flydata.internal.DataTablesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * myFly平台扩展数据访问服务
 * 
 * 通过myfly扩展数据访问服务，返回的数据，即使是jdbc实现也会将字段名更改为属性名称。带view参数的，会增加label、link等扩展字段。
 * 
 * 1、自动进行ID名称转换<br>
 * 2、自动处理审计日志<br>
 * 3、自动处理默认值<br>
 * 4、受元模型定义控制<br>
 * 5、所有参数中entityName表示实体唯一标示<br>
 * 6、Blob字段返回为base64编码后的字符串
 * 
 * @author xiangwanhong
 *
 */
public interface IFlyDataAccessService {
	/**
	 * 按主键查询单个实体，返回实体。如果是jpa实现，则返回实现类；如果是jdbc实现，则返回为Map，并且key要转换为属性名称
	 * 
	 * @param entityName
	 * @param uid
	 * @return
	 */
	<T> T findOne(final String entityName, final String uid);

	/**
	 * 按主键查询单个实体，返回实体。如果是jpa实现，则返回实现类；如果是jdbc实现，则返回为Map，并且key要转换为属性名称
	 * 
	 * @param entityName
	 * @param keyParams
	 * @return
	 */
	public <T> T findOne(final String entityName, final Map<String, Object> keyParams);

	/**
	 * 查询单个实体，用于表单视图，并将结果转换为可直接显示的内容，增加相关链接
	 * 
	 * @param entityName
	 * @param uid
	 * @param formViewName
	 * @return
	 */
	FlyEntityMap findOne(String entityName, String uid, String formViewName, boolean printMode);

	/**
	 * 关联实体ID名称转换
	 * 
	 * @param entityName
	 * @param name
	 * @return
	 */
	public String transNameToUID(final String entityName, final String name);

	/**
	 * 关联实体ID名称转换
	 * 
	 * @param entityName
	 * @param uid
	 * @return
	 */
	public String transUIDToName(final String entityName, final String uid);

	/**
	 * 查询记录数，需要根据listViewName定义的过滤器过滤数据
	 * 
	 * @param entityName
	 * @param listViewName
	 * @param params
	 * @return
	 */
	public long count(final String entityName, final String listViewName, final EntityQueryMap params);

	/**
	 * 查询记录数
	 * 
	 * @param entityName
	 * @param params
	 * @return
	 */
	public long count(final String entityName, final Map<String, String> params);

	/**
	 * 查询记录数
	 * 
	 * @param entityName
	 * @param params
	 * @return
	 */
	public long count2(final String entityName, final Map<String, Object> params);

	/**
	 * 查询数据
	 * 
	 * @param entityName
	 * @param params
	 * @return
	 */
	<T> List<T> findAll(final String entityName, final Map<String, String> params);

	/**
	 * 
	 * @param entityName
	 * @param params
	 * @return
	 */
	<T> List<T> findAll2(final String entityName, final Map<String, Object> params);

	/**
	 * 实体查询,不分页
	 * 
	 * @param entityName
	 * @param params
	 * @return
	 */
	List<FlyEntityMap> findAll(final String entityName, final String listViewName,
			final EntityQueryMap params, final boolean printMode);

	/**
	 * 实体查询,返回指定页的数据
	 * 
	 * @param entityName
	 * @param params
	 * @return
	 */
	public List<FlyEntityMap> findAll(final String entityName, final String listViewName,
			final EntityQueryMap params, final int page, final int size, final boolean printMode);
	
	/**
	 * 实体查询，返回分页信息及分页数据
	 * @param entityName
	 * @param listViewName
	 * @param params
	 * @param page
	 * @param size
	 * @param printMode
	 * @return
	 */
	public Page<FlyEntityMap> findAllWithPage(String entityName, String listViewName, EntityQueryMap params, int page, int size,
			boolean printMode);

	/**
	 * 实体数据分页查询
	 * 
	 * @param entityName
	 * @param spec
	 * @param pageable
	 * @return
	 */
	Page<FlyEntityMap> findAll(final String entityName, final String listViewName,
			final EntityQueryMap params, final Pageable pageable, final boolean printMode);

	/**
	 * 获取子表记录数
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param formViewName
	 * @param params
	 * @return
	 */
	long count(final String entityName, String uid, String subTableAttr, String formViewName,
			EntityQueryMap params);

	/**
	 * 实体子表查询,不分页
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param params
	 * @return
	 */
	List<FlyEntityMap> findAllForSubEntity(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, boolean printMode);
	
	/**
	 * 实体子表查询，返回指定分页数据
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param formViewName
	 * @param params
	 * @param page
	 * @param size
	 * @param printMode
	 * @return
	 */
	List<FlyEntityMap> findAllForSubEntity(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, int page, int size, boolean printMode);

	/**
	 * 分页查询实体子表数据
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<FlyEntityMap> findAllForSubEntityWithPage(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, Pageable pageable, boolean printMode);
	
	/**
	 * 分页查询实体子表数据
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param params
	 * @param pageable
	 * @return
	 */
	Page<FlyEntityMap> findAllForSubEntityWithPage(String entityName, String uid, String subTableAttr,
			String formViewName, EntityQueryMap params, int page, int size, boolean printMode);

	/**
	 * 实体数据导出到Excel，返回文件名
	 * 
	 * @param entityName
	 * @param view
	 * @param params
	 * @return
	 */
	void exportToExcel(String entityName, String listViewName, EntityQueryMap params, String excelFileName);

	/**
	 * 实体子表数据导出到Excel，返回文件名
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param params
	 * @return
	 */
	void exportToExcel(String entityName, String uid, String subTableAttr, String listViewName,
			EntityQueryMap params, String excelFileName);

	/**
	 * 将uploadDir目录中所有的Excel数据导入数据库，返回错误数据文件
	 * 
	 * @param entityName
	 * @param uploadDir
	 *            文件目录
	 * @return
	 */
	String importExcel(String entityName, String listViewName, String uploadDir, String actionUrl);

	/**
	 * 将uploadDir目录中所有的Excel数据导入数据库，返回错误数据文件，导入到子表
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param listViewName
	 * @param uploadDir
	 * @param actionUrl
	 * @return
	 */
	String importExcel(String entityName, String uid, String subTableAttr, String listViewName, String uploadDir,
			String actionUrl);

	/**
	 * 将excel文件导入到表中，返回失败的记录，并在最后追加错误信息
	 * 
	 * @param entityName
	 * @param excelFile
	 * @return
	 */
	ImportInfo importExcelFile(String entityName, String listViewName, File excelFile);

	/**
	 * 实体数据分页查询，返回DataTable表格使用的分页数据
	 * 
	 * @param entityName
	 * @param params
	 * @param pageable
	 * @param printMode
	 *            打印模式
	 * @return
	 */
	DataTablesResponse findAllToDataTables(String entityName, String view, EntityQueryMap params, int page,
			int size, boolean printMode);

	/**
	 * 实体子表数据分页查询，返回DataTable表格使用的分页数据
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param params
	 * @param pageable
	 * @param printMode
	 *            打印模式
	 * @return
	 */
	DataTablesResponse findAllToDataTables(String entityName, String uid, String subTableAttr, String view,
			EntityQueryMap params, int page, int size, boolean printMode);

	/**
	 * 实体数据查询，只返回id和lable列，主要用于Select选择控件
	 * 
	 * @param entityName
	 * @param search
	 * @param pageable
	 * @return
	 */
	DataTablesResponse findAllForSelect(String entityName, Search search, PageRequest pageable);

	/**
	 * 搜索数据，结果以DataTableReponse返回
	 * 
	 * @param entityName
	 * @param view
	 * @param searchText
	 * @param page
	 * @param size
	 * @return
	 */
	Page<FlyEntityMap> findAllForSearch(String entityName, String listViewName, String searchText, int page,
			int size);

	/**
	 * 搜索数据，结果以DataTableReponse返回
	 * 
	 * @param entityName
	 * @param view
	 * @param searchText
	 * @param page
	 * @param size
	 * @return
	 */
	DataTablesResponse findAllForSearchDataTables(String entityName, String listViewName, String searchText, int page,
			int size);

	/**
	 * 删除实体
	 * 
	 * @param entityName
	 * @param uid
	 * @return
	 */
	int delOne(String entityName, String uid);
	
	/**
	 * 删除子实体
	 * @param entityName
	 * @param uid
	 * @return
	 */
	int delSubEntity(String entityName, String uid, String subTableAttr, String subUid);

	/**
	 * 新增实体，传入实体类，返回实体主键UID
	 * 
	 * @param entity
	 * @return
	 */
	<T> String saveEntity(T entity);

	/**
	 * 批量新增多个实体，传入实体类列表
	 * 
	 * @param entities
	 * @return
	 */
	<T> void saveEntity(List<T> entities);
	
	/**
	 * 批量保存实体
	 * @param entityName
	 * @param entities
	 */
	void saveEntity(String entityName, List<EntityMap> entities);

	/**
	 * 新增实体，返回实体主键UID
	 * 
	 * @param entityName
	 * @param values
	 * @return
	 */
	String saveEntity(String entityName, Map<String, Object> values);

	/**
	 * 新增实体，返回实体主键UID
	 * 
	 * @param entityName
	 * @param formViewName
	 * @param values
	 * @return
	 */
	String saveEntity(String entityName, String formViewName, EntityMap values);

	/**
	 * 新增实体子表，返回实体主键UID
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param formViewName
	 * @param values
	 * @return
	 */
	String saveEntity(String entityName, String uid, String subTableAttr, String formViewName, EntityMap values);

	/**
	 * 修改实体
	 * @param entity
	 * @return
	 */
	<T> void updateEntity(T entity);
	/**
	 * 修改实体
	 * 
	 * @param entityName
	 * @param values
	 * @return
	 */
	void updateEntity(String entityName, String uid, Map<String, Object> values);

	/**
	 * 修改实体，将实体替换为values最新值，没有的属性置为空
	 * 
	 * @param entityName
	 * @param uid
	 * @param formViewName
	 * @param values
	 * @return
	 */
	void updateEntity(String entityName, String uid, String formViewName, EntityMap values);
	
	/**
	 * 合并修改实体，只覆盖values部分属性
	 * @param entityName
	 * @param uid
	 * @param formViewName
	 * @param values
	 */
	void mergeEntity(String entityName, String uid, String formViewName, EntityMap values);

	/**
	 * 修改实体子表
	 * 
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param subUid
	 * @param formViewName
	 * @param values
	 * @return
	 */
	void updateEntity(String entityName, String uid, String subTableAttr, String subUid, String formViewName,
			EntityMap values);

	/**
	 * 合并修改子实体，只覆盖values部分属性
	 * @param entityName
	 * @param uid
	 * @param subTableAttr
	 * @param subUid
	 * @param formViewName
	 * @param values
	 */
	void mergeEntity(String entityName, String uid, String subTableAttr, String subUid, String formViewName,
			EntityMap values);
}
