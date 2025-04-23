package com.user.user.infraestructure.adapter.out.db;

import com.user.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryAdapterTest {

    private IJpaUserRepository jpaUserRepository;
    private UserMapper mapper;
    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        jpaUserRepository = mock(IJpaUserRepository.class);
        mapper = mock(UserMapper.class);
        adapter = new UserRepositoryAdapter(jpaUserRepository, mapper);
    }

    @Test
    void shouldSaveUser() {
        User user = new User(UUID.randomUUID(), "andres", "123");
        UserEntity entity = UserEntity.builder().id(user.getId()).username(user.getUsername()).password(user.getPassword()).build();

        when(mapper.toEntity(user)).thenReturn(entity);
        when(jpaUserRepository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(user);

        User result = adapter.save(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        verify(jpaUserRepository).save(entity);
    }

    @Test
    void shouldFindById() {
        UUID id = UUID.randomUUID();
        UserEntity entity = UserEntity.builder().id(id).username("juan").password("pass").build();
        User user = new User(id, "juan", "pass");

        when(jpaUserRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(user);

        Optional<User> result = adapter.findById(id);

        assertTrue(result.isPresent());
        assertEquals("juan", result.get().getUsername());
    }

    @Test
    void shouldFindByUsername() {
        String username = "carlos";
        UserEntity entity = UserEntity.builder().id(UUID.randomUUID()).username(username).password("pass").build();
        User user = new User(entity.getId(), username, "pass");

        when(jpaUserRepository.findByUsername(username)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(user);

        Optional<User> result = adapter.findByUsername(username);

        assertTrue(result.isPresent());
        assertEquals(username, result.get().getUsername());
    }

    @Test
    void shouldReturnAllUsers() {
        UserEntity entity = UserEntity.builder().id(UUID.randomUUID()).username("user").password("pwd").build();
        User user = new User(entity.getId(), "user", "pwd");

        when(jpaUserRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(user);

        List<User> users = adapter.findAll();

        assertEquals(1, users.size());
        assertEquals("user", users.get(0).getUsername());
    }

    @Test
    void shouldDeleteById() {
        UUID id = UUID.randomUUID();
        doNothing().when(jpaUserRepository).deleteById(id);

        adapter.deleteById(id);

        verify(jpaUserRepository).deleteById(id);
    }
}