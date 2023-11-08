package s3.fontys.babysita.business.mapper;

import org.mapstruct.Mapper;
import s3.fontys.babysita.dto.JobApplicationDTO;
import s3.fontys.babysita.persistence.entity.JobApplicationEntity;

@Mapper(componentModel = "spring")
public interface JobApplicationMapper {
    JobApplicationDTO toDTO(JobApplicationEntity jobApplicationEntity);
    JobApplicationEntity toEntity(JobApplicationDTO jobApplicationDTO);
}
