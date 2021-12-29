package com.country.routing.controller;

import com.country.routing.dto.RouteDto;
import com.country.routing.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequiredArgsConstructor
@RequestMapping("/routing")
public class RoutingController {

    private final RoutingService routingService;

    @GetMapping("/{origin}/{destination}")
    public ResponseEntity<RouteDto> getRoute(@PathVariable final String origin,
                                             @PathVariable final String destination) {
        RouteDto route = routingService.getRoute(origin, destination);

        if (route.getRoute().isEmpty()) {
            return new ResponseEntity<>(BAD_REQUEST);
        }

        return ResponseEntity.ok(route);
    }
}
