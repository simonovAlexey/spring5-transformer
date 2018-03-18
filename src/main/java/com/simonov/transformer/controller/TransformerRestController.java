package com.simonov.transformer.controller;

import com.simonov.transformer.data.Transformer;
import com.simonov.transformer.storage.TransformerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/transformer")
public class TransformerRestController
{
    @Autowired
    private TransformerRepository repository;

    @GetMapping("/{id}")
    public Transformer byId(@PathVariable String id)
    {
        System.err.println("--> RestController # Get transformer by id handled, id: " + id);

        return this.repository.get(Long.parseLong(id)).block();
    }

    @GetMapping()
    public List<Transformer> all()
    {
        System.err.println("--> RestController # Returns list of Transformers");

        return this.repository.all().collectList().block();
    }
}
