package br.com.erudio.controllers;
 
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.services.PersonService;
 
@RestController
@ExposesResourceFor(PersonVO.class)
@RequestMapping("/api/person/v1")
public class PersonController {
     
    @Autowired
    private PersonService personService;
     
    @RequestMapping(value = "/{id}",
    method = RequestMethod.GET, 
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO get(@PathVariable(value = "id") Long id){
        PersonVO personVO = personService.findById(id);
        personVO.add(linkTo(methodOn(PersonController.class).get(id)).withSelfRel());
        return personVO;
    }
     
    @RequestMapping(method = RequestMethod.GET,
	produces = { "application/json", "application/xml", "application/x-yaml" })
    public List<PersonVO> findAll(){
    	List<PersonVO> persons = personService.findAll();
    	persons
    		.stream()
    		.forEach(p -> p.add(
    				linkTo(methodOn(PersonController.class).get(p.getKey())).withSelfRel()
				)
			);
    	return persons;
    }
     
    @RequestMapping(method = RequestMethod.POST,
    consumes = { "application/json", "application/xml", "application/x-yaml" },
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO create(@RequestBody PersonVO person){
    	PersonVO personVO = personService.create(person);
        personVO.add(linkTo(methodOn(PersonController.class).get(personVO.getKey())).withSelfRel());
        return personVO;
    }
     
    @RequestMapping(method = RequestMethod.PUT,
    consumes = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO update(@RequestBody PersonVO person){
    	PersonVO personVO = personService.update(person);
        personVO.add(linkTo(methodOn(PersonController.class).get(personVO.getKey())).withSelfRel());
        return personVO;
    }
 
    @RequestMapping(value = "/{id}",
    method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
        personService.delete(id);
        return ResponseEntity.ok().build();
    }
}
