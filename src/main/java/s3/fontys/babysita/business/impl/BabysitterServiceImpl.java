package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import s3.fontys.babysita.business.BabysitterService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.dto.BabysitterDTO;
import s3.fontys.babysita.persistence.BabysitterRepository;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BabysitterServiceImpl implements BabysitterService {
    private final BabysitterRepository babysitterRepository;
    private final UserMapper userMapper;
    @Override
    public BabysitterEntity getBabysitter(int babysitterId) {
        return babysitterRepository.findById(babysitterId)
                .orElseThrow(() -> new InvalidIdException("Babysitter not found"));
    }

    @Override
    public Map<Integer, BabysitterDTO> getAllBabysitters() {
        List<BabysitterEntity> parents = babysitterRepository.findAll();
        return parents.stream()
                .collect(Collectors.toMap(
                        BabysitterEntity::getId,
                        userMapper::toBabysitterDTO
                ));
    }
}
