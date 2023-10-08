package s3.fontys.babysita.business.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import s3.fontys.babysita.persistence.entity.PosterEntity;
import s3.fontys.babysita.dto.PosterDTO;

@Mapper(componentModel = "spring")
public interface PosterMapper {
    PosterDTO toDTO(PosterEntity posterEntity);
    @Mapping(target = "id", ignore = true)
    PosterEntity toEntityWithoutId(PosterDTO posterDTO);
    PosterEntity toEntity(PosterDTO posterDTO);
}
