package s3.fontys.babysita.business.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import s3.fontys.babysita.business.ParentService;
import s3.fontys.babysita.business.PosterService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.mapper.PosterMapper;
import s3.fontys.babysita.dto.PosterDTO;
import s3.fontys.babysita.persistence.PosterRepository;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.PosterEntity;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class PosterServiceImpl implements  PosterService {
    private final PosterRepository posterRepository;
    private final PosterMapper posterMapper;
    private final ParentService parentService;
    @Override
    public void createPoster(PosterDTO poster) {
        PosterEntity posterEntity = posterMapper.toEntity(poster);
        ParentEntity parentEntity = parentService.getParent(poster.getParentId());
        posterEntity.setParent(parentEntity);
        posterRepository.save(posterEntity);
    }

    @Override
    public Map<Integer, PosterDTO> getAllPosters() {
        List<PosterEntity> posters = posterRepository.findAll();
        return posters.stream()
                .collect(Collectors.toMap(
                        PosterEntity::getId,      // Key extractor: Poster's ID
                        posterMapper::toDTO       // Value mapper: Convert entity to DTO
                ));
    }

    @Override
    public void editPoster(int posterId, PosterDTO updatedPosterDTO) {
        if(posterRepository.existsById(posterId)) {
            PosterEntity updatedEntity = posterMapper.toEntity(updatedPosterDTO);
            ParentEntity parent = parentService.getParent(updatedPosterDTO.getParentId());
            updatedEntity.setId(posterId);
            updatedEntity.setParent(parent);
            posterRepository.save(updatedEntity);
        }
        else {throw new InvalidIdException("Invalid ID.");}
    }

    @Override
    public void deletePoster(int posterId) {
        if(posterRepository.existsById(posterId)){
            posterRepository.deleteById(posterId);
        }
        else {throw new InvalidIdException("Invalid ID.");}
    }

    @Transactional(readOnly = true)
    @Override
    public PosterDTO getPoster(int posterId) {
        PosterEntity posterEntity = posterRepository.findById(posterId)
                .orElseThrow(() -> new InvalidIdException("Poster with ID " + posterId + " not found"));
        PosterDTO posterDTO = posterMapper.toDTO(posterEntity);
        posterDTO.setParentId(posterEntity.getParent().getId());
        return posterDTO;
    }

    @Override
    public void patchPoster(int posterId, PosterDTO patchedPosterDTO) {
        PosterEntity existingEntity = posterRepository.findById(posterId)
                .orElseThrow(() -> new InvalidIdException("Poster with ID " + posterId + " not found"));

        if (patchedPosterDTO.getTitle() != null) {
            existingEntity.setTitle(patchedPosterDTO.getTitle());
        }
        if (patchedPosterDTO.getDescription() != null) {
            existingEntity.setDescription(patchedPosterDTO.getDescription());
        }
        if (patchedPosterDTO.getImageUrl() != null) {
            existingEntity.setImageUrl(patchedPosterDTO.getImageUrl());
        }
        if (patchedPosterDTO.getEventDate() != null) {
            existingEntity.setEventDate(patchedPosterDTO.getEventDate());
        }
        posterRepository.save(existingEntity);
    }
}
