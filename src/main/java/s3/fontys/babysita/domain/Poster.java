package s3.fontys.babysita.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Poster {
    private int id;
    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 200, message = "Title must be between 2 and 200 characters")
    private String title;
    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 500, message = "Description must be between 2 and 500 characters")
    private String description;
    private String imageUrl;
    private LocalDate eventDate;
    private boolean isAppointed;
}
