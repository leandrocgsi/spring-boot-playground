package br.com.erudio.unittests.mockito.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import br.com.erudio.exception.RequiredObjectIsNullException;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import br.com.erudio.services.PersonServices;
import br.com.erudio.unittests.mapper.mocks.MockPerson;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

    MockPerson input;
    
    @InjectMocks
    private PersonServices service;
    
    @Mock
    PersonRepository repository;
    
    @BeforeEach
    public void setupMock() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        List<Person> list = input.mockEntityList();
        Page<Person> page = new PageImpl<Person>(list);
        
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Direction.ASC, "firstName"));
        
        when(repository.findAll(pageable)).thenReturn(page);
         
        Collection<PersonVO> searchPage = service.findAll(pageable).getContent();
        List<PersonVO> persons = searchPage.stream().collect(Collectors.toList());
        
        assertNotNull(persons);
        assertEquals(14, persons.size());
        
        var personOne = persons.get(1);
        
        assertNotNull(personOne);
        assertNotNull(personOne.getKey());
        assertNotNull(personOne.getLinks());
        assertTrue(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", personOne.getAddress());
        assertEquals("First Name Test1", personOne.getFirstName());
        assertEquals("Last Name Test1", personOne.getLastName());
        assertEquals("Female", personOne.getGender());
        
        var personFour = persons.get(4);
        
        assertNotNull(personFour);
        assertNotNull(personFour.getKey());
        assertNotNull(personFour.getLinks());
        assertTrue(personFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));
        assertEquals("Addres Test4", personFour.getAddress());
        assertEquals("First Name Test4", personFour.getFirstName());
        assertEquals("Last Name Test4", personFour.getLastName());
        assertEquals("Male", personFour.getGender());

        var personSeven = persons.get(7);
        
        assertNotNull(personSeven);
        assertNotNull(personSeven.getKey());
        assertNotNull(personSeven.getLinks());
        assertTrue(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
        assertEquals("Addres Test7", personSeven.getAddress());
        assertEquals("First Name Test7", personSeven.getFirstName());
        assertEquals("Last Name Test7", personSeven.getLastName());
        assertEquals("Female", personSeven.getGender());
    }

    @Test
    void testFindPersonByName() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        List<Person> list = new ArrayList<Person>();
        list.add(person);
        
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Direction.ASC, "firstName"));
        Page<Person> page = new PageImpl<Person>(list);
        
        when(repository.findPersonByName("Person name", pageable)).thenReturn(page);
        
        Collection<PersonVO> searchPage = service.findPersonByName("Person name", pageable).getContent();
        List<PersonVO> persons = searchPage.stream().collect(Collectors.toList());
        
        PersonVO result = persons.get(0);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
        assertTrue(result.getEnabled());
    }
    
    @Test
    void testFindById() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        var result = service.findById(1L);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }
    
    @Test
    void testCreate() {
        Person entity = input.mockEntity(1);
        Person persisted = entity;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);
        
        when(repository.save(entity)).thenReturn(persisted);
        PersonVO result = service.create(vo);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }
    
    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdate() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        
        Person persisted = entity;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);
        PersonVO result = service.update(vo);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }
    
    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void disablePerson() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        person.setEnabled(false);

        when(repository.findById(1L)).thenReturn(Optional.of(person));
        var result = service.disablePerson(1L);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }
    
    @Test
    void testDelete() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        service.delete(1L);
    }
}