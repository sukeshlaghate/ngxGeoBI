/**
 * Created by : Sukesh Laghate
 * Created on : 11-01-2018
 **/
package com.ngxGeoBI.security.ajax.dto;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class UserContext {
    private final String username;
    private final long userid;
    private final List<GrantedAuthority> authorities;

    private UserContext(String username,long userid,  List<GrantedAuthority> authorities) {
        this.username = username;
        this.userid  = userid;
        this.authorities = authorities;
    }

    public static UserContext create(String username, long userid, List<GrantedAuthority> authorities) {
        if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username is blank: " + username);
        return new UserContext(username, userid, authorities);
    }

    public String getUsername() {
        return username;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public long getUserid() { return userid;}
}
