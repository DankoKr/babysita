package s3.fontys.babysita.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import s3.fontys.babysita.persistence.entity.ParentEntity;

public interface ParentRepository extends JpaRepository<ParentEntity, Integer> {
}
