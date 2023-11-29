package s3.fontys.babysita.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BabysitterResponse extends UserResponse {
    private String gender;
    private int points;
    private boolean isAvailable;
}
