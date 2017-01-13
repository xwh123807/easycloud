package org.easycloud.platform.core.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easycloud.platform.core.domain.IKeyEntity;
import org.easycloud.platform.metadata.annotation.BaseEnum;

public class ClassUtil {
	private static final Log log = LogFactory.getLog(ClassUtil.class);

	/**
	 * @param cls
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> Map<String, T> getFieldsAnnotations(Class<?> cls, Class<T> annotationClass) {
		Map<String, T> items = new HashMap<>();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			T annotation = field.getAnnotation(annotationClass);
			if (annotation != null) {
				items.put(field.getName(), annotation);
			}
		}
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			T annotation = method.getAnnotation(annotationClass);
			if (annotation != null) {
				items.put(method.getName(), annotation);
			}
		}
		return items;
	}

	/**
	 * 
	 * @param <T>
	 * @param cls
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T getClassAnnotation(Class<?> cls, Class<T> annotationClass) {
		return cls.getAnnotation(annotationClass);
	}

	/**
	 * 获取类的短名称
	 * 
	 * @param className
	 * @return
	 */
	public static String getClassShortName(String className) {
		return className.substring(className.lastIndexOf(".") + 1);
	}

	/**
	 * 获取类的注解
	 * 
	 * @param className
	 * @param annotationClass
	 * @return
	 */
	public static <T extends Annotation> T getClassAnnotation(final String className, final Class<T> annotationClass) {
		AssertUtil.parameterEmpty(className, "className");
		AssertUtil.parameterEmpty(annotationClass, "annotationClass");
		Class<?> cls = null;
		try {
			cls = Class.forName(className);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("加载类失败，" + e.getMessage());
			}
		}
		AssertUtil.parameterEmpty(cls, "Class");
		return cls.getAnnotation(annotationClass);
	}

	/**
	 * 将值转换为字符串
	 * 
	 * @param value
	 * @return
	 */
	public static String convertValueToString(Object value) {
		if (value == null) {
			return "";
		} else {
			return convert(value, String.class);
		}
	}

	/**
	 * 值类型转换，将source转换为targetType类型
	 * 
	 * @param source
	 * @param targetType
	 * @return
	 */
	public static <T> T convert(Object source, Class<T> targetType) {
		if (targetType.getSuperclass() == IKeyEntity.class && source instanceof String) {
			// 为实体查找关系
			try {
				IKeyEntity entity = (IKeyEntity) targetType.newInstance();
				entity.setUid((String) source);
				return (T) entity;
			} catch (Exception e) {
				AssertUtil.parameterInvalide((String) source, e.getMessage());
			}
		}
		return AppUtil.getConversionService().convert(source, targetType);
	}

	private static final Map<String, BaseEnum> baseEnums = new HashMap<>();

	/**
	 * 根据枚举值获取枚举类
	 * 
	 * @param title
	 * @return
	 */
	public static BaseEnum getEnumByTitle(Class enumClass, String title) {
		String key = enumClass.getName() + "-" + title;
		BaseEnum result = baseEnums.get(key);
		if (result == null) {
			List<BaseEnum> list = EnumUtils.getEnumList(enumClass);
			for (BaseEnum item : list) {
				if (item.getTitle().equals(title)) {
					result = item;
					baseEnums.put(key, item);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * 根据枚举名称转换为枚举值
	 * @param enumClassName
	 * @param name
	 * @return
	 */
	public static String getEnumTitleByName(final String enumClassName, final String name) {
		try {
			Class enumClass = Class.forName(enumClassName);
			BaseEnum baseEnum = (BaseEnum) Enum.valueOf(enumClass, name);
			return baseEnum.getTitle();
		} catch (Exception e) {
			return name;
		}
	}

	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param pack
	 * @return
	 */
	public static Set<Class<?>> getClasses(String pack) {

		// 第一个class类的集合
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// System.err.println("file类型的扫描");
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					// System.err.println("jar类型的扫描");
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx).replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class") && !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										try {
											// 添加到classes
											classes.add(Class.forName(packageName + '.' + className));
										} catch (ClassNotFoundException e) {
											// log
											// .error("添加用户自定义视图类错误
											// 找不到此类的.class文件");
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						// log.error("在扫描用户定义视图时从jar包获取文件出错");
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
			Set<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("用户定义包名 " + packageName + " 下没有任何文件");
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
						classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 添加到集合中去
					// classes.add(Class.forName(packageName + '.' +
					// className));
					// 经过回复同学的提醒，这里用forName有一些不好，会触发static方法，没有使用classLoader的load干净
					classes.add(
							Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					// log.error("添加用户自定义视图类错误 找不到此类的.class文件");
					e.printStackTrace();
				}
			}
		}
	}
}
