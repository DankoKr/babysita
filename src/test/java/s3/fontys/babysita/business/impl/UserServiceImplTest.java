package s3.fontys.babysita.business.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import s3.fontys.babysita.domain.Babysitter;
import s3.fontys.babysita.domain.Parent;
import s3.fontys.babysita.domain.User;
import s3.fontys.babysita.persistence.UserRepository;



import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserServiceImplTest {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImplTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Test
    public void createParentAndBabysitterTest(){
        //Arrange
        User parent = new Parent(1,"parent", "password", "ex@email.com",
                "John", "Doe", "image", "phone",
        "address", "parent", 33);
        User babysitter = new Babysitter(2,"babysitter", "password", "ex@email.com",
                "Janny", "Doe", "image", "phone",
                "address", "babysitter", 33, "female");

        //Act
        userRepository.save(parent);
        userRepository.save(babysitter);

        //Assert
        int usersCount = userRepository.getAll().size();

        assertTrue(usersCount > 0);
    }

    @Test
    public void deleteUserTest(){
        User babysitter = new Babysitter(2,"babysitter", "password", "ex@email.com",
                "Janny", "Doe", "image", "phone",
                "address", "babysitter", 33, "female");
        userRepository.save(babysitter);

        userRepository.deleteById(2);
        User deletedUser = userRepository.getById(2);
        assertNull(deletedUser);
    }

    @Test
    public void checkIfUsernameExistsTest(){
        User parent1 = new Parent(1,"parent1", "password1", "ex1@email.com",
                "John1", "Doe1", "image1", "phone1",
                "address1", "parent", 33);

        userRepository.save(parent1);
        boolean exists = userRepository.existsByUsername("parent1");

        assertTrue(exists);
    }

}
