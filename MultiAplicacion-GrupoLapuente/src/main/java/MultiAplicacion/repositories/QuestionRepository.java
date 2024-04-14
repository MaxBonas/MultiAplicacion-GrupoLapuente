package MultiAplicacion.repositories;

import MultiAplicacion.entities.Question;
import MultiAplicacion.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
