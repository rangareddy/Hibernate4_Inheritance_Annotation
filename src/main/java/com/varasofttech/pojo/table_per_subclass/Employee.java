package com.varasofttech.pojo.table_per_subclass;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author Ranga Reddy
 * @date Feb 24, 2015
 * @version 1.0
 * @description : Employee.java
 */
@Entity
@Table(name="Employees")
public class Employee extends Person {
	private static final long serialVersionUID = -5050456251835366554L;
	@Column
	private String designation;
	@Column
    private Double salary;
    
    public Employee() {
    	super();
    }
    
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}
	public Double getSalary() {
		return salary;
	}
	public void setSalary(Double salary) {
		this.salary = salary;
	}
	
	@Override
	public String toString() {
		return "Employee [designation=" + designation + ", salary=" + salary
				+ "]";
	}
}
