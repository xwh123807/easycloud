package org.easycloud.platform.core.user.internal;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.easycloud.platform.core.context.UserContext;
import org.easycloud.platform.core.domain.SBaseEntity;
import org.easycloud.platform.core.user.domain.Menu;
import org.easycloud.platform.core.user.service.IMenuService;
import org.easycloud.platform.metadata.utils.AssertUtil;
import org.easycloud.platform.metadata.utils.DateUtil;
import org.easycloud.platform.metadata.utils.JSONUtil;
import org.easycloud.platform.metadata.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

/**
 * 菜单服务
 * 
 * @author xiangwanhong
 *
 */
@Service
public class MenuService implements IMenuService {
	@Autowired
	private IJpaAccessService dataAccessService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.myfly.platform.system.service.internal.IMenuService#getAllMenus()
	 */
	@Override
	public List<Menu> getAllMenus() {
		List<Menu> list = new ArrayList<>();
		for (Menu item : dataAccessService.findAll(Menu.class, new Sort(Direction.ASC, "orderNumber"))){
			//只返回顶级菜单
			if (item.getParent() == null){
				list.add(item);
			}
		}
		return list;
	}

	@Override
	public void registerMenus(URL url) {
		Menu[] menus = JSONUtil.fromJSON(url, Menu[].class);
		if (ArrayUtils.isNotEmpty(menus)) {
			long orderNumber = 0;
			for (Menu menu : menus) {
				if (menu.getOrderNumber() == null){
					menu.setOrderNumber(orderNumber);
				}
				registerMenu(menu);
				orderNumber ++;
			}
		}
	}

	@Override
	@Transactional
	public void registerMenu(Menu menu) {
		AssertUtil.parameterEmpty(menu, "menu");
		updateMenuAudit(menu);
		dataAccessService.saveEntity(menu);
	}

	/**
	 * 递归更新审计信息
	 * 
	 * @param menu
	 */
	private void updateMenuAudit(Menu menu) {
		updateAudit(menu);
		if (CollectionUtils.isNotEmpty(menu.getSubMenus())) {
			long orderNumber = 0;
			for (Menu item : menu.getSubMenus()) {
				if (item.getOrderNumber() == null){
					item.setOrderNumber(orderNumber);
				}
				item.setParent(menu);
				updateMenuAudit(item);
				orderNumber ++;
			}
		}
	}

	/**
	 * 更新实体系统信息
	 * 
	 * @param entity
	 */
	private void updateAudit(SBaseEntity entity) {
		if (StringUtils.isBlank(entity.getUid())){
			entity.setUid(UUIDUtil.newUUID());
		}
		if (entity.getCreated() == null) {
			entity.setCreated(DateUtil.nowSqlTimestamp());
		}
		if (entity.getCreatedBy() == null) {
			entity.setCreatedBy(UserContext.getUserSession().getUser());
		}
		if (entity.getUpdated() == null) {
			entity.setUpdated(entity.getCreated());
		}
		if (entity.getUpdatedBy() == null) {
			entity.setUpdatedBy(entity.getCreatedBy());
		}
		if (entity.getTenant() == null) {
			entity.setTenant(UserContext.getUserSession().getTenant());
		}
	}
}
