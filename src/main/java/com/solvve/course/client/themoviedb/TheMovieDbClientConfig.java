package com.solvve.course.client.themoviedb;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.solvve.course.domain.constant.Genre;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TheMovieDbClientConfig {

    @Bean
    public RequestInterceptor authRequestInterceptor(@Value("${themoviedb.api.key}") String apiKey) {
        return requestTemplate -> requestTemplate.header("Authorization", "Bearer " + apiKey);
    }

    @Bean
    public Decoder feignDecoder() {
        HttpMessageConverter<Object> jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);

        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    private ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return objectMapper;
    }

    public static class GenresDeserializer extends JsonDeserializer<List<Genre>> {

        @Override
        public List<Genre> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
            List<HashMap<String, String>> parsedResult = jsonParser.readValueAs(new TypeReference<>() {
            });
            return parsedResult.stream()
                .map(map -> map.get("name"))
                .map(genre -> Arrays.stream(Genre.values())
                    .filter(genreEnum -> genreEnum.name().equalsIgnoreCase(genre.replace(" ", "_")))
                    .findFirst().orElse(null))
                .collect(Collectors.toList());
        }
    }
}