package s3.fontys.babysita.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import s3.fontys.babysita.domain.UserResponse;
import s3.fontys.babysita.persistence.entity.UserEntity;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
    Boolean existsByUsername(String username);
    List<UserEntity> findByUsernameLike(String pattern);
}
