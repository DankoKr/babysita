package s3.fontys.babysita.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.PosterEntity;

import java.util.List;

public interface PosterRepository extends JpaRepository<PosterEntity, Integer> {
    List<PosterEntity> findByParent(ParentEntity parent);
    List<PosterEntity> findByBabysitter(BabysitterEntity babysitter);
    List<PosterEntity> findByBabysitterIsNull();
}
