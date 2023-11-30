package s3.fontys.babysita.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import s3.fontys.babysita.business.JobApplicationService;
import s3.fontys.babysita.dto.JobApplicationDTO;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class JobApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobApplicationService jobApplicationService;

    @Test
    @WithMockUser(roles = "babysitter")
    void createJobApplicationSuccess() throws Exception {
        JobApplicationDTO jobApplicationDTO = new JobApplicationDTO(1, "description",
                "Pending",1, 2, 3);
        mockMvc.perform(post("/jobApplications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(jobApplicationDTO)))
                .andExpect(status().isNoContent());
    }

    // Utility method to convert object to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(roles = "babysitter")
    void createJobApplicationInvalidData() throws Exception {
        JobApplicationDTO jobApplicationDTO = new JobApplicationDTO(1, "", "pending",
                1, 2, 3 );
        mockMvc.perform(post("/jobApplications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(jobApplicationDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "babysitter")
    void getBabysitterJobApplicationsSuccess() throws Exception {
        BabysitterEntity babysitter = new BabysitterEntity();
        babysitter.setId(1);
        List<JobApplicationDTO> jobs = new ArrayList<>();

        when(jobApplicationService.getBabysitterJobApplications(1)).thenReturn(jobs);

        mockMvc.perform(get("/jobApplications/babysitter/" + babysitter.getId()))
                .andExpect(status().isOk());

        verify(jobApplicationService).getBabysitterJobApplications(1);
    }

    @Test
    @WithMockUser(roles = "parent")
    void getParentJobApplicationsSuccess() throws Exception {
        ParentEntity parent = new ParentEntity();
        parent.setId(1);
        List<JobApplicationDTO> jobs = new ArrayList<>();

        when(jobApplicationService.getParentJobApplications(1)).thenReturn(jobs);

        mockMvc.perform(get("/jobApplications/parent/" + parent.getId()))
                .andExpect(status().isOk());

        verify(jobApplicationService).getParentJobApplications(1);
    }

    @Test
    @WithMockUser(roles = "parent")
    void deleteJobApplicationSuccess() throws Exception {
        int jobApplicationId = 1;
        mockMvc.perform(delete("/jobApplications/" + jobApplicationId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "parent")
    void editJobApplicationStatusSuccess() throws Exception {
        int id = 1;
        String newStatus = "Approved";
        mockMvc.perform(patch("/jobApplications/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newStatus))
                .andExpect(status().isNoContent());
    }

}