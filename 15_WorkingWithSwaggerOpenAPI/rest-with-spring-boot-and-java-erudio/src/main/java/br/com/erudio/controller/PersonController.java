package br.com.erudio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.services.PersonServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Person Endpoint")
@RestController
@RequestMapping("/api/person/v1")
public class PersonController {
    
    @Autowired
    private PersonServices service;
    
    @Operation(summary = "Find all people" ) 
    @GetMapping
    public List<PersonVO> findAll() {
        return service.findAll();
    }    
    
    @Operation(summary = "Find a specific person by your ID" )
    @GetMapping("/{id}")
    public PersonVO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }    
    
    @Operation(summary = "Create a new person") 
    @PostMapping
    public PersonVO create(@RequestBody PersonVO person) {
        return service.create(person);
    }
    
    @Operation(summary = "Update a specific person")
    @PutMapping
    public PersonVO update(@RequestBody PersonVO person) {
        return service.update(person);
    }    
    
    @Operation(summary = "Disable a specific person by your ID" )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }    
}