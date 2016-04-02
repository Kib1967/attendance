package attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        	.withUser("mckibbin").password("password").roles("EMPLOYEE").and()
	        .withUser("goldsworthy").password("password").roles("EMPLOYEE", "MANAGER").and()
	        .withUser("rowe").password("password").roles("EMPLOYEE", "MANAGER", "ADMIN");
        
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        
        http
	        .authorizeRequests()
	            .antMatchers("/css/**").permitAll() 
	            .antMatchers("/manager/**").hasRole("MANAGER") 
	            .antMatchers("/admin/**").hasRole("ADMIN") 
	            .anyRequest().authenticated() 
	            .and()
	        .formLogin()
	            .loginPage("/login")
	            .permitAll()
	            .and()
	        .logout()
	            .permitAll()
	            .logoutSuccessUrl("/login?logout");
        
        // Obviously this is heinous. I don't know yet why the commented-out solution doesn't work
        http.csrf().disable();
            //.csrfTokenRepository(csrfTokenRepository());
    }
    
    private CsrfTokenRepository csrfTokenRepository() 
    { 
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository(); 
        repository.setSessionAttributeName("_csrf");
        return repository; 
    }
}