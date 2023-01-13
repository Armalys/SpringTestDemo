package com.thehecklers.aircraftpositions;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

@AllArgsConstructor
@Component
public class PositionRetriever {
    @NonNull
    private final AircraftRepository repository;
    private final WebClient client =
            WebClient.create("http://localhost:7634");

    @GetMapping("/aircraft")
    public Iterable<Aircraft> retrieveAircraftPositions(Model model) {
        repository.deleteAll();

        client.get()
                .uri("/aircraft")
                .retrieve()
                .bodyToFlux(Aircraft.class)
                .filter(ac -> !ac.getReg().isEmpty())
                .toStream()
                .forEach(repository::save);

        return repository.findAll();
    }
}
