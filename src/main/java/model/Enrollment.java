/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.io.Serializable;

// Lớp khóa chính tổng hợp
@Embeddable
class EnrollmentId implements Serializable {
    @Column(name = "uid")
    private int uid;

    @Column(name = "eid")
    private int eid;
    
    // hashCode, equals, getters, setters
    @Override
    public int hashCode() {
        // ...
        return super.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        // ...
        return super.equals(obj);
    }
}

@Entity
@Table(name = "Enrollment")
public class Enrollment {

    @EmbeddedId
    private EnrollmentId id;

    @ManyToOne
    @MapsId("uid") // Map tới phần 'uid' của EmbeddedId
    @JoinColumn(name = "uid")
    private User user;

    @ManyToOne
    @MapsId("eid") // Map tới phần 'eid' của EmbeddedId
    @JoinColumn(name = "eid")
    private Employee employee;

    @Column(name = "active")
    private boolean active;

    // Getters and Setters
    public EnrollmentId getId() { return id; }
    public void setId(EnrollmentId id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
