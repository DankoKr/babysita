package s3.fontys.babysita.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import s3.fontys.babysita.persistence.entity.AdminEntity;

public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {
}
