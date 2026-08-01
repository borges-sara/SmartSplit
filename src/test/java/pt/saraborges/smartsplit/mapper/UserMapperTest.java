package pt.saraborges.smartsplit.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.saraborges.smartsplit.dto.request.RegisterUserDto;
import pt.saraborges.smartsplit.dto.response.CreatedUserResponseDto;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;
import pt.saraborges.smartsplit.exception.ValidationException;
import pt.saraborges.smartsplit.validator.EmailValidator;
import pt.saraborges.smartsplit.validator.PasswordValidator;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * UserMapper is written by hand (not MapStruct-generated) precisely because it needs to
 * build value objects (Email, Password) through their validating factory methods rather
 * than doing a plain field copy. These tests exercise that hand-written logic directly,
 * using the real validators (not mocks) since the validation rules ARE the behaviour
 * under test here.
 * <p>
 * We instantiate UserMapperImpl (the MapStruct-generated subclass) rather than a mock,
 * so running `./mvnw test` requires annotation processing to have already produced it -
 * consistent with the project's note that mapstruct-processor must stay wired in pom.xml.
 */
class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
        userMapper.emailValidator = new EmailValidator();
        userMapper.passwordValidator = new PasswordValidator();
    }

    @Test
    void registerUserDtoToUser_mapsPlainFieldsAndBuildsValueObjects() {
        RegisterUserDto dto = new RegisterUserDto("Jane Doe", "jane@example.com", "Str0ng!Pass", "system");

        User user = userMapper.registerUserDtoToUser(dto);

        assertThat(user.getName()).isEqualTo("Jane Doe");
        assertThat(user.getEmail().toString()).isEqualTo("jane@example.com");
        assertThat(user.checkPassword("Str0ng!Pass")).isTrue();
        assertThat(user.getCreatedBy()).isEqualTo("system");
        assertThat(user.getCreatedAt()).isNotNull();
    }

    @Test
    void registerUserDtoToUser_hashesThePassword_ratherThanStoringPlainText() {
        RegisterUserDto dto = new RegisterUserDto("Jane Doe", "jane@example.com", "Str0ng!Pass", "system");

        User user = userMapper.registerUserDtoToUser(dto);

        assertThat(user.checkPassword("some-other-password")).isFalse();
        assertThat(user.toString()).doesNotContain("Str0ng!Pass");
    }

    @Test
    void registerUserDtoToUser_propagatesEmailValidationFailure() {
        RegisterUserDto dto = new RegisterUserDto("Jane Doe", "not-an-email", "Str0ng!Pass", "system");

        assertThatThrownBy(() -> userMapper.registerUserDtoToUser(dto))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void registerUserDtoToUser_propagatesPasswordValidationFailure() {
        RegisterUserDto dto = new RegisterUserDto("Jane Doe", "jane@example.com", "weak", "system");

        assertThatThrownBy(() -> userMapper.registerUserDtoToUser(dto))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void userToUserResponseDto_exposesNameAndEmailOnly() {
        User user = new User("Jane Doe", Email.fromExisting("jane@example.com"),
                Password.fromHash("irrelevant-hash"), "system", new Date());

        CreatedUserResponseDto response = userMapper.userToUserResponseDto(user);

        assertThat(response.name()).isEqualTo("Jane Doe");
        assertThat(response.email()).isEqualTo("jane@example.com");
    }
}