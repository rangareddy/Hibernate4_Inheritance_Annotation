# Hibernate4_Inheritance_Annotation
This program demonstrates the how to use Inheritance in Hibernate using Annotations.

Hibernate supports the 3 types of Inheriatnce
----------------------------------------------------
1.	One table per one concrete class - Each subclass has a table having all the properties of super class also. (TABLE_PER_CLASS)
2.	One table per all hierarchical classes - Single table has all properties of every class in the hierarchy. (SINGLE_TABLE)
3.	One table per one concrete sub class - Each class is mapped in its own table. There is separate table for super class and subclass.  (JOINED)

1.	One table per one concrete class - Open HibernateUtil.java and enable the One table per one concreate class annotated class and run the Application.java
Here three tables are created.

2.	One table per all hierarchical classes - Open HibernateUtil.java and enable the One table per all hierarchical classes and run the Application2.java
Here only one table is created.

3.	One table per one concrete sub class - Open HibernateUtil.java and enable the One table per one concrete sub class annotated class and run the Application3.java
Here three tables are created.
