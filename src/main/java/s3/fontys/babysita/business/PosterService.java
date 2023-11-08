package s3.fontys.babysita.business;

import s3.fontys.babysita.dto.PosterDTO;
import s3.fontys.babysita.persistence.entity.PosterEntity;

import java.util.List;
import java.util.Map;

public interface PosterService {
    void createPoster(PosterDTO posterDTO);
    void editPoster(int posterId, PosterDTO updatedPosterDTO);
    void deletePoster(int posterId);
    PosterDTO getPoster(int posterId);
    PosterEntity getPosterEntity(int posterId);
    Map<Integer, PosterDTO> getAllPosters();
    Map<Integer, PosterDTO> getPostersWithoutBabysitterId();
    void patchPoster(int posterId, PosterDTO patchedPosterDTO);
    List<PosterDTO> getUserPosters(int userId);
    void assignPosterToBabysitter(int posterId, int babysitterId);
}

