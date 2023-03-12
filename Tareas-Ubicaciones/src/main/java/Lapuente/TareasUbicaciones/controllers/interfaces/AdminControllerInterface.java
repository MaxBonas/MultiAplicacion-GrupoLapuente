package Lapuente.TareasUbicaciones.controllers.interfaces;

import Lapuente.TareasUbicaciones.entities.Tarea;
import Lapuente.TareasUbicaciones.entities.Ubicacion;
import Lapuente.TareasUbicaciones.entities.Worker;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.List;

public interface AdminControllerInterface {
    @PostMapping("/workers")
@ResponseStatus(HttpStatus.CREATED)
    Worker addWorker(@RequestBody Worker workerDTO);

    @GetMapping("/workers")
    @ResponseStatus(HttpStatus.OK)
    List<Worker> getAllWorkers();

    @GetMapping("/tareas")
    @ResponseStatus(HttpStatus.OK)
    List<Tarea> getAllTareas();

    @PostMapping("/tareas")
    @ResponseStatus(HttpStatus.CREATED)
    Tarea addTarea(@RequestBody Tarea tareaDTO);

    @GetMapping("/ubicaciones")
    @ResponseStatus(HttpStatus.OK)
    List<Ubicacion> getAllUbicaciones();

    @PostMapping("/ubicaciones")
    @ResponseStatus(HttpStatus.CREATED)
    Ubicacion addUbicacion(@RequestBody Ubicacion ubicacion);
/*
    Admin addAdmin(Admin admin);
    Admin getAdminById(Long id);
    List<Admin> getAllAdmins();
    Admin updateAdmin(Long id, Admin admin);

    ThirdParty addThirdParty(ThirdParty thirdParty);
    ThirdParty getThirdPartyById(Long id);
    List<ThirdParty> getAllThirdPartys();
    ThirdParty updateThirdParty(Long id, ThirdParty thirdParty);
    AccountHolder addAccountHolder(AccountHolder accountHolder);
    AccountHolder getAccountHolderById(Long id);
    List<AccountHolder> getAllAccountHolders();
    AccountHolder updateAccountHolder(Long id, AccountHolder accountHolder);
    String deleteUserById(Long id);


    AccountPostDTO addAccountByAdmin(TypeAccount typeAccount, AccountPostDTO account);
    Account getAccountById(Long id);
    List<Account> getAllAccounts();
    AccountGetDTO updateAccountByAdmin(Long id, AccountPostDTO account);

    String deleteAccountById(Long id);

    Account patchAdminAnyAccountBalance(Long accountId, BigDecimal amount);
    AccountGetDTO patchStatusAccount (Long id);
    AccountGetDTO validateAndActivateAccount(Long id);
*/
}
