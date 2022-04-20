package com.sungjin.reviewroom.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "`show`")
@Getter
@Setter
@NoArgsConstructor
public class Show {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "show")
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.DETACH, 
                                                   CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "show_genre", 
               joinColumns = @JoinColumn(name = "show_id"), 
               inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    public void addReview(Review review) {
		
		if(reviews == null) {
			reviews = new HashSet<>();
		}
		reviews.add(review);
		review.setShow(this);
	}

    public void addGenre(Genre genre) {
        if(genres == null) {
            genres = new HashSet<>();
        }
        genres.add(genre);
    }

}
