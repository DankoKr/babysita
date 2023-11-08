package s3.fontys.babysita.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s3.fontys.babysita.business.JobApplicationService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.dto.JobApplicationDTO;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/jobApplications")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    @RolesAllowed({"babysitter"})
    @PostMapping()
    public ResponseEntity<Void> createJobApplication(@RequestBody @Valid JobApplicationDTO jobApplicationDTO)
    {
        jobApplicationService.createJobApplication(jobApplicationDTO);
        return ResponseEntity.noContent().build();
    }

    @RolesAllowed({"babysitter"})
    @GetMapping("babysitter/{userId}")
    public ResponseEntity<List<JobApplicationDTO>> getBabysitterJobApplications(@PathVariable("userId") int userId) {
        return ResponseEntity.ok(this.jobApplicationService.getBabysitterJobApplications(userId));
    }

    @RolesAllowed({"parent"})
    @GetMapping("parent/{userId}")
    public ResponseEntity<List<JobApplicationDTO>> getParentJobApplications(@PathVariable("userId") int userId) {
        return ResponseEntity.ok(this.jobApplicationService.getParentJobApplications(userId));
    }

    @RolesAllowed({"parent", "babysitter"})
    @DeleteMapping("{jobApplicationId}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable int jobApplicationId) {
        try{
            jobApplicationService.deleteJobApplication(jobApplicationId);
            return ResponseEntity.noContent().build();
        }
        catch(InvalidIdException ex){
            throw new InvalidIdException("Invalid ID.");
        }
    }

    @RolesAllowed({"parent"})
    @PatchMapping("{jobApplicationId}")
    public ResponseEntity<Void> editJobApplicationStatus(@PathVariable("jobApplicationId") int id,
                                            @RequestBody String newStatus) {
        try {
            jobApplicationService.editJobApplicationStatus(id, newStatus);
            return ResponseEntity.noContent().build();
        } catch (InvalidIdException ex) {
            throw new InvalidIdException("Invalid ID.");
        }
    }

}
