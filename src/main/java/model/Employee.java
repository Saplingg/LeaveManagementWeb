/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @Column(name = "eid")
    private int eid;

    @Column(name = "ename")
    private String ename;

    @ManyToOne
    @JoinColumn(name = "did")
    private Division division;

    @ManyToOne
    @JoinColumn(name = "supervisorid")
    private Employee supervisor;

    @OneToMany(mappedBy = "supervisor")
    private Set<Employee> subordinates; // Danh sách cấp dưới

    @OneToOne
    @JoinTable(name = "Enrollment",
        joinColumns = @JoinColumn(name = "eid"),
        inverseJoinColumns = @JoinColumn(name = "uid")
    )
    private User user; // Liên kết Employee với User
    
    // Getters and Setters...

    public int getEid() {
        return eid;
    }

    public String getEname() {
        return ename;
    }

    public Division getDivision() {
        return division;
    }

    public Employee getSupervisor() {
        return supervisor;
    }

    public Set<Employee> getSubordinates() {
        return subordinates;
    }

    public User getUser() {
        return user;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public void setSupervisor(Employee supervisor) {
        this.supervisor = supervisor;
    }

    public void setSubordinates(Set<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
}
