package s3.fontys.babysita.business.mapper;

import org.mapstruct.Mapper;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.domain.*;
import s3.fontys.babysita.persistence.entity.AdminEntity;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ParentRequest toParentRequest(ParentEntity entity);
    ParentResponse toParentResponse(ParentEntity entity);
    ParentEntity toParentEntity(ParentRequest request);
    ParentRequest toParentRequest(UserRequest request);

    BabysitterRequest toBabysitterRequest(BabysitterEntity entity);
    BabysitterResponse toBabysitterResponse(BabysitterEntity entity);
    BabysitterEntity toBabysitterEntity(BabysitterRequest request);
    BabysitterRequest toBabysitterRequest(UserRequest request);

    AdminEntity toAdminEntity(AdminRequest request);
    AdminRequest toAdminRequest(UserRequest request);

    UserResponse toResponse(UserEntity entity);

    default UserEntity toEntity(UserRequest request) {
        if (request instanceof ParentRequest) {
            return toParentEntity((ParentRequest) request);
        } else if (request instanceof BabysitterRequest) {
            return toBabysitterEntity((BabysitterRequest) request);
        } else if (request instanceof AdminRequest) {
            return toAdminEntity((AdminRequest) request);
        }
        else {
            throw new InvalidRoleException("Unknown type!");
        }
    }

}




