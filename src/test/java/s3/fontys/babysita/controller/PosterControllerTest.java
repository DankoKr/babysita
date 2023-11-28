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
import s3.fontys.babysita.business.PosterService;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.dto.PosterDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class PosterControllerTest {
    @MockBean
    private PosterService posterService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "admin")
    void getAllPostersSuccess() throws Exception {
        Map<Integer, PosterDTO> posters = new HashMap<>();
        given(posterService.getAllPosters()).willReturn(posters);

        mockMvc.perform(get("/posters"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(posters)));
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
    void getPostersWithoutBabysitterSuccess() throws Exception {
        Map<Integer, PosterDTO> posters = new HashMap<>();
        given(posterService.getPostersWithoutBabysitterId()).willReturn(posters);

        mockMvc.perform(get("/posters/noBabysitter"))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(posters)));
    }

    @Test
    @WithMockUser(roles = "parent")
    void getUserPostersSuccess() throws Exception {
        int userId = 1;
        List<PosterDTO> userPosters = new ArrayList<>();
        given(posterService.getUserPosters(userId)).willReturn(userPosters);

        mockMvc.perform(get("/posters/user/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(userPosters)));
    }


    @Test
    @WithMockUser(roles = "babysitter")
    void getPosterByIdSuccess() throws Exception {
        int id = 1;
        PosterDTO posterDTO = new PosterDTO();
        given(posterService.getPoster(id)).willReturn(posterDTO);

        mockMvc.perform(get("/posters/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(posterDTO)));
    }

    @Test
    @WithMockUser(roles = "admin")
    void getPosterByIdInvalidId() throws Exception {
        int invalidId = -1;
        given(posterService.getPoster(invalidId)).willThrow(new InvalidIdException("Invalid ID."));

        mockMvc.perform(get("/posters/" + invalidId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "admin")
    void deletePosterSuccess() throws Exception {
        int posterId = 1;
        doNothing().when(posterService).deletePoster(posterId);

        mockMvc.perform(delete("/posters/" + posterId))
                .andExpect(status().isNoContent());

        verify(posterService).deletePoster(posterId);
    }

    @Test
    @WithMockUser(roles = "admin")
    void deletePosterInvalidId() throws Exception {
        int invalidPosterId = -1;
        doThrow(new InvalidIdException("Invalid ID.")).when(posterService).deletePoster(invalidPosterId);

        mockMvc.perform(delete("/posters/" + invalidPosterId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "parent")
    void editPosterInvalidId() throws Exception {
        int invalidId = -1;
        PosterDTO updatedPosterDTO = new PosterDTO();
        doThrow(new InvalidIdException("Invalid ID.")).when(posterService).editPoster(eq(invalidId), any(PosterDTO.class));

        mockMvc.perform(put("/posters/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedPosterDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "parent")
    void patchPosterInvalidId() throws Exception {
        int invalidId = -1;
        PosterDTO patchedPosterDTO = new PosterDTO();
        doThrow(new InvalidIdException("Invalid ID.")).when(posterService).patchPoster(eq(invalidId), any(PosterDTO.class));

        mockMvc.perform(patch("/posters/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(patchedPosterDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "parent")
    void assignPosterToBabysitterSuccess() throws Exception {
        int posterId = 1;
        int babysitterId = 1;
        doNothing().when(posterService).assignPosterToBabysitter(posterId, babysitterId);

        mockMvc.perform(patch("/posters/" + posterId + "/assign/" + babysitterId))
                .andExpect(status().isOk());

        verify(posterService).assignPosterToBabysitter(posterId, babysitterId);
    }

    @Test
    @WithMockUser(roles = "parent")
    void assignPosterToBabysitterInvalidId() throws Exception {
        int invalidPosterId = -1;
        int invalidBabysitterId = -2;
        doThrow(new InvalidIdException("Invalid ID.")).when(posterService).assignPosterToBabysitter(invalidPosterId, invalidBabysitterId);

        mockMvc.perform(patch("/posters/" + invalidPosterId + "/assign/" + invalidBabysitterId))
                .andExpect(status().isBadRequest());
    }


}