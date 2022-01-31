package br.com.erudio.unittests.services.mockito;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;

@TestInstance(Lifecycle.PER_CLASS)
public class PersonServicesTest {

	private PersonRepository personRepository;
    private Person person;
    
    @BeforeAll
    public void setupMock() {
        person = mock(Person.class);
        personRepository = mock(PersonRepository.class);
    }
    
	@Test
	void testCreate() {
		Assertions.assertNotNull(person);
        Assertions.assertNotNull(personRepository);
	}

	@Test
	void testFindPersonByName() {
		fail("Not yet implemented");
	}

	@Test
	void testFindAll() {
		fail("Not yet implemented");
	}

	@Test
	void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	void testDisablePerson() {
		fail("Not yet implemented");
	}

	@Test
	void testDelete() {
		fail("Not yet implemented");
	}

}
