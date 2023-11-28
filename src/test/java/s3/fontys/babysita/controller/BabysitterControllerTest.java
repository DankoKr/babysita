package s3.fontys.babysita.controller;

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
import s3.fontys.babysita.business.BabysitterService;
import s3.fontys.babysita.dto.BabysitterDTO;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BabysitterControllerTest {
    @MockBean
    private BabysitterService babysitterService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "admin")
    public void getAllBabysittersTest() throws Exception {
        Map<Integer, BabysitterDTO> babysitters = new HashMap<>();
        BabysitterDTO babysitter = new BabysitterDTO("male", 100, true);
        babysitter.setId(1);
        babysitters.put(1, babysitter);

        when(babysitterService.getAvailableBabysitters()).thenReturn(babysitters);

        mockMvc.perform(get("/babysitters")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(babysitterService).getAvailableBabysitters();
    }

    @Test
    @WithMockUser(roles = "parent")
    void updateBabysitterPointsSuccess() throws Exception {
        int babysitterId = 1;
        mockMvc.perform(patch("/babysitters/" + babysitterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

}