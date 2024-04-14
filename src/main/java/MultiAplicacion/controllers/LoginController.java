package MultiAplicacion.controllers;

import MultiAplicacion.entities.QuizRecord;
import MultiAplicacion.repositories.QuizRecordRepository;
import MultiAplicacion.services.SociedadService;
import MultiAplicacion.services.UserService;
import MultiAplicacion.repositories.UserRepository;
import MultiAplicacion.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private UserService userService; // Asumiendo que UserService puede buscar usuarios por nombre.

    @Autowired
    private SociedadService sociedadService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizRecordRepository quizRecordRepository;

    @GetMapping("/")
    public String index(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "loginAttempt", required = false) String loginAttempt,
                        Model model) {

        boolean loginSuccess = false;
        if (error != null) {
            model.addAttribute("loginError", true);
        }

        if (logout != null) {
            model.addAttribute("logoutSuccess", true);
        }

        if (loginAttempt != null) {
            model.addAttribute("loginAttempt", true);
        }

        model.addAttribute("loginSuccess", loginSuccess);
        return "index";
    }

    @GetMapping("/entrar")
    public String entrar(Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            redirectAttributes.addAttribute("loginAttempt", true);
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("loginSuccess", true);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        Long sociedadId = sociedadService.getSociedadIdByUserName(authentication.getName());
        User user = userRepository.findByName(authentication.getName()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);

        List<QuizRecord> quizRecordsToday = quizRecordRepository.findByUserIdAndDay(user.getId(), startOfDay, endOfDay);

        boolean needsQuiz = quizRecordsToday.isEmpty();

        if (!isAdmin && needsQuiz) {
            return "redirect:/worker/" + sociedadId + "/quiz";
        }

        return isAdmin ? "redirect:/admin/" + sociedadId + "/adminsmenu" : "redirect:/worker/" + sociedadId + "/workersmenu";
    }
}

