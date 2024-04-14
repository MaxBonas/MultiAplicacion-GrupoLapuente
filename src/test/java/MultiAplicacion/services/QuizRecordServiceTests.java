package MultiAplicacion.services;

import MultiAplicacion.entities.QuizRecord;
import MultiAplicacion.repositories.QuizRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class QuizRecordServiceTests {

    @Mock
    private QuizRecordRepository quizRecordRepository;

    @InjectMocks
    private QuizRecordService quizRecordService;

    @Test
    void testFindQuizRecordsByDateRangeAndStatus() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        QuizRecord quizRecord1 = new QuizRecord(); // Suponiendo que tienes un constructor por defecto
        QuizRecord quizRecord2 = new QuizRecord(); // Suponiendo que tienes un constructor por defecto
        List<QuizRecord> expectedRecords = Arrays.asList(quizRecord1, quizRecord2);

        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        when(quizRecordRepository.findByDateBetweenAndPassed(any(LocalDateTime.class), any(LocalDateTime.class), any(Boolean.class), any(Sort.class)))
                .thenReturn(expectedRecords);

        List<QuizRecord> actualRecords = quizRecordService.findQuizRecordsByDateRangeAndStatus(startDate, endDate, true);

        verify(quizRecordRepository).findByDateBetweenAndPassed(startDate, endDate, true, sort);
        assert expectedRecords.equals(actualRecords); // Asegúrate de implementar equals en QuizRecord si es necesario
    }
}
