package br.com.erudio.services.implementations;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.models.Person;
import br.com.erudio.repository.PersonRepository;
import br.com.erudio.services.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
	
	 private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private PersonRepository personRepository;

    @Override
    public Person create(Person person) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Creating a person");
    	}
		return personRepository.save(person);
    }

    @Override
    public Person findById(String personId) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Finding a person by ID");
    	}
        return personRepository.findByIdPerson(personId);
    }

    @Override
    public List<Person> findAll() {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Finding all persons");
    	}
    	return personRepository.findAll();
    }

    @Override
    public Person update(Person person) {
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Updating a person");
    	}
    	return personRepository.save(person);
    }

    @Override
    public void delete(String personId) {
    	var person = personRepository.findByIdPerson(personId);
    	if (LOGGER.isDebugEnabled()) {
    		LOGGER.info("Deleting a person");
    	}
    	personRepository.delete(person);
    }
}
