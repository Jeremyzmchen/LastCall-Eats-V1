package com.lastcalleats.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the SpringDoc OpenAPI (Swagger UI) documentation for the LastCall Eats API.
 * Registers a Bearer JWT security scheme so that the Swagger UI's "Authorize" button
 * can be used to pass a token when testing protected endpoints interactively.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Builds the {@link OpenAPI} descriptor with project metadata and the
     * global JWT security requirement applied to all endpoints.
     *
     * @return the configured OpenAPI bean picked up by SpringDoc
     */
    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("LastCall Eats API")
                        .description("Food waste reduction platform API")
                        .version("v1.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
