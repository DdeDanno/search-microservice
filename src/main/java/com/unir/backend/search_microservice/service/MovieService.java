package com.unir.backend.search_microservice.service;

import com.unir.backend.search_microservice.model.Movie;
import com.unir.backend.search_microservice.model.response.MovieQueryResponse;

import java.util.List;

public interface MovieService {

    MovieQueryResponse getMovies(
            List<String> yearOfPublication,
            List<String> genre);

    Movie addMovie(Movie movie);

    void deleteMovie(String id);

    List<String> searchMovieByName(String name);

    List<String> searchMovieByDirector(String director);

    Movie updateMovie(String id, Movie movie);

    Movie getMovieById(String id);
}
