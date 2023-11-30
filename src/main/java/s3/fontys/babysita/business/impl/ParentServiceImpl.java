package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import s3.fontys.babysita.business.ParentService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.domain.ParentResponse;
import s3.fontys.babysita.persistence.ParentRepository;
import s3.fontys.babysita.persistence.entity.ParentEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ParentServiceImpl implements ParentService {
    private final UserMapper userMapper;
    private final ParentRepository parentRepository;
    @Override
    public ParentEntity getParent(int parentId) {
        return parentRepository.findById(parentId)
                .orElseThrow(() -> new InvalidIdException("Parent not found"));
    }

    @Override
    public Map<Integer, ParentResponse> getAllParents() {
        List<ParentEntity> parents = parentRepository.findAll();
        return parents.stream()
                .collect(Collectors.toMap(
                        ParentEntity::getId,
                        userMapper::toParentResponse
                ));
    }

}
