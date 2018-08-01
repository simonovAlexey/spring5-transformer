package com.simonov.transformer.controller;

import com.simonov.transformer.data.Message;
import com.simonov.transformer.data.Transformer;
import com.simonov.transformer.service.ReactiveService;
import com.simonov.transformer.service.ScheduledService;
import com.simonov.transformer.storage.TransformerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
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
@Slf4j
public class TransformerReactiveController
{
    @Autowired
    private TransformerRepository repository;

    @Autowired
    private ReactiveService reactiveService;

    @Autowired
    private ScheduledService scheduledService;

    @GetMapping("/{id}")
    public Mono<Transformer> byId(@PathVariable String id)
    {
        System.err.println("--> ReactiveController # Get transformer by id handled, id: " + id);

        return this.repository.get(Long.parseLong(id));
    }

    @GetMapping(value = "/{id}/spy", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> spyById(@PathVariable String id)
    {
        log.warn("Spy transformer by id handled, id: {}", id);

        Mono<Transformer> transformerMono = byId(id);

        return Flux.<String>generate(sink -> sink.next("{\"time\":\""+ LocalTime.now() + "\", " + transformerMono.block().doSomething()))
                .delayElements(Duration.ofSeconds(2));
    }

    @GetMapping(path = "/sse/finite/{count}", produces = "text/event-stream")
    public Flux<Message> getFiniteMessages(@PathVariable int count) {
        return reactiveService.getFiniteMessages(count);
    }

    @GetMapping(name = "/sse/infinite", produces = "text/event-stream")
    public Flux<ServerSentEvent<Message>> getInfiniteMessages() {
        return scheduledService.getInfiniteMessages();
    }

}
