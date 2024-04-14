package MultiAplicacion.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.Tarea;
import MultiAplicacion.entities.TareaCumplida;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.entities.Worker;
import MultiAplicacion.repositories.TareaCumplidaRepository;
import MultiAplicacion.repositories.TareaRepository;
import MultiAplicacion.repositories.UbicacionRepository;
import MultiAplicacion.repositories.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class TareaCumplidaServiceTest {

    @Mock
    private TareaCumplidaRepository tareaCumplidaRepository;

    @Mock
    private TareaRepository tareaRepository;

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private UbicacionRepository ubicacionRepository;

    @InjectMocks
    private TareaCumplidaService tareaCumplidaService;

    private TareaCumplida tareaCumplida;
    private Worker worker;
    private Ubicacion ubicacion;
    private Tarea tarea;

    @BeforeEach
    void setUp() {
        worker = new Worker();
        worker.setId(1L);
        worker.setName("Worker Test");

        ubicacion = new Ubicacion();
        ubicacion.setId(1L);
        ubicacion.setName("Ubicacion Test");

        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setName("Tarea Test");

        tareaCumplida = new TareaCumplida();
        tareaCumplida.setId(1L);
        tareaCumplida.setCumplida(true);
        tareaCumplida.setWorker(worker);
        tareaCumplida.setUbicacion(ubicacion);
        tareaCumplida.setTarea(tarea);
    }

    @Test
    void getAllTareasCumplidas_ReturnsList() {
        when(tareaCumplidaRepository.findAll()).thenReturn(Arrays.asList(tareaCumplida));

        List<TareaCumplida> result = tareaCumplidaService.getAllTareasCumplidas();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(tareaCumplida, result.get(0));
    }

    @Test
    void getTareaCumplidaById_Found_ReturnsTareaCumplida() {
        when(tareaCumplidaRepository.findById(1L)).thenReturn(Optional.of(tareaCumplida));

        TareaCumplida result = tareaCumplidaService.getTareaCumplidaById(1L);

        assertNotNull(result);
        assertEquals(tareaCumplida, result);
    }

    @Test
    void getTareaCumplidaById_NotFound_ReturnsNull() {
        when(tareaCumplidaRepository.findById(anyLong())).thenReturn(Optional.empty());

        TareaCumplida result = tareaCumplidaService.getTareaCumplidaById(999L);

        assertNull(result);
    }

    @Test
    void deleteTareaCumplida_ExistingId_DeletesTareaCumplida() {
        doNothing().when(tareaCumplidaRepository).deleteById(1L);

        assertDoesNotThrow(() -> tareaCumplidaService.deleteTareaCumplida(1L));
        verify(tareaCumplidaRepository, times(1)).deleteById(1L);
    }

    @Test
    void getTareaCumplidaByWorker_ReturnsList() {
        when(tareaCumplidaRepository.findByWorker(worker)).thenReturn(Arrays.asList(tareaCumplida));

        List<TareaCumplida> result = tareaCumplidaService.getTareaCumplidaByWorker(worker);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(tareaCumplida, result.get(0));
    }

    @Test
    void getTareaCumplidaByUbicacion_ReturnsList() {
        when(tareaCumplidaRepository.findByTarea_Ubicaciones(ubicacion)).thenReturn(Arrays.asList(tareaCumplida));

        List<TareaCumplida> result = tareaCumplidaService.getTareaCumplidaByUbicacion(ubicacion);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(tareaCumplida, result.get(0));
    }

    @Test
    void updateTareaCumplida_ExistingId_UpdatesAndReturnsTareaCumplida() {
        TareaCumplida updatedTareaCumplida = new TareaCumplida();
        updatedTareaCumplida.setCumplida(false);
        updatedTareaCumplida.setWorker(worker);

        when(tareaCumplidaRepository.findById(1L)).thenReturn(Optional.of(tareaCumplida));
        when(tareaCumplidaRepository.save(any(TareaCumplida.class))).thenReturn(updatedTareaCumplida);

        TareaCumplida result = tareaCumplidaService.updateTareaCumplida(1L, updatedTareaCumplida);

        assertNotNull(result);
        assertFalse(result.isCumplida());
        assertEquals(worker, result.getWorker());
    }

    @Test
    void updateTareaCumplida_NonExistingId_ThrowsResponseStatusException() {
        when(tareaCumplidaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tareaCumplidaService.updateTareaCumplida(999L, new TareaCumplida()));
    }

    @Test
    void save_TareaCumplidaDTOWithNonexistentWorker_ThrowsNoSuchElementException() {
        TareaCumplidaDTO tareaCumplidaDTO = new TareaCumplidaDTO();
        tareaCumplidaDTO.setWorkerId(999L); // ID de Worker inexistente
        tareaCumplidaDTO.setTareaId(1L); // Suponer existente para enfocarse en el error del worker
        tareaCumplidaDTO.setUbicacionId(1L); // Suponer existente para enfocarse en el error del worker
        tareaCumplidaDTO.setCumplida(true);
        tareaCumplidaDTO.setTurno(Turno.MANANA);

        when(workerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(NoSuchElementException.class, () -> tareaCumplidaService.save(tareaCumplidaDTO));
        assertTrue(exception.getMessage().contains("Trabajador no encontrado"));
    }

    @Test
    void save_ValidTareaCumplidaDTO_SavesAndReturnsTareaCumplida() {
        TareaCumplidaDTO tareaCumplidaDTO = new TareaCumplidaDTO();
        tareaCumplidaDTO.setTareaId(1L);
        tareaCumplidaDTO.setWorkerId(1L);
        tareaCumplidaDTO.setUbicacionId(1L);
        tareaCumplidaDTO.setCumplida(true); // Asegúrate de que este campo esté correctamente inicializado

        when(workerRepository.findById(anyLong())).thenReturn(Optional.of(worker));
        when(tareaRepository.findById(anyLong())).thenReturn(Optional.of(tarea));
        when(ubicacionRepository.findById(anyLong())).thenReturn(Optional.of(ubicacion));
        when(tareaCumplidaRepository.save(any(TareaCumplida.class))).thenReturn(tareaCumplida);

        TareaCumplida result = tareaCumplidaService.save(tareaCumplidaDTO);

        assertNotNull(result);
        assertTrue(result.isCumplida()); // Confirma que el valor de cumplida es true
        assertEquals(worker, result.getWorker());
        assertEquals(tarea, result.getTarea());
        assertEquals(ubicacion, result.getUbicacion());
    }

    @Test
    void findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida_ReturnsList() {
        LocalDateTime fecha = LocalDateTime.now();
        Turno turno = Turno.MANANA;
        boolean cumplida = true;

        when(tareaCumplidaRepository.findByUbicacionAndFechaCumplimientoBetweenAndTurnoAndCumplida(
                eq(ubicacion), any(LocalDateTime.class), any(LocalDateTime.class), eq(turno), eq(cumplida)))
                .thenReturn(Arrays.asList(tareaCumplida));

        List<TareaCumplida> result = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(ubicacion, fecha, turno, cumplida);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(tareaCumplida, result.get(0));
    }

    @Test
    void findTareasCumplidasByUbicacionAndFechaAndTurno_ReturnsList() {
        LocalDateTime fecha = LocalDateTime.now();
        Turno turno = Turno.MANANA;

        when(tareaCumplidaRepository.findByUbicacionAndFechaCumplimientoBetweenAndTurno(
                eq(ubicacion), any(LocalDateTime.class), any(LocalDateTime.class), eq(turno)))
                .thenReturn(Arrays.asList(tareaCumplida));

        List<TareaCumplida> result = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(ubicacion, fecha, turno);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(tareaCumplida, result.get(0));
    }
}
