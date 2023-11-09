package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import s3.fontys.babysita.business.exception.DuplicatedUsernameException;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.configuration.security.token.AccessToken;
import s3.fontys.babysita.dto.ParentDTO;
import s3.fontys.babysita.dto.UserDTO;
import s3.fontys.babysita.persistence.UserRepository;
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


}
