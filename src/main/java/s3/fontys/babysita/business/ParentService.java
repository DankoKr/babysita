package s3.fontys.babysita.business;

import s3.fontys.babysita.domain.ParentResponse;
import s3.fontys.babysita.persistence.entity.ParentEntity;

import java.util.Map;


public interface ParentService {
    ParentEntity getParent(int parentId);
    Map<Integer, ParentResponse> getAllParents();
}
