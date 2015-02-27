/**
 * 
 */
package com.varasofttech.configuration;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.annotation.Annotation;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.secure.spi.GrantedPermission;
import org.hibernate.secure.spi.JaccPermissionDeclarations;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 * @author Ranga Reddy
 * @date Feb 26, 2015
 * @version 1.0
 * @description : AnnotationConfiguration.java - Hibernate Configuration class
 */

@SuppressWarnings("serial")
public class AnnotationConfiguration extends Configuration {

	public AnnotationConfiguration() {
		super();
		configure( "/hibernate.cfg.xml" );		
	}
	
	public AnnotationConfiguration(String resource) {
		configure(resource);
	}

	@Override
	protected Configuration doConfigure(Document doc) throws HibernateException {
		Element sfNode = doc.getRootElement().element("session-factory");
		String name = sfNode.attributeValue("name");
		if (name != null) {
			Properties props = new Properties();
			props.setProperty(Environment.SESSION_FACTORY_NAME, name);
			addProperties(props);
		}
		addProperties(sfNode);
		parseSessionFactory(sfNode, name);

		Element secNode = doc.getRootElement().element("security");
		if (secNode != null) {
			parseSecurity(secNode);
		}
		System.out.println("Configured SessionFactory: " + name);
		System.out.println("properties: " + getProperties());
		return this;
	}

	private void addProperties(Element parent) {
		Properties props = new Properties();
		Iterator<?> itr = parent.elementIterator("property");
		while (itr.hasNext()) {
			Element node = (Element) itr.next();
			String name = node.attributeValue("name");
			String value = node.getText().trim();
			props.setProperty(name, value);
			if (!name.startsWith("hibernate")) {
				props.setProperty("hibernate." + name, value);
			}
		}
		addProperties(props);
		Environment.verifyProperties(getProperties());
	}

	private void parseSessionFactory(Element sfNode, String name) {
		Iterator<?> elements = sfNode.elementIterator();
		while (elements.hasNext()) {
			Element subelement = (Element) elements.next();
			String subelementName = subelement.getName();
			if ("mapping".equals(subelementName)) {
				parseMappingElement(subelement, name);
			} else if ("class-cache".equals(subelementName)) {
				String className = subelement.attributeValue("class");
				Attribute regionNode = subelement.attribute("region");
				final String region = (regionNode == null) ? className
						: regionNode.getValue();
				boolean includeLazy = !"non-lazy".equals(subelement
						.attributeValue("include"));
				setCacheConcurrencyStrategy(className,
						subelement.attributeValue("usage"), region, includeLazy);
			} else if ("collection-cache".equals(subelementName)) {
				String role = subelement.attributeValue("collection");
				Attribute regionNode = subelement.attribute("region");
				final String region = (regionNode == null) ? role : regionNode
						.getValue();
				setCollectionCacheConcurrencyStrategy(role,
						subelement.attributeValue("usage"), region);
			}
		}
	}

	private void parseSecurity(Element secNode) {
		final String nodeContextId = secNode.attributeValue("context");
		final String explicitContextId = getProperty(AvailableSettings.JACC_CONTEXT_ID);
		if (explicitContextId == null) {
			setProperty(AvailableSettings.JACC_CONTEXT_ID, nodeContextId);
		} else {
			if (!nodeContextId.equals(explicitContextId)) {
				throw new HibernateException("Non-matching JACC context ids");
			}
		}
		JaccPermissionDeclarations jaccPermissionDeclarations = new JaccPermissionDeclarations(
				nodeContextId);
		Iterator<?> grantElements = secNode.elementIterator();
		while (grantElements.hasNext()) {
			final Element grantElement = (Element) grantElements.next();
			final String elementName = grantElement.getName();
			if ("grant".equals(elementName)) {
				jaccPermissionDeclarations
						.addPermissionDeclaration(new GrantedPermission(
								grantElement.attributeValue("role"),
								grantElement.attributeValue("entity-name"),
								grantElement.attributeValue("actions")));
			}
		}
	}

	private void parseMappingElement(Element mappingElement, String name) {
		final Attribute resourceAttribute = mappingElement
				.attribute("resource");
		final Attribute fileAttribute = mappingElement.attribute("file");
		final Attribute jarAttribute = mappingElement.attribute("jar");
		final Attribute packageAttribute = mappingElement.attribute("package");
		final Attribute classAttribute = mappingElement.attribute("class");

		if (resourceAttribute != null) {
			final String resourceName = resourceAttribute.getValue();
			addResource(resourceName);
		} else if (fileAttribute != null) {
			final String fileName = fileAttribute.getValue();
			addFile(fileName);
		} else if (jarAttribute != null) {
			final String jarFileName = jarAttribute.getValue();
			addJar(new File(jarFileName));
		} else if (packageAttribute != null) {
			final String packageName = packageAttribute.getValue();
			addPackage(packageName);
		} else if (classAttribute != null) {
			final String classAttributeName = classAttribute.getValue();

			final List<String> classNames = new ArrayList<String>();

			if (classAttributeName.endsWith(".*")) {
				try {
					System.out.println("TRY " + classAttributeName);
					classNames
							.addAll(getAllAnnotatedClassNames(classAttributeName));
				} catch (IOException ioe) {
				} catch (URISyntaxException use) {
				}
			} else {
				classNames.add(classAttributeName);
			}

			for (String className : classNames) {
				try {
					addAnnotatedClass(ReflectHelper.classForName(className));
				} catch (Exception e) {
					throw new MappingException(
							"Unable to load class [ "
									+ className
									+ "] declared in Hibernate configuration <mapping/> entry",
							e);
				}
			}
		} else {
			throw new MappingException(
					"<mapping> element in configuration specifies no known attributes");
		}
	}

	/**
	 * Loading all annotated classes from the package
	 */
	private List<String> getAllAnnotatedClassNames(String fileAttributeName)
			throws URISyntaxException, FileNotFoundException, IOException {
		List<String> fileNames = new ArrayList<String>();
		String path = fileAttributeName.substring(0,
				fileAttributeName.lastIndexOf("."))
				.replace(".", File.separator);
		System.out.println("WILD: " + path);		
	    
	    String path2 = new File(".").getAbsolutePath();
	    path = path2.substring(0, path2.length()-1)+"src\\main\\java\\"+path;
	    File fPackageDir = new File(path);
	    
		if (!fPackageDir.exists()) {
			System.out.println("WILD !EXISTS");
			fileNames.add(fileAttributeName);
		} else {
			System.out.println("WILD ELSE2");
			File classFiles[] = fPackageDir.listFiles(new FilenameFilter() {
				public boolean accept(File file, String name) {
					return name.endsWith(".class");
				}
			});

			for (File classFile : classFiles) {
				System.out.println("CLASS: " + classFile.getName());
				DataInputStream dis = null;
				try {
					dis = new DataInputStream(new BufferedInputStream(
							new FileInputStream(classFile)));
					ClassFile cf = new ClassFile(dis);
					AnnotationsAttribute visible = (AnnotationsAttribute) cf
							.getAttribute(AnnotationsAttribute.visibleTag);
					for (Annotation ann : visible.getAnnotations()) {
						if (ann.getTypeName()
								.equals("javax.persistence.Entity")) {
							fileNames.add(cf.getName());
						}
					}
				} catch (IOException ioe) {
				} finally {
					if (dis != null) {
						dis.close();
					}
				}
			}
		}
		return fileNames;
	}
}