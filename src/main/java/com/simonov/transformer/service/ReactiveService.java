package com.simonov.transformer.service;

import com.simonov.TransformerGenerator;
import com.simonov.transformer.data.Message;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ReactiveService {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    public Flux<Message> getFiniteMessages(int count) {
        return processMany(count);
    }

    public Flux<Message> processMany(int count) {
        final List<Flux<Message>> observables = IntStream
                .range(0, count)
                .mapToObj(i -> processOneAsync(EXECUTOR_SERVICE))
                .collect(Collectors.toList());
        return Flux.merge(observables);
    }

    private Flux<Message> processOneAsync(ExecutorService executorService) {
        return Flux.<Message>create(s -> {
            s.next(processOne());
            s.complete();
        }).subscribeOn(Schedulers.fromExecutor(executorService));
    }


    private Message processOne() {
        try {
            Thread.sleep(Duration.ofSeconds(1).toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Message(LocalDateTime.now(),TransformerGenerator.getRandomAction());
    }
}
