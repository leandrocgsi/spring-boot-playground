package br.com.erudio.integrationtests.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    PersonRepository repository;
    
    private static Person person;

    @BeforeAll
    public static void setup() {
        person = new Person();
    }
    
    @Test
    @Order(1)
    void testFindPersonByName() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by(Direction.ASC, "firstName"));
        person = repository.findPersonByName("Lean", pageable).getContent().get(0);
        
        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Uberlândia - Minas Gerais - Brasil", person.getAddress());
        assertEquals("Leandro", person.getFirstName());
        assertEquals("Costa", person.getLastName());
        assertEquals("Male", person.getGender());
        assertTrue(person.getEnabled());
    }
    
    @Test
    @Order(2)
    void testDisablePersons() {
        Long id = person.getId();
        repository.disablePersons(id);
        
        var result = repository.findById(id);
        person = result.get();
        
        assertNotNull(result);
        assertNotNull(person);
        assertNotNull(person.getId());
        assertEquals("Uberlândia - Minas Gerais - Brasil", person.getAddress());
        assertEquals("Leandro", person.getFirstName());
        assertEquals("Costa", person.getLastName());
        assertEquals("Male", person.getGender());
        assertFalse(person.getEnabled());
    }
}