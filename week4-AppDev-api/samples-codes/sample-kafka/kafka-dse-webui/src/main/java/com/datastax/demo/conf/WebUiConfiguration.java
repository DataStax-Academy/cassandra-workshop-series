package com.datastax.demo.conf;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class WebUiConfiguration {

  @Autowired private SpringTemplateEngine templateEngine;

  @PostConstruct
  public void extension() {
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setTemplateMode("XHTML");
    templateResolver.setPrefix("views/view-");
    templateResolver.setSuffix(".html");
    templateResolver.setCacheTTLMs(3600000L);
    templateEngine.addTemplateResolver(templateResolver);
  }

  @Bean
  public ObjectMapper customizeJacksonConfiguration() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(Include.NON_NULL);
    return mapper;
  }
}
