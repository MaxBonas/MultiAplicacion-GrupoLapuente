package MultiAplicacion;


import MultiAplicacion.DTOs.TareaDTO;
import MultiAplicacion.DTOs.WorkerDTO;
import MultiAplicacion.DTOs.UbicacionDTO;
import MultiAplicacion.ENUMs.Cargo;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.controllers.AdminController;
import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.*;
import MultiAplicacion.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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

    @MockBean
    private QuestionRepository questionRepository;

    @MockBean
    private QuizRecordService quizRecordService;

    Admin admin;

    @BeforeEach
    void setUp() {
        // Configurar los datos de prueba y las respuestas de los servicios aquí
        Sociedad sociedad1 = new Sociedad();
        sociedad1.setId(1L);
        sociedad1.setName("Sociedad Test1");

        Sociedad sociedad2 = new Sociedad();
        sociedad2.setId(2L); // Asegúrate de que cada Sociedad tenga un ID único.
        sociedad2.setName("Sociedad Test2");

        List<Sociedad> sociedadList = new ArrayList<>();
        sociedadList.add(sociedad1);
        sociedadList.add(sociedad2);

        when(sociedadService.findAll()).thenReturn(sociedadList);
        when(sociedadService.findById(1L)).thenReturn(Optional.of(sociedad1)); // Agrega esta línea.

        admin = new Admin();
        admin.setName("admin1");
        admin.setPassword("1234");
        admin.setSociedad(sociedad1);

        when(adminRepository.findByName(any())).thenReturn(Optional.of(admin));

        when(adminRepository.findByName("admin1")).thenReturn(Optional.of(admin));

        List<Ubicacion> ubicaciones = new ArrayList<>();
        Ubicacion ubicacion1 = new Ubicacion();
        ubicacion1.setId(1L);
        ubicacion1.setName("Ubicacion Test1");

        Ubicacion ubicacion2 = new Ubicacion();
        ubicacion2.setId(2L);
        ubicacion2.setName("Ubicacion Test2");

        ubicaciones.add(ubicacion1);
        ubicaciones.add(ubicacion2);

        when(ubicacionService.findAllBySociedad(sociedad1)).thenReturn(ubicaciones);

        Question question = new Question();
        question.setId(1L);
        question.setText("Is safety gear required?");
        List<Question> questions = Arrays.asList(question);

        when(questionRepository.findAll()).thenReturn(questions);

        // Configuración de QuizRecord con Usuario correctamente asociado
        QuizRecord quizRecord = new QuizRecord();
        quizRecord.setId(1L);
        quizRecord.setUser(admin);  // Asegúrate que User no es null
        quizRecord.setDate(LocalDateTime.now());
        quizRecord.setPassed(true);

        List<QuizRecord> passedRecords = Arrays.asList(quizRecord);
        List<QuizRecord> failedRecords = new ArrayList<>();

        when(quizRecordService.findQuizRecordsByDateRangeAndStatus(any(LocalDateTime.class), any(LocalDateTime.class), eq(true)))
                .thenReturn(passedRecords);
        when(quizRecordService.findQuizRecordsByDateRangeAndStatus(any(LocalDateTime.class), any(LocalDateTime.class), eq(false)))
                .thenReturn(failedRecords);

        when(quizRecordService.formatQuizRecordDates(passedRecords))
                .thenReturn(Arrays.asList("2023-01-01 00:00")); // Proporciona datos consistentes
        when(quizRecordService.formatQuizRecordDates(failedRecords))
                .thenReturn(new ArrayList<>()); // Lista vacía para fechas fallidas
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
        workerDTO.setCargo(Cargo.OPERARIO);

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
        workerDTO.setCargo(Cargo.JEFE_DE_EQUIPO);

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
                .andExpect(redirectedUrl("/admin/" + sociedadId + "/ubicaciones"));

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
        when(ubicacionService.findAllBySociedad(any(Sociedad.class))).thenReturn(ubicaciones);

        mockMvc.perform(get("/admin/{sociedadId}/ubicaciones", sociedadId))
                .andExpect(status().isOk())
                .andExpect(view().name("admins/ubicaciones"))
                .andExpect(model().attributeExists("ubicaciones"))
                .andExpect(model().attributeExists("tareasAgrupadasPorUbicacion"));

        verify(sociedadService).findById(sociedadId);
        verify(ubicacionService).findAllBySociedad(any(Sociedad.class));
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

        verify(ubicacionService).findAllBySociedad(any()); // Se modificó esta línea
        verify(tareaCumplidaService, times(2)).findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), eq(fecha.atStartOfDay()), eq(Turno.MANANA));
        verify(tareaCumplidaService, times(2)).findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), eq(fecha.atStartOfDay()), eq(Turno.TARDE));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportarInformeDiarioExcelTest() throws Exception {
        Long sociedadId = 1L;
        LocalDate fecha = LocalDate.now();

        when(tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), eq(fecha.atStartOfDay()), eq(Turno.MANANA)))
                .thenReturn(Collections.emptyList());
        when(tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), eq(fecha.atStartOfDay()), eq(Turno.TARDE)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/admin/{sociedadId}/informes/diario/export", sociedadId)
                        .param("fecha", fecha.toString()))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=Informe_Diario_Tareas" + fecha + ".xlsx"))
                .andExpect(content().contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        verify(ubicacionService).findAllBySociedad(any(Sociedad.class)); // Reemplazamos 'findAll()' por 'findAllBySociedad()'.
        verify(tareaCumplidaService, times(2)).findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), eq(fecha.atStartOfDay()), eq(Turno.MANANA));
        verify(tareaCumplidaService, times(2)).findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), eq(fecha.atStartOfDay()), eq(Turno.TARDE));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void informeDiarioNoTareasTest() throws Exception {
        Long sociedadId = 1L;
        LocalDate fecha = LocalDate.now().minusDays(1);

        when(tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), eq(fecha.atStartOfDay()), any(Turno.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(post("/admin/{sociedadId}/informes/diario", sociedadId)
                        .param("fecha", fecha.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("informes/informeDiario"))
                .andExpect(model().attributeExists("ubicaciones", "tareasCumplidasMananaMap", "tareasCumplidasTardeMap"))
                .andExpect(model().attribute("fecha", fecha))
                .andExpect(model().attribute("message", "No se encontraron tareas cumplidas para la fecha proporcionada."));

        verify(ubicacionService).findAllBySociedad(any(Sociedad.class));
        verify(tareaCumplidaService, times(2)).findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), eq(fecha.atStartOfDay()), eq(Turno.MANANA));
        verify(tareaCumplidaService, times(2)).findTareasCumplidasByUbicacionAndFechaAndTurno(any(Ubicacion.class), eq(fecha.atStartOfDay()), eq(Turno.TARDE));
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    void informeDiarioInvalidDateTest() throws Exception {
        Long sociedadId = 1L;
        String invalidDate = "invalid-date";

        mockMvc.perform(post("/admin/{sociedadId}/informes/diario", sociedadId)
                        .param("fecha", invalidDate))
                .andExpect(status().isOk())
                .andExpect(view().name("/error"))
                .andExpect(model().attribute("message", "La fecha proporcionada no es válida."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void informeDiarioFutureDateTest() throws Exception {
        Long sociedadId = 1L;
        LocalDate futureDate = LocalDate.now().plusDays(1);

        mockMvc.perform(post("/admin/{sociedadId}/informes/diario", sociedadId)
                        .param("fecha", futureDate.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("/error"))
                .andExpect(model().attribute("message", "La fecha proporcionada no puede ser una fecha futura."));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void showEditQuestionsForm_ReturnsCorrectView() throws Exception {
        mockMvc.perform(get("/admin/1/edit-questions"))
                .andExpect(status().isOk())
                .andExpect(view().name("admins/edit-questions"))
                .andExpect(model().attributeExists("questions", "sociedadId"))
                .andExpect(model().attribute("sociedadId", 1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateQuestions_RedirectsCorrectly() throws Exception {
        // Asegúrate de que la pregunta exista y devuelva un objeto válido
        Question existingQuestion = new Question();
        existingQuestion.setId(1L);
        existingQuestion.setText("Original Text");

        when(questionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion));

        mockMvc.perform(post("/admin/1/update-questions")
                        .param("question_1", "Updated text"))  // Asegúrate de que los parámetros coincidan con lo esperado en el controlador
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/1/edit-questions"));

        verify(questionRepository).save(any(Question.class));  // Verifica que se guarde la pregunta actualizada

        // Opcional: Verifica que la pregunta fue actualizada con el nuevo texto
        ArgumentCaptor<Question> questionCaptor = ArgumentCaptor.forClass(Question.class);
        verify(questionRepository).save(questionCaptor.capture());
        assertEquals("Updated text", questionCaptor.getValue().getText());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void showQuizReports_ReturnsCorrectView() throws Exception {
        LocalDateTime startDate = LocalDate.now().minusDays(7).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().atTime(23, 59, 59);

        when(quizRecordService.findQuizRecordsByDateRangeAndStatus(startDate, endDate, true))
                .thenReturn(Collections.singletonList(new QuizRecord()));
        when(quizRecordService.findQuizRecordsByDateRangeAndStatus(startDate, endDate, false))
                .thenReturn(Collections.singletonList(new QuizRecord()));
        when(quizRecordService.formatQuizRecordDates(any()))
                .thenReturn(Collections.singletonList("2023-01-01 00:00"));

        mockMvc.perform(get("/admin/1/informes/informeCuestionario"))
                .andExpect(status().isOk())
                .andExpect(view().name("informes/informeCuestionario"))
                .andExpect(model().attributeExists("startDate", "endDate", "passedRecords", "failedRecords", "formattedPassedDates", "formattedFailedDates"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void filterQuizReports_ReturnsUpdatedData() throws Exception {
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        mockMvc.perform(post("/admin/1/informes/informeCuestionario")
                        .param("startDate", startDate.toString())  // LocalDate formato YYYY-MM-DD
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("informes/informeCuestionario"))
                .andExpect(model().attributeExists("startDate", "endDate", "passedRecords", "failedRecords", "formattedPassedDates", "formattedFailedDates"))
                .andExpect(model().attribute("startDate", startDate))
                .andExpect(model().attribute("endDate", endDate));

        verify(quizRecordService).findQuizRecordsByDateRangeAndStatus(startDateTime, endDateTime, true);
        verify(quizRecordService).findQuizRecordsByDateRangeAndStatus(startDateTime, endDateTime, false);
    }


    /*
    @Test
@WithMockUser(roles = "USER")
void adminMenuUnauthorizedTest() throws Exception {
...
}

@Test
@WithMockUser(roles = "USER")
void getAllWorkersUnauthorizedTest() throws Exception {
...
}

// Realizar pruebas de acceso no autorizado para todos los demás métodos

@Test
@WithMockUser(roles = "ADMIN")
void addWorkerInvalidInputTest() throws Exception {
...
}

@Test
@WithMockUser(roles = "ADMIN")
void updateWorkerInvalidInputTest() throws Exception {
...
}

// Realizar pruebas de entradas no válidas para todos los demás métodos que aceptan entradas del usuario

@Test
@WithMockUser(roles = "ADMIN")
void getNonExistingWorkerTest() throws Exception {
...
}

@Test
@WithMockUser(roles = "ADMIN")
void updateNonExistingWorkerTest() throws Exception {
...
}

@Test
@WithMockUser(roles = "ADMIN")
void deleteNonExistingWorkerTest() throws Exception {
...
}

// Realizar pruebas para casos en los que no se encuentren registros para todos los demás métodos que interactúan con registros

@Test
@WithMockUser(roles = "USER")
void informeDiarioUnauthorizedTest() throws Exception {
...
}

@Test
@WithMockUser(roles = "ADMIN")
void informeDiarioInvalidDateTest() throws Exception {
...
}

@Test
@WithMockUser(roles = "ADMIN")
void informeDiarioNoTareasCumplidasTest() throws Exception {
...
}

@Test
@WithMockUser(roles = "USER")
void exportarInformeDiarioExcelUnauthorizedTest() throws Exception {
...
}

@Test
@WithMockUser(roles = "ADMIN")
void exportarInformeDiarioExcelInvalidDateTest() throws Exception {
...
}

@Test
@WithMockUser(roles = "ADMIN")
void exportarInformeDiarioExcelNoTareasCumplidasTest() throws Exception {
...
}

     */
}
