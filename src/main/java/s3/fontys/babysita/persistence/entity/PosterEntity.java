package s3.fontys.babysita.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Length;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="poster")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PosterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Length(min = 2, max = 50)
    @Column(name = "title")
    private String title;

    @Length(min = 2, max = 500)
    @Column(name = "description")
    private String description;

    @Column(name = "imageUrl")
    private String imageUrl;

    @Column(name = "eventDate")
    private LocalDate eventDate;

    @Column(name = "isAppointed")
    private boolean isAppointed;
}

