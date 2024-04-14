package MultiAplicacion.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import MultiAplicacion.DTOs.TareaDTO;
import MultiAplicacion.entities.Tarea;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.repositories.TareaCumplidaRepository;
import MultiAplicacion.repositories.TareaRepository;
import MultiAplicacion.repositories.UbicacionRepository;
import MultiAplicacion.repositories.UbicacionTareaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class TareaServiceTest {

    @Mock
    private TareaRepository tareaRepository;
    @Mock
    private UbicacionRepository ubicacionRepository;
    @Mock
    private TareaCumplidaRepository tareaCumplidaRepository;
    @Mock
    private UbicacionTareaRepository ubicacionTareaRepository;
    @Mock
    private UbicacionTareaService ubicacionTareaService;

    @InjectMocks
    private TareaService tareaService;

    private Tarea tarea;
    private Ubicacion ubicacion;
    private TareaDTO tareaDTO;

    @BeforeEach
    void setUp() {
        ubicacion = new Ubicacion();
        ubicacion.setId(1L);
        ubicacion.setName("Ubicación Test");

        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setName("Tarea Test");
        tarea.setDescripcion("Descripción Test");

        tareaDTO = new TareaDTO();
        tareaDTO.setName("Tarea DTO Test");
        tareaDTO.setDescripcion("Descripción DTO Test");
    }

    @Test
    void getAllTareas_ReturnsList() {
        when(tareaRepository.findAll()).thenReturn(Arrays.asList(tarea));

        List<Tarea> result = tareaService.getAllTareas();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(tarea, result.get(0));
    }

    @Test
    void getTareaById_Found_ReturnsTarea() {
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));

        Tarea result = tareaService.getTareaById(1L);

        assertNotNull(result);
        assertEquals(tarea, result);
    }

    @Test
    void getTareaById_NotFound_ThrowsResponseStatusException() {
        when(tareaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tareaService.getTareaById(999L));
    }

    @Test
    void saveTarea_ValidTarea_SavesSuccessfully() {
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        Tarea result = tareaService.saveTarea(tareaDTO);

        assertNotNull(result);
        assertEquals(tarea.getName(), result.getName());
        assertEquals(tarea.getDescripcion(), result.getDescripcion());
    }

    @Test
    void deleteTareaById_ExistingId_DeletesTarea() {
        // Configuración inicial: Simula que la tarea se encuentra
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));

        // Ejecutar el método bajo prueba
        tareaService.deleteTareaById(1L);

        // Verificar que se busque la tarea
        verify(tareaRepository).findById(1L);

        // Verificar que se intentó eliminar las relaciones UbicacionTarea y luego marcar la tarea como eliminada.
        // Como marcar la tarea como eliminada se traduce en un save, verificamos esa llamada.
        verify(tareaRepository).save(any(Tarea.class));
    }

    @Test
    void deleteTareaById_NotFound_ThrowsResponseStatusException() {
        when(tareaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> tareaService.deleteTareaById(999L));
    }

    @Test
    void getTareasByUbicacion_ExistingUbicacion_ReturnsList() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(tareaRepository.findByUbicaciones(ubicacion)).thenReturn(Arrays.asList(tarea));

        List<Tarea> result = tareaService.getTareasByUbicacion(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(tarea, result.get(0));
    }

    @Test
    void findAllDistinctByName_ReturnsDistinctTareas() {
        when(tareaRepository.findAllDistinctByName()).thenReturn(Arrays.asList(tarea));

        List<Tarea> result = tareaService.findAllDistinctByName();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findAllOrderedById_ReturnsTareasOrdered() {
        when(tareaRepository.findAllByOrderById()).thenReturn(Arrays.asList(tarea));

        List<Tarea> result = tareaService.findAllOrderedById();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(tarea, result.get(0));
    }

    @Test
    void updateTarea_ExistingId_UpdatesSuccessfully() {
        when(tareaRepository.findById(1L)).thenReturn(Optional.of(tarea));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);

        TareaDTO updatedTareaDTO = new TareaDTO();
        updatedTareaDTO.setName("Updated Name");
        updatedTareaDTO.setDescripcion("Updated Descripción");

        Tarea updatedTarea = tareaService.updateTarea(1L, updatedTareaDTO);

        assertNotNull(updatedTarea);
        assertEquals(updatedTareaDTO.getName(), updatedTarea.getName());
        assertEquals(updatedTareaDTO.getDescripcion(), updatedTarea.getDescripcion());
    }

    @Test
    void updateTarea_NotFound_ThrowsResponseStatusException() {
        when(tareaRepository.findById(anyLong())).thenReturn(Optional.empty());

        TareaDTO updatedTareaDTO = new TareaDTO();
        updatedTareaDTO.setName("Non-existent");

        assertThrows(ResponseStatusException.class, () -> tareaService.updateTarea(999L, updatedTareaDTO));
    }

    @Test
    void findAllDistinctByName_EmptyList_ReturnsEmptyList() {
        when(tareaRepository.findAllDistinctByName()).thenReturn(Arrays.asList());

        List<Tarea> result = tareaService.findAllDistinctByName();

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllOrderedById_EmptyList_ReturnsEmptyList() {
        when(tareaRepository.findAllByOrderById()).thenReturn(Arrays.asList());

        List<Tarea> result = tareaService.findAllOrderedById();

        assertTrue(result.isEmpty());
    }

    // Añade aquí más pruebas según sea necesario para cubrir todos los métodos y casos de uso.
}
