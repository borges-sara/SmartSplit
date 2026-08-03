package pt.saraborges.smartsplit.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "SmartSplit App",
                version = "1.0",
                description = "An alternative application to SplitWise."
        )
)

@Configuration
public class OpenApiConfig {}

