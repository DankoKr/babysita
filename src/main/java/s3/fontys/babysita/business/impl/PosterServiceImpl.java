package s3.fontys.babysita.business.impl;

import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import s3.fontys.babysita.business.BabysitterService;
import s3.fontys.babysita.business.ParentService;
import s3.fontys.babysita.business.PosterService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.exception.UnauthorizedDataAccessException;
import s3.fontys.babysita.business.mapper.PosterMapper;
import s3.fontys.babysita.configuration.security.token.AccessToken;
import s3.fontys.babysita.dto.PosterDTO;
import s3.fontys.babysita.persistence.PosterRepository;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.PosterEntity;

import java.util.ArrayList;
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
    private final BabysitterService babysitterService;
    private final AccessToken requestAccessToken;
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
            PosterEntity existingEntity = posterRepository.findById(posterId)
                    .orElseThrow(() -> new InvalidIdException("Poster with ID " + posterId + " not found"));

            existingEntity.setTitle(updatedPosterDTO.getTitle());
            existingEntity.setDescription(updatedPosterDTO.getDescription());
            existingEntity.setImageUrl(updatedPosterDTO.getImageUrl());
            existingEntity.setEventDate(updatedPosterDTO.getEventDate());
            posterRepository.save(existingEntity);
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

    @Override
    public List<PosterDTO> getUserPosters(int userId) {
        String role = requestAccessToken.getRole();

        if (!role.equals("admin")) {
            if (requestAccessToken.getUserId() != userId) {
                throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
            }
        }

        List<PosterEntity> posters = new ArrayList<>();

        if (role.equals("parent") || role.equals("admin")) {
            ParentEntity parent = parentService.getParent(userId);
            posters = posterRepository.findByParent(parent);
        } else if (role.equals("babysitter")) {
            BabysitterEntity babysitter = babysitterService.getBabysitter(userId);
            posters = posterRepository.findByBabysitter(babysitter);
        }

        return posters.stream()
                .map(posterMapper::toDTO)
                .collect(Collectors.toList());
    }

}
