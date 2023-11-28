package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.dto.BabysitterDTO;
import s3.fontys.babysita.persistence.BabysitterRepository;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.business.mapper.UserMapper;

import java.util.Optional;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BabysitterServiceImplTest {

    @Mock
    private BabysitterRepository babysitterRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BabysitterServiceImpl babysitterService;

    @Test
    void getBabysitter_existingId_returnsBabysitter() {
        int babysitterId = 1;
        BabysitterEntity expectedBabysitter = new BabysitterEntity();
        when(babysitterRepository.findById(babysitterId)).thenReturn(Optional.of(expectedBabysitter));

        BabysitterEntity actualBabysitter = babysitterService.getBabysitter(babysitterId);

        assertEquals(expectedBabysitter, actualBabysitter);
        verify(babysitterRepository).findById(babysitterId);
    }

    @Test
    void getBabysitter_nonExistingId_throwsInvalidIdException() {
        int babysitterId = -1;
        when(babysitterRepository.findById(babysitterId)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> babysitterService.getBabysitter(babysitterId));
        verify(babysitterRepository).findById(babysitterId);
    }

    @Test
    void getAllBabysitters_returnsBabysitterMap() {
        BabysitterEntity babysitterEntity = new BabysitterEntity();
        babysitterEntity.setId(1);
        BabysitterDTO babysitterDTO = new BabysitterDTO("female", 22, true);
        List<BabysitterEntity> babysitterEntities = List.of(babysitterEntity);

        when(babysitterRepository.findByIsAvailableTrue()).thenReturn(babysitterEntities);
        when(userMapper.toBabysitterDTO(any(BabysitterEntity.class))).thenReturn(babysitterDTO);

        Map<Integer, BabysitterDTO> babysitterMap = babysitterService.getAvailableBabysitters();

        assertFalse(babysitterMap.isEmpty());
        assertEquals(babysitterDTO, babysitterMap.get(babysitterEntity.getId()));
        verify(babysitterRepository).findByIsAvailableTrue();
        verify(userMapper).toBabysitterDTO(babysitterEntity);
    }

    @Test
    public void whenUpdateBabysitterPoints_thenPointsAreUpdated() {
        int babysitterId = 1;
        BabysitterEntity babysitter = new BabysitterEntity();
        babysitter.setId(babysitterId);
        babysitter.setPoints(20);

        when(babysitterRepository.findById(babysitterId)).thenReturn(Optional.of(babysitter));

        babysitterService.updateBabysitterPoints(babysitterId);

        assertEquals(30, babysitter.getPoints());
        verify(babysitterRepository).save(babysitter);
    }
}
