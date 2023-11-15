package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import s3.fontys.babysita.business.exception.DuplicatedUsernameException;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.configuration.security.token.AccessToken;
import s3.fontys.babysita.domain.UserResponse;
import s3.fontys.babysita.dto.AdminDTO;
import s3.fontys.babysita.dto.BabysitterDTO;
import s3.fontys.babysita.dto.ParentDTO;
import s3.fontys.babysita.domain.UserRequest;
import s3.fontys.babysita.persistence.UserRepository;
import s3.fontys.babysita.persistence.entity.AdminEntity;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.UserEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUser_NewUser_Success() {
        UserRequest userDTO = new UserRequest();
        userDTO.setUsername("newUser");
        userDTO.setRole("parent");
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        ParentDTO parentDTO = new ParentDTO();
        parentDTO.setUsername(userDTO.getUsername());
        parentDTO.setPassword(encodedPassword);
        parentDTO.setRole(userDTO.getRole());

        ParentEntity expectedParentEntity = new ParentEntity();
        expectedParentEntity.setUsername(userDTO.getUsername());
        expectedParentEntity.setPassword(encodedPassword);
        expectedParentEntity.setRole(userDTO.getRole());

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userMapper.toParentDTO(userDTO)).thenReturn(parentDTO);
        when(userMapper.toEntity(parentDTO)).thenReturn(expectedParentEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedParentEntity);

        assertDoesNotThrow(() -> userService.createUser(userDTO, rawPassword));

        verify(userMapper).toParentDTO(userDTO);
        verify(userMapper).toEntity(parentDTO);
        verify(userRepository).save(expectedParentEntity);
    }


    @Test
    void createUser_ExistingUsername_ThrowsDuplicatedUsernameException() {
        UserRequest userDTO = new UserRequest();
        userDTO.setUsername("existingUser");

        when(userRepository.existsByUsername(userDTO.getUsername())).thenReturn(true);

        assertThrows(DuplicatedUsernameException.class, () -> userService.createUser(userDTO, "password123"));

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void deleteUser_ValidId_Success() {
        int userId = 1;

        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> userService.deleteUser(userId));

        verify(userRepository).deleteById(userId);
    }

    @Test
    void getAllUsers_ReturnsMapOfAllUserDTOs() {
        UserEntity user1 = mock(UserEntity.class);
        UserEntity user2 = mock(UserEntity.class);
        when(user1.getId()).thenReturn(1);
        when(user2.getId()).thenReturn(2);

        UserResponse userResponse1 = new UserResponse();
        userResponse1.setId(user1.getId());

        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(user2.getId());

        List<UserEntity> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        when(userMapper.toResponse(user1)).thenReturn(userResponse1);
        when(userMapper.toResponse(user2)).thenReturn(userResponse2);

        Map<Integer, UserResponse> result = userService.getAllUsers();

        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Result should contain 2 users");

        assertTrue(result.containsKey(user1.getId()), "Result should contain user1 id");
        assertTrue(result.containsKey(user2.getId()), "Result should contain user2 id");

        assertEquals(userResponse1, result.get(user1.getId()), "User1 should match");
        assertEquals(userResponse2, result.get(user2.getId()), "User2 should match");

        verify(userRepository).findAll();
        verify(userMapper).toResponse(user1);
        verify(userMapper).toResponse(user2);
    }


    @Test
    void partialUpdateUser_WithValidChanges_UpdatesUser() {
        Integer userId = 1;
        UserRequest updates = new UserRequest();
        updates.setFirstName("UpdatedName");
        updates.setLastName("UpdatedLastName");

        UserEntity existingUser = mock(UserEntity.class, CALLS_REAL_METHODS);
        existingUser.setId(userId);
        existingUser.setFirstName("OriginalName");
        existingUser.setLastName("OriginalLastName");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.partialUpdateUser(userId, updates);

        assertEquals("UpdatedName", existingUser.getFirstName());
        assertEquals("UpdatedLastName", existingUser.getLastName());

        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }

    @Test
    void createAdminUser() {
        UserRequest userDTO = new UserRequest();
        userDTO.setUsername("adminUser");
        userDTO.setRole("admin");

        when(userRepository.existsByUsername("adminUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.toAdminDTO(userDTO)).thenReturn(new AdminDTO());
        when(userMapper.toEntity(any(AdminDTO.class))).thenReturn(new AdminEntity());

        userService.createUser(userDTO, "password");

        verify(userRepository).save(any(AdminEntity.class));
    }

    @Test
    void createBabysitterUser() {
        UserRequest userDTO = new UserRequest();
        userDTO.setUsername("babysitterUser");
        userDTO.setRole("babysitter");

        when(userRepository.existsByUsername("babysitterUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.toBabysitterDTO(userDTO)).thenReturn(new BabysitterDTO("female", 22, true));
        when(userMapper.toEntity(any(BabysitterDTO.class))).thenReturn(new BabysitterEntity());

        userService.createUser(userDTO, "password");

        verify(userRepository).save(any(BabysitterEntity.class));
    }

    @Test
    void createUserWithInvalidRole() {
        UserRequest userDTO = new UserRequest();
        userDTO.setUsername("someUser");
        userDTO.setRole("invalidRole");

        when(userRepository.existsByUsername("someUser")).thenReturn(false);

        Exception exception = assertThrows(InvalidRoleException.class,
                () -> userService.createUser(userDTO, "password"));

        assertTrue(exception.getMessage().contains("Invalid Role"));
    }

    @Test
    void updateUserPhoneNumber() {
        Integer userId = 1;
        UserRequest userUpdates = new UserRequest();
        userUpdates.setPhoneNumber("1234567890");

        UserEntity existingUser = mock(UserEntity.class, CALLS_REAL_METHODS);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.partialUpdateUser(userId, userUpdates);

        verify(existingUser).setPhoneNumber("1234567890");
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUserAddress() {
        Integer userId = 1;
        UserRequest userUpdates = new UserRequest();
        userUpdates.setAddress("New Address");

        UserEntity existingUser = mock(UserEntity.class, CALLS_REAL_METHODS);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.partialUpdateUser(userId, userUpdates);

        verify(existingUser).setAddress("New Address");
        verify(userRepository).save(existingUser);
    }


    @Test
    void updateUserAge() {
        Integer userId = 1;
        UserRequest userUpdates = new UserRequest();
        userUpdates.setAge(30);

        UserEntity existingUser = mock(UserEntity.class, CALLS_REAL_METHODS);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.partialUpdateUser(userId, userUpdates);

        verify(existingUser).setAge(30);
        verify(userRepository).save(existingUser);
    }

}
