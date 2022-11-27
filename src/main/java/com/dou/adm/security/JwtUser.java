package com.dou.adm.security;

import com.dou.adm.models.User;
import com.dou.adm.models.UserProfiles;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

public class JwtUser {

    @JsonIgnore
    private Long id;

    private String username;

    private String password;

    @JsonIgnore
    UserProfiles profiles;

    public JwtUser() {}

    public JwtUser(Long id, String username){
        this.id = id;
        this.username = username;
    }

    public JwtUser(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getAccountId()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserProfiles getProfiles() {
        return profiles;
    }

    public void setProfiles(UserProfiles profiles) {
        this.profiles = profiles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JwtUser that = (JwtUser) o;
        return Objects.equals(this.username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
