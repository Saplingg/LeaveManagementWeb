/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "[User]")
public class User {
    @Id
    @Column(name = "uid")
    private int uid;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "displayname")
    private String displayName;

    @ManyToMany(fetch = FetchType.EAGER) // Tải EAGER để lấy quyền ngay khi login
    @JoinTable(
            name = "UserRole",
            joinColumns = @JoinColumn(name = "uid"),
            inverseJoinColumns = @JoinColumn(name = "rid")
    )
    private Set<Role> roles;
    
    // Getters and Setters...

    public int getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    
}
