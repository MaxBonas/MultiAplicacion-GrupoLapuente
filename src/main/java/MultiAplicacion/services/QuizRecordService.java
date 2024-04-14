package MultiAplicacion.services;

import MultiAplicacion.entities.QuizRecord;
import MultiAplicacion.repositories.QuizRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Sort;

import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizRecordService {

    @Autowired
    private QuizRecordRepository quizRecordRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public List<QuizRecord> findQuizRecordsByDateRangeAndStatus(LocalDateTime startDate, LocalDateTime endDate, boolean passed) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        return quizRecordRepository.findByDateBetweenAndPassed(startDate, endDate, passed, sort);
    }

    // Método para formatear las fechas de los registros de cuestionarios
    public List<String> formatQuizRecordDates(List<QuizRecord> records) {
        if (records.isEmpty()) {
            return Collections.emptyList();
        }
        return records.stream()
                .map(record -> record.getDate().format(formatter))
                .collect(Collectors.toList());
    }
}