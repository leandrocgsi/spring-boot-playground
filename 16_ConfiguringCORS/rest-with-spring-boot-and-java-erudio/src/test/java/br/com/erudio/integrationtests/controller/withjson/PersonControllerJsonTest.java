package br.com.erudio.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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

import br.com.erudio.configs.TestsConfig;
import br.com.erudio.data.vo.v1.security.AccountCredentialsVO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.PersonVO;
import br.com.erudio.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

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
                    .port(TestsConfig.SERVER_PORT)
                    .contentType(TestsConfig.CONTENT_TYPE_JSON)
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
                    .addHeader(TestsConfig.HEADER_PARAM_AUTHORIZATION, "Bearer " + token)
                    .setBasePath("/api/person/v1")
                    .setPort(TestsConfig.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();
    }
      
    @Test
    @Order(2)
    void testCreate() throws JsonMappingException, JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
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
    }

    @Test
    @Order(3)
    void testUpdate() throws JsonMappingException, JsonProcessingException {
        person.setLastName("Matthew Stallman");
        
        var content = given().spec(specification)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
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
    }

    
    @Test
    @Order(4)
    void testDelete() {
        given().spec(specification)
        .contentType(TestsConfig.CONTENT_TYPE_JSON)
            .pathParam("id", person.getId())
            .when()
            .delete("{id}")
        .then()
            .statusCode(204);
    }
    
    @Test
    @Order(5)
    void testFindAll() throws JsonMappingException, JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                    .extract()
                    .body()
                .as(new TypeRef<List<PersonVO>>() {}); 
        
        PersonVO foundPersonOne = content.get(0);        

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
                
        PersonVO foundPersonSeven = content.get(6);        

        assertNotNull(foundPersonSeven.getId());
        assertNotNull(foundPersonSeven.getFirstName());
        assertNotNull(foundPersonSeven.getLastName());
        assertNotNull(foundPersonSeven.getAddress());
        assertNotNull(foundPersonSeven.getGender());
        assertEquals(11, foundPersonSeven.getId());
        assertEquals("Richard", foundPersonSeven.getFirstName());
        assertEquals("Stallman", foundPersonSeven.getLastName());
        assertEquals("New York City, New York, US", foundPersonSeven.getAddress());
        assertEquals("Male", foundPersonSeven.getGender());
    }
    
    @Test
    @Order(6)
    void testFindAllWithoutToken() throws JsonMappingException, JsonProcessingException {

        RequestSpecification specificationWithoutToken =
                new RequestSpecBuilder()
                    .setBasePath("/api/person/v1")
                    .setPort(TestsConfig.SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();
        
        given().spec(specificationWithoutToken)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
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
    }
}
