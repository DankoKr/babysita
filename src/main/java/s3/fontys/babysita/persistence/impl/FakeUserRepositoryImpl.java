package s3.fontys.babysita.persistence.impl;

import org.springframework.stereotype.Repository;
import s3.fontys.babysita.domain.User;
import s3.fontys.babysita.persistence.UserRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class FakeUserRepositoryImpl implements UserRepository {
    private static int NEXT_ID = 1;
    private final Map<Integer, User> savedUsers;

    public FakeUserRepositoryImpl() {
        this.savedUsers = new HashMap<>();
    }
    @Override
    public boolean existsByUsername(String username) {
        for (User user : savedUsers.values()) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean existsById(int userId) {
        return savedUsers.containsKey(userId);
    }

    @Override
    public User getById(int userId) {
        return savedUsers.get(userId);
    }

    @Override
    public User save(User user) {
        user.setId(NEXT_ID);
        NEXT_ID++;
        savedUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(int userId) {
        savedUsers.remove(userId);
    }

    @Override
    public void editUser(User user, String email, String firstName, String lastName, String profileImage,
                         String phoneNumber, String address, int age) {
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setProfileImage(profileImage);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setAge(age);
    }

    @Override
    public int count() {
        return savedUsers.size();
    }

    @Override
    public void changePassword(User user, String password) {
        user.setPassword(password);
    }

    @Override
    public Map<Integer, User> getAll() {
        return Collections.unmodifiableMap(savedUsers);
    }
}
