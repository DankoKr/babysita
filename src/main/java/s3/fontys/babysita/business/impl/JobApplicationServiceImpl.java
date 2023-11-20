package s3.fontys.babysita.business.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import s3.fontys.babysita.business.BabysitterService;
import s3.fontys.babysita.business.JobApplicationService;
import s3.fontys.babysita.business.ParentService;
import s3.fontys.babysita.business.PosterService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.exception.UnauthorizedDataAccessException;
import s3.fontys.babysita.business.mapper.JobApplicationMapper;
import s3.fontys.babysita.configuration.security.token.AccessToken;
import s3.fontys.babysita.dto.JobApplicationDTO;
import s3.fontys.babysita.persistence.JobApplicationRepository;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.JobApplicationEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.PosterEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class JobApplicationServiceImpl implements JobApplicationService {
    private final JobApplicationRepository jobApplicationRepository;
    private final JobApplicationMapper jobApplicationMapper;
    private final PosterService posterService;
    private final BabysitterService babysitterService;
    private final ParentService parentService;
    private final AccessToken accessToken;

    @Override
    public void createJobApplication(JobApplicationDTO jobApplicationDTO) {
        JobApplicationEntity jobApplicationEntity = jobApplicationMapper.toEntity(jobApplicationDTO);
        BabysitterEntity babysitterEntity = babysitterService.getBabysitter(jobApplicationDTO.getBabysitterId());
        jobApplicationEntity.setBabysitter(babysitterEntity);
        PosterEntity posterEntity = posterService.getPosterEntity(jobApplicationDTO.getPosterId());
        jobApplicationEntity.setPoster(posterEntity);
        ParentEntity parent = parentService.getParent(jobApplicationDTO.getParentId());
        jobApplicationEntity.setParent(parent);
        jobApplicationRepository.save(jobApplicationEntity);
    }

    @Override
    public void deleteJobApplication(int jobApplicationId) {
        if(jobApplicationRepository.existsById(jobApplicationId)){
            jobApplicationRepository.deleteById(jobApplicationId);
        }
        else {throw new InvalidIdException("Invalid ID.");}
    }

    @Override
    public void editJobApplicationStatus(int jobApplicationId, String newStatus) {
        JobApplicationEntity jobApplication = jobApplicationRepository.findById(jobApplicationId)
                .orElseThrow(() -> new InvalidIdException("Poster with ID " + jobApplicationId + " not found"));

        jobApplication.setStatus(newStatus);
        jobApplicationRepository.save(jobApplication);
    }

    @Override
    public List<JobApplicationDTO> getBabysitterJobApplications(int userId) {
        if (accessToken.getUserId() != userId) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        BabysitterEntity babysitter = babysitterService.getBabysitter(userId);
        List<JobApplicationEntity> jobApplications = jobApplicationRepository.findByBabysitter(babysitter);

        return jobApplications.stream()
                .map(jobApplication -> {
                    JobApplicationDTO dto = jobApplicationMapper.toDTO(jobApplication);
                    dto.setBabysitterId(babysitter.getId());
                    dto.setParentId(jobApplication.getParent().getId());
                    dto.setPosterId(jobApplication.getPoster().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplicationDTO> getParentJobApplications(int userId) {
        if (accessToken.getUserId() != userId) {
            throw new UnauthorizedDataAccessException("USER_ID_NOT_FROM_LOGGED_IN_USER");
        }

        ParentEntity parent = parentService.getParent(userId);
        List<JobApplicationEntity> jobApplications = jobApplicationRepository.findByParent(parent);

        return jobApplications.stream()
                .map(jobApplication -> {
                    JobApplicationDTO dto = jobApplicationMapper.toDTO(jobApplication);
                    dto.setBabysitterId(jobApplication.getBabysitter().getId());
                    dto.setParentId(parent.getId());
                    dto.setPosterId(jobApplication.getPoster().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

}
