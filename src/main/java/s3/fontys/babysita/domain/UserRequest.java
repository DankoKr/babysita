package s3.fontys.babysita.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {
    private Integer id;
    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 20, message = "Username must be between 6 and 20 characters")
    private String username;
    @NotBlank
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
