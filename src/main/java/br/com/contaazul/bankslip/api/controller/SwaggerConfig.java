package br.com.contaazul.bankslip.api.controller;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket lotsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.contaazul.bankslip.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(metaData());
    }
    
    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                "Bankslip API",
                "Spring Boot REST API for Bankslip",
                "1.2 - Stage",
                "API to add, list, cancel and pay bank slips.",
                new Contact("Cleber Fonseca", "", "clebercf@gmail.com"),
               "Private Use License",
                "",
                Collections.emptyList());
        return apiInfo;
    }
}