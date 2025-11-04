package com.g_wuy.hsf302.moviehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MovieID", nullable = false)
    private Integer id;

    @Size(max = 200)
    @NotNull
    @Column(name = "Title", nullable = false, length = 200)
    private String title;

    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "Duration")
    private Integer duration;

    @Column(name = "ReleaseDate")
    private LocalDate releaseDate;

    @Size(max = 50)
    @Column(name = "\"Language\"", length = 50)
    private String language;

    @ManyToMany
    @JoinTable(
            name = "movie_category",
            joinColumns = @JoinColumn(name = "MovieID"),
            inverseJoinColumns = @JoinColumn(name = "CategoryID")
    )
    private Set<Category> categories = new HashSet<>();
}
