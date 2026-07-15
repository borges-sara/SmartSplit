package pt.saraborges.smartsplit.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;
import pt.saraborges.smartsplit.user.dto.request.RegisterUserDto;
import pt.saraborges.smartsplit.user.entity.User;
import pt.saraborges.smartsplit.user.entity.valueObjects.Email;
import pt.saraborges.smartsplit.user.entity.valueObjects.Password;
import pt.saraborges.smartsplit.user.validator.EmailValidator;
import pt.saraborges.smartsplit.user.validator.PasswordValidator;

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

        return new User(0, dto.name(), email, password, dto.createdBy(), new Date());
    }
}