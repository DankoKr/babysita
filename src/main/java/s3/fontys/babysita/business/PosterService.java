package s3.fontys.babysita.business;

import s3.fontys.babysita.dto.PosterDTO;
import java.util.Map;

public interface PosterService {
    void createPoster(PosterDTO posterDTO);
    void editPoster(int posterId, PosterDTO updatedPosterDTO);
    void deletePoster(int posterId);
    PosterDTO getPoster(int posterId);
    Map<Integer, PosterDTO> getAllPosters();
    void patchPoster(int posterId, PosterDTO patchedPosterDTO);
}

