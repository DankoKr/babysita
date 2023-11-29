package s3.fontys.babysita.business;

import s3.fontys.babysita.domain.BabysitterResponse;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;


import java.util.Map;

public interface BabysitterService {
    BabysitterEntity getBabysitter(int babysitterId);
    Map<Integer, BabysitterResponse> getAvailableBabysitters();
    void updateBabysitterPoints(int babysitterId);
}
