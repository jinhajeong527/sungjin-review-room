package com.sungjin.reviewroom.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wishlist")
@Getter
@Setter
@NoArgsConstructor
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="reviewer_id")
    @JsonIgnore
    private Reviewer reviewer;

    @OneToOne
    @JoinColumn(name="show_id")
    private Show show;

    @Column(name = "created_date")
    @CreationTimestamp
    private Date createdDate;

    public Wishlist(Show show, Reviewer reviewer) {
        this.show = show;
        this.reviewer = reviewer;
    }
    
}
