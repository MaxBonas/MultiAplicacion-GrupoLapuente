package MultiAplicacion.services.interfaces;

import MultiAplicacion.entities.QuizRecord;

import java.time.LocalDate;
import java.util.List;

public interface QuizRecordServiceInterface {
    public List<QuizRecord> findQuizRecordsByDateRangeAndStatus(LocalDate startDate, LocalDate endDate, boolean passed);
}
