package com.g_wuy.hsf302.moviehub.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MovieID", nullable = false)
    private Integer id;

    @Nationalized
    @Column(name = "Title", nullable = false, length = 200)
    private String title;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @Column(name = "Duration")
    private Integer duration;

    @Column(name = "ReleaseDate")
    private LocalDate releaseDate;

    @Size(max = 50)
    @Nationalized
    @Column(name = "Language", length = 50)
    private String language;

    @Size(max = 255)
    @Nationalized
    private String image;

    // ✅ Đây là phần đơn giản hóa quan hệ N:N
    @ManyToMany
    @JoinTable(
            name = "movie_category",                     // tên bảng trung gian
            joinColumns = @JoinColumn(name = "MovieID"), // khóa ngoại trỏ về Movie
            inverseJoinColumns = @JoinColumn(name = "CategoryID") // khóa ngoại trỏ về Category
    )
    private Set<Category> categories;
}
