package s3.fontys.babysita.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Babysitter extends User {
    private String gender;
    private int points;

    public Babysitter(int id, @NotBlank(message = "Username is required")
    @Size(min = 6, max = 20, message = "Username must be between 5 and 20 characters") String username,
                      @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be 8 characters or more")
                      String password, @Email String email, String firstName, String lastName, String profileImage,
                      String phoneNumber, String address, String role, int age, String gender) {
        super(id, username, password, email, firstName, lastName, profileImage, phoneNumber, address, role, age);
        this.gender = gender;
        this.points = 0;
    }
}
