package s3.fontys.babysita.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "job_applications")
public class JobApplicationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotBlank
    @Column(name = "description")
    private String description;

    @Column(name="status")
    private String status;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="babysitter_id")
    private BabysitterEntity babysitter;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="parent_id")
    private ParentEntity parent;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="poster_id")
    private PosterEntity poster;
}
