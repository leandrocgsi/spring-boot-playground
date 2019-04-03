package br.com.erudio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = { "br.com.erudio.web.controllers" })
public class WebMvcConfiguration implements WebMvcConfigurer {

  private static final MediaType MEDIA_TYPE_YML = MediaType.valueOf("application/x-yaml");

  @Autowired
  @Qualifier("yamlObjectMapper")
  private ObjectMapper yamlObjectMapper;

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
	    configurer.favorPathExtension(true).
	    favorParameter(false).
	    parameterName("mediaType").
	    ignoreAcceptHeader(false).
	    useRegisteredExtensionsOnly(false).
	    defaultContentType(MediaType.APPLICATION_JSON).
	    mediaType("xml", MediaType.APPLICATION_XML).
	    mediaType("json", MediaType.APPLICATION_JSON).
	    mediaType("x-yaml", MEDIA_TYPE_YML);
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
      converters.add(new YamlJackson2HttpMessageConverter());
  }
}

final class YamlJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {
    YamlJackson2HttpMessageConverter() {
        super(new YAMLMapper(), MediaType.parseMediaType("application/x-yaml"));
    }
}