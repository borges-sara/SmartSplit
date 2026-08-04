package pt.saraborges.smartsplit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record RegisterUserDto(
        @NonNull
        @NotBlank
        String name,
        @NonNull
        @NotBlank
        String email,
        @NonNull
        @NotBlank
        String password,
        String createdBy //TODO: in the future, this should be managed by the system
) {
}
