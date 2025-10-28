package mdp.tirexchageservice.respositories;

import mdp.tirexchageservice.entities.TirMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TirMessageRepository extends JpaRepository<TirMessage, Long> {
}
