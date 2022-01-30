package br.com.erudio.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.LoginResponseVO;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerYamlTest extends AbstractIntegrationTest {

	public static final String HEADER_STRING = "Authorization";
	public static final String CONTENT_TYPE_YML = "application/x-yaml";
	public static final int SERVER_PORT = 8888;
	private static ObjectMapper objectMapper;

	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	}
	
	@Test
	void testSignin() throws JsonProcessingException {
		AccountCredentialsVO user = new AccountCredentialsVO();
		user.setUsername("leandro");
		user.setPassword("admin123");
		
		String userStr = objectMapper.writeValueAsString(user);
		
		System.out.println(userStr);

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
	                .body(user)
	                .when()
	                	.post()
	                .then()
	                	.statusCode(200)
	                .extract()
	                .body()
	                	.as(LoginResponseVO.class)
	                .getToken();
	    
	    System.out.println(token);
	}

}
