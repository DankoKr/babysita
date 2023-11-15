package s3.fontys.babysita.business.mapper;

import org.mapstruct.Mapper;
import s3.fontys.babysita.business.exception.InvalidRoleException;
import s3.fontys.babysita.domain.UserResponse;
import s3.fontys.babysita.dto.AdminDTO;
import s3.fontys.babysita.dto.BabysitterDTO;
import s3.fontys.babysita.dto.ParentDTO;
import s3.fontys.babysita.domain.UserRequest;
import s3.fontys.babysita.persistence.entity.AdminEntity;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ParentDTO toParentDTO(ParentEntity entity);
    ParentEntity toParentEntity(ParentDTO dto);
    ParentDTO toParentDTO(UserRequest userDTO);

    BabysitterDTO toBabysitterDTO(BabysitterEntity entity);
    BabysitterEntity toBabysitterEntity(BabysitterDTO dto);
    BabysitterDTO toBabysitterDTO(UserRequest userDTO);

    AdminDTO toAdminDTO(AdminEntity entity);
    AdminEntity toAdminEntity(AdminDTO dto);
    AdminDTO toAdminDTO(UserRequest userDTO);

    UserResponse toResponse(UserEntity entity);

    default UserRequest toDTO(UserEntity entity) {
        if (entity instanceof ParentEntity) {
            return toParentDTO((ParentEntity) entity);
        } else if (entity instanceof BabysitterEntity) {
            return toBabysitterDTO((BabysitterEntity) entity);
        } else if (entity instanceof AdminEntity) {
            return toAdminDTO((AdminEntity) entity);
        }
        else {
            throw new InvalidRoleException("Unknown entity type!");
        }
    }

    default UserEntity toEntity(UserRequest dto) {
        if (dto instanceof ParentDTO) {
            return toParentEntity((ParentDTO) dto);
        } else if (dto instanceof BabysitterDTO) {
            return toBabysitterEntity((BabysitterDTO) dto);
        } else if (dto instanceof AdminDTO) {
            return toAdminEntity((AdminDTO) dto);
        }
        else {
            throw new InvalidRoleException("Unknown DTO type!");
        }
    }

}




