package s3.fontys.babysita.persistence;

import s3.fontys.babysita.domain.Poster;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface PosterRepository {
    boolean existsById(int posterId);

    Optional<Poster> getById(int posterId);

    Poster save(Poster poster);

    void deleteById(int posterId);

    void editPoster(Poster poster, String title, String description, String imageUrl, LocalDate eventDate);

    Map<Integer, Poster> getAll();

    int count();
}
