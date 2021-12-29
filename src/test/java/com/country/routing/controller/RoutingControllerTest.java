package com.country.routing.controller;

import com.country.routing.dto.RouteDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoutingControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void testGetRouteSuccess() {
        //when
        RouteDto result = testRestTemplate.getForObject(getUrl("CZE", "ITA"), RouteDto.class);

        //then
        assertThat(result.getRoute()).isEqualTo(Arrays.asList("CZE", "AUT", "ITA"));
    }

    @Test
    void testGetRouteWrongCountryCode() {
        //when
        ResponseEntity<IllegalArgumentException> result = testRestTemplate.getForEntity(getUrl("TR", "ITA"), IllegalArgumentException.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }


    @Test
    void testGetRouteNotFound() {
        //when
        ResponseEntity<String> result = testRestTemplate.getForEntity(getUrl("ABW", "ITA"), String.class);

        //then
        assertThat(result.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    private String getUrl(String origin, String destination) {
        return String.format("http://localhost:%d/routing/%s/%s", port, origin, destination);
    }
}