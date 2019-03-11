package br.com.erudio.services.implementations;
 
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import br.com.erudio.models.Person;
 
@Service
public class PersonService {
     
    private final AtomicLong counter = new AtomicLong();
 
    // Metodo responsável por criar uma nova pessoa
    // Se tivéssemos um banco de dados esse seria o
    // momento de persistir os dados
    public Person create(Person person) {
        return person;
    }
 
    // Método responsável por retornar uma pessoa
    // como não acessamos nenhuma base de dados
    // estamos retornando um mock
    public Person findById(String personId) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Leandro");
        person.setLastName("Costa");
        person.setAddress("Uberlândia - Minas Gerais - Brasil");
        return person;
    }
 
    // Método responsável por retornar todas as pessoas
    // mais uma vez essas informações são mocks
    public List<Person> findAll() {
        ArrayList<Person> persons = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Person person = mockPerson(i);
            persons.add(person);
        }
        return persons;
    }
     
    // Método responsável por atualizar uma pessoa
    // por ser mock retornamos a mesma informação passada
    public Person update(Person person) {
        return person;
    }
 
    // Método responsável por deletar
    // uma pessoa a partir de um ID
    public void delete(String personId) {
 
    }
         
    // Método responsável por mockar uma pessoa
    private Person mockPerson(int i) {
        Person person = new Person();
        person.setId(counter.incrementAndGet());
        person.setFirstName("Person Name " + i);
        person.setLastName("Last Name " + i);
        person.setAddress("Some Address in Brasil " + i);
        return person;
    }
}
