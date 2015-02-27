package com.varasofttech.pojo.table_per_concrete_class;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
/**
 * 
 * @author Ranga Reddy
 * @date Feb 24, 2015
 * @version 1.0
 * @description : Person.java
 */

@Entity
@Table(name="Persons")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Person implements Serializable {
	private static final long serialVersionUID = 6281099766897902592L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long id;
	@Column
    private String firstName;
	@Column
    private String lastName;
	@Column
    private int age;
    
	// Constructor
    public Person() {
		super();
	}
    
	// Getter & Setter methods
    public long getId() {
   	 return id;
    }
    public void setId(long id) {
   	 this.id = id;
    }
    public String getFirstName() {
   	 return firstName;
    }
    public void setFirstName(String firstName) {
   	 this.firstName = firstName;
    }
    public String getLastName() {
   	 return lastName;
    }
    public void setLastName(String lastName) {
   	 this.lastName = lastName;
    }
    public int getAge() {
   	 return age;
    }
    public void setAge(int age) {
   	 this.age = age;
    }
    
	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", age=" + age + "]";
	}   	 
}
