package com.simonov.transformer;

import com.simonov.transformer.controller.TransformerReactiveHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class RouterConfig {
    private static int counter = 1;

    @Bean
    public RouterFunction<ServerResponse> transformerRouting(TransformerReactiveHandler handler) {
        return RouterFunctions
                .route(GET("/transformer"), handler::list)
                .filter(filterEvery3rdRequest())
                .andRoute(GET("/transformer/{id}"), handler::get)
                .andRoute(POST("/transformer"), handler::createTransformer);
    }

    @Bean
    public RouterFunction<ServerResponse> transformerActivityRouting(TransformerReactiveHandler handler) {
        return RouterFunctions
                .route(GET("/transformer/{id}/activity"), handler::activityList)
                .andRoute(POST("/transformer/{id}/activity"), handler::addActivity);
    }

    private HandlerFilterFunction filterEvery3rdRequest() {
        return (request, next) ->
        {
            if (counter++ % 3 == 0) {
                System.out.println("This filter blocks every 3-rd request.");
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }

            return next.handle(request);
        };
    }

    @Bean
    public RouterFunction<ServerResponse> myError() {
        return RouterFunctions
                .route(GET("/error"),
                        request -> ServerResponse.ok().body(BodyInserters.fromObject("Test error page")));
    }


}
