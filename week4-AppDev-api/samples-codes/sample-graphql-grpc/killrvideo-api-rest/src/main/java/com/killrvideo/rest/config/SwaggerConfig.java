package com.killrvideo.rest.config;

/*
 * #%L
 * ff4j-spring-boot-web-api
 * %%
 * Copyright (C) 2013 - 2016 FF4J
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.killrvideo.rest.KillrVideoRestApi;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Documentation of the API
 *
 * @author DataStax Evangelist Team
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("KillrVideo")
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.killrvideo.rest.resource"))
            .paths(PathSelectors.regex("/api/v1.*"))
            .build()
            .apiInfo(apiInfo())
            .useDefaultResponseMessages(false);
    }
    
    /**
     * Initialization of documentation
     *
     * @return static infos
     */
    private ApiInfo apiInfo() {
        ApiInfoBuilder builder = new ApiInfoBuilder();
        builder.title("KillrVideo REST Api");
        builder.description("CRUD operations against DSE as Backend for killrvideo.com");
        builder.version(KillrVideoRestApi.class.getPackage().getImplementationVersion());
        builder.license("DataStax Academy Tutorials");
        builder.licenseUrl("https://academy.datastax.com/");
        return builder.build();
    }
}
