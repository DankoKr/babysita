package s3.fontys.babysita.business;

import s3.fontys.babysita.domain.User;

import java.util.Map;

public interface UserService {
    void createUser(User user);
    void editUser(User user, String email, String firstName,  String lastName,
                  String profileImage, String phoneNumber, String address, int age);
    void deleteUser(int userId);
    User getUser(int userId);
    Map<Integer, User> getAllUsers();
    void changePassword(User user, String password);
    void patchUser(User user, String email, String firstName,  String lastName,
                  String profileImage, String phoneNumber, String address, int age);
}
