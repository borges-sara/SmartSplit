package pt.saraborges.smartsplit.entity.user.valueobject;

import org.junit.jupiter.api.Test;
import pt.saraborges.smartsplit.exception.ValidationException;
import pt.saraborges.smartsplit.validator.PasswordValidator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Password never stores plain text: fromPlainText(...) validates then BCrypt-hashes before
 * construction, and fromHash(...) is the only way back in when reloading an already-hashed
 * password from the DB. Uses the real PasswordValidator (not a mock), since fromPlainText's
 * whole job is to run that validation before hashing.
 */
class PasswordTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    void fromPlainText_hashesTheInput_ratherThanStoringItVerbatim() {
        Password password = Password.fromPlainText("Str0ng!Pass", validator);

        assertThat(password.getHash()).isNotEqualTo("Str0ng!Pass");
    }

    @Test
    void fromPlainText_propagatesValidationFailure_ratherThanConstructingAnInvalidPassword() {
        assertThatThrownBy(() -> Password.fromPlainText("weak", validator))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void matches_returnsTrueForTheOriginalPlainText() {
        Password password = Password.fromPlainText("Str0ng!Pass", validator);

        assertThat(password.matches("Str0ng!Pass")).isTrue();
    }

    @Test
    void matches_returnsFalseForAWrongAttempt() {
        Password password = Password.fromPlainText("Str0ng!Pass", validator);

        assertThat(password.matches("some-other-password")).isFalse();
    }

    @Test
    void fromHash_bypassesValidationAndHashing_forAlreadyHashedValues() {
        Password password = Password.fromHash("irrelevant-hash");

        assertThat(password.getHash()).isEqualTo("irrelevant-hash");
    }

    @Test
    void toString_neverExposesTheHash() {
        Password password = Password.fromPlainText("Str0ng!Pass", validator);

        assertThat(password.toString()).isEqualTo("Password[PROTECTED]");
        assertThat(password.toString()).doesNotContain(password.getHash());
    }
}