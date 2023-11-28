package s3.fontys.babysita.business.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import s3.fontys.babysita.persistence.ParentRepository;
import s3.fontys.babysita.business.mapper.UserMapper;
import s3.fontys.babysita.dto.ParentDTO;
import s3.fontys.babysita.persistence.entity.ParentEntity;
import s3.fontys.babysita.business.exception.InvalidIdException;
import static org.mockito.ArgumentMatchers.any;

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
    void getAllParents_ReturnsMapOfParentDTOs() {
        ParentEntity parent1 = new ParentEntity();
        parent1.setId(1);
        ParentEntity parent2 = new ParentEntity();
        parent2.setId(2);

        List<ParentEntity> parentList = Arrays.asList(parent1, parent2);
        when(parentRepository.findAll()).thenReturn(parentList);

        when(userMapper.toParentDTO(any(ParentEntity.class))).thenAnswer(invocation -> {
            ParentEntity entity = invocation.getArgument(0);
            ParentDTO dto = new ParentDTO();
            dto.setId(entity.getId());
            return dto;
        });

        Map<Integer, ParentDTO> result = parentService.getAllParents();

        assertNotNull(result, "Resulting map should not be null");
        assertEquals(parentList.size(), result.size(), "Resulting map should have the same size as the parent list");
        assertTrue(result.keySet().containsAll(Arrays.asList(1, 2)), "Resulting map should contain all parent IDs");

        verify(parentRepository).findAll();
    }
}
