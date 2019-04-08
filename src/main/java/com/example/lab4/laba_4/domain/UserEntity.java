package com.example.lab4.laba_4.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;


@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "enabled")
    private Boolean enabled;
    @JsonIgnore
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Collection<ResultEntity> results;

    public UserEntity(String name, String password) {
        this.username = name;
        this.password = password;
    }

    public UserEntity() {
    }

    public UserEntity(String username, String password, Boolean enabled) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Collection<ResultEntity> getResults() {
        return results;
    }

    public void setResults(Collection<ResultEntity> results) {
        this.results = results;
    }
}
