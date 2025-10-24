package net.anassploit.conferencesevice;

import net.anassploit.conferencesevice.entities.Conference;
import net.anassploit.conferencesevice.entities.ConferenceKeynote;
import net.anassploit.conferencesevice.entities.Review;
import net.anassploit.conferencesevice.enums.ConferenceType;
import net.anassploit.conferencesevice.repository.ConferenceKeynoteRepository;
import net.anassploit.conferencesevice.repository.ConferenceRepository;
import net.anassploit.conferencesevice.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.*;

@SpringBootApplication
@EnableFeignClients
public class ConferenceSeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConferenceSeviceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(
            ConferenceRepository conferenceRepository,
            ReviewRepository reviewRepository,
            ConferenceKeynoteRepository conferenceKeynoteRepository
    ) {
        return args -> {
            if (conferenceRepository.count() > 0) {
                System.out.println("✅ Sample data already exists. Skipping initialization...");
                return;
            }

            Random random = new Random();

            // 1️⃣ Create some sample conferences
            List<Conference> conferences = Arrays.asList(
                    Conference.builder()
                            .title("AI & Cybersecurity Summit")
                            .type(ConferenceType.ACADEMIC)
                            .date(new Date())
                            .time(90)
                            .nbSubscribed(120)
                            .build(),
                    Conference.builder()
                            .title("Cloud Computing Expo")
                            .type(ConferenceType.ACADEMIC)
                            .date(new Date())
                            .time(60)
                            .nbSubscribed(85)
                            .build(),
                    Conference.builder()
                            .title("Blockchain for Developers")
                            .type(ConferenceType.COMMERCIAL)
                            .date(new Date())
                            .time(75)
                            .nbSubscribed(60)
                            .build()
            );

            conferenceRepository.saveAll(conferences);

            // 2️⃣ Add random reviews for each conference
            conferences.forEach(conf -> {
                for (int i = 0; i < 3; i++) {
                    Review review = Review.builder()
                            .conference(conf)
                            .score(1 + random.nextInt(5)) // score between 1 and 5
                            .text("Review " + (i + 1) + " for " + conf.getTitle())
                            .build();
                    reviewRepository.save(review);
                }
            });

            // 3️⃣ Link keynotes with IDs 1, 2, and 3 from keynote-service
            conferences.forEach(conf -> {
                for (Long keynoteId : List.of(1L, 2L, 3L)) {
                    ConferenceKeynote link = ConferenceKeynote.builder()
                            .conferenceId(conf.getId())
                            .keynoteId(keynoteId)
                            .build();
                    conferenceKeynoteRepository.save(link);
                }
            });
        };
    }
}