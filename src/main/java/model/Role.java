/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Role")
public class Role {
    @Id
    @Column(name = "rid")
    private int rid;

    @Column(name = "rname")
    private String rname;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "RoleFeature",
            joinColumns = @JoinColumn(name = "rid"),
            inverseJoinColumns = @JoinColumn(name = "fid")
    )
    private Set<Feature> features;
    
    // Getters and Setters...

    public int getRid() {
        return rid;
    }

    public String getRname() {
        return rname;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }
    
}
