/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Division")
public class Division {
    @Id
    @Column(name = "did")
    private int did;

    @Column(name = "dname")
    private String dname;

    @OneToMany(mappedBy = "division")
    private Set<Employee> employees;

    // Getters and Setters
    public int getDid() {
        return did;
    }
    public void setDid(int did) {
        this.did = did;
    }
    public String getDname() {
        return dname;
    }
    public void setDname(String dname) {
        this.dname = dname;
    }
    public Set<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }
}
