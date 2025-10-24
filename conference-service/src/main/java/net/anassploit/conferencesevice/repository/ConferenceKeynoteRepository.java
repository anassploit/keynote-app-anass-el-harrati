package net.anassploit.conferencesevice.repository;

import net.anassploit.conferencesevice.entities.ConferenceKeynote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ConferenceKeynoteRepository extends JpaRepository<ConferenceKeynote, Long> {
    List<ConferenceKeynote> findByConferenceId(Long conferenceId);
}

