package Lapuente.TareasUbicaciones;

import Lapuente.TareasUbicaciones.DTOs.TareaCumplidaDTO;
import Lapuente.TareasUbicaciones.DTOs.TareaDTO;
import Lapuente.TareasUbicaciones.DTOs.UbicacionDTO;
import Lapuente.TareasUbicaciones.ENUMs.Turno;
import Lapuente.TareasUbicaciones.entities.*;
import Lapuente.TareasUbicaciones.repositories.*;
import Lapuente.TareasUbicaciones.services.TareaCumplidaService;
import Lapuente.TareasUbicaciones.services.TareaService;
import Lapuente.TareasUbicaciones.services.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EntityScan(basePackages = "Lapuente.TareasUbicaciones.entities")
@EnableJpaRepositories(basePackages = "Lapuente.TareasUbicaciones.repositories")
@ComponentScan("Lapuente.TareasUbicaciones")
public class TareasUbicacionesApplication implements CommandLineRunner {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	WorkerRepository workerRepository;
	@Autowired
	TareaRepository tareaRepository;
	@Autowired
	UbicacionRepository ubicacionRepository;
	@Autowired
	TareaService tareaService;
	@Autowired
	UbicacionService ubicacionService;

	@Autowired
	TareaCumplidaService tareaCumplidaService;

	public static void main(String[] args) {
		SpringApplication.run(TareasUbicacionesApplication.class, args);
	}

	@Override
	public void run(String... args) {

		if (adminRepository.findByName("Emilio Botín").isEmpty()) {
			Admin emilio = new Admin("Emilio Botín", "santender@gmail.com", passwordEncoder.encode("MiDiN3R0"));
			adminRepository.save(emilio);
			roleRepository.save(new Role("ADMIN", emilio));
		}

		if (adminRepository.findByName("Luis Bàrcenas").isEmpty()) {
			Admin barcenas = new Admin("Luis Bàrcenas", "ppsede@gmail.com", passwordEncoder.encode("SOBREtodo"));
			adminRepository.save(barcenas);
			roleRepository.save(new Role("ADMIN", barcenas));
		}

		if (adminRepository.findByName("Tio Gilito").isEmpty()) {
			Admin titoGil = new Admin("Tio Gilito", "disney@gmail.com", passwordEncoder.encode("3patitos"));
			adminRepository.save(titoGil);
			roleRepository.save(new Role("ADMIN", titoGil));
		}

		if (workerRepository.findByName("Cristian Cristiano") == null) {
			Worker cristian = new Worker("Cristian Cristiano", passwordEncoder.encode("MiDiN3R0"), "Operario");
			workerRepository.save(cristian);
			roleRepository.save(new Role("WORKER", cristian));
		}

		if (workerRepository.findByName("Jeff Jefferson") == null) {
			Worker jefferson = new Worker("Jeff Jefferson", passwordEncoder.encode("SOBREtodo"), "Jefe de equipo");
			workerRepository.save(jefferson);
			roleRepository.save(new Role("WORKER", jefferson));
		}

		if (workerRepository.findByName("Jordi Jardi") == null) {
			Worker jordi = new Worker("Jordi Jardi", passwordEncoder.encode("3patitos"), "Limpieza");
			workerRepository.save(jordi);
			roleRepository.save(new Role("WORKER", jordi));
		}

// Crear tareas
		TareaDTO tarea1 = new TareaDTO("Limpieza de zona", "Se deberá haber seguido el protocólo de la ubicación en cuestión.", null);
		TareaDTO tarea2 = new TareaDTO("Rellenar informe diario", "Se habrá cumplimentado el informe de las tareas diarias.", null);
		TareaDTO tarea3 = new TareaDTO("Reporte de incidencia", "Se ha reportado al responsable cualquier incidencia o situación anormal.", null);
		TareaDTO tarea4 = new TareaDTO("Inspección de seguridad", "Realizar una inspección de seguridad en la ubicación.", null);
		TareaDTO tarea5 = new TareaDTO("Mantenimiento preventivo", "Llevar a cabo el mantenimiento preventivo de los equipos.", null);
		TareaDTO tarea6 = new TareaDTO("Revisión de inventario", "Revisar el inventario y registrar las existencias actuales.", null);
		TareaDTO tarea7 = new TareaDTO("Limpieza de derrames", "Limpiar y eliminar cualquier derrame o líquido en el área.", null);
		TareaDTO tarea8 = new TareaDTO("Control de calidad", "Verificar la calidad de los productos o materiales en la ubicación.", null);
		TareaDTO tarea9 = new TareaDTO("Actualización de documentación", "Actualizar y mantener la documentación relevante.", null);

		Tarea tareaSaved1 = tareaService.saveTarea(tarea1);
		Tarea tareaSaved2 = tareaService.saveTarea(tarea2);
		Tarea tareaSaved3 = tareaService.saveTarea(tarea3);
		Tarea tareaSaved4 = tareaService.saveTarea(tarea4);
		Tarea tareaSaved5 = tareaService.saveTarea(tarea5);
		Tarea tareaSaved6 = tareaService.saveTarea(tarea6);
		Tarea tareaSaved7 = tareaService.saveTarea(tarea7);
		Tarea tareaSaved8 = tareaService.saveTarea(tarea8);
		Tarea tareaSaved9 = tareaService.saveTarea(tarea9);

// Crear ubicaciones
		UbicacionDTO ubicacion1 = new UbicacionDTO( "APLANADORA 1000");
		UbicacionDTO ubicacion2 = new UbicacionDTO( "PASILLO 15A");
		UbicacionDTO ubicacion3 = new UbicacionDTO( "FLEJELADORA 320");
		UbicacionDTO ubicacion4 = new UbicacionDTO( "PERFILADORA 250");
		UbicacionDTO ubicacion5 = new UbicacionDTO( "ALMACÉN ENTRADA");
		UbicacionDTO ubicacion6 = new UbicacionDTO( "METRALLA 123");

		Ubicacion ubicacionSaved1 = ubicacionService.save(ubicacion1);
		Ubicacion ubicacionSaved2 = ubicacionService.save(ubicacion2);
		Ubicacion ubicacionSaved3 = ubicacionService.save(ubicacion3);
		Ubicacion ubicacionSaved4 = ubicacionService.save(ubicacion4);
		Ubicacion ubicacionSaved5 = ubicacionService.save(ubicacion5);
		Ubicacion ubicacionSaved6 = ubicacionService.save(ubicacion6);

// Asignar tareas a las ubicaciones
		Set<TareaDTO> tareasUbicacion1 = new HashSet<>(Arrays.asList(
				new TareaDTO(tareaSaved1.getName(), tareaSaved1.getDescripcion()),
				new TareaDTO(tareaSaved2.getName(), tareaSaved2.getDescripcion()),
				new TareaDTO(tareaSaved4.getName(), tareaSaved4.getDescripcion()),
				new TareaDTO(tareaSaved5.getName(), tareaSaved5.getDescripcion()),
				new TareaDTO(tareaSaved7.getName(), tareaSaved7.getDescripcion())));

		Set<TareaDTO> tareasUbicacion2 = new HashSet<>(Arrays.asList(
				new TareaDTO(tareaSaved2.getName(), tareaSaved2.getDescripcion()),
				new TareaDTO(tareaSaved3.getName(), tareaSaved3.getDescripcion()),
				new TareaDTO(tareaSaved6.getName(), tareaSaved6.getDescripcion()),
				new TareaDTO(tareaSaved8.getName(), tareaSaved8.getDescripcion()),
				new TareaDTO(tareaSaved9.getName(), tareaSaved9.getDescripcion())));

		Set<TareaDTO> tareasUbicacion3 = new HashSet<>(Arrays.asList(
				new TareaDTO(tareaSaved1.getName(), tareaSaved1.getDescripcion()),
				new TareaDTO(tareaSaved3.getName(), tareaSaved3.getDescripcion()),
				new TareaDTO(tareaSaved5.getName(), tareaSaved5.getDescripcion()),
				new TareaDTO(tareaSaved7.getName(), tareaSaved7.getDescripcion()),
				new TareaDTO(tareaSaved9.getName(), tareaSaved9.getDescripcion())));

		Set<TareaDTO> tareasUbicacion4 = new HashSet<>(Arrays.asList(
				new TareaDTO(tareaSaved1.getName(), tareaSaved1.getDescripcion()),
				new TareaDTO(tareaSaved4.getName(), tareaSaved4.getDescripcion()),
				new TareaDTO(tareaSaved6.getName(), tareaSaved6.getDescripcion()),
				new TareaDTO(tareaSaved7.getName(), tareaSaved7.getDescripcion()),
				new TareaDTO(tareaSaved8.getName(), tareaSaved8.getDescripcion())));

		Set<TareaDTO> tareasUbicacion5 = new HashSet<>(Arrays.asList(
				new TareaDTO(tareaSaved2.getName(), tareaSaved2.getDescripcion()),
				new TareaDTO(tareaSaved3.getName(), tareaSaved3.getDescripcion()),
				new TareaDTO(tareaSaved5.getName(), tareaSaved5.getDescripcion()),
				new TareaDTO(tareaSaved6.getName(), tareaSaved6.getDescripcion()),
				new TareaDTO(tareaSaved9.getName(), tareaSaved9.getDescripcion())));

		Set<TareaDTO> tareasUbicacion6 = new HashSet<>(Arrays.asList(
				new TareaDTO(tareaSaved1.getName(), tareaSaved1.getDescripcion()),
				new TareaDTO(tareaSaved3.getName(), tareaSaved3.getDescripcion()),
				new TareaDTO(tareaSaved4.getName(), tareaSaved4.getDescripcion()),
				new TareaDTO(tareaSaved8.getName(), tareaSaved8.getDescripcion()),
				new TareaDTO(tareaSaved9.getName(), tareaSaved9.getDescripcion())));


		ubicacionService.updateTareasDeUbicacion(ubicacionSaved1.getId(), tareasUbicacion1);
		ubicacionService.updateTareasDeUbicacion(ubicacionSaved2.getId(), tareasUbicacion2);
		ubicacionService.updateTareasDeUbicacion(ubicacionSaved3.getId(), tareasUbicacion3);
		ubicacionService.updateTareasDeUbicacion(ubicacionSaved4.getId(), tareasUbicacion4);
		ubicacionService.updateTareasDeUbicacion(ubicacionSaved5.getId(), tareasUbicacion5);
		ubicacionService.updateTareasDeUbicacion(ubicacionSaved6.getId(), tareasUbicacion6);

		// Crear TareaCumplida de ejemplo
		Worker workerCristian = workerRepository.findByName("Cristian Cristiano");
		Worker workerJeff = workerRepository.findByName("Jeff Jefferson");
		Worker workerJordi = workerRepository.findByName("Jordi Jardi");

		TareaCumplidaDTO tareaCumplida1 = new TareaCumplidaDTO(tareaSaved1.getId(), tareaSaved1.getName(), workerCristian.getId(), ubicacionSaved1.getId(),true, workerCristian.getName(), LocalDateTime.now().minusDays(1), Turno.MAÑANA, "Tarea realizada sin problemas.");
		TareaCumplidaDTO tareaCumplida2 = new TareaCumplidaDTO(tareaSaved2.getId(), tareaSaved2.getName(), workerJeff.getId(), ubicacionSaved2.getId(), false, workerJeff.getName(), LocalDateTime.now().minusDays(1), Turno.TARDE, "No fue posible completar la tarea.");
		TareaCumplidaDTO tareaCumplida3 = new TareaCumplidaDTO(tareaSaved3.getId(), tareaSaved3.getName(), workerJordi.getId(), ubicacionSaved3.getId(), true, workerJordi.getName(), LocalDateTime.now().minusDays(2), Turno.MAÑANA, "Tarea finalizada con éxito.");

		TareaCumplida tareaCumplidaSaved1 = tareaCumplidaService.save(tareaCumplida1);
		TareaCumplida tareaCumplidaSaved2 = tareaCumplidaService.save(tareaCumplida2);
		TareaCumplida tareaCumplidaSaved3 = tareaCumplidaService.save(tareaCumplida3);
	}
}
