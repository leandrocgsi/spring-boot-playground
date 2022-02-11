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

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.model.Book;
import br.com.erudio.services.BookServices;
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

@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing Book")
public class BookController {
    
    @Autowired
    private BookServices service;
    
    @GetMapping
    @Operation(summary = "Finds all Book", description = "Finds all Book.",
               tags = { "Book" },
               responses = {
                   @ApiResponse(
                       description = "Success",
                       responseCode = "200",
                       content = {
                           @Content(
                               array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
                           )
                       }
                   ),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public List<BookVO> findAll() {
        return service.findAll();
    }    
    
    @GetMapping("/{id}")
    @Operation(
               summary = "Finds a book",
               description = "Find a specific book by your ID.",
               tags = { "Book" },
               responses = {
                   @ApiResponse(
                       description = "Success",
                       responseCode = "200",
                       content = @Content(schema = @Schema(implementation = BookVO.class))
                   ),
                   @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                   // Need the empty content otherwise it fills it with the example book schema
                   // Setting empty content also hides the box in the swagger ui
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public BookVO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }    
    
    @PostMapping
    @Operation(
               summary = "Adds a new book",
               description = "Adds a new book by passing in a JSON, XML or YML representation of the book.",
               tags = { "Book" },
               responses = {
                   @ApiResponse(
                       description = "Success",
                       responseCode = "200",
                       links = @Link(name = "get", operationId = "get", parameters = @LinkParameter(name = "id", expression = "$request.body.id")),
                       content = @Content(schema = @Schema(implementation = Book.class))
                   ),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public BookVO create(@RequestBody BookVO book) {
        return service.create(book);
    }
    
    @PutMapping
    @Operation(
               summary = "Updates a book's information",
               description = "Updates a book's information by passing in a JSON, XML or YML representation of the updated book.",
               tags = { "Book" },
               responses = {
                   @ApiResponse(
                       description = "Updated",
                       responseCode = "200",
                       links = @Link(name = "get", operationId = "get", parameters = @LinkParameter(name = "id", expression = "$request.body.id")),
                       content = @Content(schema = @Schema(implementation = Book.class))
                   ),
                   @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public BookVO update(@RequestBody BookVO book) {
        return service.update(book);
    }    
    
    @DeleteMapping("/{id}")
    @Operation(
               summary = "Deletes a book",
               description = "Deletes a book by their Id.",
               tags = { "Book" },
               responses = {
                   @ApiResponse(description = "Deleted", responseCode = "204", content = @Content),
                   @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                   @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
               }
           )
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }    
}