package MultiAplicacion.controllers;

import MultiAplicacion.entities.Question;
import MultiAplicacion.entities.QuizRecord;
import MultiAplicacion.repositories.QuestionRepository;
import MultiAplicacion.repositories.QuizRecordRepository;
import MultiAplicacion.services.NotificationService;
import MultiAplicacion.services.UserService;
import MultiAplicacion.repositories.UserRepository;
import MultiAplicacion.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@Controller
@RequestMapping("/worker/{sociedadId}/quiz")
public class QuizController {

    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuizRecordRepository quizRecordRepository;

    @PostMapping("/process")
    public String processQuiz(@RequestParam Map<String, String> allParams,
                              Authentication authentication,
                              @PathVariable("sociedadId") Long sociedadId) {
        String username = authentication.getName();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        boolean passed = allParams.values().stream().noneMatch(value -> "no".equals(value));

        QuizRecord record = new QuizRecord(user, LocalDateTime.now(), passed);
        quizRecordRepository.save(record);

        if (!passed) {
            // Si la respuesta contiene "no", redirigir a la página de alerta
            return "redirect:/worker/" + sociedadId + "/quiz/alert";
        }

        // Si todas las respuestas son "Sí", redirigir al menú.
        return "redirect:/worker/" + sociedadId + "/workersmenu";
    }

    @GetMapping
    public String showQuizForm(@PathVariable("sociedadId") Long sociedadId, Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        List<QuizRecord> todayQuizRecord = quizRecordRepository.findByUserIdAndDay(user.getId(), startOfDay, endOfDay);

        if (!todayQuizRecord.isEmpty()) {
            return "redirect:/worker/" + sociedadId + "/workersmenu";
        }

        List<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        model.addAttribute("sociedadId", sociedadId);
        return "workers/quiz";
    }

    @GetMapping("/alert")
    public String showAlert(@PathVariable("sociedadId") Long sociedadId, Authentication authentication, Model model) {
        String username = authentication.getName();
        notificationService.sendAlertToAdmin(username);
        model.addAttribute("imagePath", "/images/alertImage1.png");
        model.addAttribute("message", "El compromiso con la seguridad es obligatorio. Se tomarán medidas si las preocupaciones persisten para garantizar la seguridad de todos en el lugar de trabajo.");
        return "workers/quizAlert";
    }
}