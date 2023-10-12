package s3.fontys.babysita.business;
import s3.fontys.babysita.dto.UserDTO;

import java.util.Map;

public interface UserService {
    void createUser(UserDTO userDTO);
    void deleteUser(int userId);
    UserDTO getUser(int userId);
    Map<Integer, UserDTO> getAllUsers();
    //void changePassword(UserDTO user, String password);
    //boolean checkPassword(UserEntity user, String rawPassword);
}
