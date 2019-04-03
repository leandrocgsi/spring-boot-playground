/*package br.com.erudio;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Configuration
public class ObjectMapperConfiguration {

  //This is our default JSON ObjectMapper. Add @Primary to inject is as default bean.
  @Bean
  @Primary
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    //Enable or disable features
    return objectMapper;
  }

  @Bean
  public ObjectMapper yamlObjectMapper() {
    ObjectMapper yamlObjectMapper = new ObjectMapper(new YAMLFactory());
    //Enable or disable features
    return yamlObjectMapper;
  }
}
*/