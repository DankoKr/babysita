package s3.fontys.babysita.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import s3.fontys.babysita.business.UserService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.domain.UserRequest;
import s3.fontys.babysita.domain.UserResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @WithMockUser(roles = "admin")
    public void getAllUsersTest() throws Exception {
        Map<Integer, UserResponse> users = new HashMap<>();
        UserResponse user1 = new UserResponse();
        user1.setId(1);
        users.put(1, user1);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).getAllUsers();
    }

    @Test
    @WithMockUser(roles = "admin")
    public void getUserByIdTest_ValidId() throws Exception {
        UserResponse user = new UserResponse();
        user.setId(1);

        when(userService.getUser(1)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).getUser(1);
    }

    @Test
    @WithMockUser(roles = "admin")
    public void getUserByIdTest_InvalidId() throws Exception {
        int invalidUserId = -1;
        when(userService.getUser(invalidUserId)).thenThrow(new InvalidIdException("Invalid ID."));

        mockMvc.perform(get("/users/{id}", invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).getUser(invalidUserId);
    }

    @Test
    @WithMockUser(roles = "admin")
    public void searchUsersTest() throws Exception {
        List<UserResponse> users = new ArrayList<>();
        UserResponse user = new UserResponse();
        user.setUsername("testUser");
        users.add(user);

        String username = "testUser";

        when(userService.searchByUsernamePattern(username)).thenReturn(users);

        mockMvc.perform(get("/users/search")
                        .param("username", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).searchByUsernamePattern(username);
    }

    @Test
    @WithMockUser(roles = "admin")
    public void createUserTest_Success() throws Exception {
        UserRequest userRequest = new UserRequest(1, "username", "password123",
                "eamil@mc.con", "first", "last", "image",
                "phone", "address",
                "parent", 44);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNoContent());

    }


    @Test
    @WithMockUser(roles = "admin")
    public void deleteUserTest_Success() throws Exception {
        int userId = 1;

        mockMvc.perform(delete("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    public void deleteUserTest_InvalidId() throws Exception {
        int invalidUserId = -1;
        doThrow(new InvalidIdException("Invalid ID.")).when(userService).deleteUser(invalidUserId);

        mockMvc.perform(delete("/users/{userId}", invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "admin")
    public void patchUserTest_Success() throws Exception {
        int userId = 1;
        UserRequest userRequest = new UserRequest();
        userRequest.setId(1);

        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "admin")
    public void patchUserTest_InvalidId() throws Exception {
        int invalidUserId = -1;
        UserRequest userRequest = new UserRequest();
        doThrow(new InvalidIdException("Invalid ID.")).when(userService).partialUpdateUser(eq(invalidUserId), any());

        mockMvc.perform(patch("/users/{userId}", invalidUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isNotFound());
    }



}
