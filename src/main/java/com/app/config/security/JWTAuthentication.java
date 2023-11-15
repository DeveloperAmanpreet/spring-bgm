package com.app.config.security;

import com.app.common.user.Role.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JWTAuthentication implements Authentication {
    String ROLE_PREFIX = "ROLE_";

    private String token;
    private String username;
    private List<String> roleList;

    public void setRoleList(List<String> roleList) {
      this.roleList = roleList;
    }

    public JWTAuthentication(String token) {
        this.token = token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        if(roleList == null) {
            return null;
        }
        for (String role : roleList) {
            list.add(new SimpleGrantedAuthority(ROLE_PREFIX + role));
        }

        return list;
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public String getPrincipal() {
        return this.username;
    }

//    @Override
//    public UserDetails getPrincipal() {
//        return new User(username, "", getAuthorities());
//    }

    @Override
    public boolean isAuthenticated() {
        return username != null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (!isAuthenticated) {
            username = null;
        }
    }

    @Override
    public String getName() {
        return username;
    }
}
