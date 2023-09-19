package s3.fontys.babysita.business;

import s3.fontys.babysita.domain.Poster;

import java.time.LocalDate;
import java.util.Map;

public interface PosterService {
    void createPoster(Poster poster);
    void editPoster(Poster poster, String title, String description, String imageUrl, LocalDate eventDate);
    void deletePoster(int posterId);
    Poster getPoster(int posterId);
    Map<Integer, Poster> getAllPosters();
    void patchPoster(Poster oldPoster, String title, String description, String imageUrl, LocalDate eventDate);
}
