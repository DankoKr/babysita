package s3.fontys.babysita.persistence.impl;

import org.springframework.stereotype.Repository;
import s3.fontys.babysita.domain.Poster;
import s3.fontys.babysita.persistence.PosterRepository;

import java.time.LocalDate;
import java.util.*;

@Repository
public class FakePosterRepositoryImpl implements PosterRepository {
    private static int NEXT_ID = 1;
    private final Map<Integer, Poster> savedPosters;

    public FakePosterRepositoryImpl() {
        this.savedPosters = new HashMap<>();
    }

    @Override
    public boolean existsById(int posterId) {
        return savedPosters.containsKey(posterId);
    }

    @Override
    public Poster getById(int posterId) {
        return savedPosters.get(posterId);
    }

    @Override
    public void save(Poster poster) {
        poster.setId(NEXT_ID);
        NEXT_ID++;
        savedPosters.put(poster.getId(), poster);
    }

    @Override
    public void deleteById(int posterId) {
        savedPosters.remove(posterId);
    }

    @Override
    public void editPoster(Poster poster, String title, String description, String imageUrl, LocalDate eventDate) {
        poster.setTitle(title);
        poster.setDescription(description);
        poster.setImageUrl(imageUrl);
        poster.setEventDate(eventDate);
    }

    @Override
    public Map<Integer, Poster> getAll() {
        return Collections.unmodifiableMap(savedPosters);
    }

}
