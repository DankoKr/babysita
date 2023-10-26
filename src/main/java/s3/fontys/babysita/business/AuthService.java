package s3.fontys.babysita.business;

import s3.fontys.babysita.domain.LoginRequest;
import s3.fontys.babysita.domain.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
