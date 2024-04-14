package MultiAplicacion;

import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.QuizRecordRepository;
import MultiAplicacion.repositories.UserRepository;
import MultiAplicacion.services.SociedadService;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SociedadService sociedadService;

    @MockBean
    private QuizRecordRepository quizRecordRepository;

    private User user;
    private Sociedad sociedad;

    @BeforeEach
    void setUp() {
        user = new Worker(); // Asumiendo que Worker es una clase concreta que extiende User
        user.setId(1L);
        user.setName("testUser");

        sociedad = new Sociedad();
        sociedad.setId(1L);
        sociedad.setName("Sociedad Test");

        when(userRepository.findByName("testUser")).thenReturn(Optional.of(user));
        when(sociedadService.getSociedadIdByUserName("testUser")).thenReturn(1L);
    }

    @Test
    void testIndexWithError() throws Exception {
        mockMvc.perform(get("/").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("loginError"))
                .andExpect(model().attribute("loginError", true));
    }

    @Test
    void testIndexWithLogout() throws Exception {
        mockMvc.perform(get("/").param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("logoutSuccess"))
                .andExpect(model().attribute("logoutSuccess", true));
    }

    @Test
    void testIndexWithLoginAttempt() throws Exception {
        mockMvc.perform(get("/").param("loginAttempt", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("loginAttempt"))
                .andExpect(model().attribute("loginAttempt", true));
    }

    @Test
    void testIndexNormal() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("loginSuccess", false));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testEntrarAsAdmin() throws Exception {
        when(quizRecordRepository.findByUserIdAndDay(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/entrar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/1/adminsmenu"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testEntrarAsUserWithQuizNeeded() throws Exception {
        when(quizRecordRepository.findByUserIdAndDay(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/entrar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/worker/1/quiz"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void testEntrarAsUserNoQuizNeeded() throws Exception {
        List<QuizRecord> quizRecords = Arrays.asList(new QuizRecord());
        when(quizRecordRepository.findByUserIdAndDay(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(quizRecords);

        mockMvc.perform(get("/entrar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/worker/1/workersmenu"));
    }
}
