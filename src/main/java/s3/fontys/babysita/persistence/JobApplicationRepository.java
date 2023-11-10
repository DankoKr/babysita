package s3.fontys.babysita.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.JobApplicationEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;

import java.util.List;

public interface JobApplicationRepository extends JpaRepository<JobApplicationEntity, Integer> {
    List<JobApplicationEntity> findByBabysitter(BabysitterEntity babysitter);
    List<JobApplicationEntity> findByParent(ParentEntity parent);
}
