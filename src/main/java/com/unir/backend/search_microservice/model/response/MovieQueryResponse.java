package com.unir.backend.search_microservice.model.response;

import com.unir.backend.search_microservice.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieQueryResponse {

    private List<Movie> movies;
    private Map<String, List<AggregationsDetails>> aggs;
}
