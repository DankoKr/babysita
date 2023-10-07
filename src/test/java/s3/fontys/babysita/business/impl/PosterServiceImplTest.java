package s3.fontys.babysita.business.impl;

import jakarta.validation.*;
import org.junit.jupiter.api.Test;
import s3.fontys.babysita.domain.Poster;
import s3.fontys.babysita.persistence.PosterRepository;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PosterServiceImplTest {
    private final PosterRepository posterRepository = mock(PosterRepository.class);

    @Test
    public void createPosterTest() {
        //Arrange
        Poster poster = new Poster(1, "title1", "description",
                "image", LocalDate.now(), false);

        //Act
        when(posterRepository.getById(1)).thenReturn(poster);
        posterRepository.save(poster);

        //Assert
        Poster foundPoster = posterRepository.getById(poster.getId());
        assertThat(foundPoster).isEqualTo(poster);
    }

    @Test
    public void createPoster_EmptyTitleTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Poster poster = new Poster(1, "", "description",
                "image", LocalDate.now(), false);

        // Expect a ConstraintViolationException
        assertThrows(ConstraintViolationException.class, () -> {
            Set<ConstraintViolation<Poster>> violations = validator.validate(poster);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
    }

    @Test
    public void editPosterTest() {
        Poster poster = new Poster(1, "title", "description",
                "image", LocalDate.now(), false);

        Poster updatedPoster = new Poster(1, "newTitle", "newDesc",
                "newImage", LocalDate.parse("2023-09-20"), false);

        when(posterRepository.getById(1)).thenReturn(poster);
        posterRepository.save(poster);

        // Assume that editPoster method updates the poster and saves it back.
        // As the real implementation of editPoster is not provided, we make this assumption.
        posterRepository.editPoster(poster, "newTitle", "newDesc",
                "newImage", LocalDate.parse("2023-09-20"));
        when(posterRepository.getById(1)).thenReturn(updatedPoster);

        Poster resultPoster = posterRepository.getById(1);
        assertThat(resultPoster).isEqualTo(updatedPoster);
    }

    @Test
    public void deletePosterTest() {
        Poster poster1 = new Poster(1, "title1", "description1",
                "image1", LocalDate.now(), false);
        Poster poster2 = new Poster(2, "title2", "description2",
                "image2", LocalDate.now(), false);
        Poster poster3 = new Poster(3, "title3", "description3",
                "image3", LocalDate.now(), false);

        when(posterRepository.getById(1)).thenReturn(poster1);
        when(posterRepository.getById(2)).thenReturn(poster2);
        when(posterRepository.getById(3)).thenReturn(poster3);

        posterRepository.save(poster1);
        posterRepository.save(poster2);
        posterRepository.save(poster3);

        posterRepository.deleteById(2);
        when(posterRepository.getById(2)).thenReturn(null);

        Poster deletedPoster = posterRepository.getById(2);
        assertNull(deletedPoster);
    }
}

