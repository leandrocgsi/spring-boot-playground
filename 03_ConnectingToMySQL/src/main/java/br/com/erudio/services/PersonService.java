package br.com.erudio.services;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.models.Person;
import br.com.erudio.repository.PersonRepository;
 
@Service
public class PersonService {
     
    @Autowired
    PersonRepository repository;
    
    public Person create(Person person) {
    	return repository.save(person);
    }
 
    public Person findById(Long id) {
        return repository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    }
 
    public List<Person> findAll() {
    	return repository.findAll();
    }
     
    public Person update(Person person) {
    	Person persistedPerson = repository.findById(person.getId())
                 .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		persistedPerson.setAddress(person.getAddress());
		persistedPerson.setFirstName(person.getFirstName());
		persistedPerson.setLastName(person.getLastName());
		persistedPerson.setGender(person.getGender());
		
		return repository.save(persistedPerson);
    }
 
    public void delete(Long id) {
    	Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(person);
    }
}
