package net.anassploit.conferencesevice.entities;

import jakarta.persistence.*;
import lombok.*;
import net.anassploit.conferencesevice.DTO.KeynoteDTO;
import net.anassploit.conferencesevice.enums.ConferenceType;

import java.util.*;

@Entity
@AllArgsConstructor @NoArgsConstructor @Getter @Setter @Builder
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    @Enumerated(EnumType.STRING)
    ConferenceType type;
    Date date;
    int time;
    int nbSubscribed;
    @Transient
    public float getScore() {
        if (this.reviews == null || this.reviews.isEmpty()) {
            return 0.0f;
        }

        return (float) this.reviews.stream()
                .mapToInt(Review::getScore)
                .average()
                .orElse(0.0);
    }
    @OneToMany(mappedBy = "conference")
    List<Review>  reviews = new ArrayList<>();

    @Transient
    List<KeynoteDTO> keynotes;

}
