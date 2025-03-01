package com.unir.backend.search_microservice.data;

import com.unir.backend.search_microservice.model.Movie;
import com.unir.backend.search_microservice.model.response.AggregationsDetails;
import com.unir.backend.search_microservice.model.response.MovieQueryResponse;
import com.unir.backend.search_microservice.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRepository {
    private final ElasticsearchOperations elasticsearchOperations;

    @SneakyThrows
    public MovieQueryResponse findMovies(
            List<String> yearOfPublication,
            List<String> genre) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        if (yearOfPublication != null && !yearOfPublication.isEmpty()) {
            yearOfPublication.forEach(
                    year -> query.must(QueryBuilders.termQuery(Consts.YEAR_OF_PUBLICATION, year))
            );
        }

        if (genre != null && !genre.isEmpty()) {
            genre.forEach(
                    gen -> query.must(QueryBuilders.termQuery(Consts.GENRE, gen))
            );
        }
        if (!query.hasClauses()) {
            query.must(QueryBuilders.matchAllQuery());
        }

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(query);

        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("genres").field(Consts.GENRE).size(10000));
        nativeSearchQueryBuilder.withMaxResults(5);

        Query searchQuery = nativeSearchQueryBuilder.build();
        SearchHits<Movie> result = elasticsearchOperations.search(searchQuery, Movie.class);
        return new MovieQueryResponse(getResponseMovies(result), getResponseAggregations(result));
    }

    private List<Movie> getResponseMovies(SearchHits<Movie> result) {
        return result.getSearchHits().stream().map(SearchHit::getContent).toList();
    }

    private Map<String, List<AggregationsDetails>> getResponseAggregations(SearchHits<Movie> result) {
        Map<String, List<AggregationsDetails>> aggregations = new HashMap<>();
        if (result.hasAggregations()) {
            Map<String, Aggregation> aggregationsDetails = Objects.requireNonNull(result.getAggregations()).asMap();
            aggregationsDetails.forEach((key, value) -> {
                if (!aggregations.containsKey(key)) {
                    aggregations.put(key, new LinkedList<>());
                }

                if (value instanceof ParsedStringTerms parsedStringTerms) {
                    parsedStringTerms.getBuckets().forEach(bucket -> {
                        aggregations.get(key).add(new AggregationsDetails(bucket.getKey().toString(), (int) bucket.getDocCount()));
                    });
                }

                if (value instanceof ParsedRange parsedRange) {
                    parsedRange.getBuckets().forEach(bucket -> {
                        aggregations.get(key).add(new AggregationsDetails(bucket.getKeyAsString(), (int) bucket.getDocCount()));
                    });
                }
            });
        }
        return aggregations;
    }
}
