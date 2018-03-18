package com.simonov.transformer;

import com.simonov.TransformerGenerator;
import com.simonov.transformer.controller.TransformerRestController;
import com.simonov.transformer.storage.TransformerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.doReturn;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(TransformerRestController.class)
public class ControllerTests
{
    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TransformerRepository repository;

    @Before
    public void setup()
    {
        doReturn(Mono.empty()).when(this.repository).save(any());
    }

    @Test
    public void getTransformer() throws Exception
    {
        final long id = 1;
        final String name = "BB";

        doReturn(Mono.fromSupplier(()
                -> TransformerGenerator.generateTransformerWithIdAndName(id, name)))
                .when(this.repository).get(id);

        this.mockMvc.perform(get("/rest/transformer/" + id).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("{\"id\":" + id
                                + ",\"name\":\"" + name
                                + "\",\"age\":5000,\"activity\":[{\"name\":\"Say\"},{\"name\":\"Kill\"},{\"name\":\"Transform\"}]}"));
    }

}
