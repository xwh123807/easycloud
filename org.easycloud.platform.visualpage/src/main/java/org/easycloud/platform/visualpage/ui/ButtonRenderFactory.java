package org.easycloud.platform.visualpage.ui;

import org.easycloud.platform.metadata.annotation.view.EntityAction;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.ui.control.button.BackButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.CustomButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.DelSubmitButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.EditButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.ExcelButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.NewButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.PdfButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.PrintButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.SaveAndNewSubmitButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.SaveSubmitButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.SubmitButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.UpdateSaveSubmitButtonRender;
import org.easycloud.platform.visualpage.ui.control.button.UrlButtonRender;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 获取ButtonRender，常用ButtonRender在此注册
 * 
 * @author xiangwanhong
 *
 */
public class ButtonRenderFactory {
	/**
	 * 根据Button类型，获取Button
	 * 
	 * @param buttonType
	 * @return
	 */
	private static BaseButtonRender getRender(EntityAction action) {
		AssertUtil.parameterEmpty(action, "action");
		BaseButtonRender render = null;
		switch (action) {
		case BACK:
			render = new BackButtonRender();
			break;
		case EXCEL:
			render = new ExcelButtonRender();
			break;
		case PDF:
			render = new PdfButtonRender();
			break;
		case PRINT:
			render = new PrintButtonRender();
			break;
		case SAVE:
			render = new SaveSubmitButtonRender();
			break;
		case SAVEANDNEW:
			render = new SaveAndNewSubmitButtonRender();
			break;
		case DEL:
			render = new DelSubmitButtonRender();
			break;
		case EDIT:
			render = new EditButtonRender();
			break;
		case CUSTOM:
			render = new CustomButtonRender();
			break;
		case SAVEUPDATE:
			render = new UpdateSaveSubmitButtonRender();
			break;
		case ALL:
			break;
		case DELALL:
			break;
		case EXCELIMPORT:
			break;
		case LIST:
			break;
		case NEW:
			render = new NewButtonRender();
			break;
		case OUTLINE:
			break;
		case SHOWEXCELIMPORT:
			break;
		case VIEW:
			break;
		default:
			break;
		}
		AssertUtil.parameterEmpty(render, "render-" + action, "通过实现并在ButtonRenderFactory注册该按钮。");
		return render;
	}
	
	public static BaseButtonRender getRender(EntityAction action, String url){
		if (EntityAction.BACK.equals(action)){
			return new BackButtonRender();
		}
		if (RequestMethod.GET.equals(action.getMethods())){
			return new UrlButtonRender(action.getTitle(), url);
		}else{
			return new SubmitButtonRender(action.getTitle(), action.getMethods(), url);
		}
	}
}
