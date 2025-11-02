/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "RequestForLeave")
public class RequestForLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rid")
    private int rid;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private Employee createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false)
    private Date createdTime;

    @Temporal(TemporalType.DATE)
    @Column(name = "from_date", nullable = false)
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "to_date", nullable = false)
    private Date toDate;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "status", nullable = false)
    private int status; // 0: Inprogress, 1: Approved, 2: Rejected

    @ManyToOne
    @JoinColumn(name = "processed_by")
    private Employee processedBy;
    
    @Column(name = "manager_comment")
    private String managerComment;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("change_time ASC") // Sắp xếp lịch sử theo thời gian
    private List<RequestHistory> history;
    
    // Getters and Setters...

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Employee getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Employee processedBy) {
        this.processedBy = processedBy;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public List<RequestHistory> getHistory() {
        return history;
    }

    public void setHistory(List<RequestHistory> history) {
        this.history = history;
    }
    

    // Thêm hàm tiện ích để lấy trạng thái
    public String getStatusText() {
        switch (this.status) {
            case 0: return "In Progress";
            case 1: return "Approved";
            case 2: return "Rejected";
            default: return "Unknown";
        }
    }
}
