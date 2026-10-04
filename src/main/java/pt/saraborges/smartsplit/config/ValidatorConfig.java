package pt.saraborges.smartsplit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.saraborges.smartsplit.validator.EmailValidator;
import pt.saraborges.smartsplit.validator.PasswordValidator;

@Configuration
public class ValidatorConfig {

    @Bean
    public EmailValidator emailValidator(){
        return new EmailValidator();
    }

    @Bean
    public PasswordValidator passwordValidator(){
        return new PasswordValidator();
    }
}
