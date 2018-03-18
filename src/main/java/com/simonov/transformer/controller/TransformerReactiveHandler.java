package com.simonov.transformer.controller;

import com.simonov.transformer.data.Activity;
import com.simonov.transformer.data.Transformer;
import com.simonov.transformer.storage.TransformerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class TransformerReactiveHandler
{
    @Autowired
    private TransformerRepository repository;

    public Mono<ServerResponse> get(ServerRequest request)
    {
        long id = Long.parseLong(request.pathVariable("id"));

        System.err.println("--> Get transformer by id handled, id: " + id);

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();

        Mono<Transformer> samuraiMono = this.repository.get(id);

        return samuraiMono
                .flatMap(this::packTransformer)
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> packTransformer(Transformer transformer)
    {
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(transformer));
    }

    public Mono<ServerResponse> createTransformer(ServerRequest request)
    {
        System.err.println("--> Create transformer handled");
        Mono<Transformer> robotMono = request.bodyToMono(Transformer.class);

        return ServerResponse.ok()
                .build(this.repository.save(robotMono));
    }

    public Mono<ServerResponse> list(ServerRequest request)
    {
        System.err.println("--> Get all transformer handled");

        Flux<Transformer> transformerFlux = this.repository.all();

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(transformerFlux, Transformer.class);
    }

    public Mono<ServerResponse> activityList(ServerRequest request)
    {
        long id = Long.parseLong(request.pathVariable("id"));

        System.err.println("--> Get all activities for transformer handled, id: " + id);

        Flux<Activity> activityFlux = this.repository.getAllActivities(id);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(activityFlux, Activity.class);
    }

    public Mono<ServerResponse> addActivity(ServerRequest request)
    {
        long id = Long.parseLong(request.pathVariable("id"));
        Mono<Activity> activityMono = request.bodyToMono(Activity.class);

        System.err.println("--> Add activity to transformer handled, id: " + id);

        Mono<Void> response = this.repository.addActivity(id, activityMono);

        return ServerResponse.ok().build(response);
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(activityFlux, Activity.class);
    }

}
