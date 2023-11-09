package s3.fontys.babysita.business.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.fontys.babysita.business.BabysitterService;
import s3.fontys.babysita.business.ParentService;
import s3.fontys.babysita.business.mapper.PosterMapper;
import s3.fontys.babysita.configuration.security.token.AccessToken;
import s3.fontys.babysita.dto.PosterDTO;
import s3.fontys.babysita.persistence.PosterRepository;
import s3.fontys.babysita.persistence.entity.BabysitterEntity;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.persistence.entity.PosterEntity;
import s3.fontys.babysita.business.exception.InvalidIdException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PosterServiceImplTest {

    @Mock
    private PosterRepository posterRepository;

    @Mock
    private PosterMapper posterMapper;

    @Mock
    private ParentService parentService;

    @Mock
    private BabysitterService babysitterService;

    @Mock
    private AccessToken requestAccessToken;

    @InjectMocks
    private PosterServiceImpl posterService;

    @Test
    void createPoster_Success() {
        PosterDTO posterDTO = mock(PosterDTO.class);
        PosterEntity posterEntity = mock(PosterEntity.class);
        ParentEntity parentEntity = mock(ParentEntity.class);
        int parentId = 1;

        when(posterDTO.getParentId()).thenReturn(parentId);
        when(posterMapper.toEntity(posterDTO)).thenReturn(posterEntity);
        when(parentService.getParent(parentId)).thenReturn(parentEntity);

        posterService.createPoster(posterDTO);

        verify(posterMapper).toEntity(posterDTO);
        verify(parentService).getParent(parentId);
        verify(posterRepository).save(posterEntity);
    }

    @Test
    void deletePoster_InvalidId_ThrowsException() {
        int posterId = 1;
        when(posterRepository.existsById(posterId)).thenReturn(false);

        assertThatThrownBy(() -> posterService.deletePoster(posterId))
                .isInstanceOf(InvalidIdException.class)
                .hasMessageContaining("Invalid ID.");
    }

    @Test
    void getPoster_Success() {
        int posterId = 1;
        PosterEntity posterEntity = mock(PosterEntity.class);
        ParentEntity parentEntity = mock(ParentEntity.class);
        PosterDTO posterDTO = mock(PosterDTO.class);

        when(posterRepository.findById(posterId)).thenReturn(java.util.Optional.of(posterEntity));
        when(posterEntity.getParent()).thenReturn(parentEntity);
        when(parentEntity.getId()).thenReturn(1);
        when(posterMapper.toDTO(posterEntity)).thenReturn(posterDTO);

        PosterDTO resultDTO = posterService.getPoster(posterId);

        assertThat(resultDTO).isEqualTo(posterDTO);
        verify(posterRepository).findById(posterId);
        verify(posterMapper).toDTO(posterEntity);
    }

    @Test
    public void getPoster_WithInvalidId() {
        when(posterRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> posterService.getPoster(1));
    }

    @Test
    void editPoster_Success() {
        int posterId = 1;
        PosterDTO updatedPosterDTO = new PosterDTO();
        updatedPosterDTO.setTitle("New Title");
        updatedPosterDTO.setDescription("New Description");
        PosterEntity existingEntity = new PosterEntity();
        existingEntity.setId(posterId);

        given(posterRepository.existsById(posterId)).willReturn(true);
        given(posterRepository.findById(posterId)).willReturn(Optional.of(existingEntity));

        posterService.editPoster(posterId, updatedPosterDTO);

        verify(posterRepository).save(existingEntity);
        assertThat(existingEntity.getTitle()).isEqualTo(updatedPosterDTO.getTitle());
        assertThat(existingEntity.getDescription()).isEqualTo(updatedPosterDTO.getDescription());
    }
    @Test
    public void patchPoster_WithValidId() {
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
    void editPoster_InvalidId_ThrowsException() {
        int posterId = 1;
        PosterDTO updatedPosterDTO = new PosterDTO();

        given(posterRepository.existsById(posterId)).willReturn(false);

        assertThatThrownBy(() -> posterService.editPoster(posterId, updatedPosterDTO))
                .isInstanceOf(InvalidIdException.class)
                .hasMessageContaining("Invalid ID.");
    }

    @Test
    void assignPosterToBabysitter_Success() {
        int posterId = 1;
        int babysitterId = 2;
        PosterEntity poster = new PosterEntity();
        BabysitterEntity babysitter = new BabysitterEntity();

        given(posterRepository.findById(posterId)).willReturn(Optional.of(poster));
        given(babysitterService.getBabysitter(babysitterId)).willReturn(babysitter);

        posterService.assignPosterToBabysitter(posterId, babysitterId);

        verify(posterRepository).save(poster);
        assertThat(poster.getBabysitter()).isEqualTo(babysitter);
    }

    @Test
    void getUserPosters_Success_ForParentRole() {
        int userId = 1;
        List<PosterEntity> posters = new ArrayList<>();
        ParentEntity parent = new ParentEntity();
        posters.add(new PosterEntity());

        given(requestAccessToken.getRole()).willReturn("parent");
        given(requestAccessToken.getUserId()).willReturn(userId);
        given(parentService.getParent(userId)).willReturn(parent);
        given(posterRepository.findByParent(parent)).willReturn(posters);
        given(posterMapper.toDTO(any(PosterEntity.class))).willReturn(new PosterDTO());

        List<PosterDTO> resultPosters = posterService.getUserPosters(userId);

        assertThat(resultPosters).isNotEmpty();
        verify(posterRepository).findByParent(parent);
    }
}


