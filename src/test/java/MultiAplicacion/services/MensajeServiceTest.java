package MultiAplicacion.services;

import MultiAplicacion.entities.Admin;
import MultiAplicacion.entities.Mensaje;
import MultiAplicacion.entities.User;
import MultiAplicacion.entities.Worker;
import MultiAplicacion.repositories.MensajeRepository;
import MultiAplicacion.repositories.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class MensajeServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MensajeService mensajeService;

    private Mensaje mensaje;
    private Worker receptor;
    private Worker emisor;

    @BeforeEach
    void setUp() {
        receptor = new Worker();
        receptor.setId(1L);
        receptor.setName("Receptor");

        emisor = new Worker();
        emisor.setId(2L);
        emisor.setName("Emisor");

        mensaje = new Mensaje();
        mensaje.setId(1L);
        mensaje.setAsunto("Asunto Test");
        mensaje.setContenido("Contenido Test");
        mensaje.setEmisor(emisor);
        mensaje.setReceptor(receptor);
        mensaje.setCircular(false);
        mensaje.setFechaHora(LocalDateTime.now());
        mensaje.setLeido(false);
    }
    @Test
    void createMensaje_SavesMensajeWithCorrectAttributes() {
        when(mensajeRepository.save(any(Mensaje.class))).thenReturn(mensaje);

        Mensaje savedMensaje = mensajeService.createMensaje(mensaje);

        assertNotNull(savedMensaje);
        assertEquals("Asunto Test", savedMensaje.getAsunto());
        assertFalse(savedMensaje.isLeido());
        assertNotNull(savedMensaje.getFechaHora());

        verify(mensajeRepository).save(any(Mensaje.class));
    }

    @Test
    void findMensajeById_Found() {
        when(mensajeRepository.findById(1L)).thenReturn(Optional.of(mensaje));

        Optional<Mensaje> foundMensaje = mensajeService.findMensajeById(1L);

        assertTrue(foundMensaje.isPresent());
        assertEquals(1L, foundMensaje.get().getId());
    }

    @Test
    void findMensajeById_NotFound() {
        when(mensajeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Mensaje> foundMensaje = mensajeService.findMensajeById(1L);

        assertFalse(foundMensaje.isPresent());
    }

    @Test
    void findMensajesByReceptor_ReturnsMessages() {
        when(mensajeRepository.findByReceptor(receptor)).thenReturn(Arrays.asList(mensaje));

        List<Mensaje> mensajes = mensajeService.findMensajesByReceptor(receptor);

        assertFalse(mensajes.isEmpty());
        assertEquals(1, mensajes.size());
        assertEquals("Asunto Test", mensajes.get(0).getAsunto());
    }

    @Test
    void findMensajesByEmisor_ReturnsMessages() {
        when(mensajeRepository.findByEmisor(emisor)).thenReturn(Arrays.asList(mensaje));

        List<Mensaje> mensajes = mensajeService.findMensajesByEmisor(emisor);

        assertFalse(mensajes.isEmpty());
        assertEquals(1, mensajes.size());
        assertEquals("Asunto Test", mensajes.get(0).getAsunto());
    }

    @Test
    void findMensajesCirculares_ReturnsCircularMessages() {
        mensaje.setCircular(true);
        when(mensajeRepository.findByCircular(true)).thenReturn(Arrays.asList(mensaje));

        List<Mensaje> mensajes = mensajeService.findMensajesCirculares();

        assertFalse(mensajes.isEmpty());
        assertTrue(mensajes.get(0).isCircular());
    }

    @Test
    void findMensajesNoLeidos_ReturnsUnreadMessages() {
        when(mensajeRepository.findByReceptorAndLeido(receptor, false)).thenReturn(Arrays.asList(mensaje));

        List<Mensaje> mensajes = mensajeService.findMensajesNoLeidos(receptor);

        assertFalse(mensajes.isEmpty());
        assertFalse(mensajes.get(0).isLeido());
    }

    @Test
    void setMensajeLeido_SetsLeidoToTrue() {
        when(mensajeRepository.save(mensaje)).thenReturn(mensaje);

        Mensaje updatedMensaje = mensajeService.setMensajeLeido(mensaje, true);

        assertTrue(updatedMensaje.isLeido());
    }

    @Test
    void deleteMensaje_DeletesById() {
        doNothing().when(mensajeRepository).deleteById(1L);

        mensajeService.deleteMensaje(1L);

        verify(mensajeRepository).deleteById(1L);
    }

    // Aquí puedes añadir pruebas adicionales para cubrir más casos o escenarios de error.
}
