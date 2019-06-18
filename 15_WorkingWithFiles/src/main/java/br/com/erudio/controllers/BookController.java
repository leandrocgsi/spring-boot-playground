package br.com.erudio.controllers;
 
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.services.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
 
@Api(value = "BookEndpoint") 
@RestController
@RequestMapping("/api/book/v1")
public class BookController {
     
    @Autowired
    private BookService bookService;
    
	@Autowired
	private PagedResourcesAssembler<BookVO> assembler;
	
    @ApiOperation(value = "Find a specific book by your ID" )
    @RequestMapping(value = "/{id}",
    method = RequestMethod.GET, 
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public BookVO get(@PathVariable(value = "id") Long id){
        BookVO bookVO = bookService.findById(id);
        bookVO.add(linkTo(methodOn(BookController.class).get(id)).withSelfRel());
        return bookVO;
    }
    
    @ApiOperation(value = "Find all books" ) 
    @RequestMapping(method = RequestMethod.GET,
	produces = { "application/json", "application/xml", "application/x-yaml" })
    public List<BookVO> findAll(){
    	List<BookVO> books = bookService.findAll();
    	books
    		.stream()
    		.forEach(p -> p.add(
    				linkTo(methodOn(BookController.class).get(p.getKey())).withSelfRel()
				)
			);
    	return books;
    }
    
    @ApiOperation(value = "Create a new book") 
    @RequestMapping(method = RequestMethod.POST,
    consumes = { "application/json", "application/xml", "application/x-yaml" },
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public BookVO create(@RequestBody BookVO book){
    	BookVO bookVO = bookService.create(book);
        bookVO.add(linkTo(methodOn(BookController.class).get(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    @ApiOperation(value = "Update a specific book")
    @RequestMapping(method = RequestMethod.PUT,
    consumes = { "application/json", "application/xml", "application/x-yaml" })
    public BookVO update(@RequestBody BookVO book){
    	BookVO bookVO = bookService.update(book);
        bookVO.add(linkTo(methodOn(BookController.class).get(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    @ApiOperation(value = "Delete a specific book by your ID")
    @RequestMapping(value = "/{id}",
    method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
        bookService.delete(id);
        return ResponseEntity.ok().build();
    }
}
