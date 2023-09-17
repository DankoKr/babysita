package s3.fontys.babysita.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
public class UserDTO {
    private int id;
    @Size(min = 6, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;
    @Size(min = 8, message = "Password must be 8 characters or more")
    private String password;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String phoneNumber;
    private String address;
    private int age;
    private String role;
    private String gender;
}
