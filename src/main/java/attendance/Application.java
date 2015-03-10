package attendance;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import attendance.model.AttendanceItem;
import attendance.model.AttendanceItemStatus;
import attendance.model.AttendanceItemType;
import attendance.model.Employee;
import attendance.repository.AttendanceItemRepository;
import attendance.repository.EmployeeRepository;

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Bean
	CommandLineRunner init() {
		return (evt) -> loadData();
	}
	
	// TODO: do I really need this??
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setDefaultLocale(Locale.US);
	    return slr;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private AttendanceItemRepository attendanceItemRepository;
	
	@Value("${spring.profiles.active:}")
    private String activeProfiles;
	
	private void loadData() {
		
		LOGGER.warn("loadData()");
		
		// Can't find a better way to inject some data that I want only when I want to do interactive testing of the
		// application, but not to get in the way when I do testing
		if( activeProfiles.equals( "interactive" )) {
	        Employee goldan7n = employeeRepository.save(new Employee("goldan7n", "Andy Goldsworthy", "goldan7n@gmail.com", null));
	        Employee rowesigl = employeeRepository.save(new Employee("rowesigl", "Simon Rowe", "rowesigl@gmail.com", null));
	        Employee z001rfah = employeeRepository.save(new Employee("z001rfah", "Andrew McKibbin", "andymckibbin@yahoo.co.uk", goldan7n));
	        
			attendanceItemRepository.save(new AttendanceItem(z001rfah,
					AttendanceItemStatus.APPROVED,
					AttendanceItemType.ANNUAL_LEAVE,
					convert( LocalDate.of(2015, 1, 1)),
					convert( LocalDate.of(2015, 1, 1))));
			attendanceItemRepository.save(new AttendanceItem(z001rfah,
					AttendanceItemStatus.APPROVED,
					AttendanceItemType.ANNUAL_LEAVE,
					convert( LocalDate.of(2015, 3, 5)),
					convert( LocalDate.of(2015, 3, 10))));
			attendanceItemRepository.save(new AttendanceItem(z001rfah,
					AttendanceItemStatus.REQUESTED,
					AttendanceItemType.ANNUAL_LEAVE,
					convert( LocalDate.of(2015, 4, 5)),
					convert( LocalDate.of(2015, 4, 10))));
			attendanceItemRepository.save(new AttendanceItem(z001rfah,
					AttendanceItemStatus.REJECTED,
					AttendanceItemType.ANNUAL_LEAVE,
					convert( LocalDate.of(2015, 5, 5)),
					convert( LocalDate.of(2015, 5, 10))));
		}
	}
	
	private Date convert( LocalDate localDate ) {
		Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}
}