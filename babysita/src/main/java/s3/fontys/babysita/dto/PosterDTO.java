package s3.fontys.babysita.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PosterDTO {
    private int id;
    @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    private String title;
    @Size(min = 2, max = 500, message = "Description must be between 2 and 500 characters")
    private String description;
    private String imageUrl;
    private LocalDate eventDate;
}
