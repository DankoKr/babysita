package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import s3.fontys.babysita.business.UserService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.domain.User;
import s3.fontys.babysita.persistence.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void editUser(User user, String email, String firstName, String lastName, String profileImage,
                         String phoneNumber, String address, int age) {
        if(userRepository.existsByUsername(user.getUsername())) {
            userRepository.editUser(user, email, firstName, lastName, profileImage, phoneNumber, address, age);
        }
        else {throw new InvalidIdException("Invalid ID.");}
    }

    @Override
    public void deleteUser(int userId) {
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
        }
        else {throw new InvalidIdException("Invalid ID.");}
    }

    @Override
    public User getUser(int userId) {
        Optional<User> user = this.userRepository.getById(userId);
        if(user.isEmpty()) throw new InvalidIdException("Invalid ID.");
        else return user.get();
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public void changePassword(User user, String password) {

    }

    @Override
    public void patchUser(User user, String email, String firstName, String lastName, String profileImage,
                          String phoneNumber, String address, int age) {
        if (email != null) {
            user.setEmail(email);
        }
        if (firstName != null) {
            user.setFirstName(firstName);
        }
        if (lastName != null) {
            user.setLastName(lastName);
        }
        if (profileImage != null) {
            user.setProfileImage(profileImage);
        }
        if (phoneNumber != null) {
            user.setPhoneNumber(phoneNumber);
        }
        if (address != null) {
            user.setAddress(address);
        }
        if (age > 0) {
            user.setAge(age);
        }

        userRepository.editUser(user, user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getProfileImage(), user.getPhoneNumber(), user.getAddress(), user.getAge());
    }
}
