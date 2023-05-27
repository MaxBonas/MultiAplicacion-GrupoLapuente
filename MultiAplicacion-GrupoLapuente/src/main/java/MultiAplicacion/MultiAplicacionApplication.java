package MultiAplicacion;


import MultiAplicacion.repositories.AdminRepository;
import MultiAplicacion.repositories.InformeRepository;
import MultiAplicacion.repositories.RoleRepository;
import MultiAplicacion.repositories.SociedadRepository;
import MultiAplicacion.repositories.TareaRepository;
import MultiAplicacion.repositories.UbicacionRepository;
import MultiAplicacion.repositories.WorkerRepository;
import MultiAplicacion.services.InformeService;
import MultiAplicacion.services.SociedadService;
import MultiAplicacion.services.TareaCumplidaService;
import MultiAplicacion.services.TareaService;
import MultiAplicacion.services.UbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

//TODO arreglar el metodo InformarTarea en el controlador y en la vista
//TODO hacer funcional el CambioSociedad.
//TODO arreglar crear tarea y crear ubicacion en la vista
//TODO Cambiar el InformeDiario para que sea una vista html y luego un excel (basarse en la vista ubicaciones.html)
//TODO Conjugar la sociedadid de la url con la seguridad para evitar consultas intersociedades

@SpringBootApplication
@EntityScan(basePackages = "MultiAplicacion.entities")
@EnableJpaRepositories(basePackages = "MultiAplicacion.repositories")
@ComponentScan("MultiAplicacion")
public class MultiAplicacionApplication implements CommandLineRunner {

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
	@Autowired
	InformeRepository informeRepository;
	@Autowired
	InformeService informeService;

	@Autowired
	SociedadRepository sociedadRepository;

	@Autowired
	SociedadService sociedadService;

	public static void main(String[] args) {
		SpringApplication.run(MultiAplicacionApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//Introducció de dades de mostra de manera forçada
/*
		Sociedad goodPolish = new Sociedad("GoodPolish S.L:");
		Sociedad lapuente = new Sociedad("Lapuente S.L:");
		Sociedad spb = new Sociedad("Stainless Products BCN S.L:");
		Sociedad isotubi = new Sociedad("Isotubi S.L:");
		Sociedad gestora = new Sociedad("Gestora Lapuente S.L:");
		Sociedad turolense = new Sociedad("Turolense del Inoxidable S.L:");

		sociedadService.save(goodPolish);
		sociedadService.save(lapuente);
		sociedadService.save(spb);
		sociedadService.save(isotubi);
		sociedadService.save(gestora);
		sociedadService.save(turolense);

		if (adminRepository.findByName("Admin1").isEmpty()) {
			Admin testAdmin1 = new Admin("Admin1", "Admin1@gmail.com", passwordEncoder.encode("123456"), goodPolish);
			adminRepository.save(testAdmin1);
			roleRepository.save(new Role("ADMIN", testAdmin1));
		}

		if (adminRepository.findByName("Admin2").isEmpty()) {
			Admin testAdmin2 = new Admin("Admin2", "Admin2@gmail.com", passwordEncoder.encode("123456"), lapuente);
			adminRepository.save(testAdmin2);
			roleRepository.save(new Role("ADMIN", testAdmin2));
		}

		if (adminRepository.findByName("Admin3").isEmpty()) {
			Admin testAdmin3 = new Admin("Admin3", "Admin3@gmail.com", passwordEncoder.encode("123456"), spb);
			adminRepository.save(testAdmin3);
			roleRepository.save(new Role("ADMIN", testAdmin3));
		}

		if (workerRepository.findByName("Juan Test").isEmpty()) {
			Worker workertest1 = new Worker("Juan Test", passwordEncoder.encode("123456"), "Operario", goodPolish);
			workerRepository.save(workertest1);
			roleRepository.save(new Role("WORKER", workertest1));
		}

		if (workerRepository.findByName("Marcos Test").isEmpty()) {
			Worker workertest2 = new Worker("Marcos Test", passwordEncoder.encode("123456"), "Jefe de equipo", goodPolish);
			workerRepository.save(workertest2);
			roleRepository.save(new Role("WORKER", workertest2));
		}

		if (workerRepository.findByName("Francisco Test").isEmpty()) {
			Worker workertest3 = new Worker("Francisco Test", passwordEncoder.encode("123456"), "Limpieza", goodPolish);
			workerRepository.save(workertest3);
			roleRepository.save(new Role("WORKER", workertest3));
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
		UbicacionDTO ubicacion1 = new UbicacionDTO("GRINDIGMASATER 4000", goodPolish.getId());
		UbicacionDTO ubicacion2 = new UbicacionDTO("PULIDORA MARPUL 1000", goodPolish.getId());
		UbicacionDTO ubicacion3 = new UbicacionDTO("PULIDORA MARPUL 1500", goodPolish.getId());
		UbicacionDTO ubicacion4 = new UbicacionDTO("PULIDORA SILLEM 1500", goodPolish.getId());
		UbicacionDTO ubicacion5 = new UbicacionDTO("PULIDORA SILLEM 2000", goodPolish.getId());
		UbicacionDTO ubicacion6 = new UbicacionDTO("SATINADORA IMEAS 1000/1250", goodPolish.getId());
		UbicacionDTO ubicacion7 = new UbicacionDTO("SATINADORA ITALIANA 1500", goodPolish.getId());
		UbicacionDTO ubicacion8 = new UbicacionDTO("PERFILADORA TEST 5000", lapuente.getId());

		Ubicacion ubicacionSaved1 = ubicacionService.save(ubicacion1);
		Ubicacion ubicacionSaved2 = ubicacionService.save(ubicacion2);
		Ubicacion ubicacionSaved3 = ubicacionService.save(ubicacion3);
		Ubicacion ubicacionSaved4 = ubicacionService.save(ubicacion4);
		Ubicacion ubicacionSaved5 = ubicacionService.save(ubicacion5);
		Ubicacion ubicacionSaved6 = ubicacionService.save(ubicacion6);
		Ubicacion ubicacionSaved7 = ubicacionService.save(ubicacion7);
		Ubicacion ubicacionSaved8 = ubicacionService.save(ubicacion8);

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


		//Crear ubicaciones para Sociedades:


// Crear TareaCumplida de ejemplo e informes de ejemplo:
		Worker workerCristian = workerRepository.findByName("Juan Test") .orElseThrow();
		Worker workerJeff = workerRepository.findByName("Marcos Test").orElseThrow();
		Worker workerJordi = workerRepository.findByName("Francisco Test").orElseThrow();

		TareaCumplidaDTO tareaCumplida1 = new TareaCumplidaDTO(tareaSaved1.getId(), tareaSaved1.getName(),
				workerCristian.getId(), ubicacionSaved1.getId(), true, workerCristian.getName(),
				LocalDateTime.now().minusDays(1), Turno.MANANA);
		TareaCumplidaDTO tareaCumplida2 = new TareaCumplidaDTO(tareaSaved2.getId(), tareaSaved2.getName(),
				workerJeff.getId(), ubicacionSaved2.getId(), false, workerJeff.getName(),
				LocalDateTime.now().minusDays(1), Turno.TARDE);
		TareaCumplidaDTO tareaCumplida3 = new TareaCumplidaDTO(tareaSaved3.getId(), tareaSaved3.getName(),
				workerJordi.getId(), ubicacionSaved3.getId(), true, workerJordi.getName(),
				LocalDateTime.now().minusDays(2), Turno.MANANA);

		List<TareaCumplidaDTO> tareasCumplidas1 = new ArrayList<>();
		tareasCumplidas1.add(tareaCumplida1);
		tareasCumplidas1.add(tareaCumplida2);

		List<TareaCumplidaDTO> tareasCumplidas2 = new ArrayList<>();
		tareasCumplidas2.add(tareaCumplida3);

		InformeDTO informe1 = new InformeDTO(LocalDateTime.now().minusDays(1), ubicacionSaved1.getId(), Turno.MANANA,
				 "Informe de prueba1", tareasCumplidas1);
		InformeDTO informe2 = new InformeDTO(LocalDateTime.now().minusDays(2), ubicacionSaved2.getId(), Turno.MANANA,
				 "Informe de prueba2", tareasCumplidas2);

		Informe informeSaved1 = informeService.save(new Informe(informe1, ubicacionSaved1));
		Informe informeSaved2 = informeService.save(new Informe(informe2, ubicacionSaved2));

// Actualizar las tareas cumplidas del informe 1
		TareaCumplidaDTO tareaCumplida4 = new TareaCumplidaDTO(tareaSaved4.getId(), tareaSaved4.getName(),
				workerCristian.getId(), ubicacionSaved1.getId(), true, workerCristian.getName(),
				LocalDateTime.now().minusDays(1), Turno.MANANA);
		List<TareaCumplidaDTO> tareasCumplidasActualizadas = new ArrayList<>(tareasCumplidas1);
		tareasCumplidasActualizadas.add(tareaCumplida4);
		informeService.updateTareasCumplidasDeInforme(informeSaved1.getId(), tareasCumplidasActualizadas);

		// Agregar una tarea cumplida al informe 2
		TareaCumplidaDTO tareaCumplida5 = new TareaCumplidaDTO(tareaSaved5.getId(), tareaSaved5.getName(),
				workerJordi.getId(), ubicacionSaved2.getId(), true, workerJordi.getName(),
				LocalDateTime.now().minusDays(2), Turno.MANANA);
		tareasCumplidas2.add(tareaCumplida5);
		informeService.updateTareasCumplidasDeInforme(informeSaved2.getId(), tareasCumplidas2);

		// Comprueba que las listas no estén vacías
*/
	}

}
