package mdp.tirexchageservice.respositories;

import mdp.tirexchageservice.models.TirMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TirMessageRepository extends JpaRepository<TirMessage, Long> {
}
