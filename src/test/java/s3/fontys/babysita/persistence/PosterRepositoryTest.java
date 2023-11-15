package s3.fontys.babysita.persistence;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.PosterEntity;

import java.time.LocalDate;
import java.util.List;

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
    @Autowired
    private BabysitterRepository babysitterRepository;
    @Test
    void save_shouldSavePosterWithAllFields() {
        ParentEntity parent = new ParentEntity();
        parent.setUsername("JohnDoe");
        parent.setPassword("password1");
        parent.setEmail("parent@email.com");
        parent.setRole("parent");
        parentRepository.save(parent);

        PosterEntity posterEntity = new PosterEntity(1, "Title", "Description",
                "ImageUrl", LocalDate.now(), parent, null, null);

        PosterEntity savedPoster = posterRepository.save(posterEntity);
        assertNotNull(savedPoster.getId());
        savedPoster = entityManager.find(PosterEntity.class, savedPoster.getId());
        PosterEntity expectedPoster = posterRepository.getById(savedPoster.getId());

        assertEquals(expectedPoster, savedPoster);
    }

    @Test
    void findByParent_shouldReturnPostersOfSpecificParent() {
        ParentEntity parent = new ParentEntity();
        parent.setUsername("Joe doe moe");
        parent.setPassword("password123");
        parent.setEmail("parent@email.com");
        parent.setRole("parent");
        ParentEntity savedParent = parentRepository.save(parent);

        PosterEntity poster1 = new PosterEntity(null, "Title", "Description",
                "ImageUrl", LocalDate.now(), savedParent, null, null);
        PosterEntity poster2 = new PosterEntity(null, "Title2", "Description2",
                "ImageUrl2", LocalDate.now(), savedParent, null, null);
        posterRepository.save(poster1);
        posterRepository.save(poster2);

        entityManager.flush();

        List<PosterEntity> foundPosters = posterRepository.findByParent(savedParent);
        assertEquals(2, foundPosters.size());

        Assertions.assertTrue(foundPosters.stream().anyMatch(p -> "Title".equals(p.getTitle())));
        Assertions.assertTrue(foundPosters.stream().anyMatch(p -> "Title2".equals(p.getTitle())));
    }


    @Test
    void findByBabysitter_shouldReturnPostersOfSpecificBabysitter() {
        BabysitterEntity babysitter = new BabysitterEntity();
        babysitter.setUsername("Babysitter");
        babysitter.setPassword("password123");
        babysitter.setEmail("babysitter@email.com");
        babysitter.setRole("babysitter");

        ParentEntity parent = new ParentEntity();
        parent.setUsername("Parent");
        parent.setPassword("password123");
        parent.setEmail("parent@email.com");
        parent.setRole("parent");
        ParentEntity savedParent = parentRepository.save(parent);

        BabysitterEntity savedBabysitter = babysitterRepository.save(babysitter);

        PosterEntity poster1 = new PosterEntity(null, "Title", "Description",
                "ImageUrl", LocalDate.now(), savedParent, savedBabysitter, null);
        PosterEntity poster2 = new PosterEntity(null, "Title2", "Description2",
                "ImageUrl2", LocalDate.now(), savedParent, savedBabysitter, null);

        posterRepository.save(poster1);
        posterRepository.save(poster2);

        List<PosterEntity> foundPosters = posterRepository.findByBabysitter(savedBabysitter);
        assertEquals(2, foundPosters.size());
    }

    @Test
    void findByBabysitterIsNull_shouldReturnPostersWithNoBabysitter() {
        ParentEntity parent = new ParentEntity();
        parent.setUsername("Parent");
        parent.setPassword("password123");
        parent.setEmail("parent@email.com");
        parent.setRole("parent");
        ParentEntity savedParent = parentRepository.save(parent);

        PosterEntity poster1 = new PosterEntity(null, "Title", "Description",
                "ImageUrl", LocalDate.now(), savedParent, null, null);
        PosterEntity poster2 = new PosterEntity(null, "Title2", "Description2",
                "ImageUrl2", LocalDate.now(), savedParent, null, null);

        parentRepository.save(parent);
        posterRepository.save(poster1);
        posterRepository.save(poster2);

        List<PosterEntity> foundPosters = posterRepository.findByBabysitterIsNull();
        Assertions.assertTrue(foundPosters.contains(poster1));
        Assertions.assertTrue(foundPosters.contains(poster2));
    }
}
