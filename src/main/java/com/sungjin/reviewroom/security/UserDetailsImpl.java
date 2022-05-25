package com.sungjin.reviewroom.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sungjin.reviewroom.entity.Reviewer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

    private int id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean verified;
    
    public UserDetailsImpl(int id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities, boolean verified) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.verified = verified;
    }
    public static UserDetailsImpl build(Reviewer reviewer) {
        List<GrantedAuthority> authorities = reviewer.getRoles().stream()
                                            .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                                            .collect(Collectors.toList());
        return new UserDetailsImpl(reviewer.getId(), 
                                   reviewer.getFirstName() + " " + reviewer.getLastName(), //이렇게 사용가능한지 체크 필요하다.
                                   reviewer.getEmail(), 
                                   reviewer.getPassword(), 
                                   authorities,
                                   reviewer.isVerified());
    }

    //id 및 email은 UserDetails의 디폴트 리턴 정보가 아니다. 즉 오버라이드 메서드가 아니다.
    public int getId() {
		return id;
	}
	public String getEmail() {
		return email;
	}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }
     
    @Override
    public String getUsername() {
        return username;
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
        return verified;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(this == obj)
         return true;
        if (obj == null || getClass() != obj.getClass())
         return false;
        UserDetailsImpl reviewer = (UserDetailsImpl) obj;
        return Objects.equals(id, reviewer.id);
    }

    
    
}
