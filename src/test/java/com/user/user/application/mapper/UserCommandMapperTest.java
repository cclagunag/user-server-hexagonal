package com.user.user.application.mapper;

import com.user.user.application.command.UserCreateCommand;
import com.user.user.application.command.UserUpdateCommand;
import com.user.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserCommandMapperTest {


    private UserCommandMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserCommandMapper();
    }

    @Test
    void shouldMapCreateCommandToUserModel() {
        UserCreateCommand command = new UserCreateCommand("andres", "123456");

        User user = mapper.CreateCommandToModel(command);

        assertNotNull(user);
        assertEquals("andres", user.getUsername());
        assertEquals("123456", user.getPassword());
    }

    @Test
    void shouldMapUpdateCommandToUserModel() {
        UUID id = UUID.randomUUID();
        UserUpdateCommand command = new UserUpdateCommand(id, "andres", "654321");

        User user = mapper.UpdateCommandToModel(command);

        assertNotNull(user);
        assertEquals(id, user.getId());
        assertEquals("andres", user.getUsername());
        assertEquals("654321", user.getPassword());
    }
}