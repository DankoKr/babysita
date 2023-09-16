package s3.fontys.babysita.persistence;

import s3.fontys.babysita.domain.Poster;
import s3.fontys.babysita.domain.User;

import java.util.Map;
import java.util.Optional;

public interface UserRepository {
    boolean existsByUsername(String username);
    boolean existsById(int userId);
    Optional<User> getById(int userId);
    User save(User user);
    void deleteById(int userId);
    void editUser(User user, String email, String firstName,  String lastName,
                  String profileImage, String phoneNumber, String address, int age);
    int count();
    void changePassword(User user,String password);
    Map<Integer, User> getAll();

}
