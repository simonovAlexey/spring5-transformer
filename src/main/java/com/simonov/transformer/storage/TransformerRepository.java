package com.simonov.transformer.storage;

import com.simonov.transformer.data.Activity;
import com.simonov.transformer.data.Transformer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TransformerRepository
{
    Mono<Transformer> get(long id);

    Flux<Transformer> all();

    Mono<Void> save(Mono<Transformer> samurai);

    Flux<Activity> getAllActivities(long id);

    Mono<Void> addActivity(long id, Mono<Activity> activity);

    Mono<Void> removeActivity(long samuraiId, String activity);
}
