package Lapuente.TareasUbicaciones;

import Lapuente.TareasUbicaciones.DTOs.TareaDTO;
import Lapuente.TareasUbicaciones.DTOs.UbicacionDTO;
import Lapuente.TareasUbicaciones.entities.*;
import Lapuente.TareasUbicaciones.repositories.*;
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

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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

		if (workerRepository.findByName("Jordi Jardí") == null) {
			Worker jordi = new Worker("Jordi Jardí", passwordEncoder.encode("3patitos"), "Limpieza");
			workerRepository.save(jordi);
			roleRepository.save(new Role("WORKER", jordi));
		}

// Crear tareas
		TareaDTO tarea1 = new TareaDTO("Limpieza de zona", "Se deberá haber seguido el protocólo de la ubicación en cuestión.", null);
		TareaDTO tarea2 = new TareaDTO("Rellenar informe diario", "Se habrá cumplimentado el informe de las tareas diarias.", null);
		TareaDTO tarea3 = new TareaDTO("Reporte de incidencia", "Se ha reportado al responsable cualquier incidencia o situación anormal.", null);

		Tarea tareaSaved1 = tareaService.saveTarea(tarea1);
		Tarea tareaSaved2 = tareaService.saveTarea(tarea2);
		Tarea tareaSaved3 = tareaService.saveTarea(tarea3);

// Crear ubicaciones
		UbicacionDTO ubicacion1 = new UbicacionDTO(null, "APLANADORA 1000");
		UbicacionDTO ubicacion2 = new UbicacionDTO(null, "PASILLO 15A");
		UbicacionDTO ubicacion3 = new UbicacionDTO(null, "FLEJELADORA 320");

		Ubicacion ubicacionSaved1 = ubicacionService.save(ubicacion1);
		Ubicacion ubicacionSaved2 = ubicacionService.save(ubicacion2);
		Ubicacion ubicacionSaved3 = ubicacionService.save(ubicacion3);

// Asignar tareas a las ubicaciones
		Set<TareaDTO> tareasUbicacion1 = new HashSet<>(Arrays.asList(new TareaDTO(tareaSaved1.getNombre(), tareaSaved1.getDescripcion(), ubicacionSaved1.getId()), new TareaDTO(tareaSaved2.getNombre(), tareaSaved2.getDescripcion(), ubicacionSaved1.getId())));
		Set<TareaDTO> tareasUbicacion2 = new HashSet<>(Arrays.asList(new TareaDTO(tareaSaved2.getNombre(), tareaSaved2.getDescripcion(), ubicacionSaved2.getId()), new TareaDTO(tareaSaved3.getNombre(), tareaSaved3.getDescripcion(), ubicacionSaved2.getId())));
		Set<TareaDTO> tareasUbicacion3 = new HashSet<>(Arrays.asList(new TareaDTO(tareaSaved1.getNombre(), tareaSaved1.getDescripcion(), ubicacionSaved3.getId()), new TareaDTO(tareaSaved3.getNombre(), tareaSaved3.getDescripcion(), ubicacionSaved3.getId())));

		ubicacionService.updateTareasDeUbicacion(ubicacionSaved1.getId(), tareasUbicacion1);
		ubicacionService.updateTareasDeUbicacion(ubicacionSaved2.getId(), tareasUbicacion2);
		ubicacionService.updateTareasDeUbicacion(ubicacionSaved3.getId(), tareasUbicacion3);

	}
}
