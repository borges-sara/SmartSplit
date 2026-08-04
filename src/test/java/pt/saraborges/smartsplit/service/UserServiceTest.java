package pt.saraborges.smartsplit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.saraborges.smartsplit.dto.request.RegisterUserDto;
import pt.saraborges.smartsplit.dto.response.CreatedUserResponseDto;
import pt.saraborges.smartsplit.entity.user.User;
import pt.saraborges.smartsplit.entity.user.valueobject.Email;
import pt.saraborges.smartsplit.entity.user.valueobject.Password;
import pt.saraborges.smartsplit.exception.ConflictException;
import pt.saraborges.smartsplit.mapper.UserMapper;
import pt.saraborges.smartsplit.repository.UserRepository;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the core "Register User" workflow orchestration.
 * <p>
 * UserService itself does not perform validation (that is delegated to the DTO,
 * the mapper and the value objects) - its job is purely to:
 * 1. Reject duplicate emails.
 * 2. Delegate DTO -> entity mapping.
 * 3. Persist the new entity.
 * 4. Delegate entity -> response DTO mapping.
 * <p>
 * Because of that single responsibility, UserMapper and UserRepository are mocked here:
 * this test only proves UserService calls them correctly and in the right order,
 * not that mapping/validation itself is correct (that's covered by UserMapperTest).
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    private RegisterUserDto validDto;

    @BeforeEach
    void setUp() {
        userService = new UserService(userMapper, userRepository);
        validDto = new RegisterUserDto("Jane Doe", "jane@example.com", "Str0ng!Pass", "system");
    }

    @Test
    void registerUser_savesAndReturnsResponse_whenEmailIsNotAlreadyRegistered() {
        User mappedUser = new User("Jane Doe", Email.fromExisting("jane@example.com"),
                Password.fromHash("hashed-value"), "system", new Date());
        CreatedUserResponseDto expectedResponse = new CreatedUserResponseDto("jane@example.com", "Jane Doe");

        when(userRepository.existsUserByEmail(any(Email.class))).thenReturn(false);
        when(userMapper.registerUserDtoToUser(validDto)).thenReturn(mappedUser);
        when(userMapper.userToUserResponseDto(mappedUser)).thenReturn(expectedResponse);

        CreatedUserResponseDto result = userService.registerUser(validDto);

        assertThat(result).isEqualTo(expectedResponse);
        verify(userRepository).save(mappedUser);
    }

    @Test
    void registerUser_checksExistingEmail_beforeMappingOrSaving() {
        User mappedUser = new User("Jane Doe", Email.fromExisting("jane@example.com"),
                Password.fromHash("hashed-value"), "system", new Date());

        when(userRepository.existsUserByEmail(any(Email.class))).thenReturn(false);
        when(userMapper.registerUserDtoToUser(validDto)).thenReturn(mappedUser);
        when(userMapper.userToUserResponseDto(mappedUser))
                .thenReturn(new CreatedUserResponseDto("jane@example.com", "Jane Doe"));

        userService.registerUser(validDto);

        InOrder inOrder = inOrder(userRepository, userMapper);
        inOrder.verify(userRepository).existsUserByEmail(any(Email.class));
        inOrder.verify(userMapper).registerUserDtoToUser(validDto);
        inOrder.verify(userRepository).save(mappedUser);
        inOrder.verify(userMapper).userToUserResponseDto(mappedUser);
    }

    @Test
    void registerUser_throwsConflict_whenEmailAlreadyRegistered() {
        when(userRepository.existsUserByEmail(any(Email.class))).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(validDto))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining(validDto.email());

        verify(userMapper, never()).registerUserDtoToUser(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerUser_looksUpByTheSubmittedEmail_notAnArbitraryOne() {
        when(userRepository.existsUserByEmail(any(Email.class))).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(validDto))
                .isInstanceOf(ConflictException.class);

        ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
        verify(userRepository).existsUserByEmail(emailCaptor.capture());
        assertThat(emailCaptor.getValue().toString()).isEqualTo(validDto.email());
    }
}
