package pl.fakturownia.bridge.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiDocumentationConfig {

    @Bean
    OpenAPI bridgeOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fakturownia Bridge API")
                        .description("API pośredniczące do tworzenia faktur i odczytu statusów Fakturowni/KSeF. " +
                                "Aplikacja używa wygenerowanego klienta OpenAPI do komunikacji z Fakturownią.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Fakturownia Bridge")
                                .url("https://app.fakturownia.pl/api"))
                        .license(new License().name("Internal integration use")))
                .addServersItem(new Server().url("http://localhost:8080").description("Local"))
                .externalDocs(new ExternalDocumentation()
                        .description("Dokumentacja API Fakturownia")
                        .url("https://app.fakturownia.pl/api"));
    }
}
