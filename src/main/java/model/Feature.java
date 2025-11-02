/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Feature")
public class Feature {
    @Id
    @Column(name = "fid")
    private int fid;

    @Column(name = "url")
    private String url;
    
    @Column(name = "fname")
    private String fname;
    
    @ManyToMany(mappedBy = "features")
    private Set<Role> roles;

    // Getters and Setters...
    
    public int getFid() {
        return fid;
    }

    public String getUrl() {
        return url;
    }

    public String getFname() {
        return fname;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }
    public Set<Role> getRoles() {
        return roles;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
