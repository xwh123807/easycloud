package org.easycloud.platform.core.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

import org.myfly.platform.core.metadata.annotation.FieldView;

/**
 * 只包含主键的实体基类
 * @author xiangwanhong
 *
 */
@MappedSuperclass
@Inheritance(strategy=InheritanceType.JOINED)
public class SKeyEntity implements IKeyEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2942054081933531779L;
	
	@Id
	@org.springframework.data.annotation.Id
	@Column(length=32, updatable=false)
	//TODO 屏蔽自动生成值，由于hibernate设置自动生成值后，无法强制手工赋值
	//@GeneratedValue(generator="system-uuid")
	//@GenericGenerator(name="system-uuid", strategy="uuid")
	@FieldView(title="UID", dataType=FieldDataType.UUID)
	private String uid;

	@Override
	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	@Override
	public String toString() {
		return "uid: " + getUid();
	}
}
