package br.com.erudio.integrationtests.controller.withyaml;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;

import br.com.erudio.integrationtests.controller.withyaml.mapper.YMLMapper;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.integrationtests.vo.AccountCredentialsVO;
import br.com.erudio.integrationtests.vo.TokenVO;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
class AuthControllerYamlTest extends AbstractIntegrationTest {

    public static final String HEADER_PARAM = "Authorization";
    public static final String CONTENT_TYPE_YML = "application/x-yaml";
    public static final int SERVER_PORT = 8888;
    private static YMLMapper objectMapper;
    private static TokenVO tokenVO;

    @BeforeAll
    public static void setup() {
        objectMapper = new YMLMapper();
    }
    
    @Test
    @Order(1)
    void testSignin() throws JsonProcessingException {
        AccountCredentialsVO user = new AccountCredentialsVO();
        user.setUsername("leandro");
        user.setPassword("admin123");

        tokenVO = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(CONTENT_TYPE_YML, ContentType.TEXT)))
            .basePath("/auth/signin")
            .port(SERVER_PORT)
            .contentType(CONTENT_TYPE_YML)
            .accept(CONTENT_TYPE_YML)
            .body(user, objectMapper)
            .when()
                .post()
            .then()
            .statusCode(200)
                .extract()
                .body()
                    .as(TokenVO.class, objectMapper);
        
        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }
    
    @Test
    @Order(2)
    void testRefresh() {
        
        var newTokenVO = given()
        .basePath("/auth/refresh")
        .port(SERVER_PORT)
        .contentType(CONTENT_TYPE_YML)
            .pathParam("username", tokenVO.getUsername())
            .header(HEADER_PARAM, "Bearer " + tokenVO.getRefreshToken())
        .when()
            .put("{username}")
        .then()
            .statusCode(200)
        .extract()
        .body()
            .as(TokenVO.class, objectMapper);
        
        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }
}