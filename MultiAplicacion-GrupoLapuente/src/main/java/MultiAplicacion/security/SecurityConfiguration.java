package MultiAplicacion.security;

import MultiAplicacion.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Autowired
    CustomUserDetailsService customUserDetailsSevice;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/worker/**").hasAnyRole("WORKER", "ADMIN")
                .antMatchers(HttpMethod.PATCH, "/worker/**").hasAnyRole("WORKER", "ADMIN")
                .antMatchers("/", "/index", "/login-error").permitAll() // permitir acceso a la página de inicio y error de inicio de sesión
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/")
                .loginProcessingUrl("/login")
                .failureHandler(authenticationFailureHandler()) // agregar URL de error
                .defaultSuccessUrl("/entrar", true)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout") // Agrega esta línea para especificar una URL de logout
                .logoutSuccessUrl("/") // Agrega esta línea para especificar la URL a la que se redirige después de cerrar sesión
                .invalidateHttpSession(true) // Asegúrese de que la sesión HTTP se invalide después de cerrar sesión
                .deleteCookies("JSESSIONID") // Elimina la cookie de la sesión después de cerrar sesión
                .permitAll();

        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new SimpleUrlAuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException exception) throws IOException, ServletException {
                setDefaultFailureUrl("/?error=true");
                super.onAuthenticationFailure(request, response, exception);
            }
        };
    }
}

