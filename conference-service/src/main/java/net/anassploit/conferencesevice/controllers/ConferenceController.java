package net.anassploit.conferencesevice.controllers;

import lombok.RequiredArgsConstructor;
import net.anassploit.conferencesevice.DTO.KeynoteDTO;
import net.anassploit.conferencesevice.entities.ConferenceKeynote;
import net.anassploit.conferencesevice.repository.ConferenceKeynoteRepository;
import net.anassploit.conferencesevice.services.ConferenceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api2/conferences")
public class ConferenceController {
    private final ConferenceService conferenceService;
    private final ConferenceKeynoteRepository conferenceKeynoteRepository;

    @PostMapping("/{conferenceId}/keynotes/{keynoteId}")
    public ConferenceKeynote addKeynoteToConference(
            @PathVariable Long conferenceId,
            @PathVariable Long keynoteId) {

        ConferenceKeynote link = ConferenceKeynote.builder()
                .conferenceId(conferenceId)
                .keynoteId(keynoteId)
                .build();
        return conferenceKeynoteRepository.save(link);
    }

    @GetMapping("/{conferenceId}/keynotes")
    public List<KeynoteDTO> getConferenceKeynotes(@PathVariable Long conferenceId) {
        return conferenceService.getConferenceWithKeynotes(conferenceId).getKeynotes();
    }
}

