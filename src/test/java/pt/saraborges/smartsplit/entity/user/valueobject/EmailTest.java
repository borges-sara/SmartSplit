package pt.saraborges.smartsplit.entity.user.valueobject;

import org.junit.jupiter.api.Test;
import pt.saraborges.smartsplit.exception.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Email keeps invalid instances unrepresentable by routing newEmail(...) through
 * EmailValidator before construction, while fromExisting(...) skips validation entirely
 * for data already trusted (loaded back from the DB).
 */
class EmailTest {

    @Test
    void newEmail_buildsEmailWhenValid() {
        Email email = Email.newEmail("jane@example.com");

        assertThat(email.toString()).isEqualTo("jane@example.com");
        assertThat(email.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void newEmail_propagatesValidationFailure_ratherThanConstructingAnInvalidEmail() {
        assertThatThrownBy(() -> Email.newEmail("not-an-email"))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    void fromExisting_bypassesValidation() {
        Email email = Email.fromExisting("not-an-email");

        assertThat(email.toString()).isEqualTo("not-an-email");
    }

    @Test
    void toString_returnsTheRawEmailAddress() {
        Email email = Email.fromExisting("jane@example.com");

        assertThat(email.toString()).isEqualTo("jane@example.com");
    }
}