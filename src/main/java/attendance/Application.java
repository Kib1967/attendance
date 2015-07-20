package attendance;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

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
	
	@Bean
	CommandLineRunner init() {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				loadData();
			}
		};
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
    private String activeProfilesAsStr;
	
	private void loadData() {
		
		Collection<String> activeProfiles = Arrays.asList(activeProfilesAsStr.split(",")); //.forEach((item) -> item.trim());
		
		// Can't find a better way to inject some data that I want only when I want to do interactive testing of the
		// application, but not to get in the way when I do testing
		if( activeProfiles.contains( "interactive" )) {
	        Employee goldsworthy = employeeRepository.save(new Employee("goldsworthy", "Andy Goldsworthy", "goldan7n@gmail.com", null));
	        Employee rowe = employeeRepository.save(new Employee("rowe", "Simon Rowe", "rowesigl@gmail.com", null));
	        Employee mckibbin = employeeRepository.save(new Employee("mckibbin", "Andrew McKibbin", "andymckibbin@yahoo.co.uk", goldsworthy));
	        
			attendanceItemRepository.save(new AttendanceItem(mckibbin,
					AttendanceItemStatus.APPROVAL_GRANTED,
					AttendanceItemType.ANNUAL_LEAVE,
					convert(2015, 1, 1),
					convert(2015, 1, 1)));
			attendanceItemRepository.save(new AttendanceItem(mckibbin,
					AttendanceItemStatus.APPROVAL_GRANTED,
					AttendanceItemType.BUSINESS_TRAVEL,
					convert(2015, 3, 5),
					convert(2015, 3, 10)));
			attendanceItemRepository.save(new AttendanceItem(mckibbin,
					AttendanceItemStatus.APPROVAL_REQUESTED,
					AttendanceItemType.TRAINING,
					convert(2015, 4, 5),
					convert(2015, 4, 10)));
			attendanceItemRepository.save(new AttendanceItem(mckibbin,
					AttendanceItemStatus.CREATED,
					AttendanceItemType.SICK,
					convert(2015, 5, 5),
					convert(2015, 5, 5)));
		}
	}
	
	private Date convert( int year, int month, int day ) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		
		return cal.getTime();
	}
}