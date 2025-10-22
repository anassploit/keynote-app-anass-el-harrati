package net.anassploit.conferencesevice.repository;

import net.anassploit.conferencesevice.entities.Conference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConferenceRepository extends JpaRepository<Conference, Long> {
}
