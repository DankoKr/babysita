package s3.fontys.babysita.business;

import s3.fontys.babysita.dto.BabysitterDTO;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;


import java.util.Map;

public interface BabysitterService {
    BabysitterEntity getBabysitter(int babysitterId);
    Map<Integer, BabysitterDTO> getAvailableBabysitters();
    void updateBabysitterPoints(int babysitterId);
}
