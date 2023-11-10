package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import s3.fontys.babysita.business.exception.DuplicatedUsernameException;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.business.exception.UnauthorizedDataAccessException;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.configuration.security.token.AccessToken;
import s3.fontys.babysita.dto.AdminDTO;
import s3.fontys.babysita.dto.BabysitterDTO;
import s3.fontys.babysita.dto.ParentDTO;
import s3.fontys.babysita.dto.UserDTO;
import s3.fontys.babysita.persistence.UserRepository;
import s3.fontys.babysita.persistence.entity.AdminEntity;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUser");
        userDTO.setRole("parent");
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        // Create a ParentDTO from the UserDTO, simulating what the userMapper would do.
        ParentDTO parentDTO = new ParentDTO();
        parentDTO.setUsername(userDTO.getUsername());
        parentDTO.setPassword(encodedPassword);
        parentDTO.setRole(userDTO.getRole());

        // The expected ParentEntity that should be returned by the mapper.
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
        UserDTO userDTO = new UserDTO();
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
    void getUser_ValidUserId_ReturnsUser() {
        int userId = 1;
        UserEntity userEntity = mock(UserEntity.class);
        userEntity.setId(userId);
        UserDTO expectedDTO = new UserDTO();

        when(requestAccessToken.getRole()).thenReturn("admin");
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userMapper.toDTO(userEntity)).thenReturn(expectedDTO);

        UserDTO actualDTO = userService.getUser(userId);

        assertNotNull(actualDTO, "The returned UserDTO should not be null.");
        assertEquals(expectedDTO, actualDTO, "The returned UserDTO is not the one expected.");

        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(userEntity);
    }

    @Test
    void getAllUsers_ReturnsMapOfAllUserDTOs() {
        List<UserEntity> users = new ArrayList<>();
        UserEntity user1 = mock(UserEntity.class);
        UserEntity user2 = mock(UserEntity.class);

        when(user1.getId()).thenReturn(10);
        when(user2.getId()).thenReturn(20);

        users.add(user1);
        users.add(user2);

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTO(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity entity = invocation.getArgument(0);
            UserDTO dto = new UserDTO();
            dto.setId(entity.getId());
            return dto;
        });

        Map<Integer, UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(users.size(), result.size());
        assertTrue(result.containsKey(user1.getId()));
        assertTrue(result.containsKey(user2.getId()));

        verify(userRepository).findAll();
        verify(userMapper, times(users.size())).toDTO(any(UserEntity.class));
    }

    @Test
    void partialUpdateUser_WithValidChanges_UpdatesUser() {
        Integer userId = 1;
        UserDTO updates = new UserDTO();
        updates.setFirstName("UpdatedName");
        updates.setLastName("UpdatedLastName");

        //CALLS_REAL_METHODS -> invokes the methods instead of mocks that
        //do not have state and do not automatically retain values
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
        UserDTO userDTO = new UserDTO();
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
        UserDTO userDTO = new UserDTO();
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
        UserDTO userDTO = new UserDTO();
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
        UserDTO userUpdates = new UserDTO();
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
        UserDTO userUpdates = new UserDTO();
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
        UserDTO userUpdates = new UserDTO();
        userUpdates.setAge(30);

        UserEntity existingUser = mock(UserEntity.class, CALLS_REAL_METHODS);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        userService.partialUpdateUser(userId, userUpdates);

        verify(existingUser).setAge(30);
        verify(userRepository).save(existingUser);
    }

    @Test
    void nonAdminUserAccessingOwnData() {
        int userId = 1;
        when(requestAccessToken.getRole()).thenReturn("parent");
        when(requestAccessToken.getUserId()).thenReturn(userId);

        ParentEntity parentEntity = new ParentEntity();
        parentEntity.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(parentEntity));
        when(userMapper.toDTO(parentEntity)).thenReturn(new UserDTO());

        UserDTO result = userService.getUser(userId);

        assertNotNull(result);
        verify(userRepository).findById(userId);
    }


    @Test
    void nonAdminUserAccessingOtherUserData() {
        int userId = 1;
        int differentUserId = 2;
        when(requestAccessToken.getRole()).thenReturn("parent");
        when(requestAccessToken.getUserId()).thenReturn(differentUserId);

        Exception exception = assertThrows(UnauthorizedDataAccessException.class,
                () -> userService.getUser(userId));

        assertTrue(exception.getMessage().contains("USER_ID_NOT_FROM_LOGGED_IN_USER"));
    }

}
