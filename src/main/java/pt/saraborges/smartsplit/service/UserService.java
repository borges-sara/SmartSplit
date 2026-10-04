package pt.saraborges.smartsplit.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.dto.request.user.RegisterUserDto;
import pt.saraborges.smartsplit.dto.response.user.CreatedUserResponseDto;
import pt.saraborges.smartsplit.dto.response.user.GetUserResponseDto;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.exception.ConflictException;
import pt.saraborges.smartsplit.mapper.UserMapper;
import pt.saraborges.smartsplit.repository.UserRepository;
import pt.saraborges.smartsplit.validator.EmailValidator;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    protected UserMapper userMapper;
    protected UserRepository userRepository;
    protected EmailValidator emailValidator;

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

    public List<GetUserResponseDto> getAllUsers(){
        var registeredUsers = userRepository.findAll();

        return registeredUsers
                .stream()
                .map(u -> userMapper.userToGetUserDto(u))
                .toList();
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findAllByEmail(Email.newEmail(email, emailValidator));
    }
}
