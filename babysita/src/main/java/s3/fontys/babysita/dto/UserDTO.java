package s3.fontys.babysita.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String phoneNumber;
    private String address;
    private int age;
}
