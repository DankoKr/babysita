package s3.fontys.babysita.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "babysitters")
@PrimaryKeyJoinColumn(name = "user_id")
public class BabysitterEntity extends UserEntity {
    @Column(name = "gender")
    private String gender;
    @Column(name = "points")
    private int points;
    @Column(name = "is_available")
    private boolean isAvailable = true;
    @OneToMany(mappedBy = "babysitter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PosterEntity> myPosters;
}
