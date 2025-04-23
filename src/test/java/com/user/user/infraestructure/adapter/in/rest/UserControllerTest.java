package com.user.user.infraestructure.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.user.application.DTOS.UserDTO;
import com.user.user.application.command.UserCreateCommand;
import com.user.user.application.command.UserUpdateCommand;
import com.user.user.application.port.in.IUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateUser() throws Exception {
        UserCreateCommand command = new UserCreateCommand("andres", "123456");
        UserDTO response = new UserDTO(UUID.randomUUID(), "andres", "123456");

        Mockito.when(userService.createUser(any())).thenReturn(response);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("andres"));
    }

    @Test
    void shouldReturnUserById() throws Exception {
        UUID id = UUID.randomUUID(); // Este será el ID esperado
        UserDTO response = new UserDTO(id, "andres", "123456"); // Usamos el mismo ID

        Mockito.when(userService.getUserById(eq(id))).thenReturn(Optional.of(response));

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.username").value("andres"));
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        Mockito.when(userService.getUserById(eq(id))).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnAllUsers() throws Exception {
        UserDTO user1 = new UserDTO(UUID.randomUUID(), "andres", "123456");
        UserDTO user2 = new UserDTO(UUID.randomUUID(), "andresDos", "123456");

        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        UserUpdateCommand command = new UserUpdateCommand(UUID.randomUUID(), "andres", "654321");
        UserDTO response = new UserDTO(command.getId(), command.getUsername(), command.getPassword());

        Mockito.when(userService.updateUser(any())).thenReturn(response);

        mockMvc.perform(put("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("andres"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/usuarios/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(userService).deleteUser(eq(id));
    }
}