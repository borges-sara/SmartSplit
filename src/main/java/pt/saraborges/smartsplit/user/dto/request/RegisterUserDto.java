package pt.saraborges.smartsplit.user.dto.request;

import pt.saraborges.smartsplit.common.exception.ValidationException;

public record RegisterUserDto(
        String name,
        String email,
        String password,
        String createdBy //TODO: in the future, this should be managed by the system
) {
    public RegisterUserDto{
        if(name == null || name.isEmpty()){
            fail("The 'Name' field is mandatory.");
        }

        if(email == null || email.isEmpty()){
            fail("The 'Email' field is mandatory.");
        }

        if(password == null || password.isEmpty()){
            fail("The 'Password' field is mandatory.");
        }

        if(createdBy == null || createdBy.isEmpty()){
            fail("The 'Created By' field is mandatory.");
        }
    }

    private void fail(String message){
        throw new ValidationException(message);
    }
}
