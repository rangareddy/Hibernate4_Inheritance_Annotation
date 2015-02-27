package com.varasofttech.pojo.table_per_concrete_class;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author Ranga Reddy
 * @date Feb 24, 2015
 * @version 1.0
 * @description : PermanentEmployee.java
 */
@Entity
@Table(name="PermanentEmployees")
public class PermanentEmployee extends Employee {
	private static final long serialVersionUID = -9128526681505398594L;
	@Column
	private double allowance;
	@Column
    private int noOfLeaves;
    
    public PermanentEmployee() {
		super();
	}
	// Getter & Setter methods 
    public double getAllowance() {
   	 return allowance;
    }
    public void setAllowance(double allowance) {
   	 this.allowance = allowance;
    }
    public int getNoOfLeaves() {
   	 return noOfLeaves;
    }
    public void setNoOfLeaves(int noOfLeaves) {
   	 this.noOfLeaves = noOfLeaves;
    }
	@Override
	public String toString() {
		return "PermanentEmployee [allowance=" + allowance + ", noOfLeaves="
				+ noOfLeaves + "]";
	}  
}
