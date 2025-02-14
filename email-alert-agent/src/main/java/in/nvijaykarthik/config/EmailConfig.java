package in.nvijaykarthik.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class EmailConfig {

    String username;
    String password;
    String host;
    List<String> allowedFromAddress;
}
