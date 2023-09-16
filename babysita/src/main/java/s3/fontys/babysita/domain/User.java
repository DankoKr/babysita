package s3.fontys.babysita.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class User {
    private int id;
    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 20, message = "Username must be between 5 and 20 characters")
    private String username;
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be 8 characters or more")
    private String password;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String phoneNumber;
    private String address;
    private String role;
    private int age;
}
