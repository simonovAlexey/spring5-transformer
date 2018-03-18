package com.simonov.webClient;

import com.simonov.TransformerGenerator;
import com.simonov.transformer.data.Activity;
import com.simonov.transformer.data.Transformer;
import lombok.extern.java.Log;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * -Dserver.port=8081
 */
@Log
@SpringBootApplication
public class TransformerClient
{
    public static void main(String[] args)
    {
        SpringApplication.run(TransformerClient.class, args);
    }

    @Bean
    WebClient webClient() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    CommandLineRunner demo(WebClient client)
    {
//        return getTransformerList(client);

        return addGeneratedTransformer(client);

//        return addActivityTransformerById(client, 1);
    }

    private CommandLineRunner addActivityTransformerById(WebClient client, long samuraiId)
    {
        return strings -> client
                .get()
                .uri("/transformer/{id}", samuraiId)
                .retrieve()
                .bodyToMono(Transformer.class)
                .flatMap(samurai -> client
                        .post()
                        .uri("/transformer/{id}/activity", samuraiId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .syncBody(new Activity("hara-kiri"))
                        .retrieve()
                        .bodyToMono(Void.class)
                )
                .subscribe(System.err::println);
    }


    private CommandLineRunner getTransformerList(WebClient client)
    {
        return strings -> client
                .get()
                .uri("/transformer")
                .retrieve()
                .bodyToFlux(Transformer.class)
                .subscribe(System.err::println); //for red text in console
    }

    private CommandLineRunner addGeneratedTransformer(WebClient client)
    {
        Transformer transformer = TransformerGenerator.generateTransformer();

        System.err.println("--> Trying to add transformer: " + transformer);
        return strings -> client
                .post()
                .uri("/transformer")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(transformer)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(System.err::println);
    }


}
