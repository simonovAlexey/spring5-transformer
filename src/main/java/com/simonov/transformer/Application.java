package com.simonov.transformer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.simonov.TransformerGenerator;
import com.simonov.transformer.controller.TransformerReactiveHandler;
import com.simonov.transformer.storage.TransformerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.reactive.config.EnableWebFlux;
import reactor.core.publisher.Mono;

@Configuration
@Import(RouterConfig.class)

@ComponentScan
@SpringBootApplication
@EnableWebFlux
public class Application
{

    public static void main(String[] args)
    {

        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.REACTIVE);

        app.run(args);
    }

    @Bean
    TransformerReactiveHandler getTransformerHandler()
    {
        return new TransformerReactiveHandler();
    }

    @Bean
    CommandLineRunner init(TransformerRepository repository)
    {
        return (env) ->
        {
//			System.out.println("REPO --> " + repository);
    //add transformers:
            repository.save(Mono.fromSupplier(TransformerGenerator::generateTransformer)).subscribe();
            repository.save(Mono.fromSupplier(TransformerGenerator::generateTransformer)).subscribe();
			repository.save(Mono.fromSupplier(TransformerGenerator::generateTransformer)).subscribe();
        };
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(mapper);
        return converter;
    }


}
