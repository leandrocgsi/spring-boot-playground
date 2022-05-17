package br.com.erudio.integrationtests.controller.cors.withjson;

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

import br.com.erudio.configs.TestsConfig;
import br.com.erudio.data.vo.v1.security.TokenVO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.PersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerCorsWithJsonTest extends AbstractIntegrationTest {

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
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

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
                    .header(TestsConfig.HEADER_PARAM_ORIGIN, "https://erudio.com.br")
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
    void testCreateWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        mockPerson();
        
        var content = given().spec(specification)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
                .header(TestsConfig.HEADER_PARAM_ORIGIN, "https://semeru.com.br")
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();
        
        
        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(4)
    void testFindById() throws JsonMappingException, JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
                .header(TestsConfig.HEADER_PARAM_ORIGIN, "http://localhost:8080")
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
        assertEquals("Stallman", foundPerson.getLastName());
        assertEquals("New York City, New York, US", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
    }
    
    @Test
    @Order(5)
    void testFindByIdWithWrongOrigin() throws JsonMappingException, JsonProcessingException {
        
        var content = given().spec(specification)
                .contentType(TestsConfig.CONTENT_TYPE_JSON)
                .header(TestsConfig.HEADER_PARAM_ORIGIN, "https://semeru.com.br")
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                    .statusCode(403)
                .extract()
                    .body()
                        .asString();
        
        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }
    
    private void mockPerson() {
        person.setFirstName("Richard");
        person.setLastName("Stallman");
        person.setAddress("New York City, New York, US");
        person.setGender("Male");
    }
}
