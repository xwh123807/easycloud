package org.easycloud.platform.core.context;

import java.util.HashMap;
import java.util.Map;

import org.easycloud.platform.core.utils.AssertUtil;

/**
 * 用户上下文
 * 
 * @author xiangwanhong
 *
 */
public class UserContext {
	/**
	 * 用户Tokenid，存储在cookie中，在系统filter中，从cookie中取出，赋给线程变量
	 */
	private static ThreadLocal<String> userTokenid;

	/**
	 * 存放用户会话信息。<br>
	 * TODO xwh 集群下会有问题，要使用分布式缓存<br>
	 * 
	 */
	private static Map<String, UserSession> userSessions = new HashMap<>();

	/**
	 * 登录成功后调用
	 * 
	 * @param userSession
	 */
	public static void addUserSession(UserSession userSession) {
		
	}

	/**
	 * 获取用户会话信息
	 * 
	 * @param tokenid
	 * @return
	 */
	public static UserSession getUserSession(String tokenid) {
		AssertUtil.parameterEmpty(tokenid, "tokenid");
		UserSession userSession = userSessions.get(tokenid);
		AssertUtil.parameterEmpty(userSession, "userSession", "用户还未登录或tokenid不合法.");
		return userSession;
	}

	/**
	 * 获取当前登录用户的tokenid
	 * 
	 * @return
	 */
	public static String getUserTokenid() {
		return SystemContext.SYS_SUSER_UID;
	}

	/**
	 * 设置当前登录用户的tokenid
	 * 
	 * @param tokenid
	 */
	public void setUserTokenid(String tokenid) {
		userTokenid.set(tokenid);
	}

	/**
	 * 获取用户登录上下文
	 * 
	 * @return
	 */
	public static UserSession getUserSession() {
		return getUserSession(getUserTokenid());
	}

	static {
		// TODO 系统初始化时填值，仅用于开发不登录不验证用户身份
		UserSession userSession = new UserSession();
		userSession.setTokenuid(SystemContext.SYS_SUSER_UID);
		SUser user = new SUser();
		user.setUid(SystemContext.SYS_SUSER_UID);
		userSession.setUser(user);
		Tenant tenant = new Tenant();
		tenant.setUid(SystemContext.SYS_STENANT_UID);
		userSession.setTenant(tenant);
		userSession.setLayoutName(VisualPageConstants.LAYOUT_DEFAULT_NAME);
		userSessions.put(userSession.getTokenuid(), userSession);
	}
}
