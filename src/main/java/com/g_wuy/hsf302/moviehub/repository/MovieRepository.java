package com.g_wuy.hsf302.moviehub.repository;

import com.g_wuy.hsf302.moviehub.entity.Movie;
import com.g_wuy.hsf302.moviehub.model.response.MovieResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    @Query(value = """
            SELECT 
                m.MovieID AS movieId,
                m.Title AS title,
                m.Description AS description,
                m.Duration AS duration,
                m.ReleaseDate AS releaseDate,
                m.Language AS language,
                COALESCE(STRING_AGG(c.CategoryName, ', '), 'None') AS categories
            FROM Movie m
            LEFT JOIN movie_category mc ON m.MovieID = mc.MovieID
            LEFT JOIN Category c ON c.CategoryID = mc.CategoryID
            GROUP BY m.MovieID, m.Title, m.Description, m.Duration, m.ReleaseDate, m.Language
            """, nativeQuery = true)
    List<MovieResponse> getMovieWithCategories();

}