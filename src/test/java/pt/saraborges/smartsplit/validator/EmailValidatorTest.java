package pt.saraborges.smartsplit.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import pt.saraborges.smartsplit.exception.ValidationException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * EmailValidator is one of the two gatekeepers (with PasswordValidator) that the
 * register-user workflow relies on before a User entity can ever be constructed
 * (see UserMapper.registerUserDtoToUser -> Email.newEmail).
 */
class EmailValidatorTest {

    private EmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"jane@example.com", "j@d.co", "first.last+tag@sub.example.com"})
    void acceptsWellFormedEmails(String email) {
        assertThatCode(() -> validator.validate(email)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsNullOrEmptyEmail(String email) {
        assertThatThrownBy(() -> validator.validate(email))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("cannot be null or empty");
    }

    @ParameterizedTest
    @ValueSource(strings = {"not-an-email", "missing-at-sign.com"})
    void rejectsEmailsWithoutAnAtSign(String email) {
        assertThatThrownBy(() -> validator.validate(email))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("username@domain.com");
    }

    @Test
    void prefixesFailureMessagesWithTheValidatorName() {
        assertThatThrownBy(() -> validator.validate(null))
                .hasMessageContaining("Email Validator");
    }
}
