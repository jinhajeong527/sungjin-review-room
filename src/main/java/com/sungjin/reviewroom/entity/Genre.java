package com.sungjin.reviewroom.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "genre")
@Getter
@Setter
@NoArgsConstructor
public class Genre {

    @Id
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, 
                                                   CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "show_genre", 
               joinColumns = @JoinColumn(name = "genre_id"), 
               inverseJoinColumns = @JoinColumn(name = "show_id"))
    @JsonIgnore
    private Set<Show> shows;


    
}
