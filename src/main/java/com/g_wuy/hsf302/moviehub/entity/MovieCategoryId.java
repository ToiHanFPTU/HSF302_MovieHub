package com.g_wuy.hsf302.moviehub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.ProxyUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MovieCategoryId implements Serializable {

    private static final long serialVersionUID = 364815906128879151L;

    @NotNull
    @Column(name = "MovieID", nullable = false)
    private Integer movieID;

    @NotNull
    @Column(name = "CategoryID", nullable = false)
    private Integer categoryID;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || ProxyUtils.getUserClass(this) != ProxyUtils.getUserClass(o)) return false;
        MovieCategoryId entity = (MovieCategoryId) o;
        return Objects.equals(this.movieID, entity.movieID) &&
                Objects.equals(this.categoryID, entity.categoryID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieID, categoryID);
    }

}