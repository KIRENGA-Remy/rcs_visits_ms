package gitoli.java.projects.com.rcs_visits_ms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "RCS Visits Management System API",
                version = "1.0",
                description = "API for managing prison visitors, prisoners, and related operations",
                contact = @Contact(
                        name = "GITOLI Remy Claudien",
                        email = "gitoliremy@gmail.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        )
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}


//
//package gitoli.java.projects.com.rcs_visits_ms.config;
//
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
//import io.swagger.v3.oas.annotations.info.Contact;
//import io.swagger.v3.oas.annotations.info.Info;
//import io.swagger.v3.oas.annotations.security.SecurityScheme;
//import io.swagger.v3.oas.annotations.servers.Server;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@OpenAPIDefinition(
//        info = @Info(
//                title = "RCS Visits API",
//                version = "1.0",
//                description = "API documentation for the RCS Visits Management System",
//                contact = @Contact(
//                        name = "Gitoli Java Projects",
//                        email = "support@example.com",
//                        url = "https://your-website.com"
//                )
//        ),
//        servers = {
//                @Server(url = "http://localhost:8080", description = "Local Server"),
//                @Server(url = "https://api.your-domain.com", description = "Production Server")
//        }
//)
//@SecurityScheme(
//        name = "bearerAuth",
//        type = SecuritySchemeType.HTTP,
//        bearerFormat = "JWT",
//        scheme = "bearer"
//)
//public class OpenApiConfig {
//    // No body required — annotations handle everything
//}
