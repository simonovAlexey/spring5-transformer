package com.simonov.transformer;

import com.simonov.TransformerGenerator;
import com.simonov.transformer.data.Transformer;
import com.simonov.transformer.controller.TransformerReactiveHandler;
import com.simonov.transformer.storage.TransformerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
@WebFluxTest(TransformerReactiveHandler.class)
public class WebFluxTests
{
    @Autowired
    private WebTestClient webClient;

    @SpyBean
    private TransformerRepository repository;

    @Before
    public void setup()
    {
        doReturn(Mono.empty()).when(this.repository).save(any());
    }

    @Test
    public void getSamurai() throws Exception
    {
        final long id = 2;
        final String name = "Udzuki";

        Transformer actualTransformer = TransformerGenerator.generateTransformerWithIdAndName(id, name);

        doReturn(Mono.fromSupplier(()
                -> actualTransformer))
                .when(this.repository).get(id);

        this.webClient.get()
                .uri("/samurai/" + id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Transformer.class).isEqualTo(actualTransformer);
    }

}
