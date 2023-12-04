package br.com.erudio.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.model.Book;
import br.com.erudio.model.Person;
 
@Component
public class ModelMapperConfig {
 
    @Bean
    ModelMapper modelMapper() {
 
        ModelMapper mapper = new ModelMapper();
        mapper.createTypeMap(
                Person.class,
                PersonVO.class)
            .addMapping(Person::getId, PersonVO::setKey);
        mapper.createTypeMap(
                PersonVO.class,
                Person.class)
            .addMapping(PersonVO::getKey, Person::setId);
        
        mapper.createTypeMap(
                Book.class,
                BookVO.class)
        .addMapping(Book::getId, BookVO::setKey);
        mapper.createTypeMap(
                BookVO.class,
                Book.class)
        .addMapping(BookVO::getKey, Book::setId);
        
        return mapper;
    }
}