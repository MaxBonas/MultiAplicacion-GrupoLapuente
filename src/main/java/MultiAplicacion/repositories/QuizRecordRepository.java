package MultiAplicacion.repositories;

import MultiAplicacion.entities.QuizRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;

public interface QuizRecordRepository extends JpaRepository<QuizRecord, Long> {
    // Esta consulta ahora verifica cualquier registro dentro del día, independientemente de la hora exacta
    @Query("SELECT qr FROM QuizRecord qr WHERE qr.user.id = :userId AND qr.date BETWEEN :startOfDay AND :endOfDay")
    List<QuizRecord> findByUserIdAndDay(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    // Añade un parámetro Sort para incluir el ordenamiento por fecha
    List<QuizRecord> findByDateBetweenAndPassed(LocalDateTime startDate, LocalDateTime endDate, boolean passed, Sort sort);
}
