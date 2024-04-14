package MultiAplicacion.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import MultiAplicacion.DTOs.TareaDTO;
import MultiAplicacion.DTOs.UbicacionDTO;
import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.entities.Tarea;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.entities.UbicacionTarea;
import MultiAplicacion.repositories.SociedadRepository;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
class UbicacionServiceTest {

    @Mock
    private UbicacionRepository ubicacionRepository;
    @Mock
    private TareaRepository tareaRepository;
    @Mock
    private SociedadRepository sociedadRepository;
    @Mock
    private UbicacionTareaRepository ubicacionTareaRepository;
    @Mock
    private UbicacionTareaService ubicacionTareaService;

    @InjectMocks
    private UbicacionService ubicacionService;

    private Ubicacion ubicacion;
    private Tarea tarea;
    private Sociedad sociedad;
    private UbicacionDTO ubicacionDTO;
    private TareaDTO tareaDTO;

    @BeforeEach
    void setUp() {
        sociedad = new Sociedad();
        sociedad.setId(1L);
        sociedad.setName("Sociedad Test");

        ubicacion = new Ubicacion();
        ubicacion.setId(1L);
        ubicacion.setName("Ubicacion Test");
        ubicacion.setSociedad(sociedad);

        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setName("Tarea Test");
        tarea.setDescripcion("Descripción Test");

        ubicacionDTO = new UbicacionDTO();
        ubicacionDTO.setName("Ubicacion DTO Test");
        ubicacionDTO.setSociedadId(sociedad.getId());

        tareaDTO = new TareaDTO();
        tareaDTO.setName("Tarea DTO Test");
        tareaDTO.setDescripcion("Descripción DTO Test");
    }

    @Test
    void findAll_ReturnsAllUbicaciones() {
        when(ubicacionRepository.findAll()).thenReturn(Arrays.asList(ubicacion));

        List<Ubicacion> result = ubicacionService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(ubicacion, result.get(0));
    }

    @Test
    void findById_ExistingId_ReturnsUbicacion() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));

        Ubicacion result = ubicacionService.findById(1L);

        assertNotNull(result);
        assertEquals(ubicacion, result);
    }

    @Test
    void findById_NotFound_ThrowsResponseStatusException() {
        when(ubicacionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> ubicacionService.findById(999L));
    }

    @Test
    void save_ValidUbicacion_SavesSuccessfully() {
        when(sociedadRepository.findById(anyLong())).thenReturn(Optional.of(sociedad));
        when(ubicacionRepository.save(any(Ubicacion.class))).thenReturn(ubicacion);

        Ubicacion result = ubicacionService.save(ubicacionDTO);

        assertNotNull(result);
        assertEquals(ubicacion.getName(), result.getName());
    }

    @Test
    void deleteById_ExistingId_DeletesUbicacion() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(ubicacionRepository.save(ubicacion)).thenReturn(ubicacion); // Simula el guardado

        assertDoesNotThrow(() -> ubicacionService.deleteById(1L));
        verify(ubicacionRepository, times(1)).save(ubicacion); // Verifica que save se ha llamado
    }

    @Test
    void deleteById_NotFound_ThrowsResponseStatusException() {
        when(ubicacionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> ubicacionService.deleteById(999L));
    }

    @Test
    void findByName_ExistingName_ReturnsList() {
        when(ubicacionRepository.findByName("Ubicacion Test")).thenReturn(Arrays.asList(ubicacion));

        List<Ubicacion> result = ubicacionService.findByName("Ubicacion Test");

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(ubicacion, result.get(0));
    }

    @Test
    void findAllBySociedad_SociedadExists_ReturnsList() {
        when(ubicacionRepository.findBySociedadCustomOrder(sociedad)).thenReturn(Arrays.asList(ubicacion));

        List<Ubicacion> result = ubicacionService.findAllBySociedad(sociedad);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(ubicacion, result.get(0));
    }

    @Test
    void getTareasByUbicacionId_ExistingId_ReturnsList() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(tareaRepository.findByUbicaciones(ubicacion)).thenReturn(Arrays.asList(tarea));

        List<Tarea> result = ubicacionService.getTareasByUbicacionId(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(tarea, result.get(0));
    }

    @Test
    void addTareaAUbicacion_ValidIds_AddsTareaToUbicacion() {
        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);
        when(ubicacionTareaRepository.save(any(UbicacionTarea.class))).thenReturn(new UbicacionTarea(ubicacion, tarea));

        Ubicacion result = ubicacionService.addTareaAUbicacion(1L, tareaDTO);

        assertNotNull(result);
        verify(ubicacionTareaRepository, times(1)).save(any(UbicacionTarea.class));
    }

    @Test
    void updateTareasDeUbicacion_ValidIds_UpdatesTareasDeUbicacion() {
        Set<TareaDTO> tareasDTO = new HashSet<>();
        tareasDTO.add(tareaDTO);

        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(tareaRepository.save(any(Tarea.class))).thenReturn(tarea);
        when(ubicacionTareaRepository.save(any(UbicacionTarea.class))).thenReturn(new UbicacionTarea(ubicacion, tarea));

        Ubicacion result = ubicacionService.updateTareasDeUbicacion(1L, tareasDTO);

        assertNotNull(result);
        verify(ubicacionTareaService, times(1)).deleteByUbicacion(ubicacion);
        verify(ubicacionTareaRepository, times(1)).save(any(UbicacionTarea.class));
    }

    @Test
    void updateUbicacion_ValidIdAndDTO_UpdatesUbicacion() {
        UbicacionDTO newUbicacionDTO = new UbicacionDTO();
        newUbicacionDTO.setName("Updated Ubicacion");
        newUbicacionDTO.setSociedadId(sociedad.getId());

        when(ubicacionRepository.findById(1L)).thenReturn(Optional.of(ubicacion));
        when(sociedadRepository.getById(sociedad.getId())).thenReturn(sociedad);

        // Asegúrate de que el objeto ubicacion se actualiza antes de ser devuelto
        when(ubicacionRepository.save(any(Ubicacion.class))).thenAnswer(invocation -> {
            Ubicacion savedUbicacion = invocation.getArgument(0);
            return savedUbicacion; // Devuelve la ubicación actualizada
        });

        Ubicacion result = ubicacionService.updateUbicacion(1L, newUbicacionDTO);

        assertNotNull(result);
        assertEquals("Updated Ubicacion", result.getName()); // Ahora debería pasar
        verify(ubicacionRepository, times(1)).save(any(Ubicacion.class)); // Verifica que save se ha llamado
    }

    @Test
    void findAllAvailableForTarea_TareaId_ReturnsAvailableUbicaciones() {
        List<UbicacionTarea> ubicacionTareas = Arrays.asList(new UbicacionTarea(ubicacion, tarea));
        when(ubicacionRepository.findAll()).thenReturn(Arrays.asList(ubicacion));
        when(ubicacionTareaRepository.findAllByTareaIdAndDeletedFalse(tarea.getId())).thenReturn(ubicacionTareas);

        List<Ubicacion> result = ubicacionService.findAllAvailableForTarea(tarea.getId());

        assertTrue(result.isEmpty()); // Suponiendo que todas las ubicaciones están ocupadas por la tarea
    }

    @Test
    void findAllBySociedadOrderedById_Sociedad_ReturnsUbicacionesOrdered() {
        when(ubicacionRepository.findAllBySociedadOrderById(sociedad)).thenReturn(Arrays.asList(ubicacion));

        List<Ubicacion> result = ubicacionService.findAllBySociedadOrderedById(sociedad);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(ubicacion, result.get(0));
    }
    // Aquí puedes añadir más pruebas según sea necesario para cubrir todos los métodos y casos de uso.
}