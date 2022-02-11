package br.com.erudio.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.controller.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exception.RequiredObjectIsNullException;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.mapper.DozerConverter;
import br.com.erudio.model.Book;
import br.com.erudio.repository.BookRepository;

@Service
public class BookServices {
    
    @Autowired
    BookRepository repository;
        
    public List<BookVO> findAll() {
        List<BookVO> books = DozerConverter.parseListObjects(repository.findAll(), BookVO.class);
        books
            .stream()
            .forEach(p -> p.add(
                linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()
            )
        );
        return books;
    }    
    
    public BookVO findById(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        BookVO bookVO = DozerConverter.parseObject(entity, BookVO.class);
        bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return bookVO;
    }
    
    public BookVO create(BookVO book) {
        if(book == null) throw new RequiredObjectIsNullException();
        var entity = DozerConverter.parseObject(book, Book.class);
        var bookVO = DozerConverter.parseObject(repository.save(entity), BookVO.class);
        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    public BookVO update(BookVO book) {
        if(book == null) throw new RequiredObjectIsNullException();
        var entity = repository.findById(book.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        
        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());
        
        var bookVO = DozerConverter.parseObject(repository.save(entity), BookVO.class);
        bookVO.add(linkTo(methodOn(BookController.class).findById(bookVO.getKey())).withSelfRel());
        return bookVO;
    }    
    
    public void delete(Long id) {
        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        repository.delete(entity);
    }

}
