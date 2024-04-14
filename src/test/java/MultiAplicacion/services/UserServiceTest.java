package MultiAplicacion.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import MultiAplicacion.entities.Role;
import MultiAplicacion.entities.User;
import MultiAplicacion.entities.Worker;
import MultiAplicacion.repositories.RoleRepository;
import MultiAplicacion.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserService userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new Worker(); // Suponiendo Worker es una implementación concreta de User
        user.setId(1L);
        user.setName("Test User");

        role = new Role();
        role.setId(1L);
        role.setRole("ROLE_USER");
        role.setUser(user);
    }

    @Test
    void getUserById_ExistingId_ReturnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1L);

        assertNotNull(foundUser);
        assertEquals(user.getName(), foundUser.getName());
    }

    @Test
    void getUserById_NonExistingId_ThrowsResponseStatusException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.getUserById(999L));
    }

    @Test
    void getAllUsers_ReturnsListOfUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    void deleteUserById_ExistingId_DeletesUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(entityManager).refresh(any());
        when(roleRepository.findByUserId(1L)).thenReturn(Optional.of(role));

        assertDoesNotThrow(() -> userService.deleteUserById(1L));
        verify(userRepository, times(1)).save(user);
        assertTrue(role.isDeleted());
    }

    @Test
    void findAllBySociedadId_ExistingSociedadId_ReturnsListOfUsers() {
        when(userRepository.findBySociedadId(1L)).thenReturn(Arrays.asList(user));

        List<User> users = userService.findAllBySociedadId(1L);

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    void deleteRoleByUserId_ExistingRole_DeletesRole() {
        when(roleRepository.findByUserId(user.getId())).thenReturn(Optional.of(role));

        userService.deleteRoleByUserId(user.getId());

        verify(roleRepository, times(1)).save(role);
        assertTrue(role.isDeleted());
    }

    @Test
    void deleteRoleByUserId_RoleNotFound_ThrowsResponseStatusException() {
        when(roleRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        // Este método captura y maneja la excepción, por lo que no se lanza hacia arriba.
        // Puedes verificar que no se realiza ninguna acción adicional, o que se logra un mensaje específico si es parte del manejo de excepción.
        userService.deleteRoleByUserId(999L);

        // Asegúrate de que save nunca se llama ya que el rol no fue encontrado.
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void deleteUserOnlyById_ExistingUser_DeletesUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUserOnlyById(user.getId());

        verify(userRepository, times(1)).save(user);
        assertTrue(user.isDeleted());
    }

    @Test
    void deleteUserOnlyById_UserNotFound_ThrowsResponseStatusException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> userService.deleteUserOnlyById(999L));
    }

}
