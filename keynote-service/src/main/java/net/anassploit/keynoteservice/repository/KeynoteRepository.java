package net.anassploit.keynoteservice.repository;

import net.anassploit.keynoteservice.entities.Keynote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface KeynoteRepository extends JpaRepository<Keynote, Long> {
    // This will create the endpoint: /keynotes/search/findByIdIn?ids=1,2,3
    List<Keynote> findByIdIn(Set<Long> ids);
}
