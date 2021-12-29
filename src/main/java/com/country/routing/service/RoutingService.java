package com.country.routing.service;

import com.country.routing.dto.CountryDto;
import com.country.routing.dto.RouteDto;
import lombok.RequiredArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class RoutingService {
    private final DataLoader dataService;

    public RouteDto getRoute(final String origin, final String destination) {
        List<CountryDto> countries = dataService.getCountries();

        CountryDto originCountry = countries.stream()
                .filter(country -> country.getCca3().equals(origin)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("origin country code not found! %s", origin)));
        countries.stream()
                .filter(country -> country.getCca3().equals(destination)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("destination country code not found! %s",destination)));

        if (originCountry.getBorders().contains(destination)) {
            return new RouteDto(Arrays.asList(origin, destination));
        }

        final Graph<String, DefaultEdge> graph = fillGraph(countries);
        DijkstraShortestPath<String, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultEdge> path = dijkstraShortestPath.getPath(origin, destination);

        if (Objects.isNull(path)) {
            return new RouteDto();
        }

        return new RouteDto(path.getVertexList());
    }

    private Graph<String, DefaultEdge> fillGraph(List<CountryDto> countries) {
        final Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);

        countries.forEach(countryDto -> {
            graph.addVertex(countryDto.getCca3());

            countryDto.getBorders().forEach(b -> {
                graph.addVertex(b);
                graph.addEdge(countryDto.getCca3(), b);
            });
        });

        return graph;
    }

}
