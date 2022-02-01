package br.com.erudio.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.LoginResponseVO;
import br.com.erudio.integrationtests.vo.PersonVO;
import br.com.erudio.integrationtests.vo.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static final String HEADER_PARAM = "Authorization";
    public static final String CONTENT_TYPE_YML = "application/x-yaml";
    private static final int SERVER_PORT = 8888;
    private static RequestSpecification specification;

    private static YMLMapper objectMapper;

    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        objectMapper = new YMLMapper();
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
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .basePath("/auth/signin")
                    .port(SERVER_PORT)
                    .contentType(CONTENT_TYPE_YML)
                    .body(user, objectMapper)
                    .when()
                        .post()
                    .then()
                        .statusCode(200)
                    .extract()
                    .body()
                        .as(LoginResponseVO.class, objectMapper)
                    .getToken();

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

        PersonVO createdPerson = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .body(person, objectMapper)
                    .when()
                    .post()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .as(PersonVO.class, objectMapper);
        
        person = createdPerson;

        Assertions.assertNotNull(createdPerson.getId());
        Assertions.assertNotNull(createdPerson.getFirstName());
        Assertions.assertNotNull(createdPerson.getLastName());
        Assertions.assertNotNull(createdPerson.getAddress());
        Assertions.assertNotNull(createdPerson.getGender());
        Assertions.assertTrue(createdPerson.getId() > 0);
        Assertions.assertEquals("Richard", createdPerson.getFirstName());
        Assertions.assertEquals("Stallman", createdPerson.getLastName());
        Assertions.assertEquals("New York City, New York, US", createdPerson.getAddress());
        Assertions.assertEquals("Male", createdPerson.getGender());
        Assertions.assertEquals(true, createdPerson.getEnabled());
    }

    @Test
    @Order(3)
    void testUpdate() throws JsonMappingException, JsonProcessingException {
        person.setLastName("Matthew Stallman");
        
        PersonVO updatedPerson = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .body(person, objectMapper)
                    .when()
                    .post()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .as(PersonVO.class, objectMapper);
        
        Assertions.assertNotNull(updatedPerson.getId());
        Assertions.assertNotNull(updatedPerson.getFirstName());
        Assertions.assertNotNull(updatedPerson.getLastName());
        Assertions.assertNotNull(updatedPerson.getAddress());
        Assertions.assertNotNull(updatedPerson.getGender());
        Assertions.assertEquals(updatedPerson.getId(), person.getId());
        Assertions.assertEquals("Richard", updatedPerson.getFirstName());
        Assertions.assertEquals("Matthew Stallman", updatedPerson.getLastName());
        Assertions.assertEquals("New York City, New York, US", updatedPerson.getAddress());
        Assertions.assertEquals("Male", updatedPerson.getGender());
        Assertions.assertEquals(true, updatedPerson.getEnabled());
    }

    @Test
    @Order(4)
    void testDisablePerson() throws JsonMappingException, JsonProcessingException {
        person.setEnabled(false);
        
        PersonVO patchedPerson = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .pathParam("id", person.getId())
                    .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .as(PersonVO.class, objectMapper);        

        Assertions.assertNotNull(patchedPerson.getId());
        Assertions.assertNotNull(patchedPerson.getFirstName());
        Assertions.assertNotNull(patchedPerson.getLastName());
        Assertions.assertNotNull(patchedPerson.getAddress());
        Assertions.assertNotNull(patchedPerson.getGender());
        Assertions.assertEquals(patchedPerson.getId(), person.getId());
        Assertions.assertEquals("Richard", patchedPerson.getFirstName());
        Assertions.assertEquals("Matthew Stallman", patchedPerson.getLastName());
        Assertions.assertEquals("New York City, New York, US", patchedPerson.getAddress());
        Assertions.assertEquals("Male", patchedPerson.getGender());
        Assertions.assertEquals(false, patchedPerson.getEnabled());
    }
    
    @Test
    @Order(5)
    void testFindPersonByName() throws JsonMappingException, JsonProcessingException {
        WrapperPersonVO wrapper = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .pathParam("firstName", "Leandro")
                    .queryParams("page", 0 , "limit", 5, "direction", "asc")
                    .when()
                    .get("findPersonByName/{firstName}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .as(WrapperPersonVO.class, objectMapper);    
        
        var persons = wrapper.getEmbedded().getPersons();

        PersonVO foundPersonOne = persons.get(0);        

        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getFirstName());
        Assertions.assertNotNull(foundPersonOne.getLastName());
        Assertions.assertNotNull(foundPersonOne.getAddress());
        Assertions.assertNotNull(foundPersonOne.getGender());
        Assertions.assertEquals(1, foundPersonOne.getId());
        Assertions.assertEquals("Leandro", foundPersonOne.getFirstName());
        Assertions.assertEquals("Costa", foundPersonOne.getLastName());
        Assertions.assertEquals("Uberlândia - Minas Gerais - Brasil", foundPersonOne.getAddress());
        Assertions.assertEquals("Male", foundPersonOne.getGender());
        Assertions.assertEquals(true, foundPersonOne.getEnabled());
    }

    @Test
    @Order(6)
    void testFindById() throws JsonMappingException, JsonProcessingException {
        PersonVO foundPerson = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .pathParam("id", person.getId())
                    .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .as(PersonVO.class, objectMapper);
        
        Assertions.assertNotNull(foundPerson.getId());
        Assertions.assertNotNull(foundPerson.getFirstName());
        Assertions.assertNotNull(foundPerson.getLastName());
        Assertions.assertNotNull(foundPerson.getAddress());
        Assertions.assertNotNull(foundPerson.getGender());
        Assertions.assertEquals(foundPerson.getId(), person.getId());
        Assertions.assertEquals("Richard", foundPerson.getFirstName());
        Assertions.assertEquals("Matthew Stallman", foundPerson.getLastName());
        Assertions.assertEquals("New York City, New York, US", foundPerson.getAddress());
        Assertions.assertEquals("Male", foundPerson.getGender());
        Assertions.assertEquals(false, foundPerson.getEnabled());
    }
    
    @Test
    @Order(7)
    void testDelete() {
        given()
        .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
            .spec(specification)
        .contentType(CONTENT_TYPE_YML)
            .pathParam("id", person.getId())
            .when()
            .delete("{id}")
        .then()
            .statusCode(204);
    }
    
    @Test
    @Order(8)
    void testFindAll() throws JsonMappingException, JsonProcessingException {
        WrapperPersonVO wrapper = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .queryParams("page", 6 , "limit", 10, "direction", "asc")
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .as(WrapperPersonVO.class, objectMapper);
        
        var people = wrapper.getEmbedded().getPersons();
            
        PersonVO foundPersonOne = people.get(0);        

        Assertions.assertNotNull(foundPersonOne.getId());
        Assertions.assertNotNull(foundPersonOne.getFirstName());
        Assertions.assertNotNull(foundPersonOne.getLastName());
        Assertions.assertNotNull(foundPersonOne.getAddress());
        Assertions.assertNotNull(foundPersonOne.getGender());
        Assertions.assertEquals(964, foundPersonOne.getId());
        Assertions.assertEquals("Ardath", foundPersonOne.getFirstName());
        Assertions.assertEquals("Leckenby", foundPersonOne.getLastName());
        Assertions.assertEquals("9 Chive Trail", foundPersonOne.getAddress());
        Assertions.assertEquals("Female", foundPersonOne.getGender());
        Assertions.assertEquals(true, foundPersonOne.getEnabled());
                
        PersonVO foundPersonSeven = people.get(6);        

        Assertions.assertNotNull(foundPersonSeven.getId());
        Assertions.assertNotNull(foundPersonSeven.getFirstName());
        Assertions.assertNotNull(foundPersonSeven.getLastName());
        Assertions.assertNotNull(foundPersonSeven.getAddress());
        Assertions.assertNotNull(foundPersonSeven.getGender());
        Assertions.assertEquals(189, foundPersonSeven.getId());
        Assertions.assertEquals("Arlena", foundPersonSeven.getFirstName());
        Assertions.assertEquals("Wagenen", foundPersonSeven.getLastName());
        Assertions.assertEquals("1 Spaight Parkway", foundPersonSeven.getAddress());
        Assertions.assertEquals("Female", foundPersonSeven.getGender());
        Assertions.assertEquals(false, foundPersonSeven.getEnabled());
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
        
        given()
            .config(
                    RestAssuredConfig
                        .config()
                        .encoderConfig(EncoderConfig.encoderConfig()
                                .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specificationWithoutToken)
                .contentType(CONTENT_TYPE_YML)
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
