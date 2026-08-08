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
 * PasswordValidator is the second gatekeeper in the register-user workflow
 * (Password.fromPlainText -> validator.validate), enforcing password strength
 * before a Password value object - and therefore a User - can be created.
 * <p>
 * Note the validator checks length/upper/lower/digit/special IN THAT ORDER and
 * stops at the first failure (it's an if/else-if chain), so each "rejects..." test
 * below uses a password that is valid in every other respect, to isolate the rule
 * being tested.
 */
class PasswordValidatorTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Str0ng!Pass", "Ab1$defg", "C0mplex#Password123"})
    void acceptsPasswordsMeetingAllRules(String password) {
        assertThatCode(() -> validator.validate(password)).doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsNullOrEmptyPassword(String password) {
        assertThatThrownBy(() -> validator.validate(password))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("at least 8 characters");
    }

    @Test
    void rejectsPasswordsShorterThanEightCharacters() {
        assertThatThrownBy(() -> validator.validate("A1b!cd"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("at least 8 characters");
    }

    @Test
    void rejectsPasswordsWithoutUpperCase() {
        assertThatThrownBy(() -> validator.validate("str0ng!pass"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("upper case");
    }

    @Test
    void rejectsPasswordsWithoutLowerCase() {
        assertThatThrownBy(() -> validator.validate("STR0NG!PASS"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("lower case");
    }

    @Test
    void rejectsPasswordsWithoutDigits() {
        assertThatThrownBy(() -> validator.validate("Strong!Pass"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("digits");
    }

    @Test
    void rejectsPasswordsWithoutSpecialCharacters() {
        assertThatThrownBy(() -> validator.validate("Str0ngPass"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("special characters");
    }

    @Test
    void prefixesFailureMessagesWithTheValidatorName() {
        assertThatThrownBy(() -> validator.validate(null))
                .hasMessageContaining("Password Validator");
    }
}
