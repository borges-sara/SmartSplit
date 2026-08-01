package pt.saraborges.smartsplit.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.saraborges.smartsplit.dto.request.RegisterUserDto;
import pt.saraborges.smartsplit.exception.ConflictException;
import pt.saraborges.smartsplit.mapper.UserMapper;
import pt.saraborges.smartsplit.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {
    protected UserMapper userMapper;
    protected UserRepository userRepository;

    public void registerUser(RegisterUserDto dto)
    {
        if(userRepository.existsUserByEmail(dto.email())){
            throw new ConflictException("There is already an user registered with the email '" +
                    dto.email() + "'.");
        }

        var newUser = userMapper.registerUserDtoToUser(dto);
        userRepository.save(newUser);
    }
}
