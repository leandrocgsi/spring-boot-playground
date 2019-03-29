package br.com.erudio.services;
 
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.erudio.converter.DozerConverter;
import br.com.erudio.converter.custom.PersonConverter;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.models.Person;
import br.com.erudio.repository.PersonRepository;
import br.com.erudio.vo.PersonVO;
import br.com.erudio.vo.v2.PersonVOV2;
 
@Service
public class PersonService {
     
    @Autowired
    PersonRepository repository;
    
    @Autowired
    PersonConverter converter;
    
    public PersonVO create(PersonVO person) {
    	var entity = DozerConverter.parseObject(person, Person.class);
    	var vo = DozerConverter.parseObject(repository.save(entity), PersonVO.class);
    	return vo;
    }
    
    public PersonVOV2 createV2(PersonVOV2 person) {
    	var entity = converter.convertVOtoEntity(person);
    	var vo = converter.convertEntitytoVO(entity);
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
    	Person entity = repository.findById(person.getId())
                 .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));

		entity.setAddress(person.getAddress());
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setGender(person.getGender());
		var vo = DozerConverter.parseObject(repository.save(entity), PersonVO.class);
    	return vo;
    }
 
    public void delete(Long id) {
    	Person person = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		
		repository.delete(person);
    }
}
