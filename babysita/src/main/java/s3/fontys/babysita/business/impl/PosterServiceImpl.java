package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import s3.fontys.babysita.business.PosterService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.domain.Poster;
import s3.fontys.babysita.persistence.PosterRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PosterServiceImpl implements  PosterService {
    private final PosterRepository posterRepository;
    @Override
    public void createPoster(Poster poster) {
        posterRepository.save(poster);
    }

    @Override
    public void editPoster(Poster poster, String title, String description, String imageUrl, LocalDate eventDate) {
        if(posterRepository.existsById(poster.getId())) {
            posterRepository.editPoster(poster, title, description, imageUrl, eventDate);
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

    @Override
    public Poster getPoster(int posterId) {
        Optional<Poster> poster = this.posterRepository.getById(posterId);
        if(poster.isEmpty()) throw new InvalidIdException("Invalid ID.");
        else return poster.get();
    }

    @Override
    public Map<Integer, Poster> getAllPosters() {
        return posterRepository.getAll();
    }

    @Override
    public void patchPoster(Poster oldPoster, String title, String description, String imageUrl, LocalDate eventDate) {
        if (title != null) {
            oldPoster.setTitle(title);
        }
        if (description != null) {
            oldPoster.setDescription(description);
        }
        if (imageUrl != null) {
            oldPoster.setImageUrl(imageUrl);
        }
        if (eventDate != null) {
            oldPoster.setEventDate(eventDate);
        }

        posterRepository.editPoster(oldPoster, oldPoster.getTitle(), oldPoster.getDescription(), oldPoster.getImageUrl(), oldPoster.getEventDate());
    }
}
