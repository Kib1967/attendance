package attendance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        	.withUser("z001rfah").password("password").roles("EMPLOYEE").and()
	        .withUser("goldan7n").password("password").roles("EMPLOYEE", "MANAGER").and()
	        .withUser("rowesigl").password("password").roles("EMPLOYEE", "MANAGER", "ADMIN");
        
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception {
        
        http
	        .authorizeRequests()
	        .antMatchers("/css/**").permitAll() 
	        .anyRequest().authenticated()
	        .and()
	    .formLogin()
	        .loginPage("/login")
	        .permitAll()
	        .and()
	    .logout()
	        .permitAll()
	        .logoutSuccessUrl("/login?logout");
    }
}