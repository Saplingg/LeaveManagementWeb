/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "RequestHistory")
public class RequestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hid")
    private int hid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rid", nullable = false)
    private RequestForLeave request;

    @ManyToOne
    @JoinColumn(name = "changed_by_eid", nullable = false)
    private Employee changedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "change_time", nullable = false)
    private Date changeTime;
    
    @Column(name = "old_status")
    private Integer oldStatus;
    
    @Column(name = "new_status", nullable = false)
    private int newStatus;
    
    @Column(name = "comment")
    private String comment;

    // Getters and Setters...

    public int getHid() {
        return hid;
    }

    public void setHid(int hid) {
        this.hid = hid;
    }

    public RequestForLeave getRequest() {
        return request;
    }

    public void setRequest(RequestForLeave request) {
        this.request = request;
    }

    public Employee getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Employee changedBy) {
        this.changedBy = changedBy;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Integer getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Integer oldStatus) {
        this.oldStatus = oldStatus;
    }

    public int getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
}
