package com.solvve.course.client.themoviedb;


import com.solvve.course.client.themoviedb.dto.MovieReadDto;
import com.solvve.course.client.themoviedb.dto.MoviesPageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "api.themoviedb.org", url = "${themoviedb.api.url}", configuration = TheMovieDbClientConfig.class)
public interface TheMovieDbClient {

    @RequestMapping(method = RequestMethod.GET, value = "/movie/{movieId}")
    MovieReadDto getMovie(@PathVariable String movieId, @RequestParam String language);

    @RequestMapping(method = RequestMethod.GET, value = "/movie/top_rated")
    MoviesPageDto getTopRatedMovies();
}