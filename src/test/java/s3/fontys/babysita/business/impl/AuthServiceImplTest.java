package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.fontys.babysita.business.exception.InvalidCredentialsException;
import s3.fontys.babysita.configuration.security.token.AccessTokenEncoder;
import s3.fontys.babysita.domain.LoginRequest;
import s3.fontys.babysita.domain.LoginResponse;
import s3.fontys.babysita.persistence.UserRepository;
import s3.fontys.babysita.persistence.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccessTokenEncoder accessTokenEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void login_withValidCredentials_returnsLoginResponse() {
        String username = "user";
        String password = "pass";
        String encodedPassword = "encodedPass";
        String accessToken = "accessToken";

        UserEntity userEntity = mock(UserEntity.class); //Mocked because it is abstract
        when(userEntity.getUsername()).thenReturn(username);
        when(userEntity.getPassword()).thenReturn(encodedPassword);
        when(userEntity.getId()).thenReturn(1);

        LoginRequest loginRequest = new LoginRequest(username, password);

        when(userRepository.findByUsername(username)).thenReturn(userEntity);
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(accessTokenEncoder.encode(any())).thenReturn(accessToken);

        LoginResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(accessToken, response.getAccessToken());
    }

    @Test
    void login_withInvalidUsername_throwsInvalidCredentialsException() {
        String username = "nonexistentUser";
        String password = "pass";

        LoginRequest loginRequest = new LoginRequest(username, password);

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(loginRequest));
    }

}
