package br.com.erudio.services;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.erudio.converter.DozerConverter;
import br.com.erudio.data.models.Book;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.repository.BookRepository;
 
@Service
public class BookService {
     
    @Autowired
    BookRepository repository;
    
    public BookVO create(BookVO book) {
    	var entity = DozerConverter.parseObject(book, Book.class);
    	var vo = DozerConverter.parseObject(repository.save(entity), BookVO.class);
    	return vo;
    }
 
    public BookVO findById(Long id) {
    	var entity = repository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return DozerConverter.parseObject(entity, BookVO.class);
    }
 
    public Page<BookVO> findAll(Pageable pageableRequest) {
		var entities = repository.findAll(pageableRequest);
	    return entities.map(this::convertToBookVO);
    }
     
    public BookVO update(BookVO book) {
    	Book entity = repository.findById(book.getKey())
                 .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		entity.setAuthor(book.getAuthor());
		entity.setLaunchDate(book.getLaunchDate());;
		entity.setPrice(book.getPrice());
		entity.setTitle(book.getTitle());
		var vo = DozerConverter.parseObject(repository.save(entity), BookVO.class);
    	return vo;
    }
 
    public void delete(Long id) {
    	Book book = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(book);
    }
    
	private BookVO convertToBookVO(Book entity) {
	    return DozerConverter.parseObject(entity, BookVO.class);
	}
}
