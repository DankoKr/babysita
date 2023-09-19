package s3.fontys.babysita.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import s3.fontys.babysita.domain.Babysitter;
import s3.fontys.babysita.domain.Parent;
import s3.fontys.babysita.domain.Poster;
import s3.fontys.babysita.persistence.PosterRepository;
import s3.fontys.babysita.persistence.UserRepository;

import java.time.LocalDate;

@Component
@AllArgsConstructor
public class FakeDatabaseDataInitializer {
    private PosterRepository posterRepository;
    private UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void populateDatabaseInitialDummyData() {
        if (posterRepository.count() == 0) {
            posterRepository.save(new Poster(0, "title1", "desc1", "image1", LocalDate.now()));
            posterRepository.save(new Poster(0, "title2", "desc2", "image2", LocalDate.now()));
            posterRepository.save(new Poster(0, "title3", "desc3", "image3", LocalDate.now()));
        }

        if (userRepository.count() == 0) {
            userRepository.save(new Parent(0, "parent1", "password1", "dummy1@email.com",
                    "first1", "last1", "pic1", "phone1", "address1", "parent", 34));
            userRepository.save(new Parent(0, "parent2", "password2", "dummy2@email.com",
                    "first2", "last2", "pic2", "phone2", "address2", "parent", 37));
            userRepository.save(new Babysitter(0, "babysitter1", "password1", "dummy3@email.com",
                    "first1", "last1", "pic1", "phone1", "address3", "babysitter",22, "male"));
            userRepository.save(new Babysitter(0, "babysitter2", "password2", "dummy4@email.com",
                    "first2", "last2", "pic2", "phone2", "address4", "babysitter",20, "female"));
        }
    }
}
