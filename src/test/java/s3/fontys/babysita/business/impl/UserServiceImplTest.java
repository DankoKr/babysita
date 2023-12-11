package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import s3.fontys.babysita.business.exception.DuplicatedUsernameException;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.configuration.security.token.AccessToken;
import s3.fontys.babysita.domain.*;
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
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("newUser");
        userRequest.setRole("parent");
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        ParentRequest parentRequest = new ParentRequest();
        parentRequest.setUsername(userRequest.getUsername());
        parentRequest.setPassword(encodedPassword);
        parentRequest.setRole(userRequest.getRole());

        ParentEntity expectedParentEntity = new ParentEntity();
        expectedParentEntity.setUsername(userRequest.getUsername());
        expectedParentEntity.setRole(userRequest.getRole());

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userMapper.toParentRequest(userRequest)).thenReturn(parentRequest);
        when(userMapper.toEntity(parentRequest)).thenReturn(expectedParentEntity);
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedParentEntity);

        assertDoesNotThrow(() -> userService.createUser(userRequest, rawPassword));

        verify(userMapper).toParentRequest(userRequest);
        verify(userMapper).toEntity(parentRequest);
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
    void getAllUsers_ReturnsMapOfAllUserResponses() {
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
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("adminUser");
        userRequest.setRole("admin");

        when(userRepository.existsByUsername("adminUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.toAdminRequest(userRequest)).thenReturn(new AdminRequest());
        when(userMapper.toEntity(any(AdminRequest.class))).thenReturn(new AdminEntity());

        userService.createUser(userRequest, "password");

        verify(userRepository).save(any(AdminEntity.class));
    }

    @Test
    void createBabysitterUser() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("babysitterUser");
        userRequest.setRole("babysitter");

        when(userRepository.existsByUsername("babysitterUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userMapper.toBabysitterRequest(userRequest)).thenReturn(new BabysitterRequest());
        when(userMapper.toEntity(any(BabysitterRequest.class))).thenReturn(new BabysitterEntity());

        userService.createUser(userRequest, "password");

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
    void updateUserImage() {
        Integer userId = 1;
        UserRequest userUpdates = new UserRequest();
        userUpdates.setProfileImage("New image");

        UserEntity existingUser = mock(UserEntity.class, CALLS_REAL_METHODS);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.partialUpdateUser(userId, userUpdates);

        verify(existingUser).setProfileImage("New image");
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

    @Test
     void testSearchByUsernamePattern() {
        UserEntity user1 = mock(UserEntity.class);
        UserEntity user2 = mock(UserEntity.class);
        when(user1.getId()).thenReturn(1);
        when(user2.getId()).thenReturn(2);

        UserResponse userResponse1 = new UserResponse();
        userResponse1.setId(user1.getId());
        userResponse1.setUsername("test1");

        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(user2.getId());
        userResponse2.setUsername("test2");

        List<UserEntity> users = Arrays.asList(user1, user2);
        String pattern = "test";
        String likePattern = "%" + pattern + "%";

        when(userRepository.findByUsernameLike(likePattern)).thenReturn(users);
        when(userMapper.toResponse(user1)).thenReturn(userResponse1);
        when(userMapper.toResponse(user2)).thenReturn(userResponse2);

        List<UserResponse> actualResponse = userService.searchByUsernamePattern(pattern);

        assertEquals(2, actualResponse.size());
        assertEquals(userResponse1, actualResponse.get(0));
        assertEquals(userResponse2, actualResponse.get(1));

        verify(userRepository).findByUsernameLike(likePattern);
        verify(userMapper, times(2)).toResponse(any(UserEntity.class));
    }

    @Test
     void whenUserFound_thenReturnsUserResponse() {
        int userId = 1;
        UserEntity userEntity = mock(UserEntity.class);
        UserResponse expectedResponse = new UserResponse();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toResponse(userEntity)).thenReturn(expectedResponse);

        UserResponse actualResponse = userService.getUser(userId);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
     void whenUserNotFound_thenThrowsException() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> userService.getUser(userId));
    }

    @Test
    void getUsersById_BothUsersExist() {
        int firstUserId = 1;
        int secondUserId = 2;

        UserEntity userEntity1 = mock(UserEntity.class);
        UserEntity userEntity2 = mock(UserEntity.class);

        UserResponse userResponse1 = new UserResponse();
        UserResponse userResponse2 = new UserResponse();

        when(userRepository.findById(firstUserId)).thenReturn(Optional.of(userEntity1));
        when(userRepository.findById(secondUserId)).thenReturn(Optional.of(userEntity2));

        when(userMapper.toResponse(userEntity1)).thenReturn(userResponse1);
        when(userMapper.toResponse(userEntity2)).thenReturn(userResponse2);

        var result = userService.getUsersById(firstUserId, secondUserId);

        assertEquals(2, result.size());
        assertTrue(result.contains(userResponse1));
        assertTrue(result.contains(userResponse2));

        // Verify that findById was called for both user IDs
        verify(userRepository).findById(firstUserId);
        verify(userRepository).findById(secondUserId);

        // Verify that toResponse was called for both user entities
        verify(userMapper).toResponse(userEntity1);
        verify(userMapper).toResponse(userEntity2);
    }

}
