package ru.kata.spring.boot_security.demo.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    //Убираем наш AuthenticationProvider так как будем использовать встроенный аппарат Security
    // сервис собран в один класс
    // разобрал обратно
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Приём http запроса
        // Конфигурация Security
        // Конфигурация авторизации
        http.csrf().disable() // отключаем защиту от межсайтовой подделки запросов
                .authorizeRequests()
                .antMatchers("/login", "/baseRolesInitiation", "/error").permitAll() // общий доступ
                .anyRequest().authenticated()

                .and().formLogin().loginPage("/login")
                .loginProcessingUrl("/process_login")
                .successHandler(onAuthenticationSuccessHandler())
//                .defaultSuccessUrl("/user", true) // пока указан дефолт, кастомный хендлер не включается?
                .failureUrl("/login?error")

                // стирание сессии и куки - разлогинивание. Встроенный функционал Security
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login");
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationSuccessHandler onAuthenticationSuccessHandler(){
        return new SuccessUserHandler();
    }
}