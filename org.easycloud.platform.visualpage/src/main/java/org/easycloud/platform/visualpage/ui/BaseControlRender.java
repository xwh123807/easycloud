package org.easycloud.platform.visualpage.ui;

import org.easycloud.platform.metadata.utils.AssertUtil;

/**
 * 控件基类
 * 
 * @author xiangwanhong
 *
 */
public abstract class BaseControlRender {
	/**
	 * 显示方式
	 */
	private ViewType viewType = ViewType.VIEW;

	/**
	 * 控件类型
	 */
	// public abstract ControlTpye getControlType();

	/**
	 * 新增模式
	 * 
	 * @return
	 */
	public abstract String controlForNew();

	/**
	 * 编辑模式，只生成控件html内容
	 * 
	 * @return
	 */
	public abstract String controlForEdit();

	/**
	 * 新增模式
	 * @return
	 */
	public abstract String htmlForNew();
	/**
	 * 编辑模式，生成全部html内容，包含布局
	 * 
	 * @return
	 */
	public abstract String htmlForEdit();

	/**
	 * 只读模式，生成全部html内容，包含布局
	 * 
	 * @return
	 */
	public abstract String htmlForView();

	/**
	 * 只读模式，只生成控件html内容
	 * 
	 * @return
	 */
	public abstract String controlForView();

	/**
	 * 打印模式，生成全部html内容，包含布局
	 * 
	 * @return
	 */
	public abstract String htmlForPrint();

	/**
	 * 打印模式，只生成控件html内容
	 * 
	 * @return
	 */
	public abstract String controlForPrint();
	
	public abstract String htmlForSearch();
	
	public abstract String controlForSearch();

	/**
	 * 根据显示模式，返回控件html内容
	 * 
	 * @return
	 */
	public String control() {
		String result = "";
		switch (getViewType()) {
		case NEW:
			result = controlForNew();
			break;
		case VIEW:
			result = controlForView();
			break;
		case PRINT:
			result = controlForPrint();
			break;
		case EDIT:
			result = controlForEdit();
			break;
		case SEARCH:
			result = controlForSearch();
			break;
		default:
			AssertUtil.notSupport(getViewType().getTitle());
		}
		return result;
	}

	/**
	 * 根据显示模式，全部全部html内容，包含布局
	 * 
	 * @return
	 */
	public String html() {
		String result = "";
		switch (getViewType()) {
		case NEW:
			result = htmlForNew();
			break;
		case VIEW:
			result = htmlForView();
			break;
		case PRINT:
			result = htmlForPrint();
			break;
		case EDIT:
			result = htmlForEdit();
			break;
		case SEARCH:
			result = htmlForSearch();
			break;
		default:
			AssertUtil.notSupport(getViewType().getTitle());
		}
		return result;
	}

	public ViewType getViewType() {
		return viewType;
	}

	public void setViewType(ViewType viewType) {
		this.viewType = viewType;
	}
}
