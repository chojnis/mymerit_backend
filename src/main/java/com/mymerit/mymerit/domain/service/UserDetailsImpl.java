package com.mymerit.mymerit.domain.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mymerit.mymerit.domain.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class UserDetailsImpl implements UserDetails, OAuth2User {
    private String id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private String role;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attributes;

    public UserDetailsImpl(String id, String username, String email, String password, String role, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole(),
                authorities
        );
    }

    public static UserDetailsImpl build(User user, Map<String, Object> attributes) {
        UserDetailsImpl userDetailsImpl = UserDetailsImpl.build(user);
        userDetailsImpl.setAttributes(attributes);

        return userDetailsImpl;
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
    public String getName() {
        return String.valueOf(id);
    }
}