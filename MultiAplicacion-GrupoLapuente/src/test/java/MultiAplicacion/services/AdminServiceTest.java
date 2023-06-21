package MultiAplicacion.services;

import MultiAplicacion.entities.Admin;
import MultiAplicacion.entities.Role;
import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.repositories.AdminRepository;
import MultiAplicacion.repositories.RoleRepository;
import MultiAplicacion.repositories.SociedadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private SociedadRepository sociedadRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Configurar los datos de prueba y las respuestas de los servicios aquí
        Admin admin = new Admin();
        admin.setName("admin1");
        admin.setPassword("1234");
        admin.setEmail("admin1@test.com");

        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        sociedad.setName("Sociedad Test");
        admin.setSociedad(sociedad);

        when(adminRepository.save(any())).thenReturn(admin);
        when(adminRepository.findByName(any())).thenReturn(Optional.of(admin));
        when(adminRepository.findByEmail(any())).thenReturn(Optional.of(admin));
        when(sociedadRepository.findById(any())).thenReturn(Optional.of(sociedad));
    }

    @Test
    void createAdmin() {
        Admin admin = adminService.createAdmin("admin1", "admin1@test.com", "1234", 1L);
        assertNotNull(admin);
        assertEquals("admin1", admin.getName());
        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    void findAdminByName() {
        Optional<Admin> admin = adminService.findAdminByName("admin1");
        assertTrue(admin.isPresent());
        assertEquals("admin1", admin.get().getName());
    }

    @Test
    void findAdminByEmail() {
        Optional<Admin> admin = adminService.findAdminByEmail("admin1@test.com");
        assertTrue(admin.isPresent());
        assertEquals("admin1@test.com", admin.get().getEmail());
    }

    @Test
    void adminDeleteById() {
        doNothing().when(userService).deleteUserById(anyLong());
        adminService.adminDeleteById(1L);
        verify(userService, times(1)).deleteUserById(anyLong());
    }

    @Test
    void changePasswordAdmin() {
        UserDetails userDetails = User.builder().username("admin1").password("1234").roles("ADMIN").build();
        Admin admin = new Admin("admin1", "admin1@test.com", "1234", new Sociedad());
        admin.setId(1L);

        when(passwordEncoder.encode(anyString())).thenAnswer(invocation -> "Encoded_" + invocation.getArguments()[0]);
        when(adminRepository.findByName(userDetails.getUsername())).thenReturn(Optional.of(admin));

        Admin adminWithChangedPassword = new Admin("admin1", "admin1@test.com", "Encoded_12345", new Sociedad());
        adminWithChangedPassword.setId(1L);

        when(adminRepository.save(any(Admin.class))).thenReturn(adminWithChangedPassword);

        adminService.changePasswordAdmin(userDetails, "12345");

        ArgumentCaptor<Admin> adminArgumentCaptor = ArgumentCaptor.forClass(Admin.class);
        verify(adminRepository, times(1)).save(adminArgumentCaptor.capture());

        Admin savedAdmin = adminArgumentCaptor.getValue();
        assertEquals("Encoded_12345", savedAdmin.getPassword());
    }

    @Test
    void createAdmin_withNullParams_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.createAdmin(null, null, null, null);
        });
    }

    @Test
    void createAdmin_withInvalidData_shouldReturnError() {
        // Simula que no se encuentra la sociedad
        when(sociedadRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            adminService.createAdmin("admin1", "admin1@test.com", "1234", 1L);
        });
    }

    @Test
    void createAdmin_dependencyMethodsCalled() {
        adminService.createAdmin("admin1", "admin1@test.com", "1234", 1L);
        verify(adminRepository, times(1)).save(any());
        verify(roleRepository, times(1)).save(any());
    }

    @Test
    void createAdmin_withSociedadRepoFailure_shouldHandleError() {
        when(sociedadRepository.findById(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            adminService.createAdmin("admin1", "admin1@test.com", "1234", 1L);
        });
    }

    @Test
    void findAdminByName_withNullName_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.findAdminByName(null);
        });
    }

    @Test
    void findAdminByName_withInvalidName_shouldReturnError() {
        when(adminRepository.findByName(any())).thenReturn(Optional.empty());

        Optional<Admin> result = adminService.findAdminByName("invalid");
        assertFalse(result.isPresent());
    }

    @Test
    void findAdminByEmail_withNullEmail_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.findAdminByEmail(null);
        });
    }

    @Test
    void findAdminByEmail_withInvalidEmail_shouldReturnError() {
        when(adminRepository.findByEmail(any())).thenReturn(Optional.empty());

        Optional<Admin> result = adminService.findAdminByEmail("invalid");
        assertFalse(result.isPresent());
    }

    @Test
    void adminDeleteById_withNullId_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.adminDeleteById(null);
        });
    }

    @Test
    void adminDeleteById_withInvalidId_shouldHandleError() {
        doThrow(new RuntimeException()).when(userService).deleteUserById(anyLong());

        assertThrows(RuntimeException.class, () -> {
            adminService.adminDeleteById(1L);
        });
    }

    @Test
    void changePasswordAdmin_withNullParams_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            adminService.changePasswordAdmin(null, null);
        });
    }

    @Test
    void changePasswordAdmin_withInvalidPassword_shouldReturnError() {
        UserDetails userDetails = User.builder().username("admin1").password("1234").roles("ADMIN").build();

        assertThrows(IllegalArgumentException.class, () -> {
            adminService.changePasswordAdmin(userDetails, "");
        });
    }

    @Test
    void changePasswordAdmin_dependencyMethodsCalled() {
        UserDetails userDetails = User.builder().username("admin1").password("1234").roles("ADMIN").build();
        adminService.changePasswordAdmin(userDetails, "12345");

        verify(adminRepository, times(1)).save(any());
    }

    @Test
    void changePasswordAdmin_withPasswordEncoderFailure_shouldHandleError() {
        UserDetails userDetails = User.builder().username("admin1").password("1234").roles("ADMIN").build();
        when(passwordEncoder.encode(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> {
            adminService.changePasswordAdmin(userDetails, "12345");
        });
    }

}
