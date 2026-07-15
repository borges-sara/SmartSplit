package pt.saraborges.smartsplit.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.saraborges.smartsplit.user.controller.UserController;
import pt.saraborges.smartsplit.user.mapper.UserMapper;
import pt.saraborges.smartsplit.user.service.UserService;
import pt.saraborges.smartsplit.user.validator.EmailValidator;
import pt.saraborges.smartsplit.user.validator.PasswordValidator;

@Configuration
public class UserConfiguration {

    @Bean
    public UserController userController(){
        return new UserController(userService());
    }

    @Bean
    public UserService userService(){
        return new UserService();
    }

    @Bean
    public EmailValidator emailValidator(){
        return new EmailValidator();
    }

    @Bean
    public PasswordValidator passwordValidator(){
        return new PasswordValidator();
    }
}
