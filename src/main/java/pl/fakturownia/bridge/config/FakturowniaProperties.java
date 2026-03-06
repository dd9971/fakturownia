package pl.fakturownia.bridge.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "fakturownia")
public record FakturowniaProperties(
        @NotBlank String baseUrl,
        @NotBlank String apiToken
) {
}
