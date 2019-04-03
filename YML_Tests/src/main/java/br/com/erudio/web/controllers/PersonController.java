package br.com.erudio.web.controllers;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.models.Person;
import br.com.erudio.services.implementations.PersonService;
 
@RestController
@RequestMapping("/person")
public class PersonController {
     
    @Autowired
    private PersonService personService;
     
    @RequestMapping(value = "/{personId}",
    method = RequestMethod.GET, 
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public Person get(@PathVariable(value = "personId") String personId){
        return personService.findById(personId);
    }
    
	@RequestMapping(method = RequestMethod.GET,
	produces = { "application/json", "application/xml", "application/x-yaml" })
    public List<Person> findAll(){
        return personService.findAll();
    }
     
    @RequestMapping(method = RequestMethod.PUT,
    consumes = { "application/json", "application/xml", "application/x-yaml" },
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public Person create(@RequestBody Person person){
        return personService.create(person);
    }
     
    @RequestMapping(method = RequestMethod.POST,
    consumes = { "application/json", "application/xml", "application/x-yaml" })
    public Person update(@RequestBody Person person){
        return personService.update(person);
    }
 
    @RequestMapping(value = "/{personId}",
    method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "personId") String personId){
        personService.delete(personId);
    }
}
