package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.fontys.babysita.business.BabysitterService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobApplicationServiceImplTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private JobApplicationMapper jobApplicationMapper;

    @Mock
    private PosterService posterService;

    @Mock
    private BabysitterService babysitterService;

    @Mock
    private ParentService parentService;

    @Mock
    private AccessToken accessToken;

    @InjectMocks
    private JobApplicationServiceImpl jobApplicationService;

    @Test
    void createJobApplication_SuccessfullyCreates() {
        JobApplicationDTO dto = new JobApplicationDTO();
        JobApplicationEntity entity = new JobApplicationEntity();
        BabysitterEntity babysitter = new BabysitterEntity();
        PosterEntity poster = new PosterEntity();
        ParentEntity parent = new ParentEntity();

        when(jobApplicationMapper.toEntity(dto)).thenReturn(entity);
        when(babysitterService.getBabysitter(dto.getBabysitterId())).thenReturn(babysitter);
        when(posterService.getPosterEntity(dto.getPosterId())).thenReturn(poster);
        when(parentService.getParent(dto.getParentId())).thenReturn(parent);

        jobApplicationService.createJobApplication(dto);

        verify(jobApplicationRepository).save(entity);
        verify(babysitterService).getBabysitter(dto.getBabysitterId());
        verify(posterService).getPosterEntity(dto.getPosterId());
        verify(parentService).getParent(dto.getParentId());
    }

    @Test
    void deleteJobApplication_WithValidId_DeletesJobApplication() {
        int jobApplicationId = 1;
        when(jobApplicationRepository.existsById(jobApplicationId)).thenReturn(true);

        jobApplicationService.deleteJobApplication(jobApplicationId);

        verify(jobApplicationRepository).deleteById(jobApplicationId);
        verify(jobApplicationRepository).existsById(jobApplicationId);
    }

    @Test
    void deleteJobApplication_WithInvalidId_ThrowsException() {
        int jobApplicationId = 1;
        when(jobApplicationRepository.existsById(jobApplicationId)).thenReturn(false);

        assertThrows(InvalidIdException.class, () -> jobApplicationService.deleteJobApplication(jobApplicationId));
        verify(jobApplicationRepository).existsById(jobApplicationId);
    }

    @Test
    void editJobApplicationStatus_WithValidId_UpdatesStatus() {
        int jobApplicationId = 1;
        String newStatus = "ACCEPTED";
        JobApplicationEntity jobApplication = new JobApplicationEntity();
        jobApplication.setId(jobApplicationId);
        when(jobApplicationRepository.findById(jobApplicationId)).thenReturn(Optional.of(jobApplication));

        jobApplicationService.editJobApplicationStatus(jobApplicationId, newStatus);

        verify(jobApplicationRepository).save(jobApplication);
        assertEquals(newStatus, jobApplication.getStatus());
        verify(jobApplicationRepository).findById(jobApplicationId);
    }

    @Test
    void editJobApplicationStatus_WithInvalidId_ThrowsException() {
        when(jobApplicationRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> jobApplicationService.editJobApplicationStatus(1, "REJECTED"));
        verify(jobApplicationRepository).findById(anyInt());
    }
    
    @Test
    void getBabysitterJobApplications_WithInvalidUserId_ThrowsUnauthorizedDataAccessException() {
        when(accessToken.getUserId()).thenReturn(2);

        assertThrows(UnauthorizedDataAccessException.class, () -> jobApplicationService.getBabysitterJobApplications(1));
        verify(accessToken).getUserId();
    }

    @Test
    void getParentJobApplications_WithValidUserId_ReturnsJobApplications() {
        int userId = 1;
        when(accessToken.getUserId()).thenReturn(userId);

        ParentEntity parent = new ParentEntity();
        parent.setId(userId);
        BabysitterEntity babysitter = new BabysitterEntity();
        babysitter.setId(2);
        PosterEntity poster = new PosterEntity();
        poster.setId(1);

        List<JobApplicationEntity> jobApplicationEntities = new ArrayList<>();
        JobApplicationEntity jobApplication1 = new JobApplicationEntity();
        jobApplication1.setId(10);
        jobApplication1.setParent(parent);
        jobApplication1.setBabysitter(babysitter);
        jobApplication1.setPoster(poster);
        jobApplicationEntities.add(jobApplication1);

        JobApplicationEntity jobApplication2 = new JobApplicationEntity();
        jobApplication2.setId(20);
        jobApplication2.setParent(parent);
        jobApplication2.setBabysitter(babysitter);
        jobApplication2.setPoster(poster);
        jobApplicationEntities.add(jobApplication2);

        when(parentService.getParent(userId)).thenReturn(parent);
        when(jobApplicationRepository.findByParent(parent)).thenReturn(jobApplicationEntities);

        when(jobApplicationMapper.toDTO(any(JobApplicationEntity.class))).thenAnswer(invocation -> {
            JobApplicationEntity entity = invocation.getArgument(0);
            JobApplicationDTO dto = new JobApplicationDTO();
            dto.setId(entity.getId());
            return dto;
        });

        List<JobApplicationDTO> result = jobApplicationService.getParentJobApplications(userId);

        assertNotNull(result, "The result should not be null");
        assertFalse(result.isEmpty(), "The result should not be empty");
        assertEquals(jobApplicationEntities.size(), result.size(), "The size of the result list should match the number of job applications");
        verify(parentService).getParent(userId);
        verify(jobApplicationRepository).findByParent(parent);
        verify(jobApplicationMapper, times(jobApplicationEntities.size())).toDTO(any(JobApplicationEntity.class));
    }

    @Test
    void getParentJobApplications_WithInvalidUserId_ThrowsUnauthorizedDataAccessException() {
        when(accessToken.getUserId()).thenReturn(2);

        assertThrows(UnauthorizedDataAccessException.class, () -> jobApplicationService.getParentJobApplications(1));
        verify(accessToken).getUserId();
    }

    @Test
    public void getBabysitterJobApplications_WithValidUserId_ReturnsJobApplications() {
        int userId = 2;
        when(accessToken.getUserId()).thenReturn(userId);

        ParentEntity parent = new ParentEntity();
        parent.setId(1);
        BabysitterEntity babysitter = new BabysitterEntity();
        babysitter.setId(userId);
        PosterEntity poster = new PosterEntity();
        poster.setId(1);

        List<JobApplicationEntity> jobApplicationEntities = new ArrayList<>();
        JobApplicationEntity jobApplication1 = new JobApplicationEntity();
        jobApplication1.setId(10);
        jobApplication1.setParent(parent);
        jobApplication1.setBabysitter(babysitter);
        jobApplication1.setPoster(poster);
        jobApplicationEntities.add(jobApplication1);

        JobApplicationEntity jobApplication2 = new JobApplicationEntity();
        jobApplication2.setId(20);
        jobApplication2.setParent(parent);
        jobApplication2.setBabysitter(babysitter);
        jobApplication2.setPoster(poster);
        jobApplicationEntities.add(jobApplication2);

        when(babysitterService.getBabysitter(userId)).thenReturn(babysitter);
        when(jobApplicationRepository.findByBabysitter(babysitter)).thenReturn(jobApplicationEntities);

        when(jobApplicationMapper.toDTO(any(JobApplicationEntity.class))).thenAnswer(invocation -> {
            JobApplicationEntity entity = invocation.getArgument(0);
            JobApplicationDTO dto = new JobApplicationDTO();
            dto.setId(entity.getId());
            return dto;
        });

        List<JobApplicationDTO> result = jobApplicationService.getBabysitterJobApplications(userId);

        assertNotNull(result, "The result should not be null");
        assertFalse(result.isEmpty(), "The result should not be empty");
        assertEquals(jobApplicationEntities.size(), result.size(), "The size of the result list should match the number of job applications");

        verify(babysitterService).getBabysitter(userId);
        verify(jobApplicationRepository).findByBabysitter(babysitter);
        verify(jobApplicationMapper, times(jobApplicationEntities.size())).toDTO(any(JobApplicationEntity.class));
    }

}
