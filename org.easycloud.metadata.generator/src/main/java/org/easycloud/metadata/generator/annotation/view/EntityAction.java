package org.easycloud.metadata.generator.annotation.view;

import org.easycloud.metadata.generator.annotation.entity.BaseEnum;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 实体操作定义
 * 
 * @author xiangwanhong
 *
 */
public enum EntityAction implements BaseEnum {
	// 全部
	ALL("全部", null),
	// 显示实体列表查看页面
	LIST("列表", RequestMethod.GET),
	// 显示实体页面
	VIEW("查看", RequestMethod.GET),
	// 显示新增实体页面
	NEW("新增", RequestMethod.GET),
	// 实体新增保存，提交新增页面
	SAVE("保存", RequestMethod.POST),
	// 实体保存后新增，提交新增页面保存，并返回新增页面
	SAVEANDNEW("保存后新增", RequestMethod.PATCH),
	// 显示编辑页面
	EDIT("编辑", RequestMethod.GET),
	// 实体编辑保存，提交编辑页面
	SAVEUPDATE("保存", RequestMethod.PUT),
	// 实体删除
	DEL("删除", RequestMethod.DELETE),
	// 删除全部
	DELALL("删除全部", RequestMethod.DELETE),
	// 显示实体简档页面
	OUTLINE("简档", RequestMethod.GET),
	//列表打印
	LISTPRINT("打印", RequestMethod.GET),
	// 显示实体打印页面
	PRINT("打印", RequestMethod.GET),
	// 导出到pdf
	PDF("PDF导出", RequestMethod.GET),
	// 导出到excel
	EXCEL("EXCEL导出", RequestMethod.GET),
	// 显示从Excel导入界面
	SHOWEXCELIMPORT("Excel导入", RequestMethod.GET),
	// 从Excel导入
	EXCELIMPORT("从Excel导入", RequestMethod.POST), 
	//上传附件
	UPLOAD("上传", RequestMethod.GET),
	// 返回
	BACK("返回", null), 
	// 自定义
	CUSTOM("自定义", null);

	private String title;
	private RequestMethod method;

	private EntityAction(final String title, final RequestMethod method) {
		this.title = title;
		this.method = method;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public RequestMethod getMethods() {
		return method;
	}

//	/**
//	 * 获取指定状态下可以执行的操作
//	 * @param viewType
//	 * @return
//	 */
//	public static EntityAction[] getActionsByViewType(ViewType viewType){
//		return actionConfigs.get(viewType);
//	}
//	
//	/**
//	 * 存储各种View状态下，可以使用的操作
//	 */
//	private static Map<ViewType, EntityAction[]> actionConfigs;
//	
//	static {
//		actionConfigs = new LinkedHashMap<>();
//		actionConfigs.put(ViewType.NEW, new EntityAction[] { EntityAction.SAVE, EntityAction.SAVEANDNEW, EntityAction.BACK });
//		actionConfigs.put(ViewType.EDIT, new EntityAction[] { EntityAction.SAVEUPDATE, EntityAction.BACK });
//		actionConfigs.put(ViewType.VIEW, new EntityAction[] { EntityAction.NEW, EntityAction.EDIT, EntityAction.DEL, EntityAction.PRINT, EntityAction.PDF, EntityAction.BACK });
//	}
}
