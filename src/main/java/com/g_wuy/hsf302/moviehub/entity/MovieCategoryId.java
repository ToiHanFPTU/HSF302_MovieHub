package com.g_wuy.hsf302.moviehub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MovieCategoryId implements Serializable {
    private static final long serialVersionUID = 7943483240981011500L;
    @NotNull
    @Column(name = "movie_id", nullable = false)
    private Integer movieId;

    @NotNull
    @Column(name = "category_id", nullable = false)
    private Integer categoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        MovieCategoryId entity = (MovieCategoryId) o;
        return Objects.equals(this.movieId, entity.movieId) &&
                Objects.equals(this.categoryId, entity.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, categoryId);
    }

}