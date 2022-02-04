package br.com.erudio.integrationtests.controller.withjson;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.erudio.data.vo.v1.security.AccountCredentialsVO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.TokenVO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
class AuthControllerJsonTest extends AbstractIntegrationTest {

    public static final String HEADER_PARAM = "Authorization";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final int SERVER_PORT = 8888;
    private static TokenVO tokenVO;
    
    @Test
    @Order(1)
    void testSignin() {
        AccountCredentialsVO user = new AccountCredentialsVO();
        user.setUsername("leandro");
        user.setPassword("admin123");

        tokenVO = given()
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
                .as(TokenVO.class);
        
        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }
    
    @Test
    @Order(2)
    void testRefresh() {
        
        var newTokenVO = given()
        .basePath("/auth/refresh")
        .port(SERVER_PORT)
        .contentType(CONTENT_TYPE_JSON)
            .pathParam("username", tokenVO.getUsername())
            .header(HEADER_PARAM, "Bearer " + tokenVO.getRefreshToken())
        .when()
            .put("{username}")
        .then()
            .statusCode(200)
        .extract()
        .body()
            .as(TokenVO.class);
        
        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }

}
