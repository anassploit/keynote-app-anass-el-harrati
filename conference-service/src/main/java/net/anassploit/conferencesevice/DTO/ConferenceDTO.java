package net.anassploit.conferencesevice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.anassploit.conferencesevice.enums.ConferenceType;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceDTO {
    private Long id;
    private String title;
    private ConferenceType type;
    private Date date;
    private int time;
    private int nbSubscribed;
    private float score;
    private List<KeynoteDTO> keynotes;
}

