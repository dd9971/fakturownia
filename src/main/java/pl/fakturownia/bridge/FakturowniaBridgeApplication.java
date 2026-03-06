package pl.fakturownia.bridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class FakturowniaBridgeApplication {

    public static void main(String[] args) {
        SpringApplication.run(FakturowniaBridgeApplication.class, args);
    }
}
