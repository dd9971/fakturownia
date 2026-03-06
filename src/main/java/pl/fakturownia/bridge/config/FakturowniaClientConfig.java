package pl.fakturownia.bridge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.fakturownia.client.api.InvoicesApi;
import pl.fakturownia.client.invoker.ApiClient;

@Configuration
public class FakturowniaClientConfig {

    @Bean
    ApiClient fakturowniaApiClient(FakturowniaProperties properties) {
        ApiClient client = new ApiClient();
        client.setBasePath(properties.baseUrl());
        return client;
    }

    @Bean
    InvoicesApi invoicesApi(ApiClient apiClient) {
        return new InvoicesApi(apiClient);
    }
}
