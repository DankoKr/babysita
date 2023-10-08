package s3.fontys.babysita.persistence.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import s3.fontys.babysita.persistence.PosterRepository;
import s3.fontys.babysita.persistence.entity.PosterEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class PosterRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PosterRepository posterRepository;
    @Test
    void save_shouldSavePosterWithAllFields() {
        PosterEntity posterEntity = new PosterEntity(1, "Title", "Description",
                "ImageUrl", LocalDate.now(), false);

        PosterEntity savedPoster = posterRepository.save(posterEntity);
        assertNotNull(savedPoster.getId());
        savedPoster = entityManager.find(PosterEntity.class, savedPoster.getId());
        PosterEntity expectedCourse = new PosterEntity(savedPoster.getId(), "Title", "Description",
                "ImageUrl", LocalDate.now(), false);


        assertEquals(expectedCourse, savedPoster);
    }
}
