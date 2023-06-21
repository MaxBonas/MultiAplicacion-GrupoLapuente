package MultiAplicacion.services;

import MultiAplicacion.DTOs.WorkerDTO;
import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.entities.Worker;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class WorkerServiceTest {

    @Autowired
    private WorkerService workerService;

    @MockBean
    private WorkerRepository workerRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private SociedadRepository sociedadRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private UbicacionRepository ubicacionRepository;

    @MockBean
    private TareaCumplidaService tareaCumplidaService;

    @MockBean
    private UbicacionTareaRepository ubicacionTareaRepository;

    private Worker worker;

    @BeforeEach
    void setUp() {
        worker = new Worker();
        worker.setName("worker1");
        worker.setPassword("1234");
        worker.setId(1L);
    }

    @Test
    void getAllWorkers() {
        when(workerRepository.findAll()).thenReturn(Collections.singletonList(worker));
        List<Worker> workers = workerService.getAllWorkers();
        assertEquals(1, workers.size());
        assertEquals(worker.getName(), workers.get(0).getName());
    }

    @Test
    void getWorkerById() {
        when(workerRepository.findById(anyLong())).thenReturn(Optional.of(worker));
        Worker resultWorker = workerService.getWorkerById(1L);
        assertEquals(worker.getName(), resultWorker.getName());
    }

    @Test
    void saveWorker() {
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setName("worker1");
        workerDTO.setPassword("1234");
        workerDTO.setSociedadId(1L);
        when(sociedadRepository.findById(anyLong())).thenReturn(Optional.of(new Sociedad()));
        when(workerRepository.save(any())).thenReturn(worker);
        Worker resultWorker = workerService.saveWorker(workerDTO);
        assertEquals(worker.getName(), resultWorker.getName());
    }

    @Test
    void deleteWorkerById() {
        doNothing().when(userService).deleteUserById(anyLong());
        workerService.deleteWorkerById(1L);
        verify(userService, times(1)).deleteUserById(anyLong());
    }

    @Test
    void findByName() {
        when(workerRepository.findByName(anyString())).thenReturn(Optional.of(worker));
        Worker resultWorker = workerService.findByName("worker1");
        assertEquals(worker.getName(), resultWorker.getName());
    }

    @Test
    void updateWorker() {
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setName("worker2");
        when(workerRepository.getById(anyLong())).thenReturn(worker);
        when(sociedadRepository.getById(anyLong())).thenReturn(new Sociedad());
        when(workerRepository.save(any())).thenReturn(worker);
        Worker resultWorker = workerService.updateWorker(1L, workerDTO);
        assertEquals(worker.getName(), resultWorker.getName());
    }

    @Test
    void cambiarPassword() {
        when(workerRepository.findById(anyLong())).thenReturn(Optional.of(worker));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(workerRepository.save(any())).thenReturn(worker);
        assertDoesNotThrow(() -> workerService.cambiarPassword(1L, "1234", "12345"));
    }

    @Test
    void getWorkersBySociedad() {
        when(workerRepository.findBySociedadId(anyLong())).thenReturn(Collections.singletonList(worker));
        List<Worker> workers = workerService.getWorkersBySociedad(1L);
        assertEquals(1, workers.size());
        assertEquals(worker.getName(), workers.get(0).getName());
    }

    @Test
    void deleteRoleByUserId() {
        doNothing().when(roleRepository).deleteByUserId(anyLong());
        workerService.deleteRoleByUserId(1L);
        verify(roleRepository, times(1)).deleteByUserId(anyLong());
    }

    // Nota: Este es un caso de prueba muy básico para el método 'crearTareasCumplidasVacias' y podría no cubrir todos los casos posibles.
    @Test
    void crearTareasCumplidasVacias() {
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setId(1L);
        when(workerRepository.findById(anyLong())).thenReturn(Optional.of(worker));
        when(ubicacionRepository.findById(anyLong())).thenReturn(Optional.of(ubicacion));
        when(tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(any(), any(), any(), anyBoolean())).thenReturn(new ArrayList<>());
        when(ubicacionTareaRepository.findAllByUbicacionId(anyLong())).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> workerService.crearTareasCumplidasVacias(1L, 1L, LocalDateTime.now(), Turno.MANANA, "worker1"));
    }
}
