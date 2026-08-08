package pt.saraborges.smartsplit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import pt.saraborges.smartsplit.dto.request.RegisterUserDto;
import pt.saraborges.smartsplit.dto.response.CreatedUserResponseDto;
import pt.saraborges.smartsplit.dto.response.GetUserResponseDto;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;
import pt.saraborges.smartsplit.validator.EmailValidator;
import pt.saraborges.smartsplit.validator.PasswordValidator;

import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {

    @Autowired
    protected EmailValidator emailValidator;

    @Autowired
    protected PasswordValidator passwordValidator;

    public User registerUserDtoToUser(RegisterUserDto dto) {
        Email email = Email.newEmail(dto.email(), emailValidator);
        Password password = Password.fromPlainText(dto.password(), passwordValidator);

        return new User(dto.name(), email, password, dto.createdBy(), new Date());
    }

    public CreatedUserResponseDto userToUserResponseDto(User user){
        return new CreatedUserResponseDto(user.getName(), user.getEmail().toString());
    }

    public GetUserResponseDto userToGetUserDto(User user){
        return new GetUserResponseDto(
                user.getName(),
                user.getEmail().toString(),
                user.getCreatedBy(),
                user.getCreatedAt());
    }
}