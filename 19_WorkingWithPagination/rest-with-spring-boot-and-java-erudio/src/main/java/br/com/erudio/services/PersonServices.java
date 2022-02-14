package br.com.erudio.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.erudio.controller.PersonController;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exception.RequiredObjectIsNullException;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.mapper.DozerConverter;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;

@Service
public class PersonServices {
    
    @Autowired
    PersonRepository repository;
        
    public CollectionModel<PersonVO> findAll(Pageable pageable) {
        var page = repository.findAll(pageable);
        var persons = page.map(this::convertToPersonVO);
        
        persons
            .stream()
            .forEach(p -> p.add(
                        linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()
                    )
            );
        
        Link findAllLink = linkTo(
          methodOn(PersonController.class).findAll(pageable.getPageNumber(),
                                                   pageable.getPageSize(),
                                                   "asc")).withSelfRel();
        
        return CollectionModel.of(persons, findAllLink);
    }    
    
    public CollectionModel<PersonVO>  findPersonByName(String firstName, Pageable pageable) {
        var page = repository.findPersonByName(firstName, pageable);
        var persons = page.map(this::convertToPersonVO);
        persons
        .stream()
        .forEach(p -> p.add(
                            linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()
                    )
                );
        
        Link findAllLink = linkTo(
          methodOn(PersonController.class)
              .findPersonByName(firstName,
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    "asc")
                ).withSelfRel();
        
        return CollectionModel.of(persons, findAllLink);
    }    
    
    public PersonVO findById(Long id) {

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        PersonVO personVO = DozerConverter.parseObject(entity, PersonVO.class);
        personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personVO;
    }
    
    public PersonVO create(PersonVO person) {
        if(person == null) throw new RequiredObjectIsNullException();
        var entity = DozerConverter.parseObject(person, Person.class);
        var personVO = DozerConverter.parseObject(repository.save(entity), PersonVO.class);
        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }

    public PersonVO update(PersonVO person) {
        if(person == null) throw new RequiredObjectIsNullException();
        var entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        
        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        
        var personVO = DozerConverter.parseObject(repository.save(entity), PersonVO.class);
        personVO.add(linkTo(methodOn(PersonController.class).findById(personVO.getKey())).withSelfRel());
        return personVO;
    }    
    
    @Transactional
    public PersonVO disablePerson(Long id) {
        repository.disablePersons(id);          
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        PersonVO personVO = DozerConverter.parseObject(entity, PersonVO.class);
        personVO.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return personVO;
    }

    public void delete(Long id) {
        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        repository.delete(entity);
    }

    private PersonVO convertToPersonVO(Person entity) {
        return DozerConverter.parseObject(entity, PersonVO.class);
    }
}