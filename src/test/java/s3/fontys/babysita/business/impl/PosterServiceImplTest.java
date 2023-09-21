package s3.fontys.babysita.business.impl;

import jakarta.validation.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import s3.fontys.babysita.domain.Poster;
import s3.fontys.babysita.persistence.PosterRepository;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PosterServiceImplTest {
    private final PosterRepository posterRepository;

    @Autowired
    public PosterServiceImplTest(PosterRepository posterRepository){
        this.posterRepository = posterRepository;
    }

    @Test
    public void createPosterTest(){
        //Arrange
        Poster poster = new Poster(1,"title1", "description",
                "image", LocalDate.now());

        //Act
        posterRepository.save(poster);

        //Assert
        Poster foundPoster = posterRepository.getById(poster.getId());
        assertThat(foundPoster).isEqualTo(poster);
    }

    @Test
    public void createPoster_EmptyTitleTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Poster poster = new Poster(1,"", "description",
                "image", LocalDate.now());

        // Expect a ConstraintViolationException
        assertThrows(ConstraintViolationException.class, () -> {
            Set<ConstraintViolation<Poster>> violations = validator.validate(poster);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        });
    }

    @Test
    public void editPosterTest(){
        Poster poster = new Poster(1,"title", "description",
                "image", LocalDate.now());
        posterRepository.save(poster);

        Poster currentPoster = posterRepository.getById(1);
        posterRepository.editPoster(currentPoster, "newTitle", "newDesc",
                "newImage", LocalDate.parse("2023-09-20"));

        Poster updatedPoster = posterRepository.getById(1);

        assertThat(updatedPoster.getTitle()).isEqualTo("newTitle");
        assertThat(updatedPoster.getDescription()).isEqualTo("newDesc");
        assertThat(updatedPoster.getImageUrl()).isEqualTo("newImage");
        assertThat(updatedPoster.getEventDate()).isEqualTo(LocalDate.parse("2023-09-20"));
    }

    @Test
    public void deletePosterTest(){
        Poster poster1 = new Poster(1,"title1", "description1",
                "image1", LocalDate.now());
        Poster poster2 = new Poster(2,"title2", "description2",
                "image2", LocalDate.now());
        Poster poster3 = new Poster(3,"title3", "description3",
                "image3", LocalDate.now());

        posterRepository.save(poster1);
        posterRepository.save(poster2);
        posterRepository.save(poster3);

        posterRepository.deleteById(2);
        Poster deletedPoster = posterRepository.getById(2);

        assertNull(deletedPoster);
    }

}
