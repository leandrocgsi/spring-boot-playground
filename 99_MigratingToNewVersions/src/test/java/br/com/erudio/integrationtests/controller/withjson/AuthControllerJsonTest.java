package br.com.erudio.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.erudio.data.vo.v1.security.AccountCredentialsVO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.LoginResponseVO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerJsonTest extends AbstractIntegrationTest {

	public static final String HEADER_STRING = "Authorization";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final int SERVER_PORT = 8888;
	
	@Test
	void testSignin() {
		AccountCredentialsVO user = new AccountCredentialsVO();
		user.setUsername("leandro");
		user.setPassword("admin123");

	    given()
	        .basePath("/auth/signin")
	        .port(SERVER_PORT)
	        .contentType(CONTENT_TYPE_JSON)
	        .body(user)
	        .when()
	        	.post()
	        .then()
	        	.statusCode(200)
	        .extract()
	        .body()
	        	.as(LoginResponseVO.class)
	        .getToken();
	}

}
