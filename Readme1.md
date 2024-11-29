```code

package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "employees") // Map to the "employees" table
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "department", nullable = false)
    private String department;

    @Column(name = "salary")
    private Double salary;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}






-- Step 1: Add an ID column
ALTER TABLE Transactions
ADD ID BIGINT IDENTITY(1,1);

-- Step 2: Drop the composite PK
ALTER TABLE Transactions
DROP CONSTRAINT PK_Transactions;

-- Step 3: Add ID as the new PK
ALTER TABLE Transactions
ADD CONSTRAINT PK_Transactions_New PRIMARY KEY CLUSTERED (ID);







````
