package br.com.erudio.unittests.services.mockito;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import br.com.erudio.services.PersonServices;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

	@InjectMocks
	private PersonServices service;
	
	@Mock
    PersonRepository repository;
    
	@BeforeEach
    public void setupMock() {
    	// service = new PersonServices();
        // repository = mock(PersonRepository.class);
		MockitoAnnotations.openMocks(this);
    }
    
	@Test
	void testCreateWithDefaultMocks() {
        Assertions.assertNotNull(repository);
	}
	
	@Test
	void testCreate() {
		Person entity = mockPerson(1);
		Person savedPerson = mockPerson(1);
		PersonVO vo = mockPersonVO(1);
		
		when(repository.save(entity)).thenReturn(savedPerson);
		PersonVO savedvo = service.create(vo);
		
		
		Assertions.assertNotNull(savedvo);
		Assertions.assertNotNull(savedvo.getKey());
		Assertions.assertEquals(entity.getAddress(), savedvo.getAddress());
		Assertions.assertEquals(entity.getFirstName(), savedvo.getFirstName());
		Assertions.assertNotNull(repository);
	}

	@Test
	void testUpdate() {
		Person entity = mockPerson(1);
		PersonVO vo = mockPersonVO(1);
		
		when(repository.save(entity)).thenReturn(entity);
		
		Assertions.assertNotNull(vo);
		Assertions.assertNotNull(vo.getKey());
		Assertions.assertEquals("Some address in Brasil 1", vo.getAddress());
		Assertions.assertEquals("Person name 1", vo.getAddress());
		Assertions.assertNotNull(repository);
	}

	@Test
	void testFindPersonByName() {
		Person person = mockPerson(1);
		person.setId(1L);
        List<Person> list = new ArrayList<Person>();
        list.add(person);
        
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Direction.ASC, "firstName"));
		Page<Person> page = new PageImpl<Person>(list);
        
		when(repository.findPersonByName("Person name", pageable)).thenReturn(page);
		Page<PersonVO> searchPage = service.findPersonByName("Person name", pageable);
		PersonVO searchedPerson = searchPage.getContent().get(0);
		
		Assertions.assertNotNull(searchedPerson);
		Assertions.assertNotNull(searchedPerson.getKey());
		Assertions.assertEquals("Some address in Brasil 1", searchedPerson.getAddress());
		Assertions.assertEquals("Person name 1", searchedPerson.getFirstName());
		Assertions.assertEquals("Last name 1", searchedPerson.getLastName());
		Assertions.assertEquals("Male", searchedPerson.getGender());
		Assertions.assertEquals(true, searchedPerson.getEnabled());
	}

	@Test
	void testFindAll() {
        List<Person> list = findAll();
        Page<Person> page = new PageImpl<Person>(list);
        
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Direction.ASC, "firstName"));
        
        when(repository.findAll(pageable)).thenReturn(page);
         
        Page<PersonVO> searchPage = service.findAll(pageable);
        List<PersonVO> persons = searchPage.getContent();
        
		Assertions.assertNotNull(searchPage);
		Assertions.assertNotNull(persons);
		Assertions.assertEquals(8, searchPage.getContent().size());
		
		var personOne = persons.get(1);
		
		Assertions.assertNotNull(personOne);
		//Assertions.assertNotNull(searchedPerson.getKey());
		
		Assertions.assertEquals("Some address in Brasil 1", personOne.getAddress());
		Assertions.assertEquals("Person name 1", personOne.getFirstName());
		Assertions.assertEquals("Last name 1", personOne.getLastName());
		Assertions.assertEquals("Male", personOne.getGender());
		Assertions.assertEquals(true, personOne.getEnabled());
		
		var personFour = persons.get(4);
		
		Assertions.assertNotNull(personFour);
		//Assertions.assertNotNull(searchedPerson.getKey());
		
		Assertions.assertEquals("Some address in Brasil 4", personFour.getAddress());
		Assertions.assertEquals("Person name 4", personFour.getFirstName());
		Assertions.assertEquals("Last name 4", personFour.getLastName());
		Assertions.assertEquals("Male", personFour.getGender());
		Assertions.assertEquals(true, personFour.getEnabled());
		
		var personSeven = persons.get(7);
		
		Assertions.assertNotNull(personSeven);
		//Assertions.assertNotNull(searchedPerson.getKey());
		
		Assertions.assertEquals("Some address in Brasil 7", personSeven.getAddress());
		Assertions.assertEquals("Person name 7", personSeven.getFirstName());
		Assertions.assertEquals("Last name 7", personSeven.getLastName());
		Assertions.assertEquals("Male", personSeven.getGender());
		Assertions.assertEquals(true, personSeven.getEnabled());
         
	}

	@Test
	void testFindById() {
		Person person = mockPerson(1);
		person.setId(1L);
		when(repository.findById(1L)).thenReturn(Optional.of(person));
		var searchedPerson = service.findById(1L);
		
		Assertions.assertNotNull(searchedPerson);
		Assertions.assertNotNull(searchedPerson.getKey());
		Assertions.assertEquals("Some address in Brasil 1", searchedPerson.getAddress());
		Assertions.assertEquals("Person name 1", searchedPerson.getFirstName());
		Assertions.assertEquals("Last name 1", searchedPerson.getLastName());
		Assertions.assertEquals("Male", searchedPerson.getGender());
		Assertions.assertEquals(true, searchedPerson.getEnabled());
	}

	@Test
	void testDisablePerson() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}
	
	private Person mockPerson(int i) {
		Person person = new Person();
		person.setFirstName("Person name " + i);
		person.setLastName("Last name " + i);
		person.setAddress("Some address in Brasil " + i);
		person.setGender("Male");
		person.setEnabled(true);
		return person;
	}
	
	private PersonVO mockPersonVO(int i) {
		PersonVO person = new PersonVO();
		person.setFirstName("Person name " + i);
		person.setLastName("Last name " + i);
		person.setAddress("Some address in Brasil " + i);
		person.setGender("Male");
		person.setEnabled(true);
		return person;
	}
	
	public List<Person> findAll() {
		List<Person> persons = new ArrayList<Person>();
		for (int i = 0; i < 8; i++) {
			Person person = mockPerson(i);
			persons.add(person);			
		}
		return persons;
	}
	
	
	public List<PersonVO> findAllVO() {
		List<PersonVO> persons = new ArrayList<PersonVO>();
		for (int i = 0; i < 8; i++) {
			PersonVO person = mockPersonVO(i);
			persons.add(person);			
		}
		return persons;
	}
}
