package MultiAplicacion.services;

import MultiAplicacion.entities.Tarea;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.entities.UbicacionTarea;
import MultiAplicacion.repositories.UbicacionTareaRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class UbicacionTareaServiceTest {

    @Mock
    private UbicacionTareaRepository ubicacionTareaRepository;

    @InjectMocks
    private UbicacionTareaService ubicacionTareaService;

    private UbicacionTarea ubicacionTarea;
    private Ubicacion ubicacion;
    private Tarea tarea;

    @BeforeEach
    void setUp() {
        ubicacion = new Ubicacion();
        ubicacion.setId(1L);
        ubicacion.setName("Ubicacion Test");

        tarea = new Tarea();
        tarea.setId(1L);
        tarea.setName("Tarea Test");

        ubicacionTarea = new UbicacionTarea();
        ubicacionTarea.setUbicacion(ubicacion);
        ubicacionTarea.setTarea(tarea);
        ubicacionTarea.setDeleted(false);
    }

    @Test
    void deleteByUbicacionIdAndTareaId_FoundAndDeleted() {
        when(ubicacionTareaRepository.findByUbicacionIdAndTareaId(ubicacion.getId(), tarea.getId())).thenReturn(ubicacionTarea);

        ubicacionTareaService.deleteByUbicacionIdAndTareaId(ubicacion.getId(), tarea.getId());

        assertTrue(ubicacionTarea.isDeleted());
        verify(ubicacionTareaRepository).save(ubicacionTarea);
    }

    @Test
    void getAllByUbicacionId_ReturnsList() {
        when(ubicacionTareaRepository.findAllByUbicacionId(ubicacion.getId())).thenReturn(Arrays.asList(ubicacionTarea));

        List<UbicacionTarea> result = ubicacionTareaService.getAllByUbicacionId(ubicacion.getId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(ubicacionTarea, result.get(0));
    }

    @Test
    void getAllByTareaId_ReturnsList() {
        when(ubicacionTareaRepository.findAllByTareaIdAndDeletedFalse(tarea.getId())).thenReturn(Arrays.asList(ubicacionTarea));

        List<UbicacionTarea> result = ubicacionTareaService.getAllByTareaId(tarea.getId());

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(ubicacionTarea, result.get(0));
    }

    @Test
    void deleteByUbicacion_DeletesAllUbicacionTareas() {
        // Preparación: Devuelve una lista que contiene ubicacionTarea cuando se busque por ID de ubicación
        when(ubicacionTareaRepository.findAllByUbicacionId(ubicacion.getId())).thenReturn(Arrays.asList(ubicacionTarea));

        // Simulación: Devuelve ubicacionTarea después de cualquier operación de guardado
        when(ubicacionTareaRepository.save(any(UbicacionTarea.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Acción: Intenta borrar todas las UbicacionTarea asociadas a una ubicación
        ubicacionTareaService.deleteByUbicacion(ubicacion);

        // Verificación: Asegura que el estado "borrado" se establece en verdadero para todas las ubicacionTarea relevantes
        assertTrue(ubicacionTarea.isDeleted());

        // Verifica que el método save se llama la cantidad correcta de veces y con los argumentos esperados
        verify(ubicacionTareaRepository, times(1)).save(ubicacionTarea);
    }

}
