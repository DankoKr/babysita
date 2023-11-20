package s3.fontys.babysita.business;
import s3.fontys.babysita.domain.UserRequest;
import s3.fontys.babysita.domain.UserResponse;

import java.util.List;
import java.util.Map;

public interface UserService {
    void createUser(UserRequest userRequest, String password);
    void deleteUser(int userId);
    UserResponse getUser(int userId);
    Map<Integer, UserResponse> getAllUsers();
    void partialUpdateUser(Integer id, UserRequest userUpdates);
    List<UserResponse> searchByUsernamePattern(String pattern);
}
