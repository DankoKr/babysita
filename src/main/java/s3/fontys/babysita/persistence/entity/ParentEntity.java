package s3.fontys.babysita.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "parents")
@PrimaryKeyJoinColumn(name = "user_id")
public class ParentEntity extends UserEntity{
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PosterEntity> myPosters;
}
