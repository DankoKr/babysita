package s3.fontys.babysita.persistence.impl;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import s3.fontys.babysita.persistence.ParentRepository;
import s3.fontys.babysita.persistence.PosterRepository;
import s3.fontys.babysita.persistence.entity.ParentEntity;
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
    @Autowired
    private ParentRepository parentRepository;
    @Test
    void save_shouldSavePosterWithAllFields() {
        ParentEntity parent = new ParentEntity();
        parent.setId(1);
        parent.setUsername("JohnDoe");
        parent.setPassword("password1");
        parent.setEmail("parent@email.com");
        parent.setRole("parent");
        parentRepository.save(parent);

        PosterEntity posterEntity = new PosterEntity(1, "Title", "Description",
                "ImageUrl", LocalDate.now(), parent, null);

        PosterEntity savedPoster = posterRepository.save(posterEntity);
        assertNotNull(savedPoster.getId());
        savedPoster = entityManager.find(PosterEntity.class, savedPoster.getId());
        PosterEntity expectedPoster = posterRepository.getById(savedPoster.getId());

        assertEquals(expectedPoster, savedPoster);
    }
}
