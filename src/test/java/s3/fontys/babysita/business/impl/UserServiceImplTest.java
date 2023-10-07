package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import s3.fontys.babysita.domain.Babysitter;
import s3.fontys.babysita.domain.Parent;
import s3.fontys.babysita.domain.User;
import s3.fontys.babysita.persistence.UserRepository;


public class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);

    @Test
    public void createParentAndBabysitterTest(){
        // Arrange
        User parent = new Parent(1,"parent", "password", "ex@email.com",
                "John", "Doe", "image", "phone",
                "address", "parent", 33);
        User babysitter = new Babysitter(2,"babysitter", "password", "ex@email.com",
                "Janny", "Doe", "image", "phone",
                "address", "babysitter", 33, "female");

        // Act
        userRepository.save(parent);
        userRepository.save(babysitter);

        // Assert
        verify(userRepository, times(1)).save(parent);
        verify(userRepository, times(1)).save(babysitter);
    }

    @Test
    public void deleteUserTest(){
        User babysitter = new Babysitter(2,"babysitter", "password", "ex@email.com",
                "Janny", "Doe", "image", "phone",
                "address", "babysitter", 33, "female");

        when(userRepository.getById(2)).thenReturn(null);

        userRepository.deleteById(2);
        User deletedUser = userRepository.getById(2);

        assertNull(deletedUser);
    }

    @Test
    public void checkIfUsernameExistsTest(){
        User parent1 = new Parent(1,"parent1", "password1", "ex1@email.com",
                "John1", "Doe1", "image1", "phone1",
                "address1", "parent", 33);

        when(userRepository.existsByUsername("parent1")).thenReturn(true);

        boolean exists = userRepository.existsByUsername("parent1");

        assertTrue(exists);
    }

}

