package s3.fontys.babysita.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationDTO {
    private Integer id;
    @NotBlank(message = "Description is required")
    @Size(min = 6, message = "Description must be at least 6 characters")
    private String description;
    private String status;
    @NotNull
    private int babysitterId;
    @NotNull
    private int posterId;
    @NotNull
    private int parentId;
}
