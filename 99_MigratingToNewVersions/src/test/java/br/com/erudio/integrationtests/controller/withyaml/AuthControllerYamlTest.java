package br.com.erudio.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.LoginResponseVO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AuthControllerYamlTest extends AbstractIntegrationTest {

	public static final String HEADER_STRING = "Authorization";
	public static final int SERVER_PORT = 8888;
	
	@Test
	void testSignin() {
		AccountCredentialsVO user = new AccountCredentialsVO();
		user.setUsername("leandro");
		user.setPassword("admin123");

	    var token =
	            given()
	                .basePath("/auth/signin")
	                .port(SERVER_PORT)
	                .contentType("application/x-yaml")
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
