package com.user.user.application.service;

import com.user.user.application.DTOS.UserDTO;
import com.user.user.application.command.UserCreateCommand;
import com.user.user.application.command.UserUpdateCommand;
import com.user.user.application.mapper.UserCommandMapper;
import com.user.user.application.mapper.UserDTOMapper;
import com.user.user.domain.exceptions.ResourceNotFoundException;
import com.user.user.domain.exceptions.UniqueConstraintException;
import com.user.user.domain.model.User;
import com.user.user.domain.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserCommandMapper commandMapper;

    @Mock
    private UserDTOMapper dtoMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_success() {
        UserCreateCommand command = new UserCreateCommand("andres", "123456");
        User user = new User(UUID.randomUUID(), "andres", "123456");
        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getPassword());

        when(userRepository.findByUsername(command.getUsername())).thenReturn(Optional.empty());
        when(commandMapper.CreateCommandToModel(command)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(dtoMapper.ModelToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(command);

        assertEquals(userDTO, result);
    }

    @Test
    void createUser_shouldThrowUniqueConstraintException() {
        UserCreateCommand command = new UserCreateCommand("andres", "123456");
        when(userRepository.findByUsername(command.getUsername())).thenReturn(Optional.of(new User()));

        assertThrows(UniqueConstraintException.class, () -> userService.createUser(command));
    }

    @Test
    void getUserById_success() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "andres", "123456");
        UserDTO userDTO = new UserDTO(id, "andres", "123456");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(dtoMapper.ModelToDTOOptional(user)).thenReturn(Optional.of(userDTO));

        Optional<UserDTO> result = userService.getUserById(id);

        assertTrue(result.isPresent());
        assertEquals(userDTO, result.get());
    }

    @Test
    void getUserById_shouldThrowResourceNotFoundException() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(id));
    }

    @Test
    void getAllUsers_success() {
        List<User> users = List.of(new User(UUID.randomUUID(), "andres", "123456"));
        List<UserDTO> userDTOs = List.of(new UserDTO(users.get(0).getId(), "andres", "123456"));

        when(userRepository.findAll()).thenReturn(users);
        when(dtoMapper.ModelToDTO(any(User.class))).thenReturn(userDTOs.get(0));

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(userDTOs.size(), result.size());
        assertEquals(userDTOs.get(0), result.get(0));
    }

    @Test
    void updateUser_success() {
        UUID id = UUID.randomUUID();
        UserUpdateCommand command = new UserUpdateCommand(id, "andres", "654321");
        User user = new User(id, "andres", "654321");
        UserDTO userDTO = new UserDTO(id, "andres", "654321");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(command.getUsername())).thenReturn(Optional.of(user));
        when(commandMapper.UpdateCommandToModel(command)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encoded");
        when(userRepository.save(user)).thenReturn(user);
        when(dtoMapper.ModelToDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(command);

        assertEquals(userDTO, result);
    }

    @Test
    void updateUser_shouldThrowUniqueConstraintException() {
        UUID id = UUID.randomUUID();
        UserUpdateCommand command = new UserUpdateCommand(id, "andres", "654321");
        User existingUser = new User(UUID.randomUUID(), "andres", "123");

        when(userRepository.findById(id)).thenReturn(Optional.of(new User(id, "other", "123")));
        when(userRepository.findByUsername("andres")).thenReturn(Optional.of(existingUser));

        assertThrows(UniqueConstraintException.class, () -> userService.updateUser(command));
    }

    @Test
    void deleteUser_success() {
        UUID id = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(id);

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
    }
}