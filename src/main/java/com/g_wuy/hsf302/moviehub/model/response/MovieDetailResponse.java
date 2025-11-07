package com.g_wuy.hsf302.moviehub.model.response;


import com.g_wuy.hsf302.moviehub.model.dto.ShowtimeDTO;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MovieDetailResponse {
    private Integer movieId;
    private String title;
    private String description;
    private Integer duration;
    private LocalDate releaseDate;
    private String language;
    private String imageUrl;
    private List<String> categories;
    private List<ShowtimeDTO> showtimes;
}