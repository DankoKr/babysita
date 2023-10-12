package s3.fontys.babysita.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;

public interface BabysitterRepository extends JpaRepository<BabysitterEntity, Integer> {
}
