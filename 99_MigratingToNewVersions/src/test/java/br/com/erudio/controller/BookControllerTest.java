package br.com.erudio.controller;

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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.erudio.security.AccountCredentialsVO;
import br.com.erudio.vo.BookVO;
import br.com.erudio.vo.LoginResponseVO;
import br.com.erudio.vo.WrapperBookVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerTest {

	private static final String HEADER_PARAM = "Authorization";
	private static final int SERVER_PORT = 8888;
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static BookVO book;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
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
	                .basePath("/auth/signin")
	                .port(SERVER_PORT)
	                .contentType("application/json")
	                .body(user)
	                .when()
	                	.post()
	                .then()
	                	.statusCode(200)
	                .extract()
	                .body()
	                	.as(LoginResponseVO.class)
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

		var content = given().spec(specification)
				.contentType("application/json")
					.body(book)
					.when()
					.post()
                .then()
            		.statusCode(200)
			            .extract()
			            .body()
			            	.asString();
		
		book = objectMapper.readValue(content, BookVO.class);
		
		Assertions.assertNotNull(book.getKey());
		Assertions.assertNotNull(book.getTitle());
		Assertions.assertNotNull(book.getAuthor());
		Assertions.assertNotNull(book.getPrice());
		Assertions.assertTrue(book.getKey() > 0);
		Assertions.assertEquals("Docker Deep Dive", book.getTitle());
		Assertions.assertEquals("Nigel Poulton", book.getAuthor());
		Assertions.assertEquals(55.99, book.getPrice());
	}
	  
	@Test
	@Order(3)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		
		book.setTitle("Docker Deep Dive - Updated");

		var content = given().spec(specification)
				.contentType("application/json")
					.body(book)
					.when()
					.put()
                .then()
            		.statusCode(200)
			            .extract()
			            .body()
			            	.asString();
		
		BookVO bookUpdated = objectMapper.readValue(content, BookVO.class);
		
		Assertions.assertNotNull(bookUpdated.getKey());
		Assertions.assertNotNull(bookUpdated.getTitle());
		Assertions.assertNotNull(bookUpdated.getAuthor());
		Assertions.assertNotNull(bookUpdated.getPrice());
		Assertions.assertEquals(bookUpdated.getKey(), book.getKey());
		Assertions.assertEquals("Docker Deep Dive - Updated", bookUpdated.getTitle());
		Assertions.assertEquals("Nigel Poulton", bookUpdated.getAuthor());
		Assertions.assertEquals(55.99, bookUpdated.getPrice());
	}

	@Test
	@Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		var content = given().spec(specification)
				.contentType("application/json")
					.pathParam("id", book.getKey())
					.when()
					.get("{id}")
                .then()
            		.statusCode(200)
			            .extract()
			            .body()
			            	.asString();
		
		BookVO foundBook = objectMapper.readValue(content, BookVO.class);
		
		Assertions.assertNotNull(foundBook.getKey());
		Assertions.assertNotNull(foundBook.getTitle());
		Assertions.assertNotNull(foundBook.getAuthor());
		Assertions.assertNotNull(foundBook.getPrice());
		Assertions.assertEquals(foundBook.getKey(), book.getKey());
		Assertions.assertEquals("Docker Deep Dive - Updated", foundBook.getTitle());
		Assertions.assertEquals("Nigel Poulton", foundBook.getAuthor());
		Assertions.assertEquals(55.99, foundBook.getPrice());
	}
	
	@Test
	@Order(5)
	public void testDelete() {
		given().spec(specification)
				.contentType("application/json")
					.pathParam("id", book.getKey())
					.when()
					.delete("{id}")
                .then()
            		.statusCode(204);
	}
	
	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.contentType("application/json")
					.queryParams("page", 0 , "limit", 5, "direction", "asc")
					.when()
					.get()
                .then()
            		.statusCode(200)
			            .extract()
			            .body()
			            	.asString();
		
		WrapperBookVO wrapper = objectMapper.readValue(content, WrapperBookVO.class);
		var books = wrapper.getEmbedded().getBooks();

		BookVO foundBookOne = books.get(0);
		
		Assertions.assertNotNull(foundBookOne.getKey());
		Assertions.assertNotNull(foundBookOne.getTitle());
		Assertions.assertNotNull(foundBookOne.getAuthor());
		Assertions.assertNotNull(foundBookOne.getPrice());
		Assertions.assertTrue(foundBookOne.getKey() > 0);
		Assertions.assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", foundBookOne.getTitle());
		Assertions.assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundBookOne.getAuthor());
		Assertions.assertEquals(54.00, foundBookOne.getPrice());
		
		BookVO foundBookFive = books.get(4);
		
		Assertions.assertNotNull(foundBookFive.getKey());
		Assertions.assertNotNull(foundBookFive.getTitle());
		Assertions.assertNotNull(foundBookFive.getAuthor());
		Assertions.assertNotNull(foundBookFive.getPrice());
		Assertions.assertTrue(foundBookFive.getKey() > 0);
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