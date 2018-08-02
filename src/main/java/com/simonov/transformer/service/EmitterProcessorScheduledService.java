package com.simonov.transformer.service;

import com.simonov.TransformerGenerator;
import com.simonov.transformer.data.Message;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmitterProcessorScheduledService {

    private final EmitterProcessor<ServerSentEvent<Message>> emitter;
    private static AtomicLong SHEDULED_SSE_IDS = new AtomicLong(1);

    public EmitterProcessorScheduledService() {
        emitter = EmitterProcessor.create();
    }

    public Flux<ServerSentEvent<Message>> getInfiniteMessages() {
//        Flux<ServerSentEvent<Message>> log = emitter.log();
        Flux<ServerSentEvent<Message>> share = emitter.share();
        return share;
    }

    @Scheduled(fixedRate = 1500L)
    void timerHandler() {
        try {
            emitter.onNext(
                    ServerSentEvent.builder(
                            new Message(LocalDateTime.now(), TransformerGenerator.getRandomAction())).
                            id(String.valueOf(SHEDULED_SSE_IDS.getAndIncrement())).
                            build());
        } catch (Exception e) {
            emitter.onError(e);
        }
    }
}
