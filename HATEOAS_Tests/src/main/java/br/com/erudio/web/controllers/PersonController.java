package br.com.erudio.web.controllers;
 
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.erudio.models.Person;
import br.com.erudio.services.PersonService;
 
@RestController
@RequestMapping("/api/person/v1")
public class PersonController {
     
    @Autowired
    private PersonService personService;
     
    @RequestMapping(value = "/{id}",
    method = RequestMethod.GET, 
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public Person get(@PathVariable(value = "id") Long id){
        Person person = personService.findById(id);
        person.add(linkTo(methodOn(PersonController.class).get(id)).withSelfRel());
        return person;
    }
     
    @JsonIgnoreProperties(ignoreUnknown=true)
    @RequestMapping(method = RequestMethod.GET,
	produces = { "application/json", "application/xml", "application/x-yaml" })
    public List<Person> findAll(){
    	List<Person> persons = personService.findAll();
    	ArrayList<Person> personsReturn = new ArrayList<Person>();
        for (Person person : persons) {
            person.add(linkTo(methodOn(PersonController.class).get(person.getKey())).withSelfRel());
            personsReturn.add(person);
        }
    	/*persons
    		.stream()
    		.forEach(p -> p.add(
    				linkTo(methodOn(PersonController.class).get(p.getKey())).withSelfRel()
				)
			);*/
    	
    	return personsReturn;
    }
     
    @RequestMapping(method = RequestMethod.POST,
    consumes = { "application/json", "application/xml", "application/x-yaml" },
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public Person create(@RequestBody Person p){
    	Person person = personService.create(p);
        person.add(linkTo(methodOn(PersonController.class).get(person.getKey())).withSelfRel());
        return person;
    }
     
    @RequestMapping(method = RequestMethod.PUT,
    consumes = { "application/json", "application/xml", "application/x-yaml" })
    public Person update(@RequestBody Person p){
    	Person person = personService.update(p);
        person.add(linkTo(methodOn(PersonController.class).get(person.getKey())).withSelfRel());
        return person;
    }
 
    @RequestMapping(value = "/{id}",
    method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
        personService.delete(id);
        return ResponseEntity.ok().build();
    }
}