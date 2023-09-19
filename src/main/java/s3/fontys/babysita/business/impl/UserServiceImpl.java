package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import s3.fontys.babysita.business.UserService;
import s3.fontys.babysita.business.exception.DuplicatedUsernameException;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.domain.Babysitter;
import s3.fontys.babysita.domain.Parent;
import s3.fontys.babysita.domain.User;
import s3.fontys.babysita.dto.UserDTO;
import s3.fontys.babysita.persistence.UserRepository;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void createUser(UserDTO userDTO) {
        User user;
        if(!userRepository.existsByUsername(userDTO.getUsername())){
            if(userDTO.getRole().equals("parent")){
                user = new Parent(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(),
                        userDTO.getFirstName(), userDTO.getLastName(), userDTO.getProfileImage(), userDTO.getPhoneNumber(),
                        userDTO.getAddress(), "parent", userDTO.getAge());
            }
            else if (userDTO.getRole().equals("babysitter")){
                user = new Babysitter(userDTO.getId(), userDTO.getUsername(), userDTO.getPassword(), userDTO.getEmail(),
                        userDTO.getFirstName(), userDTO.getLastName(), userDTO.getProfileImage(), userDTO.getPhoneNumber(),
                        userDTO.getAddress(), "babysitter", userDTO.getAge(), userDTO.getGender());
            }
            else {
                throw new InvalidRoleException("Invalid role!");
            }
            userRepository.save(user);
        }
        else {
            throw new DuplicatedUsernameException("Username is not unique!");
        }
    }

    @Override
    public void editUser(User user, String email, String firstName, String lastName, String profileImage,
                         String phoneNumber, String address, int age) {
        if(userRepository.existsById(user.getId())) {
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
        if(userRepository.existsById(userId)){
            return userRepository.getById(userId);
        }
        else throw new InvalidIdException("Invalid ID.");
    }

    @Override
    public Map<Integer, User> getAllUsers() {
        return userRepository.getAll();
    }

    @Override
    public void changePassword(User user, String password) {
        //Not fully implemented
        user.setPassword(password);
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
