package net.anassploit.conferencesevice;

import net.anassploit.conferencesevice.entities.Conference;
import net.anassploit.conferencesevice.entities.Review;
import net.anassploit.conferencesevice.enums.ConferenceType;
import net.anassploit.conferencesevice.repository.ConferenceRepository;
import net.anassploit.conferencesevice.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class ConferenceSeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferenceSeviceApplication.class, args);
    }

    @Bean
    CommandLineRunner init(ConferenceRepository conferenceRepository, ReviewRepository reviewRepository) {

        return args -> {

            Conference conference = Conference.builder()
                    .title("Conference Sevice")
                    .date(new Date())
                    .type(ConferenceType.ACADEMIC)
                    .time(60)
                    .nbSubscribed(80)
                    .build();
            List<Review> reviews = new ArrayList<>();
            for(int i = 0; i < 10; i++){
                reviews.add(Review.builder()
                                .date(new Date())
                                .score(new Random().nextInt(6))
                                .text("Good Conference")
                                .conference(conference)
                        .build());
            }
          conferenceRepository.save(conference);
            reviewRepository.saveAll(reviews);
        };
    }

}
