package br.com.erudio.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;

import java.util.Date;

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
import br.com.erudio.integrationtests.vo.BookVO;
import br.com.erudio.integrationtests.vo.LoginResponseVO;
import br.com.erudio.integrationtests.vo.WrapperBookVO;
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
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static final String HEADER_PARAM = "Authorization";
    public static final String CONTENT_TYPE_YML = "application/x-yaml";
    private static final int SERVER_PORT = 8888;
    private static RequestSpecification specification;

    private static YMLMapper objectMapper;

    private static BookVO book;

    @BeforeAll
    public static void setup() {
        objectMapper = new YMLMapper();
        book = new BookVO();
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
                    .setBasePath("/api/book/v1")
                    .setPort(SERVER_PORT)
                    .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                    .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                    .build();
    }
      
    @Test
    @Order(2)
    public void testCreate() throws JsonMappingException, JsonProcessingException {
        
        mockBook();

        book = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .body(book, objectMapper)
                    .when()
                    .post()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                            .as(BookVO.class, objectMapper);
        
        Assertions.assertNotNull(book.getId());
        Assertions.assertNotNull(book.getTitle());
        Assertions.assertNotNull(book.getAuthor());
        Assertions.assertNotNull(book.getPrice());
        Assertions.assertTrue(book.getId() > 0);
        Assertions.assertEquals("Docker Deep Dive", book.getTitle());
        Assertions.assertEquals("Nigel Poulton", book.getAuthor());
        Assertions.assertEquals(55.99, book.getPrice());
    }
      
    @Test
    @Order(3)
    public void testUpdate() throws JsonMappingException, JsonProcessingException {
        
        book.setTitle("Docker Deep Dive - Updated");

        BookVO bookUpdated = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .body(book, objectMapper)
                    .when()
                    .put()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(BookVO.class, objectMapper);
        
        Assertions.assertNotNull(bookUpdated.getId());
        Assertions.assertNotNull(bookUpdated.getTitle());
        Assertions.assertNotNull(bookUpdated.getAuthor());
        Assertions.assertNotNull(bookUpdated.getPrice());
        Assertions.assertEquals(bookUpdated.getId(), book.getId());
        Assertions.assertEquals("Docker Deep Dive - Updated", bookUpdated.getTitle());
        Assertions.assertEquals("Nigel Poulton", bookUpdated.getAuthor());
        Assertions.assertEquals(55.99, bookUpdated.getPrice());
    }

    @Test
    @Order(4)
    public void testFindById() throws JsonMappingException, JsonProcessingException {
        var foundBook = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .pathParam("id", book.getId())
                    .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(BookVO.class, objectMapper);
        
        Assertions.assertNotNull(foundBook.getId());
        Assertions.assertNotNull(foundBook.getTitle());
        Assertions.assertNotNull(foundBook.getAuthor());
        Assertions.assertNotNull(foundBook.getPrice());
        Assertions.assertEquals(foundBook.getId(), book.getId());
        Assertions.assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
        Assertions.assertEquals("Nigel Poulton", foundBook.getAuthor());
        Assertions.assertEquals(55.99, foundBook.getPrice());
    }
    
    @Test
    @Order(5)
    public void testDelete() {
        given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
            .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .pathParam("id", book.getId())
                    .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }
    
    @Test
    @Order(6)
    public void testFindAll() throws JsonMappingException, JsonProcessingException {
        
        WrapperBookVO wrapper = given()
                    .config(
                        RestAssuredConfig
                            .config()
                            .encoderConfig(EncoderConfig.encoderConfig()
                                    .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
                    .spec(specification)
                .contentType(CONTENT_TYPE_YML)
                    .queryParams("page", 0 , "limit", 5, "direction", "asc")
                    .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                        .body()
                        .as(WrapperBookVO.class, objectMapper);
        
        var books = wrapper.getEmbedded().getBooks();

        BookVO foundBookOne = books.get(0);
        
        Assertions.assertNotNull(foundBookOne.getId());
        Assertions.assertNotNull(foundBookOne.getTitle());
        Assertions.assertNotNull(foundBookOne.getAuthor());
        Assertions.assertNotNull(foundBookOne.getPrice());
        Assertions.assertTrue(foundBookOne.getId() > 0);
        Assertions.assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", foundBookOne.getTitle());
        Assertions.assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundBookOne.getAuthor());
        Assertions.assertEquals(54.00, foundBookOne.getPrice());
        
        BookVO foundBookFive = books.get(4);
        
        Assertions.assertNotNull(foundBookFive.getId());
        Assertions.assertNotNull(foundBookFive.getTitle());
        Assertions.assertNotNull(foundBookFive.getAuthor());
        Assertions.assertNotNull(foundBookFive.getPrice());
        Assertions.assertTrue(foundBookFive.getId() > 0);
        Assertions.assertEquals("Domain Driven Design", foundBookFive.getTitle());
        Assertions.assertEquals("Eric Evans", foundBookFive.getAuthor());
        Assertions.assertEquals(92.00, foundBookFive.getPrice());
    }
     
    private void mockBook() {
        book.setTitle("Docker Deep Dive");
        book.setAuthor("Nigel Poulton");
        book.setPrice(Double.valueOf(55.99));
        book.setLaunchDate(new Date());
    }
}