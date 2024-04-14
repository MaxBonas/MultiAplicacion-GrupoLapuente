package MultiAplicacion;

import MultiAplicacion.DTOs.TareaCumplidaListWrapper;
import MultiAplicacion.controllers.WorkerController;
import MultiAplicacion.entities.Sociedad;
import MultiAplicacion.entities.TareaCumplida;
import MultiAplicacion.entities.Ubicacion;
import MultiAplicacion.entities.Worker;
import MultiAplicacion.repositories.SociedadRepository;
import MultiAplicacion.repositories.TareaCumplidaRepository;
import MultiAplicacion.repositories.UbicacionRepository;
import MultiAplicacion.repositories.WorkerRepository;
import MultiAplicacion.services.TareaCumplidaService;
import MultiAplicacion.services.UbicacionService;
import MultiAplicacion.services.WorkerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class WorkerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkerController workerController;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private UbicacionService ubicacionService;

    @MockBean
    private WorkerRepository workerRepository;

    @MockBean
    private UbicacionRepository ubicacionRepository;

    @MockBean
    private TareaCumplidaRepository tareaCumplidaRepository;

    @MockBean
    private TareaCumplidaService tareaCumplidaService;

    @MockBean
    private SociedadRepository sociedadRepository;

    @BeforeEach
    void setUp() {
        // Configurar los datos de prueba y las respuestas de los servicios aquí
        Worker worker = new Worker();
        worker.setName("worker1");
        worker.setPassword("1234");

        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        sociedad.setName("Sociedad Test");
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        List<Ubicacion> ubicaciones = new ArrayList<>();
        Ubicacion ubicacion1 = new Ubicacion();
        ubicacion1.setId(1L);
        ubicaciones.add(ubicacion1);

        when(ubicacionService.findAllBySociedad(any())).thenReturn(ubicaciones);
        when(ubicacionService.findById(any())).thenReturn(ubicacion1);
    }


    @Test
    void contextLoads() {
        // Verificar que el controlador se haya cargado correctamente
        assertNotNull(workerController);
    }


    @Test
    @WithMockUser(roles = "WORKER")
    void getAllUbicacionesTest() throws Exception {
        mockMvc.perform(get("/worker/1/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/workersubicaciones"))
                .andExpect(model().attributeExists("worker", "ubicaciones"));

        verify(workerService).findByName(any());
        verify(ubicacionService).findAllBySociedad(any());
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void selectTurnoTest() throws Exception {
        mockMvc.perform(get("/worker/1/ubicaciones/1/selectturno"))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/workersturno"))
                .andExpect(model().attributeExists("worker", "ubicacionId"));

        verify(workerService).findByName(any());
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void showTareasTest() throws Exception {
        mockMvc.perform(get("/worker/1/ubicaciones/1/tareas")
                        .param("turno", "MANANA")
                        .param("workers", "worker1")) // Añade esto
                .andExpect(status().isOk())
                .andExpect(view().name("workers/workerstareas"))
                .andExpect(model().attributeExists("worker", "ubicacion", "turnoInformado", "tareasCumplidasNo", "tareasCumplidasSi", "tareaCumplida", "tareaCumplidaListWrapper"));

        verify(workerService).findByName(any());
        verify(ubicacionService).findById(any());
    }


    @Test
    @WithMockUser(roles = "WORKER")
    void updateTareasTest() throws Exception {
        TareaCumplida tareaCumplida1 = new TareaCumplida();
        tareaCumplida1.setId(1L);
        List<TareaCumplida> tareasCumplidasNo = Collections.singletonList(tareaCumplida1);

        when(tareaCumplidaService.updateTareaCumplida(any(), any())).thenReturn(tareaCumplida1);

        TareaCumplidaListWrapper tareaCumplidaListWrapper = new TareaCumplidaListWrapper();
        tareaCumplidaListWrapper.setTareasCumplidas(tareasCumplidasNo);

        mockMvc.perform(post("/worker/1/ubicaciones/1/tareas")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("turno", "MANANA")
                        .param("workers", "worker1")
                        .flashAttr("tareaCumplidaListWrapper", tareaCumplidaListWrapper))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/worker/1/ubicaciones/1/tareas?turno=MANANA&workers=worker1"));

        verify(workerService).findByName(any());
        verify(tareaCumplidaService).updateTareaCumplida(any(), any());
    }





    @Test
    @WithMockUser(roles = "WORKER")
    void workerMenuTest() throws Exception {
        mockMvc.perform(get("/worker/1/workersmenu"))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/workersmenu"))
                .andExpect(model().attributeExists("worker"));

        verify(workerService).findByName(any());
    }



    @Test
    @WithMockUser(roles = "ANOTHER_ROLE")
    void accessDeniedTest() throws Exception {
        mockMvc.perform(get("/worker/1/ubicaciones"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/worker/1/ubicaciones/1/selectturno"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/worker/1/ubicaciones/1/tareas").param("turno", "MANANA"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/worker/1/password"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/worker/1/workersmenu"))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser(roles = "WORKER")
    void getAllUbicacionesUnauthorizedSociedadTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        mockMvc.perform(get("/worker/2/ubicaciones"))
                .andExpect(status().isForbidden());

        verify(workerService).findByName(any());
    }
    @Test
    @WithMockUser(roles = "WORKER")
    void getAllUbicacionesEmptyListTest() throws Exception {
        when(ubicacionService.findAllBySociedad(any())).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/worker/1/ubicaciones"))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/workersubicaciones"))
                .andExpect(model().attributeExists("worker", "ubicaciones"));

        verify(workerService).findByName(any());
        verify(ubicacionService).findAllBySociedad(any());
    }
    @Test
    @WithMockUser(roles = "WORKER")
    void selectTurnoUnauthorizedSociedadTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        mockMvc.perform(get("/worker/2/ubicaciones/1/selectturno"))
                .andExpect(status().isForbidden());

        verify(workerService).findByName(any());
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void showTareasUnauthorizedSociedadTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        mockMvc.perform(get("/worker/2/ubicaciones/1/tareas")
                        .param("turno", "MANANA")
                        .param("workers", "worker1"))  // Agregado el parámetro 'workers'
                .andExpect(status().isForbidden());

        verify(workerService).findByName(any());
    }


    @Test
    @WithMockUser(roles = "WORKER")
    void updateTareasUnauthorizedSociedadTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        TareaCumplida tareaCumplida1 = new TareaCumplida();
        tareaCumplida1.setId(1L);
        List<TareaCumplida> tareasCumplidasNo = Collections.singletonList(tareaCumplida1);

        TareaCumplidaListWrapper tareaCumplidaListWrapper = new TareaCumplidaListWrapper();
        tareaCumplidaListWrapper.setTareasCumplidas(tareasCumplidasNo);

        mockMvc.perform(post("/worker/2/ubicaciones/1/tareas")
                        .param("turno", "MANANA")
                        .param("workers", "worker1") // Agregamos el parámetro 'workers'
                        .flashAttr("tareaCumplidaListWrapper", tareaCumplidaListWrapper))
                .andExpect(status().isForbidden());

        verify(workerService).findByName(any());
    }




    @Test
    @WithMockUser(roles = "WORKER")
    void workerMenuUnauthorizedSociedadTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        mockMvc.perform(get("/worker/2/workersmenu"))
                .andExpect(status().isForbidden());

        verify(workerService).findByName(any());
    }

    @Test
    @WithMockUser(roles = "WORKER")
    void updateTareasEmptyListTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        TareaCumplidaListWrapper tareaCumplidaListWrapper = new TareaCumplidaListWrapper();
        tareaCumplidaListWrapper.setTareasCumplidas(new ArrayList<>());

        mockMvc.perform(post("/worker/1/ubicaciones/1/tareas")
                        .param("turno", "MANANA")
                        .param("workers", "worker1")
                        .flashAttr("tareaCumplidaListWrapper", tareaCumplidaListWrapper))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/worker/1/ubicaciones/1/tareas?turno=MANANA&workers=worker1")); // Aquí se cambia la URL esperada

        verify(workerService).findByName(any());
        verify(tareaCumplidaService, never()).updateTareaCumplida(any(), any());
    }




    //Estos son los tests de cambiar password para el worker que ahora mismo no usamos

    /*
    @Test
    @WithMockUser(roles = "WORKER")
    void cambiarPasswordInvalidOldPasswordTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);
        doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña actual es incorrecta")).when(workerService).cambiarPassword(any(), any(), any());

        mockMvc.perform(post("/worker/1/password")
                        .param("oldPassword", "incorrectOldPassword")
                        .param("newPassword", "newPassword")
                        .param("confirmNewPassword", "newPassword"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/worker/1/workersmenu"));

        verify(workerService).findByName(any());
        verify(workerService).cambiarPassword(any(), any(), any());
    }


    @Test
    @WithMockUser(roles = "WORKER")
    void showChangePasswordFormTest() throws Exception {
        mockMvc.perform(get("/worker/1/password"))
                .andExpect(status().isOk())
                .andExpect(view().name("workers/cambiar-password"))
                .andExpect(model().attributeExists("worker"));

        verify(workerService).findByName(any());
    }
    @Test
    @WithMockUser(roles = "WORKER")
    void cambiarPasswordEmptyInputTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        mockMvc.perform(post("/worker/1/password")
                        .param("oldPassword", "")
                        .param("newPassword", "")
                        .param("confirmNewPassword", ""))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/worker/1/password"));

        verify(workerService).findByName(any());
        verify(workerService, never()).cambiarPassword(any(), any(), any());
    }
    @Test
    @WithMockUser(roles = "WORKER")
    void cambiarPasswordTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        mockMvc.perform(post("/worker/1/password")
                        .param("oldPassword", "oldPassword")
                        .param("newPassword", "newPassword")
                        .param("confirmNewPassword", "newPassword"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/worker/1/workersmenu"));

        verify(workerService).findByName(any());
        verify(workerService).cambiarPassword(any(), any(), any());
    }
    @Test
    @WithMockUser(roles = "WORKER")
    void showChangePasswordFormUnauthorizedSociedadTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        mockMvc.perform(get("/worker/2/password"))
                .andExpect(status().isForbidden());

        verify(workerService).findByName(any());
    }
    @Test
    @WithMockUser(roles = "WORKER")
    void cambiarPasswordMismatchTest() throws Exception {
        Worker worker = new Worker();
        Sociedad sociedad = new Sociedad();
        sociedad.setId(1L);
        worker.setSociedad(sociedad);

        when(workerService.findByName(any())).thenReturn(worker);

        mockMvc.perform(post("/worker/1/password")
                        .param("oldPassword", "oldPassword")
                        .param("newPassword", "newPassword")
                        .param("confirmNewPassword", "differentNewPassword"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/worker/1/password"));

        verify(workerService).findByName(any());
        verify(workerService, never()).cambiarPassword(any(), any(), any());
    }
    */
}
