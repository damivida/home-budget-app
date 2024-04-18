package com.example.homebudgetapp.core.configuration.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Value("${application.url}")
    private String applicationUrl;

    @Value("${application.documentation.name}")
    private String applicationName;

    @Value("${application.documentation.description}")
    private String applicationDescription;

    @Value("${application.documentation.version}")
    private String applicationVersion;

    @Bean
    public OpenAPI openApiInformation() {
        Server localServer = new Server().url(applicationUrl);

        Info info = new Info().description(applicationDescription).title(applicationName).version(
                applicationVersion);
        return new OpenAPI().info(info).addServersItem(localServer);
    }

    // all
    @Bean
    public GroupedOpenApi groupAll() {
        return GroupedOpenApi.builder().group("Home Budget API").pathsToMatch("/api/v1/home-budget-app/**").build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("User API").pathsToMatch("/api/v1/home-budget-app/user/**").build();
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return GroupedOpenApi.builder().group("Category API").pathsToMatch("/api/v1/home-budget-app/category/**").build();
    }

    @Bean
    public GroupedOpenApi expenseApi() {
        return GroupedOpenApi.builder().group("Expense API").pathsToMatch("/api/v1/home-budget-app/expense/**").build();
    }
}
