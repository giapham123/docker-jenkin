package com.dou.adm.security;

import com.dou.adm.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class JwtUser implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private long isAdmin;
    private Collection<? extends GrantedAuthority> authorities;


    public JwtUser(){}

    public JwtUser(String username, long isAdmin) {
        this.username = username.toUpperCase();
        this.isAdmin = isAdmin;
    }

    public JwtUser(Long id, String username, String password, long isAdmin, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username.toUpperCase();
        this.password = password;
        this.isAdmin = isAdmin;
        this.authorities = authorities;
    }

    public static JwtUser create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
                new SimpleGrantedAuthority(role.getRoleCd())
        ).collect(Collectors.toList());

        return new JwtUser(
                user.getId(),
                user.getAccountId(),
                user.getPassword(),
                0,
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public long getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(long isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
