package MultiAplicacion.services;

import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.repositories.AdminRepository;
import MultiAplicacion.repositories.SociedadRepository;
import MultiAplicacion.repositories.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class SociedadServiceTest {

    @Mock
    private SociedadRepository sociedadRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private WorkerRepository workerRepository;

    @InjectMocks
    private SociedadService sociedadService;

    private Sociedad sociedad;

    @BeforeEach
    void setUp() {
        sociedad = new Sociedad();
        sociedad.setId(1L);
        sociedad.setName("Sociedad Test");
    }

    @Test
    void findAll_ReturnsAllSociedades() {
        when(sociedadRepository.findAll()).thenReturn(Arrays.asList(sociedad));

        List<Sociedad> sociedades = sociedadService.findAll();

        assertFalse(sociedades.isEmpty());
        assertEquals(1, sociedades.size());
        assertEquals(sociedad, sociedades.get(0));
    }

    @Test
    void findById_ExistingId_ReturnsSociedad() {
        when(sociedadRepository.findById(1L)).thenReturn(Optional.of(sociedad));

        Optional<Sociedad> foundSociedad = sociedadService.findById(1L);

        assertTrue(foundSociedad.isPresent());
        assertEquals(sociedad, foundSociedad.get());
    }

    @Test
    void findById_NonExistingId_ReturnsEmpty() {
        when(sociedadRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<Sociedad> foundSociedad = sociedadService.findById(999L);

        assertFalse(foundSociedad.isPresent());
    }

    @Test
    void save_ValidSociedad_SavesSuccessfully() {
        when(sociedadRepository.save(any(Sociedad.class))).thenReturn(sociedad);

        Sociedad savedSociedad = sociedadService.save(new Sociedad());

        assertNotNull(savedSociedad);
        assertEquals(sociedad, savedSociedad);
    }

    @Test
    void deleteById_ExistingId_DeletesSociedad() {
        when(sociedadRepository.findById(1L)).thenReturn(Optional.of(sociedad));
        when(sociedadRepository.save(any(Sociedad.class))).thenReturn(sociedad); // Mockear el comportamiento de save

        assertDoesNotThrow(() -> sociedadService.deleteById(1L));

        verify(sociedadRepository).save(any(Sociedad.class)); // Verificar que save fue llamado
    }

    @Test
    void deleteById_NonExistingId_ThrowsResponseStatusException() {
        when(sociedadRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> sociedadService.deleteById(999L));
    }

    @Test
    void sociedadSeleccionada_ExistingId_ReturnsSociedad() {
        when(sociedadRepository.findById(1L)).thenReturn(Optional.of(sociedad));

        Sociedad foundSociedad = sociedadService.sociedadSeleccionada(1L);

        assertNotNull(foundSociedad);
        assertEquals(sociedad, foundSociedad);
    }

    @Test
    void sociedadSeleccionada_NonExistingId_ThrowsIllegalArgumentException() {
        when(sociedadRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> sociedadService.sociedadSeleccionada(999L));
    }

    @Test
    void getSociedadIdByUserName_ExistingUser_ReturnsSociedadId() {
        when(sociedadRepository.findByUsersName("userExists")).thenReturn(sociedad);

        Long sociedadId = sociedadService.getSociedadIdByUserName("userExists");

        assertEquals(1L, sociedadId);
    }

    @Test
    void getSociedadIdByUserName_NonExistingUser_ThrowsResponseStatusException() {
        when(sociedadRepository.findByUsersName("userDoesNotExist")).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> sociedadService.getSociedadIdByUserName("userDoesNotExist"));
    }

    @Test
    void existsById_ExistingId_ReturnsTrue() {
        when(sociedadRepository.existsById(1L)).thenReturn(true);

        assertTrue(sociedadService.existsById(1L));
    }

    @Test
    void existsById_NonExistingId_ReturnsFalse() {
        when(sociedadRepository.existsById(999L)).thenReturn(false);

        assertFalse(sociedadService.existsById(999L));
    }
}
