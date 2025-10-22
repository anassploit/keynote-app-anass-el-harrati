package net.anassploit.keynoteservice;

import net.anassploit.keynoteservice.entities.Keynote;
import net.anassploit.keynoteservice.repository.KeynoteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class KeynoteServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KeynoteServiceApplication.class, args);
    }
    @Bean
    CommandLineRunner init(KeynoteRepository keynoteRepository) {
        return args -> {
            keynoteRepository.save(Keynote.builder()
                    .lastName("EL HARRATI")
                    .firstName("Anass")
                    .email("anass@gmail.com")
                    .function("CyberSec student")
                    .build());
            keynoteRepository.save(Keynote.builder()
                    .lastName("EL HARRATI1")
                    .firstName("Anass1")
                    .email("anass1@gmail.com")
                    .function("CyberSec student")
                    .build());
            keynoteRepository.save(Keynote.builder()
                    .lastName("EL HARRATI2")
                    .firstName("Anass2")
                    .email("anass2@gmail.com")
                    .function("CyberSec student")
                    .build());
        };
    }

}
