package Lapuente.TareasUbicaciones.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        @RequestParam(value = "loginAttempt", required = false) String loginAttempt,
                        Model model) {

        boolean loginSuccess = false;
        if (error != null) {
            model.addAttribute("loginError", true);
        }

        if (logout != null) {
            model.addAttribute("logoutSuccess", true);
        }

        if (loginAttempt != null) {
            model.addAttribute("loginAttempt", true);
        }

        model.addAttribute("loginSuccess", loginSuccess);
        return "index";
    }

    @GetMapping("/entrar")
    public String entrar(Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null) {
            redirectAttributes.addAttribute("loginAttempt", true);
            return "redirect:/";
        }

        redirectAttributes.addFlashAttribute("loginSuccess", true);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "redirect:/admin/adminsmenu";
        } else {
            return "redirect:/worker/workersmenu";
        }
    }

}
