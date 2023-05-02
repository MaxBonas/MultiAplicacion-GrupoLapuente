package MultiAplicacion;


import MultiAplicacion.DTOs.TareaCumplidaListWrapper;
import MultiAplicacion.DTOs.TareaDTO;
import MultiAplicacion.DTOs.WorkerDTO;
import MultiAplicacion.DTOs.UbicacionDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.controllers.AdminController;
import MultiAplicacion.controllers.WorkerController;
import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.*;
import MultiAplicacion.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdminController adminController;

    @MockBean
    private SociedadService sociedadService;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private TareaService tareaService;

    @MockBean
    private UbicacionService ubicacionService;

    @MockBean
    private TareaCumplidaService tareaCumplidaService;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private SociedadRepository sociedadRepository;

    Admin admin;

    @BeforeEach
    void setUp() {
        // Configurar los datos de prueba y las respuestas de los servicios aquí
        Sociedad sociedad1 = new Sociedad();
        sociedad1.setId(1L);
        sociedad1.setName("Sociedad Test1");

        Sociedad sociedad2 = new Sociedad();
        sociedad2.setId(1L);
        sociedad2.setName("Sociedad Test2");

        List<Sociedad> sociedadList = new ArrayList<>();
        sociedadList.add(sociedad1);
        sociedadList.add(sociedad2);

        when(sociedadService.findAll()).thenReturn(sociedadList);

        admin = new Admin();
        admin.setName("admin1");
        admin.setPassword("1234");
        admin.setSociedad(sociedad1);

        when(adminRepository.findByName(any())).thenReturn(Optional.of(admin));
    }

    @Test
    void contextLoads() {
        // Verificar que el controlador se haya cargado correctamente
        assertNotNull(adminController);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminMenuTest() throws Exception {
        mockMvc.perform(get("/admin/1/adminsmenu"))
                .andExpect(status().isOk())
                .andExpect(view().name("admins/adminsmenu"))
                .andExpect(model().attributeExists("sociedades"));

        // Verificar que el servicio 'findAll' de SociedadService haya sido llamado
        verify(sociedadService).findAll();
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllWorkersTest() throws Exception {
        // Crear una lista de trabajadores para la prueba
        List<Worker> workers = new ArrayList<>();
        Worker worker1 = new Worker();
        worker1.setId(1L);
        worker1.setName("Worker Test1");
        workers.add(worker1);

        when(workerService.getWorkersBySociedad(1L)).thenReturn(workers);

        mockMvc.perform(get("/admin/1/workers"))
                .andExpect(status().isOk())
                .andExpect(view().name("admins/workers"))
                .andExpect(model().attributeExists("workers"));

        verify(workerService).getWorkersBySociedad(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addWorkerTest() throws Exception {
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setName("Worker Test3");
        workerDTO.setCargo("Cargo Test3");

        mockMvc.perform(post("/admin/1/crear-trabajador")
                        .flashAttr("workerDTO", workerDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/1/workers"));

        verify(workerService).saveWorker(workerDTO);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateWorkerTest() throws Exception {
        WorkerDTO workerDTO = new WorkerDTO();
        workerDTO.setId(1L);
        workerDTO.setName("Worker Test Updated");
        workerDTO.setCargo("Cargo Test Updated");

        mockMvc.perform(post("/admin/1/workers/1/update")
                        .flashAttr("workerDTO", workerDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/1/workers"));

        verify(workerService).updateWorker(1L, workerDTO);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteWorkerTest() throws Exception {
        mockMvc.perform(get("/admin/1/workers/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/1/workers"));

        verify(workerService).deleteWorkerById(1L);
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void showAssignTareaFormTest() throws Exception {
        Long ubicacionId = 1L;
        List<Tarea> tareas = new ArrayList<>();
        Tarea tarea1 = new Tarea();
        tarea1.setId(1L);
        tarea1.setName("Tarea Test1");
        tareas.add(tarea1);

        when(tareaService.findAllDistinctByName()).thenReturn(tareas);

        mockMvc.perform(get("/admin/1/ubicaciones/asignar").param("ubicacionId", ubicacionId.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("admins/asignar-tareas"))
                .andExpect(model().attributeExists("tareas"))
                .andExpect(model().attribute("ubicacionId", ubicacionId));

        verify(tareaService).findAllDistinctByName();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createAndAssignTareaTest() throws Exception {
        Long ubicacionId = 1L;
        Long tareaId = 1L;
        String name = "Tarea Test1";
        String descripcion = "Descripcion Test1";

        Tarea tareaOriginal = new Tarea();
        tareaOriginal.setId(1L);
        tareaOriginal.setName(name);
        tareaOriginal.setDescripcion(descripcion);

        when(tareaService.getTareaById(tareaId)).thenReturn(tareaOriginal);

        mockMvc.perform(post("/admin/1/ubicaciones/asignar")
                        .param("ubicacionId", ubicacionId.toString())
                        .param("tareaId", tareaId.toString())
                        .param("name", name)
                        .param("descripcion", descripcion))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/1/ubicaciones"));

        verify(tareaService).saveTarea(any(TareaDTO.class));
        verify(ubicacionService).addTareaAUbicacion(eq(ubicacionId), any(TareaDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void showEditTareaFormTest() throws Exception {
        Long tareaId = 1L;
        Tarea tarea = new Tarea();
        tarea.setId(tareaId);
        tarea.setName("Tarea Test1");
        tarea.setDescripcion("Descripcion Test1");

        when(tareaService.getTareaById(tareaId)).thenReturn(tarea);

        mockMvc.perform(get("/admin/1/tareas/editar/{id}", tareaId))
                .andExpect(status().isOk())
                .andExpect(view().name("admins/editar-tarea"))
                .andExpect(model().attributeExists("tareaDTO"));

        verify(tareaService).getTareaById(tareaId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateTareaTest() throws Exception {
        Long sociedadId = 1L;
        Long tareaId = 1L;
        TareaDTO tareaDTO = new TareaDTO();
        tareaDTO.setId(tareaId);
        tareaDTO.setName("Tarea Test1");
        tareaDTO.setDescripcion("Descripcion Test1");

        mockMvc.perform(post("/admin/{sociedadId}/tareas/{id}/update", sociedadId, tareaId)
                        .flashAttr("tareaDTO", tareaDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/" + sociedadId + "/ubicaciones"));

        verify(tareaService).updateTarea(tareaId, tareaDTO);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTareaTest() throws Exception {
        Long sociedadId = 1L;
        Long tareaId = 1L;

        mockMvc.perform(get("/admin/{sociedadId}/tareas/{id}/delete", sociedadId, tareaId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/" + sociedadId + "/tareas"));

        verify(tareaService).deleteTareaById(tareaId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUbicacionesTest() throws Exception {
        Long sociedadId = 1L;
        List<Ubicacion> ubicaciones = new ArrayList<>();
        Ubicacion ubicacion1 = new Ubicacion();
        ubicacion1.setId(1L);
        ubicaciones.add(ubicacion1);

        when(sociedadService.findById(sociedadId)).thenReturn(Optional.of(new Sociedad()));
        when(ubicacionService.findAllBySociedadOrderedById(any(Sociedad.class))).thenReturn(ubicaciones);

        mockMvc.perform(get("/admin/{sociedadId}/ubicaciones", sociedadId))
                .andExpect(status().isOk())
                .andExpect(view().name("admins/ubicaciones"))
                .andExpect(model().attributeExists("ubicaciones"))
                .andExpect(model().attributeExists("tareasAgrupadasPorUbicacion"));

        verify(sociedadService).findById(sociedadId);
        verify(ubicacionService).findAllBySociedadOrderedById(any(Sociedad.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addUbicacionTest() throws Exception {
        Long sociedadId = 1L;
        UbicacionDTO ubicacionDTO = new UbicacionDTO();
        ubicacionDTO.setName("Ubicacion Test1");

        mockMvc.perform(post("/admin/{sociedadId}/ubicaciones", sociedadId)
                        .flashAttr("ubicacionDTO", ubicacionDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/" + sociedadId + "/ubicaciones"));

        verify(ubicacionService).save(ubicacionDTO);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUbicacionTest() throws Exception {
        Long sociedadId = 1L;
        Long ubicacionId = 1L;
        UbicacionDTO ubicacionDTO = new UbicacionDTO();
        ubicacionDTO.setId(ubicacionId);
        ubicacionDTO.setName("Ubicacion Test1 Updated");

        mockMvc.perform(post("/admin/{sociedadId}/ubicaciones/{id}/update", sociedadId, ubicacionId)
                        .flashAttr("ubicacionDTO", ubicacionDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/" + sociedadId + "/ubicaciones"));

        verify(ubicacionService).updateUbicacion(ubicacionId, ubicacionDTO);
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUbicacionTest() throws Exception {
        Long sociedadId = 1L;
        Long ubicacionId = 1L;

        mockMvc.perform(get("/admin/{sociedadId}/ubicaciones/{id}/delete", sociedadId, ubicacionId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/" + sociedadId + "/ubicaciones"));

        verify(ubicacionService).deleteById(ubicacionId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void informeDiarioRequestTest() throws Exception {
        Long sociedadId = 1L;

        mockMvc.perform(get("/admin/{sociedadId}/informes/diario/request", sociedadId))
                .andExpect(status().isOk())
                .andExpect(view().name("informes/informeDiarioRequest"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void informeDiarioTest() throws Exception {
        Long sociedadId = 1L;
        LocalDate fecha = LocalDate.now();

        mockMvc.perform(post("/admin/{sociedadId}/informes/diario", sociedadId)
                        .param("fecha", fecha.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("informes/informeDiario"))
                .andExpect(model().attributeExists("ubicaciones"))
                .andExpect(model().attributeExists("tareasCumplidasMananaMap"))
                .andExpect(model().attributeExists("tareasCumplidasTardeMap"))
                .andExpect(model().attribute("fecha", fecha));

        verify(ubicacionService).findAll();
        verify(tareaCumplidaService, times(2)).findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), any(LocalDateTime.class), any(Turno.class));
    }
// Agrega casos de prueba para los demás métodos siguiendo el mismo patrón.

}
