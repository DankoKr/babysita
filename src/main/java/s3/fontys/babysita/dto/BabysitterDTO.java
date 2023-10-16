package s3.fontys.babysita.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BabysitterDTO extends UserDTO{
    private String gender;
    private int points;
    private boolean isAvailable;
}
