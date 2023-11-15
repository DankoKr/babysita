package s3.fontys.babysita.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String profileImage;
    private String phoneNumber;
    private String address;
    private String role;
    private int age;
}
