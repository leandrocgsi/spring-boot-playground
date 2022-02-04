package br.com.erudio.integrationtests.controller.withxml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.TokenVO;
import br.com.erudio.integrationtests.vo.PersonVO;
import br.com.erudio.integrationtests.vo.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static final String HEADER_PARAM = "Authorization";
    public static final String CONTENT_TYPE_XML = "application/xml";
    private static final int SERVER_PORT = 8888;
    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        person = new PersonVO();
    }

    @Test
    @Order(1)
    public void authorization() {
        AccountCredentialsVO user = new AccountCredentialsVO();
        user.setUsername("leandro");
        user.setPassword("admin123");

        var token =
                given()
                    .basePath("/auth/signin")
                    .port(SERVER_PORT)
                    .contentType(CONTENT_TYPE_XML)
                    .body(user)
                    .when()
                        .post()
                    .then()
                        .statusCode(200)
                    .extract()
                    .body()
                        .as(TokenVO.class)
                    .getAccessToken();

            specification =
                new RequestSpecBuilder()
                    .addHeader(HEADER_PARAM, "Bearer " + token)
                    .setBasePath("/api/person/v1")
                    .setPort(SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();
    }
      
    @Test
    @Order(2)
    void testCreate() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(CONTENT_TYPE_XML)
                    .body(person)
                    .when()
                    .post()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();
        
        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);    
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.getId() > 0);
        assertEquals("Richard", createdPerson.getFirstName());
        assertEquals("Stallman", createdPerson.getLastName());
        assertEquals("New York City, New York, US", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertEquals(true, createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void testUpdate() throws JsonMappingException, JsonProcessingException {
        person.setLastName("Matthew Stallman");
        
        var content = given().spec(specification)
                .contentType(CONTENT_TYPE_XML)
                    .body(person)
                    .when()
                    .post()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();    
        
        PersonVO updatedPerson = objectMapper.readValue(content, PersonVO.class);        

        assertNotNull(updatedPerson.getId());
        assertNotNull(updatedPerson.getFirstName());
        assertNotNull(updatedPerson.getLastName());
        assertNotNull(updatedPerson.getAddress());
        assertNotNull(updatedPerson.getGender());
        assertEquals(updatedPerson.getId(), person.getId());
        assertEquals("Richard", updatedPerson.getFirstName());
        assertEquals("Matthew Stallman", updatedPerson.getLastName());
        assertEquals("New York City, New York, US", updatedPerson.getAddress());
        assertEquals("Male", updatedPerson.getGender());
        assertEquals(true, updatedPerson.getEnabled());
    }

    @Test
    @Order(4)
    void testDisablePerson() throws JsonMappingException, JsonProcessingException {
        person.setEnabled(false);
        
        var content = given().spec(specification)
                .contentType(CONTENT_TYPE_XML)
                    .pathParam("id", person.getId())
                    .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();    
        
        PersonVO patchedPerson = objectMapper.readValue(content, PersonVO.class);        

        assertNotNull(patchedPerson.getId());
        assertNotNull(patchedPerson.getFirstName());
        assertNotNull(patchedPerson.getLastName());
        assertNotNull(patchedPerson.getAddress());
        assertNotNull(patchedPerson.getGender());
        assertEquals(patchedPerson.getId(), person.getId());
        assertEquals("Richard", patchedPerson.getFirstName());
        assertEquals("Matthew Stallman", patchedPerson.getLastName());
        assertEquals("New York City, New York, US", patchedPerson.getAddress());
        assertEquals("Male", patchedPerson.getGender());
        assertEquals(false, patchedPerson.getEnabled());
    }
    
    @Test
    @Order(5)
    void testFindPersonByName() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(CONTENT_TYPE_XML)
                    .pathParam("firstName", "Leandro")
                    .queryParams("page", 0 , "limit", 5, "direction", "asc")
                    .when()
                    .get("findPersonByName/{firstName}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();    
        
        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        var persons = wrapper.getEmbedded().getPersons();

        PersonVO foundPersonOne = persons.get(0);        

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        assertEquals(1, foundPersonOne.getId());
        assertEquals("Leandro", foundPersonOne.getFirstName());
        assertEquals("Costa", foundPersonOne.getLastName());
        assertEquals("Uberlândia - Minas Gerais - Brasil", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());
        assertEquals(true, foundPersonOne.getEnabled());
    }

    @Test
    @Order(6)
    void testFindById() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(CONTENT_TYPE_XML)
                    .pathParam("id", person.getId())
                    .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();    
        
        PersonVO foundPerson = objectMapper.readValue(content, PersonVO.class);        

        assertNotNull(foundPerson.getId());
        assertNotNull(foundPerson.getFirstName());
        assertNotNull(foundPerson.getLastName());
        assertNotNull(foundPerson.getAddress());
        assertNotNull(foundPerson.getGender());
        assertEquals(foundPerson.getId(), person.getId());
        assertEquals("Richard", foundPerson.getFirstName());
        assertEquals("Matthew Stallman", foundPerson.getLastName());
        assertEquals("New York City, New York, US", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertEquals(false, foundPerson.getEnabled());
    }
    
    @Test
    @Order(7)
    void testDelete() {
        given().spec(specification)
        .contentType(CONTENT_TYPE_XML)
            .pathParam("id", person.getId())
            .when()
            .delete("{id}")
        .then()
            .statusCode(204);
    }
    
    @Test
    @Order(8)
    void testFindAll() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(CONTENT_TYPE_XML)
                    .queryParams("page", 6 , "limit", 10, "direction", "asc")
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .asString();
        
        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapper.getEmbedded().getPersons();
            
        PersonVO foundPersonOne = people.get(0);        

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        assertEquals(964, foundPersonOne.getId());
        assertEquals("Ardath", foundPersonOne.getFirstName());
        assertEquals("Leckenby", foundPersonOne.getLastName());
        assertEquals("9 Chive Trail", foundPersonOne.getAddress());
        assertEquals("Female", foundPersonOne.getGender());
        assertEquals(true, foundPersonOne.getEnabled());
                
        PersonVO foundPersonSeven = people.get(6);        

        assertNotNull(foundPersonSeven.getId());
        assertNotNull(foundPersonSeven.getFirstName());
        assertNotNull(foundPersonSeven.getLastName());
        assertNotNull(foundPersonSeven.getAddress());
        assertNotNull(foundPersonSeven.getGender());
        assertEquals(189, foundPersonSeven.getId());
        assertEquals("Arlena", foundPersonSeven.getFirstName());
        assertEquals("Wagenen", foundPersonSeven.getLastName());
        assertEquals("1 Spaight Parkway", foundPersonSeven.getAddress());
        assertEquals("Female", foundPersonSeven.getGender());
        assertEquals(false, foundPersonSeven.getEnabled());
    }
    
    @Test
    @Order(9)
    void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

        RequestSpecification specificationWithoutToken =
                new RequestSpecBuilder()
                    .setBasePath("/api/person/v1")
                    .setPort(SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();
        
        given().spec(specificationWithoutToken)
                .contentType(CONTENT_TYPE_XML)
                .queryParams("page", 6 , "limit", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockPerson() {
        person.setFirstName("Richard");
        person.setLastName("Stallman");
        person.setAddress("New York City, New York, US");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
