package MultiAplicacion;


import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.QuestionRepository;
import MultiAplicacion.repositories.QuizRecordRepository;
import MultiAplicacion.repositories.UserRepository;
import MultiAplicacion.services.NotificationService;
import MultiAplicacion.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class QuizControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private QuestionRepository questionRepository;
    @MockBean
    private QuizRecordRepository quizRecordRepository;

    private User user;
    private Question question;
    private QuizRecord quizRecord;

    @BeforeEach
    void setUp() {
        user = new Worker();  // Asumiendo que Worker es una clase concreta que extiende User
        user.setId(1L);
        user.setName("testUser");

        question = new Question();
        question.setId(1L);
        question.setText("Is safety gear required?");

        List<Question> questions = Arrays.asList(question);

        when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));
        when(questionRepository.findAll()).thenReturn(questions);
    }

    @WithMockUser(username = "testUser", roles = {"WORKER"})
    void showQuizForm_ShowsQuiz() throws Exception {
        when(quizRecordRepository.findByUserIdAndDay(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/worker/1/quiz"))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/quiz"))
                .andExpect(model().attributeExists("questions", "sociedadId"))
                .andExpect(model().attribute("sociedadId", 1L));
    }

    @Test
    @WithMockUser(username = "testUser")
    void processQuiz_WithAllYesAnswers_RedirectsToWorkersMenu() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("question1", "yes");

        mockMvc.perform(post("/worker/1/quiz/process")
                        .param("question1", "yes"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/worker/1/workersmenu"));

        verify(quizRecordRepository).save(any(QuizRecord.class));
    }

    @Test
    @WithMockUser(username = "testUser")
    void processQuiz_WithANoAnswer_RedirectsBackToQuiz() throws Exception {
        mockMvc.perform(post("/worker/1/quiz/process")
                        .param("question1", "no"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/worker/1/quiz/alert"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"WORKER"})
    void showQuizForm_RedirectsWhenQuizAlreadyTaken() throws Exception {
        QuizRecord quizRecord = new QuizRecord(user, LocalDateTime.now(), true);
        List<QuizRecord> records = Arrays.asList(quizRecord);

        when(quizRecordRepository.findByUserIdAndDay(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(records);

        mockMvc.perform(get("/worker/1/quiz"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/worker/1/workersmenu"));
    }
    @Test
    @WithMockUser(username = "testUser", roles = {"WORKER"})
    void showAlert_ShowsAlertPage() throws Exception {
        mockMvc.perform(get("/worker/1/quiz/alert"))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/quizAlert"))
                .andExpect(model().attributeExists("imagePath", "message"));
    }

}

