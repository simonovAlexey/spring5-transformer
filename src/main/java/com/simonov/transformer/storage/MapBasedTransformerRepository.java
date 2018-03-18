package com.simonov.transformer.storage;

import com.simonov.transformer.data.Activity;
import com.simonov.transformer.data.Transformer;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MapBasedTransformerRepository implements TransformerRepository
{

    private Map<Long, Transformer> repository;

    public MapBasedTransformerRepository()
    {
        repository = new HashMap<>(10);
    }

    @Override
    public Mono<Transformer> get(long id)
    {
        return Mono.justOrEmpty(repository.get(id));
    }

    @Override
    public Flux<Transformer> all()
    {
        return Flux.fromIterable(repository.values());
    }

    @Override
    public Flux<Activity> getAllActivities(long id)
    {
        return Flux.fromIterable(repository.get(id).getActivity());
    }


    @Override
    public Mono<Void> addActivity(long id, Mono<Activity> activity)
    {
        return activity
                .doOnNext(a -> repository.get(id).addActivity(a))
                .then(Mono.empty());
    }

    @Override
    public Mono<Void> removeActivity(long samuraiId, String activity)
    {
        return get(samuraiId)
                .doOnNext(s -> s.getActivity().removeIf(a -> activity.equals(a.getName())))
                .thenEmpty(Mono.empty());
    }

    @Override
    public Mono<Void> save(Mono<Transformer> samurai)
    {
        return samurai.doOnNext(this::storeSamurai)
                .thenEmpty(Mono.empty());
    }

    private void storeSamurai(Transformer transformer)
    {
        transformer.setId(repository.size() + 1);
        repository.put(transformer.getId(), transformer);
    }

}
