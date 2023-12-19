package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import s3.fontys.babysita.business.UserService;
import s3.fontys.babysita.business.exception.DuplicatedUsernameException;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.business.exception.UnauthorizedDataAccessException;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.configuration.security.token.AccessToken;
import s3.fontys.babysita.domain.*;
import s3.fontys.babysita.persistence.UserRepository;
import s3.fontys.babysita.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AccessToken accessToken;

    @Override
    public void createUser(UserRequest userRequest, String password) {
        if(!userRepository.existsByUsername(userRequest.getUsername())){
            String encodedPassword = passwordEncoder.encode(password);
            userRequest.setPassword(encodedPassword);

            switch (userRequest.getRole()) {
                case "parent" -> {
                    ParentRequest parentRequest = userMapper.toParentRequest(userRequest);
                    UserEntity userEntity = userMapper.toEntity(parentRequest);
                    userRepository.save(userEntity);
                }
                case "babysitter" -> {
                    BabysitterRequest babysitterRequest = userMapper.toBabysitterRequest(userRequest);
                    UserEntity userEntity = userMapper.toEntity(babysitterRequest);
                    userRepository.save(userEntity);
                }
                case "admin" -> {
                    AdminRequest adminRequest = userMapper.toAdminRequest(userRequest);
                    UserEntity userEntity = userMapper.toEntity(adminRequest);
                    userRepository.save(userEntity);
                }
                default -> throw new InvalidRoleException("Invalid Role");
            }
        }
        else { throw new DuplicatedUsernameException("Username has already been taken");}

    }

    @Override
    public void deleteUser(int userId) {
        checkUserPermission(userId);
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponse getUser(int userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidIdException("User not found"));
        return userMapper.toResponse(userEntity);
    }

    @Override
    public Map<Integer, UserResponse> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .collect(Collectors.toMap(
                        UserEntity::getId,
                        userMapper::toResponse
                ));
    }

    @Override
    public void partialUpdateUser(Integer id, UserRequest userUpdates) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new InvalidIdException("Invalid User id"));

        checkUserPermission(id);

        if(userUpdates.getFirstName() != null) {
            existingUser.setFirstName(userUpdates.getFirstName());
        }
        if(userUpdates.getLastName() != null) {
            existingUser.setLastName(userUpdates.getLastName());
        }
        if(userUpdates.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(userUpdates.getPhoneNumber());
        }
        if(userUpdates.getAddress() != null) {
            existingUser.setAddress(userUpdates.getAddress());
        }
        if(userUpdates.getAge() != 0) {
            existingUser.setAge(userUpdates.getAge());
        }
        if(userUpdates.getProfileImage() != null) {
            existingUser.setProfileImage(userUpdates.getProfileImage());
        }

        userRepository.save(existingUser);
    }

    @Override
    public List<UserResponse> searchByUsernamePattern(String pattern) {
        String likePattern = "%" + pattern + "%";
        List<UserEntity> matches = userRepository.findByUsernameLike(likePattern);
        return matches.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getUsersById(int firstUserId, int secondUserId) {
        List<UserResponse> users = new ArrayList<>();
        UserResponse user1 = getUser(firstUserId);
        UserResponse user2 = getUser(secondUserId);
        users.add(user1);
        users.add(user2);
        return users;
    }

    @Override
    public void checkUserPermission(int userId) {
        if (!accessToken.getRole().equals("admin")) {
            if (accessToken.getUserId() != userId) {
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
        }
    }
}

