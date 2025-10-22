package net.anassploit.keynoteservice.repository;

import net.anassploit.keynoteservice.entities.Keynote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeynoteRepository extends JpaRepository<Keynote, String> {

}
