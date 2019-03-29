package br.com.erudio.services;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.converter.DozerParser;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.models.Person;
import br.com.erudio.repository.PersonRepository;
import br.com.erudio.vo.PersonVO;
 
@Service
public class PersonService {
     
    @Autowired
    PersonRepository repository;
    
    public PersonVO create(PersonVO person) {
    	var entity = DozerParser.parseObjectInputToObjectOutput(person, Person.class);
    	var vo = DozerParser.parseObjectInputToObjectOutput(repository.save(entity), PersonVO.class);
    	return vo;
    }
 
    public PersonVO findById(Long id) {
    	var entity = repository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return DozerParser.parseObjectInputToObjectOutput(entity, PersonVO.class);
    }
 
    public List<PersonVO> findAll() {
    	return DozerParser.parserListObjectInputToObjectOutput(repository.findAll(), PersonVO.class);
    }
     
    public PersonVO update(PersonVO person) {
    	Person entity = repository.findById(person.getId())
                 .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		entity.setAddress(person.getAddress());
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setGender(person.getGender());
		var vo = DozerParser.parseObjectInputToObjectOutput(repository.save(entity), PersonVO.class);
    	return vo;
    }
 
    public void delete(Long id) {
    	Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(person);
    }
}
