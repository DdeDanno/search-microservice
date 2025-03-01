package com.unir.backend.search_microservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import java.util.UUID;

@Document(indexName = "movies", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Movie {
    @Id
    private String id;

    @MultiField(mainField = @Field(type = FieldType.Search_As_You_Type, name = "name"),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword))
    private String name;

    @MultiField(mainField = @Field(type = FieldType.Search_As_You_Type, name = "director"),
            otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword))
    private String director;

    @Field(type = FieldType.Integer, name = "yearOfPublication")
    private Integer yearOfPublication;

    @Field(type = FieldType.Text, name = "synopsis")
    private String synopsis;

    @Field(type = FieldType.Text, name = "reviews")
    private String reviews;

    @Field(type = FieldType.Integer, name = "duration")
    private Integer duration;

    @Field(type = FieldType.Keyword, name = "image")
    private String image;

    @Field(type = FieldType.Keyword, name = "trailer")
    private String trailer;

    @Field(type = FieldType.Keyword, name = "genre")
    private String genre;

    @Field(type = FieldType.Double, name = "rentPrice")
    private Double rentPrice;

    @Field(type = FieldType.Double, name = "buyPrice")
    private Double buyPrice;

    public Movie(String name, String director, Integer yearOfPublication, String synopsis, String reviews, Integer duration, String image, String trailer, String genre, Double rentPrice, Double buyPrice) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.director = director;
        this.yearOfPublication = yearOfPublication;
        this.synopsis = synopsis;
        this.reviews = reviews;
        this.duration = duration;
        this.image = image;
        this.trailer = trailer;
        this.genre = genre;
        this.rentPrice = rentPrice;
        this.buyPrice = buyPrice;
    }

    public static Movie.MovieBuilder builder() {
        return new MovieBuilder() {
            private final String id = UUID.randomUUID().toString();

            @Override
            public Movie build() {
                if (super.id == null) {
                    super.id = this.id;
                }
                return super.build();
            }
        };
    }
}


