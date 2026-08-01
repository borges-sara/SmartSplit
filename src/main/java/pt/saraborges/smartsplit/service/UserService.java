package pt.saraborges.smartsplit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.dto.request.RegisterUserDto;
import pt.saraborges.smartsplit.dto.response.CreatedUserResponseDto;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.exception.ConflictException;
import pt.saraborges.smartsplit.mapper.UserMapper;
import pt.saraborges.smartsplit.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    protected UserMapper userMapper;
    protected UserRepository userRepository;

    public CreatedUserResponseDto registerUser(RegisterUserDto dto)
    {
        if(userRepository.existsUserByEmail(Email.fromExisting(dto.email()))){
            throw new ConflictException("There is already an user registered with the email '" +
                    dto.email() + "'.");
        }

        var newUser = userMapper.registerUserDtoToUser(dto);

        userRepository.save(newUser);

        return userMapper.userToUserResponseDto(newUser);
    }
}
