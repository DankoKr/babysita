package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.domain.BabysitterResponse;
import s3.fontys.babysita.persistence.BabysitterRepository;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.business.mapper.UserMapper;

import java.util.Arrays;
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
     void testGetAvailableBabysitters() {
        BabysitterEntity babysitter1 = new BabysitterEntity();
        babysitter1.setId(1);
        BabysitterEntity babysitter2 = new BabysitterEntity();
        babysitter2.setId(2);

        List<BabysitterEntity> babysitters = Arrays.asList(babysitter1, babysitter2);

        when(babysitterRepository.findByIsAvailableTrue()).thenReturn(babysitters);
        when(userMapper.toBabysitterResponse(babysitter1)).thenReturn(new BabysitterResponse("female", 22, true));
        when(userMapper.toBabysitterResponse(babysitter2)).thenReturn(new BabysitterResponse("male", 19, true));

        Map<Integer, BabysitterResponse> result = babysitterService.getAvailableBabysitters();

        assertEquals(2, result.size());
        verify(babysitterRepository, times(1)).findByIsAvailableTrue();
        verify(userMapper, times(1)).toBabysitterResponse(babysitter1);
        verify(userMapper, times(1)).toBabysitterResponse(babysitter2);
    }

    @Test
     void whenUpdateBabysitterPoints_thenPointsAreUpdated() {
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
