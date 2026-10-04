package pt.saraborges.smartsplit.dto.request.group;

import java.util.Date;

import pt.saraborges.smartsplit.exception.ValidationException;

public record NewGroupDto(String name,
                          String currency,
                          String adminEmail,
                          String createdBy) {
    public NewGroupDto {
        if(name == null || name.isBlank()) {
            throw new ValidationException("Name must not be blank.");
        }

        if(currency == null || currency.isBlank()) {
            throw new ValidationException("Currency must not be blank.");
        }

        if(adminEmail == null || adminEmail.isBlank()) {
            throw new ValidationException("Admin email must not be blank.");
        }

        if(createdBy == null || createdBy.isBlank()) {
            throw new ValidationException("Created By must not be blank.");
        }
    }
}
