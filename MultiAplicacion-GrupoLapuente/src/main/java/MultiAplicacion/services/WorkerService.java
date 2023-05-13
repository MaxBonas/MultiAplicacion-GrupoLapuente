package MultiAplicacion.services;

import MultiAplicacion.DTOs.TareaCumplidaDTO;
import MultiAplicacion.DTOs.WorkerDTO;
import MultiAplicacion.ENUMs.Turno;
import MultiAplicacion.entities.*;
import MultiAplicacion.repositories.*;
import MultiAplicacion.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class WorkerService implements WorkerServiceInterface {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private TareaCumplidaRepository tareaCumplidaRepository;

    @Autowired
    private TareaRepository tareaRepository;

    @Autowired
    private UbicacionRepository ubicacionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InformeRepository informeRepository;

    @Autowired
    private SociedadRepository sociedadRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InformeService informeService;
    @Autowired
    private TareaCumplidaService tareaCumplidaService;
    @Autowired
    private UserService userService;

    @Override
    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    @Override
    public Worker getWorkerById(Long id) {
        return workerRepository.findById(id).orElse(null);
    }

    @Override
    public Worker saveWorker(WorkerDTO workerDTO) {
        Worker worker = new Worker();
        worker.setId(workerDTO.getId());
        worker.setName(workerDTO.getName());
        worker.setPassword(passwordEncoder.encode(workerDTO.getPassword()));
        worker.setCargo(workerDTO.getCargo());
        worker.setSociedad(sociedadRepository.findById(workerDTO.getSociedadId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe la sociedad con id: " + workerDTO.getSociedadId() + " en la base de datos")));
        // Guarda el worker en la base de datos antes de crear y guardar el role
        Worker savedWorker = workerRepository.save(worker);
        roleRepository.save(new Role("WORKER", savedWorker));

        return savedWorker;
    }

    @Override
    public void deleteWorkerById(Long id) {
        userService.deleteUserById(id);
    }


    @Override
    public Worker findByName(String name) {
        return workerRepository.findByName(name).orElseThrow();
    }

    @Override
    public void crearTareasCumplidasVacias(Long workerId, Long ubicacionId, LocalDateTime fecha, Turno turno) {
        Worker worker = workerRepository.findById(workerId).orElseThrow(() -> new NoSuchElementException("Trabajador no encontrado"));
        Ubicacion ubicacion = ubicacionRepository.findById(ubicacionId).orElseThrow(() -> new NoSuchElementException("Ubicación no encontrada"));

        List<TareaCumplida> tareasCumplidasExistentes = tareaCumplidaService.findTareasCumplidasByUbicacionAndFechaAndTurnoAndCumplida(ubicacion, fecha, turno, false);

        if (tareasCumplidasExistentes.isEmpty()) {
            List<Tarea> tareas = tareaRepository.findByUbicaciones(ubicacion);
            for (Tarea tarea : tareas) {
                TareaCumplidaDTO tareaCumplidaDTO = new TareaCumplidaDTO(tarea.getId(), tarea.getName(), workerId, ubicacionId, false, worker.getName(), fecha, turno, null);
                tareaCumplidaService.save(tareaCumplidaDTO);
            }
        }
    }

    @Override
    public Worker updateWorker(Long id, WorkerDTO workerDTO) {
        Worker worker = new Worker(workerDTO.getName(), workerRepository.getById(id).getPassword(), workerDTO.getCargo(), sociedadRepository.getById(workerDTO.getSociedadId()));
        worker.setId(id);
        return workerRepository.save(worker);
    }
    @Override
    public void cambiarPassword(Long workerId, String oldPassword, String newPassword) {
        Worker worker = workerRepository.findById(workerId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe el trabajador con id: " + workerId + " en la base de datos"));
        if (passwordEncoder.matches(oldPassword, worker.getPassword())) {
            worker.setPassword(passwordEncoder.encode(newPassword));
            workerRepository.save(worker);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La contraseña actual proporcionada es incorrecta.");
        }
    }
    @Override
    public List<Worker> getWorkersBySociedad(Long sociedadId) {
        return workerRepository.findBySociedadId(sociedadId);
    }
    @Override
    @Transactional
    public void deleteRoleByUserId(Long userId) {
        roleRepository.deleteByUserId(userId);
    }
}
