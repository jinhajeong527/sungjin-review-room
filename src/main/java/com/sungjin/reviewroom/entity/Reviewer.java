package com.sungjin.reviewroom.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reviewer")
@Setter
@Getter
@NoArgsConstructor
public class Reviewer {

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
                          CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "review")
    private Set<Review> reviews = new HashSet<>();

    public void add(Review review) {
        if(review != null) {
            if(reviews == null) {
                reviews = new HashSet<>();
            }
            reviews.add(review);
            review.setReviewer(this);
        }
    }
    
}
