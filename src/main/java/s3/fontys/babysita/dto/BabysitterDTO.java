package s3.fontys.babysita.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import s3.fontys.babysita.domain.UserRequest;

@AllArgsConstructor
@Getter
public class BabysitterDTO extends UserRequest {
    private String gender;
    private int points;
    private boolean isAvailable;
}
