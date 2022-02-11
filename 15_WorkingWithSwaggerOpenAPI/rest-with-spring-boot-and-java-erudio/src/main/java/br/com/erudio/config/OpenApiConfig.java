package br.com.erudio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

// https://calendar.spring.io/
// https://github.com/springdoc/springdoc-openapi/issues/1284
// https://github.com/springdoc/springdoc-openapi-demos/tree/2.x
// https://github.com/springdoc/springdoc-openapi-demos/wiki/springdoc-openapi-2.x-migration-guide

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(@Value("${springdoc.version}") String appVersion) {
        //Paths foo;
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                    .title("RESTful API With Java 17 and Spring Boot 3.0.0-M1")
                    .version(appVersion)
                    .description("Some description about your API.")
                    .termsOfService("https://pub.erudio.com.br/meus-cursos")
                    .license(new License().name("Apache 2.0")
                    .url("https://pub.erudio.com.br/meus-cursos")))
                //.paths("/api/person/v1.*");
                //.paths(new Paths())
                ;
    }
}