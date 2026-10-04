package pt.saraborges.smartsplit.dto.request.user;

import pt.saraborges.smartsplit.exception.ValidationException;

public record RegisterUserDto(
        String name,
        String email,
        String password,
        String createdBy //TODO: in the future, this should be managed by the system
) {
    public RegisterUserDto {
        if (name == null || name.isBlank()) {
            throw new ValidationException("Name must not be blank.");
        }
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email must not be blank.");
        }
        if (password == null || password.isBlank()) {
            throw new ValidationException("Password must not be blank.");
        }
        if (createdBy == null || createdBy.isBlank()) {
            throw new ValidationException("Created By must not be blank.");
        }
    }
}
