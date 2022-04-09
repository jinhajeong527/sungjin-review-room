package com.sungjin.reviewroom.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_date")
    @CreationTimestamp
    private Date createdDate;

    @Column(name = "last_updated")
    @UpdateTimestamp
    private Date lastUpdated;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="show_id")
    private Show show;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
                          CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name="reviewer_id")
    private Reviewer reviewer;


    
}
