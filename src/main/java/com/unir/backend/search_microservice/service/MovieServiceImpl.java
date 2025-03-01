package com.unir.backend.search_microservice.service;

import com.unir.backend.search_microservice.data.DataAccessRepository;
import com.unir.backend.search_microservice.data.MovieRepository;
import com.unir.backend.search_microservice.model.Movie;
import com.unir.backend.search_microservice.model.response.MovieQueryResponse;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final DataAccessRepository dataAccessRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public MovieQueryResponse getMovies(
            List<String> yearOfPublication,
            List<String> genre) {
        return dataAccessRepository.findMovies(yearOfPublication, genre);
    }

    @Override
    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(String id) {
        Movie movie = movieRepository.findById(id).get();
        movieRepository.delete(movie);
    }

    @Override
    public List<String> searchMovieByName(String query) {
        NativeSearchQuery searchQuery = new NativeSearchQuery(
                QueryBuilders.matchPhrasePrefixQuery("name", query)
        );

        SearchHits<Movie> searchHits = elasticsearchOperations.search(searchQuery, Movie.class);

        return searchHits.stream()
                .map(hit -> hit.getContent().getName())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> searchMovieByDirector(String query) {
        NativeSearchQuery searchQuery = new NativeSearchQuery(
                QueryBuilders.matchPhrasePrefixQuery("director", query)
        );

        SearchHits<Movie> searchHits = elasticsearchOperations.search(searchQuery, Movie.class);

        return searchHits.stream()
                .map(hit -> hit.getContent().getDirector())
                .collect(Collectors.toList());
    }

    public Movie updateMovie(String id, Movie movie) {
        Optional<Movie> movieToUpdate = movieRepository.findById(id);
        if (movieToUpdate.isEmpty()) {
            throw new RuntimeException("Movie not exists");
        } else {
            Movie movieToBeUpdate = movieToUpdate.get();
            movieToBeUpdate.setName(movie.getName());
            movieToBeUpdate.setDirector(movie.getDirector());
            movieToBeUpdate.setYearOfPublication(movie.getYearOfPublication());
            movieToBeUpdate.setReviews(movie.getReviews());
            movieToBeUpdate.setSynopsis(movie.getSynopsis());
            movieToBeUpdate.setDuration(movie.getDuration());
            movieToBeUpdate.setImage(movie.getImage());
            movieToBeUpdate.setTrailer(movie.getTrailer());
            movieToBeUpdate.setGenre(movie.getGenre());
            return movieRepository.save(movieToBeUpdate);
        }
    }

    @Override
    public Movie getMovieById(String id) {
        return movieRepository.findById(id).get();
    }
}
