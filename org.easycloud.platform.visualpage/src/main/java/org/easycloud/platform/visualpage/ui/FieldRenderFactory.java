package org.easycloud.platform.visualpage.ui;

import org.easycloud.platform.metadata.define.entity.FieldDefinition;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.visualpage.ui.control.CheckInputFieldRender;
import org.easycloud.platform.visualpage.ui.control.DateInputFieldRender;
import org.easycloud.platform.visualpage.ui.control.DateTimeInputFieldRender;
import org.easycloud.platform.visualpage.ui.control.EmailInputFieldRender;
import org.easycloud.platform.visualpage.ui.control.EncyptTextInputFieldRender;
import org.easycloud.platform.visualpage.ui.control.FileInputFieldRender;
import org.easycloud.platform.visualpage.ui.control.SearchRelationSelectRender;
import org.easycloud.platform.visualpage.ui.control.SysEnumSelectFieldRender;
import org.easycloud.platform.visualpage.ui.control.TextInputFieldRender;
import org.easycloud.platform.visualpage.ui.control.UrlInputFieldRender;

public class FieldRenderFactory {

	public static BaseFieldRender getRender(FieldDefinition field) {
		AssertUtil.parameterEmpty(field, "FieldRenderFactory.field");
		BaseFieldRender render = null;
		switch (field.getDataType()) {
		case CHECK:
			render = new CheckInputFieldRender();
			break;
		case SYSENUM:
			render = new SysEnumSelectFieldRender();
			break;
		case URL:
			render = new UrlInputFieldRender();
			break;
		case DATE:
			render = new DateInputFieldRender();
			break;
		case DATETIME:
			render = new DateTimeInputFieldRender();
			break;
		case EMAIL:
			render = new EmailInputFieldRender();
			break;
		case FILE:
			render = new FileInputFieldRender();
			break;
		case ENCPYTTEXT:
			render = new EncyptTextInputFieldRender();
			break;
		case SEARCHRELATION:
		case FLYSEARCHRELATION:
			render = new SearchRelationSelectRender();
			break;
		case FORMULA:
		case LOCATION:
		case AUTOINC:
		case FLYMDRELATION:
		case MDRELATION:
		case MONEY:
		default:
			render = new TextInputFieldRender();
			break;
		}
		render.setField(field);
		return render;
	}

}
