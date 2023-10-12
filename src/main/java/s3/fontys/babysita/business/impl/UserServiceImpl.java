package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import s3.fontys.babysita.business.UserService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.dto.BabysitterDTO;
import s3.fontys.babysita.dto.ParentDTO;
import s3.fontys.babysita.dto.UserDTO;
import s3.fontys.babysita.persistence.UserRepository;
import s3.fontys.babysita.persistence.entity.UserEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    //private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void createUser(UserDTO userDTO) {
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

    //@Override
    //public boolean checkPassword(UserEntity user, String rawPassword) {
        //return passwordEncoder.matches(rawPassword, user.getPassword());
    //}
}

