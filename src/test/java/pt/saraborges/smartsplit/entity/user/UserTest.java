package pt.saraborges.smartsplit.entity.user;

import org.junit.jupiter.api.Test;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;
import pt.saraborges.smartsplit.validator.PasswordValidator;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User.checkPassword is the one piece of behaviour on the entity itself (everything else
 * is plain field access); it must delegate to Password.matches rather than comparing
 * anything in plain text.
 */
class UserTest {

    @Test
    void constructor_setsFieldsAndAuditInfoFromBaseEntity() {
        Date createdAt = new Date();
        User user = new User("Jane Doe", Email.fromExisting("jane@example.com"),
                Password.fromHash("some-hash"), "system", createdAt);

        assertThat(user.getName()).isEqualTo("Jane Doe");
        assertThat(user.getEmail().toString()).isEqualTo("jane@example.com");
        assertThat(user.getCreatedBy()).isEqualTo("system");
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void checkPassword_returnsTrueForTheMatchingPlainText() {
        User user = new User("Jane Doe", Email.fromExisting("jane@example.com"),
                Password.fromPlainText("Str0ng!Pass", new PasswordValidator()),
                "system", new Date());

        assertThat(user.checkPassword("Str0ng!Pass")).isTrue();
    }

    @Test
    void checkPassword_returnsFalseForAWrongAttempt() {
        User user = new User("Jane Doe", Email.fromExisting("jane@example.com"),
                Password.fromPlainText("Str0ng!Pass", new PasswordValidator()),
                "system", new Date());

        assertThat(user.checkPassword("wrong-password")).isFalse();
    }
}