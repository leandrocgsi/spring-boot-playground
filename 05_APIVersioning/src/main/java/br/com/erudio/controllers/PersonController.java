package br.com.erudio.controllers;
 
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

import br.com.erudio.services.PersonService;
import br.com.erudio.vo.PersonVO;
import br.com.erudio.vo.v2.PersonVOV2;
 
@RestController
@RequestMapping("/person")
public class PersonController {
     
    @Autowired
    private PersonService personService;
     
    @GetMapping("/{id}")
    public PersonVO get(@PathVariable(value = "id") Long id){
        return personService.findById(id);
    }
     
    @GetMapping
    public List<PersonVO> findAll(){
        return personService.findAll();
    }
    
	@PostMapping
	public PersonVO create(@RequestBody PersonVO person){
	    return personService.create(person);
	}
   
	@PostMapping("/v2")
	public PersonVOV2 createV2(@RequestBody PersonVOV2 person){
	    return personService.createV2(person);
	}
     
    @PutMapping
    public PersonVO update(@RequestBody PersonVO person){
        return personService.update(person);
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
        personService.delete(id);
        return ResponseEntity.ok().build();
    }
}
