package Lapuente.TareasUbicaciones;

import Lapuente.TareasUbicaciones.entities.*;
import Lapuente.TareasUbicaciones.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

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

	public static void main(String[] args) {
		SpringApplication.run(TareasUbicacionesApplication.class, args);
	}

	@Override
	public void run(String... args) {

		Admin emilio = new Admin("Emilio Botín", "santender@gmail.com", passwordEncoder.encode("MiDiN3R0"));
		Admin barcenas = new Admin("Luis Bàrcenas", "ppsede@gmail.com", passwordEncoder.encode("SOBREtodo"));
		Admin titoGil = new Admin("Tio Gilito", "disney@gmail.com", passwordEncoder.encode("3patitos"));
		adminRepository.save(emilio);
		adminRepository.save(barcenas);
		adminRepository.save(titoGil);
		roleRepository.save(new Role("ADMIN", emilio));
		adminRepository.save(emilio);
		roleRepository.save(new Role("ADMIN", barcenas));
		adminRepository.save(barcenas);
		roleRepository.save(new Role("ADMIN", titoGil));
		adminRepository.save(titoGil);

		Worker cristian = new Worker("Cristian Cristiano", passwordEncoder.encode("MiDiN3R0"), "Operario");
		Worker jefferson = new Worker("Jeff Jefferson", passwordEncoder.encode("SOBREtodo"), "Jefe de equipo");
		Worker jordi = new Worker("Jordi Jardí", passwordEncoder.encode("3patitos"), "Limpieza");
		workerRepository.save(cristian);
		workerRepository.save(jefferson);
		workerRepository.save(jordi);
		roleRepository.save(new Role("WORKER", emilio));
		workerRepository.save(cristian);
		roleRepository.save(new Role("WORKER", barcenas));
		workerRepository.save(jefferson);
		roleRepository.save(new Role("WORKER", titoGil));
		workerRepository.save(jordi);

		tareaRepository.saveAll(List.of(
				new Tarea("Limpieza de zona", "Se deberá haber seguido el protocólo de la ubicación en qüestión.", false),
				new Tarea("Rellenar informe diario", "Se habrá cumplimentado el informe de las tareas diarias.", false),
				new Tarea("Reporte de incidéncia", "Se ha reportado al responsable cualquier incidencia o situación anormal.", true)));

		ubicacionRepository.saveAll(List.of(
				new Ubicacion("APLANADORA 1000"),
				new Ubicacion("PASILLO 15A"),
				new Ubicacion("FLEJELADORA 320")));
	}

}
