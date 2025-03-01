package com.unir.backend.search_microservice.data;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.unir.backend.search_microservice.model.Movie;

import java.util.Optional;

public interface MovieRepository extends ElasticsearchRepository<Movie, String> {
    Optional<Movie> findById(String id);

    Movie save(Movie movie);

    void delete(Movie movie);
}
