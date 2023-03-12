package Lapuente.TareasUbicaciones.controllers;


import Lapuente.TareasUbicaciones.controllers.interfaces.AdminControllerInterface;
import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import Lapuente.TareasUbicaciones.services.interfaces.TareaServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.UbicacionServiceInterface;
import Lapuente.TareasUbicaciones.services.interfaces.WorkerServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController implements AdminControllerInterface {

    @Autowired
    private WorkerServiceInterface workerService;

    @Autowired
    private TareaServiceInterface tareaService;

    @Autowired
    private UbicacionServiceInterface ubicacionService;

    @Override
    @PostMapping("/workers")
    @ResponseStatus(HttpStatus.CREATED)
    public Worker addWorker(@RequestBody Worker workerDTO) {
        return workerService.saveWorker(workerDTO);
    }

    @Override
    @GetMapping("/workers")
    @ResponseStatus(HttpStatus.OK)
    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }

    @Override
    @GetMapping("/tareas")
    @ResponseStatus(HttpStatus.OK)
    public List<Tarea> getAllTareas() {
        return tareaService.getAllTareas();
    }

    @Override
    @PostMapping("/tareas")
    @ResponseStatus(HttpStatus.CREATED)
    public Tarea addTarea(@RequestBody Tarea tareaDTO) {
        return tareaService.saveTarea(tareaDTO);
    }

    @Override
    @GetMapping("/ubicaciones")
    @ResponseStatus(HttpStatus.OK)
    public List<Ubicacion> getAllUbicaciones() {
        return ubicacionService.findAll();
    }

    @Override
    @PostMapping("/ubicaciones")
    @ResponseStatus(HttpStatus.CREATED)
    public Ubicacion addUbicacion(@RequestBody Ubicacion ubicacion) {
        return ubicacionService.save(ubicacion);
    }
}
