package net.anassploit.conferencesevice.services;

import lombok.RequiredArgsConstructor;
import net.anassploit.conferencesevice.DTO.ConferenceDTO;
import net.anassploit.conferencesevice.DTO.KeynoteDTO;
import net.anassploit.conferencesevice.entities.Conference;
import net.anassploit.conferencesevice.entities.ConferenceKeynote;
import net.anassploit.conferencesevice.feign.KeynoteClient;
import net.anassploit.conferencesevice.repository.ConferenceKeynoteRepository;
import net.anassploit.conferencesevice.repository.ConferenceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConferenceService {
    private final ConferenceRepository conferenceRepository;
    private final ConferenceKeynoteRepository conferenceKeynoteRepository;
    private final KeynoteClient keynoteClient;

    public ConferenceDTO getConferenceWithKeynotes(Long id) {
        Conference conf = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
        List<ConferenceKeynote> links = conferenceKeynoteRepository.findByConferenceId(id);

        List<KeynoteDTO> keynotes = links.stream()
                .map(link -> keynoteClient.getKeynoteById(link.getKeynoteId()))
                .toList();

        return ConferenceDTO.builder()
                .id(conf.getId())
                .title(conf.getTitle())
                .type(conf.getType())
                .date(conf.getDate())
                .time(conf.getTime())
                .nbSubscribed(conf.getNbSubscribed())
                .score(conf.getScore())
                .keynotes(keynotes)
                .build();
    }
}

