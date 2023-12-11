package s3.fontys.babysita.business;

import s3.fontys.babysita.dto.JobApplicationDTO;

import java.util.List;

public interface JobApplicationService {
    void createJobApplication(JobApplicationDTO jobApplicationDTO);
    void deleteJobApplication(int jobApplicationId);
    void editJobApplicationStatus(int jobApplicationId, String newStatus);
    List<JobApplicationDTO> getBabysitterJobApplications(int userId);
    List<JobApplicationDTO> getParentJobApplications(int userId);
}
