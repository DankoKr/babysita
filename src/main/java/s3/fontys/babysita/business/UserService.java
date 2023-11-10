package s3.fontys.babysita.business;
import s3.fontys.babysita.dto.UserDTO;
import s3.fontys.babysita.persistence.entity.UserEntity;

import java.util.Map;

public interface UserService {
    void createUser(UserDTO userDTO, String password);
    void deleteUser(int userId);
    UserDTO getUser(int userId);
    Map<Integer, UserDTO> getAllUsers();
    void partialUpdateUser(Integer id, UserDTO userUpdates);
}
