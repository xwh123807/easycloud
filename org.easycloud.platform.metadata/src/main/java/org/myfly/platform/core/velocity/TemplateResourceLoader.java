package org.myfly.platform.core.velocity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;
import org.apache.velocity.util.ClassUtils;
import org.myfly.platform.core.utils.AssertUtil;

/**
 * 从每个jar中获取/templates目录下的velocity模板
 * 
 * @author xiangwanhong
 *
 */
public class TemplateResourceLoader extends ResourceLoader {
	private static Log log = LogFactory.getLog(TemplateResourceLoader.class);
	/**
	 * 模板文件，key为模板文件名
	 */
	private Map<String, String> templateFiles = new HashMap<>();
	/**
	 * 
	 */
	private Map<String, JarFile> jarFiles = new HashMap<>();

	@Override
	public void init(ExtendedProperties configuration) {
		if (log.isTraceEnabled()) {
			log.trace("TemplateResourceLoader init .");
		}

		String jarPaths = System.getProperty("java.class.path");
		String prefix = "myfly."; // "velo";// "myfly.";
		List<String> paths = new ArrayList<>();
		for (String path : jarPaths.split(":")) {
			String jarName = path.substring(path.lastIndexOf(File.separator) + 1);
			if (jarName.endsWith(".jar")) {
				if (jarName.startsWith(prefix)) {
					paths.add(path);
				}
			} else {
				if (!path.endsWith(String.valueOf(File.separatorChar)))
					path += "/";
				paths.add(path);
			}
		}
		loadJars(paths.toArray(new String[] {}));
	}

	/**
	 * 读取jar中的模板
	 * 
	 * @param jarPaths
	 */
	private void loadJars(String[] paths) {
		for (String jarPath : paths) {
			if (log.isDebugEnabled()) {
				log.debug(jarPath);
			}
			if (jarPath.endsWith(".jar")) {
				// jar
				try {
					JarFile jarFile = new JarFile(jarPath);
					Enumeration<JarEntry> enties = jarFile.entries();
					while (enties.hasMoreElements()) {
						JarEntry entry = enties.nextElement();
						if (entry.getName().startsWith("templates/") && entry.getName().endsWith(".vm")) {
							String templateFile = entry.getName().substring("templates/".length());
							templateFiles.put(templateFile, jarFile.getName());
						}
					}
					jarFiles.put(jarPath, jarFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				loadTemplatesFromDirectory(jarPath);
			}
		}
	}

	/**
	 * 
	 * @param jarPath
	 */
	private void loadTemplatesFromDirectory(String jarPath) {
		// 目录
		File templatesPath = new File(jarPath + "templates");
		if (templatesPath.exists() && templatesPath.isDirectory()) {
			Collection<File> files = FileUtils.listFiles(templatesPath, new String[] { "vm" }, true);
			for (File file : files) {
				if (file.getName().endsWith(".vm")) {
					String templateFile = file.getAbsolutePath()
							.substring(templatesPath.getAbsolutePath().length() + 1);
					if (!templateFiles.containsKey(templateFile)) {
						templateFiles.put(templateFile, file.getAbsolutePath());
					} else {
						if (log.isWarnEnabled()) {
							log.warn("[" + templateFiles.get(templateFile) + "]已经存在模板文件[" + templateFile + "]，将忽略["
									+ jarPath + "]中的。");
						}
					}
				}
			}
		}
	}

	@Override
	public InputStream getResourceStream(String templateName) throws ResourceNotFoundException {
		AssertUtil.parameterEmpty(templateName, "templateName");
		String tmplateFile = templateFiles.get(templateName);
		if (StringUtils.isBlank(tmplateFile)) {
			try {
				return ClassUtils.getResourceAsStream(getClass(), templateName);
			} catch (Exception e) {
			}
		}
		AssertUtil.parameterEmpty(tmplateFile, "tmplateFile");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(tmplateFile);
		} catch (Exception e) {
			if (log.isErrorEnabled()) {
				log.error("获取模板[" + templateName + "]失败");
			}
		}
		return fis;
	}

	@Override
	public boolean isSourceModified(Resource resource) {
		return false;
	}

	@Override
	public long getLastModified(Resource resource) {
		return 0;
	}

}
