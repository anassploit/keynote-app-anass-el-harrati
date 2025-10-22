package net.anassploit.conferencesevice.repository;

import net.anassploit.conferencesevice.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,Long> {
}
