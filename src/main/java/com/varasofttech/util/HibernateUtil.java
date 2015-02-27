package com.varasofttech.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * 
 * @author Ranga Reddy
 * @date Feb 24, 2015
 * @version 1.0
 * @description : HibernateUtil.java
 */
public class HibernateUtil {
	private static SessionFactory sessionFactory = null;
	
	public static SessionFactory getSessionFacoty() {
		return buildSessionFactory();
	}
	
	private static SessionFactory buildSessionFactory() {
		Configuration configuration = new Configuration();
		configuration.configure("hibernate.cfg.xml");
		
		/*configuration.addAnnotatedClass(com.varasofttech.pojo.table_per_concrete_class.Person.class);
		configuration.addAnnotatedClass(com.varasofttech.pojo.table_per_concrete_class.Employee.class);
		configuration.addAnnotatedClass(com.varasofttech.pojo.table_per_concrete_class.PermanentEmployee.class); */
		
		/*configuration.addAnnotatedClass(com.varasofttech.pojo.table_per_hierarchy.Person.class);
		configuration.addAnnotatedClass(com.varasofttech.pojo.table_per_hierarchy.Employee.class);
		configuration.addAnnotatedClass(com.varasofttech.pojo.table_per_hierarchy.PermanentEmployee.class); */
		
		configuration.addAnnotatedClass(com.varasofttech.pojo.table_per_subclass.Person.class);
		configuration.addAnnotatedClass(com.varasofttech.pojo.table_per_subclass.Employee.class);
		configuration.addAnnotatedClass(com.varasofttech.pojo.table_per_subclass.PermanentEmployee.class); 
		
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties())
						.build();
		
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		return sessionFactory;		
	}
	
	public static void closeSessionFactory() {
		if(sessionFactory != null) 
			sessionFactory.close();
	}
}
