package com.user.user.infraestructure.adapter.out.db;

import com.user.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserMapper();
    }

    @Test
    void shouldMapDomainToEntity() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .username("andres")
                .password("123456")
                .build();

        UserEntity entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(user.getId(), entity.getId());
        assertEquals(user.getUsername(), entity.getUsername());
        assertEquals(user.getPassword(), entity.getPassword());
    }

    @Test
    void shouldMapEntityToDomain() {
        UserEntity entity = UserEntity.builder()
                .id(UUID.randomUUID())
                .username("camilo")
                .password("pass123")
                .build();

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertEquals(entity.getId(), user.getId());
        assertEquals(entity.getUsername(), user.getUsername());
        assertEquals(entity.getPassword(), user.getPassword());
    }

    @Test
    void shouldReturnNullWhenDomainIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }
}