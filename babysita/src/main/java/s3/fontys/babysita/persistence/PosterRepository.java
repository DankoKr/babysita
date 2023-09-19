package s3.fontys.babysita.persistence;

import s3.fontys.babysita.domain.Poster;

import java.time.LocalDate;
import java.util.Map;

public interface PosterRepository {
    boolean existsById(int posterId);

    Poster getById(int posterId);

    Poster save(Poster poster);//Only for dummy data

    void deleteById(int posterId);

    void editPoster(Poster poster, String title, String description, String imageUrl, LocalDate eventDate);

    Map<Integer, Poster> getAll();

    int count();
}
