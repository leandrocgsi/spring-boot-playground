package br.com.erudio.services;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.erudio.converter.DozerConverter;
import br.com.erudio.data.models.Person;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.repository.PersonRepository;
 
@Service
public class PersonService {
     
    @Autowired
    PersonRepository repository;
    
    public PersonVO create(PersonVO person) {
    	var entity = DozerConverter.parseObject(person, Person.class);
    	var vo = DozerConverter.parseObject(repository.save(entity), PersonVO.class);
    	return vo;
    }
 
    public PersonVO findById(Long id) {
    	var entity = repository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
        return DozerConverter.parseObject(entity, PersonVO.class);
    }
 
    public List<PersonVO> findAll() {
    	return DozerConverter.parserListObjects(repository.findAll(), PersonVO.class);
    }
     
    public PersonVO update(PersonVO person) {
    	Person entity = repository.findById(person.getKey())
                 .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		entity.setAddress(person.getAddress());
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setGender(person.getGender());
		var vo = DozerConverter.parseObject(repository.save(entity), PersonVO.class);
    	return vo;
    }
    
    @Transactional
    public PersonVO disablePerson(Long id) {
    	repository.disablePerson(id);
    	var entity = repository.findById(id)
    			.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    	return DozerConverter.parseObject(entity, PersonVO.class);
    }
 
    public void delete(Long id) {
    	Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(person);
    }

	public Page<PersonVO> findAll(Pageable pageableRequest) {
		var entities = repository.findAll(pageableRequest);
	    return entities.map(this::convertToPersonVO);
	}
	
	private PersonVO convertToPersonVO(Person entity) {
	    return DozerConverter.parseObject(entity, PersonVO.class);
	}
}
