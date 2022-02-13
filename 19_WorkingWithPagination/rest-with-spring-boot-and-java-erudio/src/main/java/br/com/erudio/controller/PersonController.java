package br.com.erudio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.services.PersonServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.links.LinkParameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

// https://lankydan.dev/documenting-a-spring-rest-api-following-the-openapi-specification
// https://github.com/lankydan/spring-rest-api-with-swagger

// @CrossOrigin
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController {
    
    @Autowired
    private PersonServices service;
    
    @GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" })
    @Operation(summary = "Finds all People", description = "Finds all People.",
               tags = { "People" },
               responses = {
                   @ApiResponse(
                       description = "Success",
                       responseCode = "200",
                       content = {
                           @Content(
                               mediaType = "application/json",
                               array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                           )
                       }
                   ),
                   @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public List<PersonVO> findAll() {
        return service.findAll();
    }    
    
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" })
    @Operation(
               summary = "Finds a person",
               description = "Find a specific person by your ID.",
               tags = { "People" },
               responses = {
                   @ApiResponse(
                       description = "Success",
                       responseCode = "200",
                       content = @Content(schema = @Schema(implementation = PersonVO.class))
                   ),
                   @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                   // Need the empty content otherwise it fills it with the example person schema
                   // Setting empty content also hides the box in the swagger ui
                   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                   @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public PersonVO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }    
    
    @CrossOrigin(origins = {"http://localhost:8080", "https://erudio.com.br"})
    @PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, 
            consumes = { "application/json", "application/xml", "application/x-yaml" })
    @Operation(
               summary = "Adds a new person",
               description = "Adds a new person by passing in a JSON, XML or YML representation of the person.",
               tags = { "People" },
               responses = {
                   @ApiResponse(
                       description = "Success",
                       responseCode = "200",
                       links = @Link(name = "get", operationId = "get", parameters = @LinkParameter(name = "id", expression = "$request.body.id")),
                       content = @Content(schema = @Schema(implementation = PersonVO.class))
                   ),
                   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public PersonVO create(@RequestBody PersonVO person) {
        return service.create(person);
    }

    @PutMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, 
            consumes = { "application/json", "application/xml", "application/x-yaml" })
    @Operation(
               summary = "Updates a person's information",
               description = "Updates a person's information by passing in a JSON, XML or YML representation of the updated person.",
               tags = { "People" },
               responses = {
                   @ApiResponse(
                       description = "Updated",
                       responseCode = "200",
                       content = @Content(schema = @Schema(implementation = PersonVO.class))
                   ),
                   @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public PersonVO update(@RequestBody PersonVO person) {
        return service.update(person);
    }    
    
    @PatchMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" })
    @Operation(
               summary = "Disable a person",
               description = "Disable a specific person by your ID.",
               tags = { "People" },
               responses = {
                   @ApiResponse(
                       description = "Success",
                       responseCode = "200",
                       content = @Content(schema = @Schema(implementation = PersonVO.class))
                   ),
                   @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                   @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public PersonVO disablePerson(@PathVariable("id") Long id) {
        return service.disablePerson(id);
    }   
    
    @DeleteMapping("/{id}")
    @Operation(
               summary = "Deletes a person",
               description = "Deletes a person by their Id.",
               tags = { "People" },
               responses = {
                   @ApiResponse(
                        description = "No Content",
                        responseCode = "204",
                        links = @Link(name = "get", operationId = "get", parameters = @LinkParameter(name = "id", expression = "$request.body.id")),
                        content = @Content),
                   @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                   @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                   @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }    
}