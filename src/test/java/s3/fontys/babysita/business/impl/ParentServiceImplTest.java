package s3.fontys.babysita.business.impl;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.fontys.babysita.domain.ParentResponse;
import s3.fontys.babysita.persistence.ParentRepository;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.business.exception.InvalidIdException;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ParentServiceImplTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ParentServiceImpl parentService;

    @Test
    void getParent_WhenParentExists_ReturnsParent() {
        int parentId = 1;
        ParentEntity mockParent = new ParentEntity();
        mockParent.setId(parentId);
        when(parentRepository.findById(parentId)).thenReturn(Optional.of(mockParent));

        ParentEntity result = parentService.getParent(parentId);

        assertNotNull(result, "Parent should not be null");
        assertEquals(parentId, result.getId(), "Parent ID should match the requested ID");
        verify(parentRepository).findById(parentId);
    }

    @Test
    void getParent_WhenParentDoesNotExist_ThrowsInvalidIdException() {
        int parentId = 1;
        when(parentRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(InvalidIdException.class, () -> parentService.getParent(parentId),
                "Should throw InvalidIdException when parent not found");
        verify(parentRepository).findById(anyInt());
    }

    @Test
    public void testGetAllParents() {
        // Arrange
        ParentEntity parent1 = new ParentEntity();
        parent1.setId(1);
        ParentEntity parent2 = new ParentEntity();
        parent2.setId(2);

        List<ParentEntity> parents = Arrays.asList(parent1, parent2);

        when(parentRepository.findAll()).thenReturn(parents);
        when(userMapper.toParentResponse(parent1)).thenReturn(new ParentResponse());
        when(userMapper.toParentResponse(parent2)).thenReturn(new ParentResponse());

        // Act
        Map<Integer, ParentResponse> result = parentService.getAllParents();

        // Assert
        assertEquals(2, result.size());
        verify(parentRepository, times(1)).findAll();
        verify(userMapper, times(1)).toParentResponse(parent1);
        verify(userMapper, times(1)).toParentResponse(parent2);
    }
}
