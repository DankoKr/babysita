package s3.fontys.babysita.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="posters")
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

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "event_date")
    private LocalDate eventDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private ParentEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="babysitter_id")
    private BabysitterEntity babysitter;

    @OneToMany(mappedBy = "poster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobApplicationEntity> jobApplications;
}

