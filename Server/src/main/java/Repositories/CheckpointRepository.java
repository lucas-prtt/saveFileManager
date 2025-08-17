package Repositories;

import Juegos.Checkpoint;
import jakarta.persistence.Id;
import org.hibernate.annotations.Check;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckpointRepository extends JpaRepository<Checkpoint, String> {
}
