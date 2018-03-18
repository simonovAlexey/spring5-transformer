package com.simonov.transformer.controller;

import com.simonov.transformer.data.Transformer;
import com.simonov.transformer.storage.TransformerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequestMapping("/reactive/transformer")
public class TransformerReactiveController
{
    @Autowired
    private TransformerRepository repository;

    @GetMapping("/{id}")
    public Mono<Transformer> byId(@PathVariable String id)
    {
        System.err.println("--> ReactiveController # Get transformer by id handled, id: " + id);

        return this.repository.get(Long.parseLong(id));
    }

    @GetMapping(value = "/{id}/spy", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> spyById(@PathVariable String id)
    {
        System.err.println("--> ReactiveController # Spy transformer by id handled, id: " + id);

        Mono<Transformer> samuraiMono = byId(id);

        return Flux
                .<String>generate(sink -> sink.next(LocalTime.now() + ": " + samuraiMono.block().doSomething()))
                .delayElements(Duration.ofSeconds(1));
    }

}
