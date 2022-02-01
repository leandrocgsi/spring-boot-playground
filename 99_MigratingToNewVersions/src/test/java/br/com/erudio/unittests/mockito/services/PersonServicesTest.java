package br.com.erudio.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
import br.com.erudio.exception.RequiredObjectNullException;
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
        MockitoAnnotations.openMocks(this);
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
        //Assertions.assertNotNull(result.getKey());
        
        Assertions.assertEquals("Some address in Brasil 1", personOne.getAddress());
        Assertions.assertEquals("Person name 1", personOne.getFirstName());
        Assertions.assertEquals("Last name 1", personOne.getLastName());
        Assertions.assertEquals("Male", personOne.getGender());
        assertTrue(personOne.getEnabled());
        
        var personFour = persons.get(4);
        
        assertNotNull(personFour);
        //assertNotNull(result.getKey());
        
        assertEquals("Some address in Brasil 4", personFour.getAddress());
        assertEquals("Person name 4", personFour.getFirstName());
        assertEquals("Last name 4", personFour.getLastName());
        assertEquals("Male", personFour.getGender());
        assertTrue(personFour.getEnabled());
        
        var personSeven = persons.get(7);
        
        assertNotNull(personSeven);
        //assertNotNull(result.getKey());
        
        assertEquals("Some address in Brasil 7", personSeven.getAddress());
        assertEquals("Person name 7", personSeven.getFirstName());
        assertEquals("Last name 7", personSeven.getLastName());
        assertEquals("Male", personSeven.getGender());
        assertTrue(personSeven.getEnabled());
    }

    @Test
    void testFindById() {
        Person person = mockPerson(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        var result = service.findById(1L);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals("Some address in Brasil 1", result.getAddress());
        assertEquals("Person name 1", result.getFirstName());
        assertEquals("Last name 1", result.getLastName());
        assertEquals("Male", result.getGender());
        assertTrue(result.getEnabled());
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
        PersonVO result = searchPage.getContent().get(0);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals("Some address in Brasil 1", result.getAddress());
        assertEquals("Person name 1", result.getFirstName());
        assertEquals("Last name 1", result.getLastName());
        assertEquals("Male", result.getGender());
        assertTrue(result.getEnabled());
    }
    
    @Test
    void testCreateWithDefaultMocks() {
        assertNotNull(repository);
    }
    
    @Test
    void testCreate() {
        Person entity = mockPerson(1);
        Person persisted = entity;
        persisted.setId(1L);

        PersonVO vo = mockPersonVO(1);
        vo.setKey(1L);
        
        when(repository.save(entity)).thenReturn(persisted);
        PersonVO result = service.create(vo);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals("Some address in Brasil 1", result.getAddress());
        assertEquals("Person name 1", result.getFirstName());
        assertEquals("Last name 1", result.getLastName());
        assertEquals("Male", result.getGender());
        assertTrue(result.getEnabled());
    }
    
    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdate() {
        Person entity = mockPerson(1);
        entity.setId(1L);
        
        Person persisted = entity;
        persisted.setId(1L);

        PersonVO vo = mockPersonVO(1);
        vo.setKey(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);
        PersonVO result = service.update(vo);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals("Some address in Brasil 1", result.getAddress());
        assertEquals("Person name 1", result.getFirstName());
        assertEquals("Last name 1", result.getLastName());
        assertEquals("Male", result.getGender());
        assertTrue(result.getEnabled());
    }
    
    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDisablePerson() {
        Person entity = mockPerson(1);
        entity.setId(1L);
        entity.setEnabled(false);
        
        Person persisted = entity;
        persisted.setId(1L);
        persisted.setEnabled(false);

        PersonVO vo = mockPersonVO(1);
        vo.setKey(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        PersonVO result = service.disablePerson(1L);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals("Some address in Brasil 1", result.getAddress());
        assertEquals("Person name 1", result.getFirstName());
        assertEquals("Last name 1", result.getLastName());
        assertEquals("Male", result.getGender());
        assertFalse(result.getEnabled());
    }

    @Test
    void testDelete() {
        Person person = mockPerson(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        service.delete(1L);
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
