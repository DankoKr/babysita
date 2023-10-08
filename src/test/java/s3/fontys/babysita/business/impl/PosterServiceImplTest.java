package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import s3.fontys.babysita.business.exception.InvalidIdException;
import s3.fontys.babysita.business.mapper.PosterMapper;
import s3.fontys.babysita.dto.PosterDTO;
import s3.fontys.babysita.persistence.entity.PosterEntity;
import s3.fontys.babysita.persistence.PosterRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PosterServiceImplTest {
    @Mock
    private PosterRepository posterRepository;

    @Mock
    private PosterMapper posterMapper;

    @InjectMocks
    private PosterServiceImpl posterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createPosterTest() {
        PosterDTO posterDTO = new PosterDTO(0, "Title", "Description",
                "ImageUrl", LocalDate.now(), false);
        PosterEntity posterEntity = new PosterEntity(1, "Title", "Description",
                "ImageUrl", LocalDate.now(), false);

        when(posterMapper.toEntityWithoutId(posterDTO)).thenReturn(posterEntity);
        posterService.createPoster(posterDTO);

        verify(posterRepository, times(1)).save(posterEntity);
    }

    @Test
    public void editExistingPosterTest() {
        int posterId = 1;
        PosterDTO updatedPosterDTO = new PosterDTO(posterId, "Updated Title",
                "Updated Description", "Updated ImageUrl", LocalDate.now(),
                true);
        PosterEntity updatedEntity = new PosterEntity(posterId, "Updated Title",
                "Updated Description", "Updated ImageUrl", LocalDate.now(),
                true);

        when(posterRepository.existsById(posterId)).thenReturn(true);
        when(posterMapper.toEntity(updatedPosterDTO)).thenReturn(updatedEntity);

        posterService.editPoster(posterId, updatedPosterDTO);

        verify(posterRepository, times(1)).save(updatedEntity);
    }

    @Test
    public void editNonExistingPosterTest() {
        int posterId = 99;
        PosterDTO updatedPosterDTO = new PosterDTO(posterId, "Updated Title",
                "Updated Description", "Updated ImageUrl",
                LocalDate.now(), true);

        when(posterRepository.existsById(posterId)).thenReturn(false);

        assertThrows(InvalidIdException.class, () -> posterService.editPoster(posterId, updatedPosterDTO));
    }

    @Test
    public void deleteExistingPosterTest() {
        int posterId = 1;

        when(posterRepository.existsById(posterId)).thenReturn(true);

        posterService.deletePoster(posterId);

        verify(posterRepository, times(1)).deleteById(posterId);
    }

    @Test
    public void deleteNonExistingPosterTest() {
        int posterId = 99;

        when(posterRepository.existsById(posterId)).thenReturn(false);

        assertThrows(InvalidIdException.class, () -> posterService.deletePoster(posterId));
    }

    @Test
    public void getExistingPosterTest() {
        int posterId = 1;
        PosterEntity posterEntity = new PosterEntity(posterId, "Title", "Description",
                "ImageUrl", LocalDate.now(), false);
        PosterDTO posterDTO = new PosterDTO(posterId, "Title", "Description",
                "ImageUrl", LocalDate.now(), false);

        when(posterRepository.findById(posterId)).thenReturn(Optional.of(posterEntity));
        when(posterMapper.toDTO(posterEntity)).thenReturn(posterDTO);

        PosterDTO fetchedDTO = posterService.getPoster(posterId);

        assertEquals(posterDTO, fetchedDTO);
    }

    @Test
    public void getNonExistingPosterTest() {
        int posterId = 99;

        when(posterRepository.findById(posterId)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> posterService.getPoster(posterId));
    }

    @Test
    public void patchExistingPosterTest() {
        int posterId = 1;
        PosterDTO patchedPosterDTO = new PosterDTO(posterId, "Patched Title",
                null, null, null, false);
        PosterEntity existingEntity = new PosterEntity(posterId, "Original Title",
                "Original Description", "Original ImageUrl", LocalDate.now(),
                false);

        when(posterRepository.findById(posterId)).thenReturn(Optional.of(existingEntity));

        posterService.patchPoster(posterId, patchedPosterDTO);

        assertEquals("Patched Title", existingEntity.getTitle());
        assertEquals("Original Description", existingEntity.getDescription());
        verify(posterRepository, times(1)).save(existingEntity);
    }

    @Test
    public void patchNonExistingPosterTest() {
        int posterId = 99;
        PosterDTO patchedPosterDTO = new PosterDTO(posterId, "Patched Title", null,
                null, null, false);

        when(posterRepository.findById(posterId)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> posterService.patchPoster(posterId, patchedPosterDTO));
    }
}

