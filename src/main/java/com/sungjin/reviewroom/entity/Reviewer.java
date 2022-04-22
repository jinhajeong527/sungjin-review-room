package com.sungjin.reviewroom.entity;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviewer")
@Setter
@Getter
@NoArgsConstructor
public class Reviewer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "join_date")
    @CreationTimestamp
    private Date joinDate;

    @Column(name = "mbti")
    private String mbti;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                          CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "reviewer")
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                                                   CascadeType.MERGE, CascadeType.REFRESH})
	@JoinTable(name = "reviewer_role", 
			   joinColumns = @JoinColumn(name = "reviewer_id"), 
			   inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
    
    //회원가입 기능 구현 시 사용할 생성자
    public Reviewer(String email, String password, String firstName, String lastName, String mbti) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mbti = mbti;
    }

    public void add(Review review) {
        if(review != null) {
            if(reviews == null) {
                reviews = new HashSet<>();
            }
            reviews.add(review);
            review.setReviewer(this);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


    
}
