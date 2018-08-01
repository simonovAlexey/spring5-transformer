package com.simonov.transformer.service;

import com.simonov.TransformerGenerator;
import com.simonov.transformer.data.Message;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Service
public class ScheduledService {

    private final EmitterProcessor<ServerSentEvent<Message>> emitter;

    public ScheduledService() {
        emitter = EmitterProcessor.create();
    }

    public Flux<ServerSentEvent<Message>> getInfiniteMessages() {
        return emitter.log();
    }

    @Scheduled(fixedRate = 1000)
    void timerHandler() {
        try {
            emitter.onNext(
                    ServerSentEvent.builder(
                            new Message(LocalDateTime.now(),TransformerGenerator.getRandomAction())
                    ).build()
            );
        } catch (Exception e) {
            emitter.onError(e);
        }
    }
}
