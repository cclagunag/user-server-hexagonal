package com.user.user.application.mapper;

import com.user.user.application.DTOS.UserDTO;
import com.user.user.domain.model.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOMapperTest {

    private final UserDTOMapper mapper = new UserDTOMapper();

    @Test
    void testModelToDTO() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setUsername("andres");
        user.setPassword("123456");

        UserDTO dto = mapper.ModelToDTO(user);

        assertEquals(id, dto.getId());
        assertEquals("andres", dto.getUsername());
        assertEquals("123456", dto.getPassword());
    }

    @Test
    void testModelToDTOOptional_WithUser() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setUsername("andres");
        user.setPassword("123456");

        Optional<UserDTO> optionalDto = mapper.ModelToDTOOptional(user);

        assertTrue(optionalDto.isPresent());
        UserDTO dto = optionalDto.get();
        assertEquals(id, dto.getId());
        assertEquals("andres", dto.getUsername());
        assertEquals("123456", dto.getPassword());
    }

    @Test
    void testModelToDTOOptional_WithNull() {
        Optional<UserDTO> optionalDto = mapper.ModelToDTOOptional(null);

        assertFalse(optionalDto.isPresent());
    }

    @Test
    void testModelOptionalToDTOOptional_WithUserPresent() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setUsername("andres");
        user.setPassword("123456");

        UserDTO dto = mapper.ModelOptionalToDTOOptional(Optional.of(user));

        assertNotNull(dto);
        assertEquals(id, dto.getId());
        assertEquals("andres", dto.getUsername());
        assertEquals("123456", dto.getPassword());
    }

    @Test
    void testModelOptionalToDTOOptional_WithEmpty() {
        UserDTO dto = mapper.ModelOptionalToDTOOptional(Optional.empty());

        assertNull(dto);
    }
}