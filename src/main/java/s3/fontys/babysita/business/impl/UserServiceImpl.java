package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import s3.fontys.babysita.business.UserService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.business.exception.UnauthorizedDataAccessException;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.configuration.security.token.AccessToken;
import s3.fontys.babysita.dto.AdminDTO;
import s3.fontys.babysita.dto.BabysitterDTO;
import s3.fontys.babysita.dto.ParentDTO;
import s3.fontys.babysita.dto.UserDTO;
import s3.fontys.babysita.persistence.UserRepository;
import s3.fontys.babysita.persistence.entity.AdminEntity;
import s3.fontys.babysita.persistence.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AccessToken requestAccessToken;

    @Override
    public void createUser(UserDTO userDTO, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        userDTO.setPassword(encodedPassword);

        if(userDTO.getRole().equals("parent")){
            ParentDTO parentDTO = userMapper.toParentDTO(userDTO);
            UserEntity userEntity = userMapper.toEntity(parentDTO);
            userRepository.save(userEntity);
        }
        else if(userDTO.getRole().equals("babysitter")){
            BabysitterDTO babysitterDTO = userMapper.toBabysitterDTO(userDTO);
            UserEntity userEntity = userMapper.toEntity(babysitterDTO);
            userRepository.save(userEntity);
        }
        else if(userDTO.getRole().equals("admin")){
            AdminDTO adminDTO = userMapper.toAdminDTO(userDTO);
            UserEntity userEntity = userMapper.toEntity(adminDTO);
            userRepository.save(userEntity);
        }
        else {
            throw new InvalidRoleException("Invalid Role");
        }
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserDTO getUser(int userId) {
        if (!requestAccessToken.getRole().equals("admin")) {
            if (requestAccessToken.getUserId() != userId) {
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
        }

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidIdException("User not found"));
        return userMapper.toDTO(userEntity);
    }

    @Override
    public Map<Integer, UserDTO> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .collect(Collectors.toMap(
                        UserEntity::getId,
                        userMapper::toDTO
                ));
    }

    @Override
    public void partialUpdateUser(Integer id, UserDTO userUpdates) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new InvalidIdException("Invalid User id"));

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

        userRepository.save(existingUser);
    }
}

