package pt.saraborges.smartsplit.dto.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import pt.saraborges.smartsplit.dto.request.user.RegisterUserDto;
import pt.saraborges.smartsplit.exception.ValidationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * RegisterUserDto is a Java record with a "compact constructor" - the block that runs
 * before the implicit field assignment. It acts as the very first line of defence in
 * the register-user workflow: a request can't even become a DTO instance (let alone
 * reach UserService) if a mandatory field is missing.
 */
class RegisterUserDtoTest {

    @Test
    void constructsSuccessfully_whenAllFieldsArePresent() {
        RegisterUserDto dto = new RegisterUserDto("Jane Doe", "jane@example.com", "Str0ng!Pass", "system");

        assertThat(dto.name()).isEqualTo("Jane Doe");
        assertThat(dto.email()).isEqualTo("jane@example.com");
        assertThat(dto.password()).isEqualTo("Str0ng!Pass");
        assertThat(dto.createdBy()).isEqualTo("system");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsMissingName(String invalidName) {
        assertThatThrownBy(() ->
                new RegisterUserDto(invalidName, "jane@example.com", "Str0ng!Pass", "system"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Name");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsMissingEmail(String invalidEmail) {
        assertThatThrownBy(() ->
                new RegisterUserDto("Jane Doe", invalidEmail, "Str0ng!Pass", "system"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Email");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsMissingPassword(String invalidPassword) {
        assertThatThrownBy(() ->
                new RegisterUserDto("Jane Doe", "jane@example.com", invalidPassword, "system"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Password");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void rejectsMissingCreatedBy(String invalidCreatedBy) {
        assertThatThrownBy(() ->
                new RegisterUserDto("Jane Doe", "jane@example.com", "Str0ng!Pass", invalidCreatedBy))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Created By");
    }

    @Test
    void checksFieldsInDeclarationOrder_nameFailsBeforeLaterFields() {
        // Even though email is also blank, the name check runs first, so that's the
        // message the caller should see. This test pins down that ordering so a future
        // reordering of the checks doesn't silently change which error is reported first.
        assertThatThrownBy(() ->
                new RegisterUserDto(null, null, "Str0ng!Pass", "system"))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Name");
    }
}