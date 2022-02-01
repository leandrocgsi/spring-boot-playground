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
         
        Page<PersonVO> searchPage = service.findAll(pageable);
        List<PersonVO> persons = searchPage.getContent();
        
        Assertions.assertNotNull(searchPage);
        Assertions.assertNotNull(persons);
        Assertions.assertEquals(14, searchPage.getContent().size());
        
        var personOne = persons.get(1);
        
        Assertions.assertNotNull(personOne);
        Assertions.assertNotNull(personOne.getKey());
        
        Assertions.assertEquals("Addres Test1", personOne.getAddress());
        Assertions.assertEquals("First Name Test1", personOne.getFirstName());
        Assertions.assertEquals("Last Name Test1", personOne.getLastName());
        Assertions.assertEquals("Female", personOne.getGender());
        assertTrue(personOne.getEnabled());
        
        var personFour = persons.get(4);
        
        assertNotNull(personFour);
        assertNotNull(personFour.getKey());
        
        assertEquals("Addres Test4", personFour.getAddress());
        assertEquals("First Name Test4", personFour.getFirstName());
        assertEquals("Last Name Test4", personFour.getLastName());
        assertEquals("Male", personFour.getGender());
        assertTrue(personFour.getEnabled());
        
        var personSeven = persons.get(7);
        
        assertNotNull(personSeven);
        assertNotNull(personSeven.getKey());
        
        assertEquals("Addres Test7", personSeven.getAddress());
        assertEquals("First Name Test7", personSeven.getFirstName());
        assertEquals("Last Name Test7", personSeven.getLastName());
        assertEquals("Female", personSeven.getGender());
        assertTrue(personSeven.getEnabled());
    }

    @Test
    void testFindById() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        var result = service.findById(1L);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
        assertTrue(result.getEnabled());
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
        Page<PersonVO> searchPage = service.findPersonByName("Person name", pageable);
        PersonVO result = searchPage.getContent().get(0);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
        assertTrue(result.getEnabled());
    }
    
    @Test
    void testCreateWithDefaultMocks() {
        assertNotNull(repository);
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
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
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
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
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
        Person entity = input.mockEntity(1);
        entity.setId(1L);
        entity.setEnabled(false);
        
        Person persisted = entity;
        persisted.setId(1L);
        persisted.setEnabled(false);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);
        
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        PersonVO result = service.disablePerson(1L);
        
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
        assertFalse(result.getEnabled());
    }

    @Test
    void testDelete() {
        Person person = input.mockEntity(1);
        person.setId(1L);
        when(repository.findById(1L)).thenReturn(Optional.of(person));
        service.delete(1L);
    }
}