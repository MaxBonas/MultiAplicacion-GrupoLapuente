package Lapuente.TareasUbicaciones.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
public class LoginController {

    @GetMapping("/entrar")
    public String entrar(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        model.addAttribute("loginSuccess", true);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "redirect:/adminsmenu";
        } else {
            return "redirect:/workersmenu";
        }
    }

    @GetMapping("/")
    public String index(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {

        boolean loginSuccess = false;
        if (error != null) {
            model.addAttribute("loginError", true);
        }

        if (logout != null) {
            model.addAttribute("logoutSuccess", true);
        }

        model.addAttribute("loginSuccess", loginSuccess);
        return "index";
    }
}
