package pt.saraborges.smartsplit.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import pt.saraborges.smartsplit.dto.request.user.RegisterUserDto;
import pt.saraborges.smartsplit.dto.response.user.CreatedUserResponseDto;
import pt.saraborges.smartsplit.dto.response.user.GetUserResponseDto;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;
import java.util.Date;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    default User registerUserDtoToUser(RegisterUserDto dto) {
        Email email = Email.newEmail(dto.email());
        Password password = Password.fromPlainText(dto.password());

        return new User(dto.name(), email, password, dto.createdBy(), new Date());
    }

    @Mapping(target = "email", source = "email.email")
    CreatedUserResponseDto userToUserResponseDto(User user);

    @Mapping(target = "email", source = "email.email")
    GetUserResponseDto userToGetUserDto(User user);
}