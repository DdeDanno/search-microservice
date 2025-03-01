package com.unir.backend.search_microservice.controller;

import com.unir.backend.search_microservice.model.Movie;
import com.unir.backend.search_microservice.model.response.MovieQueryResponse;
import com.unir.backend.search_microservice.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MovieController {

    private final MovieService movieService;

    /**
     * Obtiene todas las peliculas
     * @return regresa una lista de peliculas
     */
    @GetMapping("/elastic/movies")
    public ResponseEntity<List<Movie>> getElasticMovies(
            @RequestParam(required = false) List<String> yearOfPublication,
            @RequestParam(required = false) List<String> genre
    ) {
        MovieQueryResponse response = movieService.getMovies(yearOfPublication, genre);
        return ResponseEntity.status(HttpStatus.OK).body(response.getMovies());
    }

    /**
     * Agrega una nueva pelicula a elasticSearch
     * @param movie la nueva pelicula a agregar
     * @return regresa ok, si se agregó la pelicula
     */
    @PostMapping("/elastic/movies")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.addMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
    }

    /**
     * Borra una pelicula basado en su id
     * @param id pelicula a borrar
     * @return regresa la pelicula borrada
     */
    @DeleteMapping("/elastic/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable String id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busqueda dinamica por nombre de pelicula
     * @param query palabra a buscar
     * @return regresa nombre de pelicula
     */
    @GetMapping("/elastic/movies/name")
    public List<String> getMovieSuggestionsByName(@RequestParam String query) {
        return movieService.searchMovieByName(query);
    }

    /**
     * Busqueda dinamica por director
     * @param query palabra a buscar
     * @return regresa nombre de director
     */
    @GetMapping("/elastic/movies/director")
    public List<String> getDirectorSuggestions(@RequestParam String query) {
        return movieService.searchMovieByDirector(query);
    }

    /**
     * Modifica una película basada en su ID
     * @param id la película a modificar
     * @param movie la película con los nuevos datos
     * @return regresa la película modificada
     */
    @PutMapping("/elastic/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable String id, @RequestBody Movie movie) {
        Movie updatedMovie = movieService.updateMovie(id, movie);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMovie);
    }

}
