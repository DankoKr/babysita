package s3.fontys.babysita.business.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import s3.fontys.babysita.business.ParentService;
import s3.fontys.babysita.business.mapper.PosterMapper;
import s3.fontys.babysita.dto.PosterDTO;
import s3.fontys.babysita.persistence.PosterRepository;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.PosterEntity;
import s3.fontys.babysita.business.exception.InvalidIdException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

public class PosterServiceImplTest {

    @Mock
    private PosterRepository posterRepository;

    @Mock
    private PosterMapper posterMapper;

    @Mock
    private ParentService parentService;

    @InjectMocks
    private PosterServiceImpl posterService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createPosterTest() {
        PosterDTO posterDTO = new PosterDTO();
        posterDTO.setParentId(1);

        PosterEntity posterEntity = new PosterEntity();
        ParentEntity parentEntity = new ParentEntity();

        when(posterMapper.toEntity(posterDTO)).thenReturn(posterEntity);
        when(parentService.getParent(1)).thenReturn(parentEntity);

        posterService.createPoster(posterDTO);

        verify(posterRepository, times(1)).save(posterEntity);
    }

    @Test
    public void getPosterWithValidIdTest() {
        PosterEntity posterEntity = new PosterEntity();
        PosterDTO posterDTO = new PosterDTO();
        ParentEntity parentEntity = new ParentEntity();
        parentEntity.setId(1);

        posterEntity.setParent(parentEntity);

        when(posterRepository.findById(1)).thenReturn(Optional.of(posterEntity));
        when(posterMapper.toDTO(posterEntity)).thenReturn(posterDTO);

        PosterDTO result = posterService.getPoster(1);

        assertEquals(1, result.getParentId());
    }

    @Test
    public void getPosterWithInvalidIdTest() {
        when(posterRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> posterService.getPoster(1));
    }

    @Test
    public void editPosterWithInvalidIdTest() {
        when(posterRepository.existsById(1)).thenReturn(false);

        assertThrows(InvalidIdException.class, () -> posterService.editPoster(1, new PosterDTO()));
    }

    @Test
    public void patchPosterWithValidIdTest() {
        PosterEntity existingEntity = new PosterEntity();
        PosterDTO patchedPosterDTO = new PosterDTO();
        patchedPosterDTO.setTitle("New Title");
        patchedPosterDTO.setDescription("New Description");

        when(posterRepository.findById(1)).thenReturn(Optional.of(existingEntity));

        posterService.patchPoster(1, patchedPosterDTO);

        assertEquals("New Title", existingEntity.getTitle());
        assertEquals("New Description", existingEntity.getDescription());
        verify(posterRepository, times(1)).save(existingEntity);
    }

    @Test
    public void patchPosterWithInvalidIdTest() {
        when(posterRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> posterService.patchPoster(1, new PosterDTO()));
    }
}


