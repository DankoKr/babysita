package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import s3.fontys.babysita.business.AuthService;
import s3.fontys.babysita.business.exception.InvalidCredentialsException;
import s3.fontys.babysita.configuration.security.token.AccessTokenEncoder;
import s3.fontys.babysita.configuration.security.token.impl.AccessTokenImpl;
import s3.fontys.babysita.domain.LoginRequest;
import s3.fontys.babysita.domain.LoginResponse;
import s3.fontys.babysita.persistence.UserRepository;
import s3.fontys.babysita.persistence.entity.UserEntity;



@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccessTokenEncoder accessTokenEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            throw new InvalidCredentialsException();
        }

        if (!matchesPassword(loginRequest.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String accessToken = generateAccessToken(user);
        return LoginResponse.builder().accessToken(accessToken).build();
    }

    private boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String generateAccessToken(UserEntity user) {
        Integer userId = user.getId();
        String role = user.getRole();

        return accessTokenEncoder.encode(
                new AccessTokenImpl(user.getUsername(), userId, role));
    }
}
