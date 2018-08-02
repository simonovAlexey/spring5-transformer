package com.simonov.transformer.controller;

import com.simonov.TransformerGenerator;
import com.simonov.transformer.data.Message;
import com.simonov.transformer.data.Transformer;
import com.simonov.transformer.service.ReactiveService;
import com.simonov.transformer.service.EmitterProcessorScheduledService;
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
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/reactive/transformer")
@Slf4j
public class TransformerReactiveController {
    @Autowired
    private TransformerRepository repository;

    @Autowired
    private ReactiveService reactiveService;

    @Autowired
    private EmitterProcessorScheduledService emitterProcessorScheduledService;

    @GetMapping("/{id}")
    public Mono<Transformer> byId(@PathVariable String id) {
        System.err.println("--> ReactiveController # Get transformer by id handled, id: " + id);

        return repository.get(Long.parseLong(id));
    }

    @GetMapping(value = "sse/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> spyById(@PathVariable String id) {
        //first  type of build SSE

        log.warn("Spy transformer by id handled, id: {}", id);
        Mono<Transformer> transformerMono = byId(id);

        return Flux.<String>generate(sink -> sink.next("{\"time\":\"" + LocalTime.now() + "\", " + transformerMono.block().doSomething()))
                .delayElements(Duration.ofSeconds(2));
    }



    @GetMapping(value = "/sse/emitterProcessor", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Message>> sseByEmitterProcessor() {
        //second type of build SSE
        return emitterProcessorScheduledService.getInfiniteMessages();
    }

    @GetMapping(value = "/sse/finite/{count}", produces = "text/event-stream")
    public Flux<Message> getFiniteMessages(@PathVariable int count) {
        return reactiveService.getFiniteMessages(count);
    }

}
