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
import s3.fontys.babysita.business.ParentService;
import s3.fontys.babysita.dto.ParentDTO;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ParentControllerTest {
    @MockBean
    private ParentService parentService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(roles = "admin")
    public void getAllParentsTest() throws Exception {
        Map<Integer, ParentDTO> parents = new HashMap<>();
        ParentDTO parent = new ParentDTO();
        parent.setId(1);
        parents.put(1, parent);

        when(parentService.getAllParents()).thenReturn(parents);

        mockMvc.perform(get("/parents")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(parentService).getAllParents();
    }

}