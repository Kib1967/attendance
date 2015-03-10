package attendance.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import attendance.Application;
import attendance.model.AttendanceItem;
import attendance.model.AttendanceItemStatus;
import attendance.model.AttendanceItemType;
import attendance.model.Employee;
import attendance.repository.AttendanceItemRepository;
import attendance.repository.EmployeeRepository;

/**
 * @author Josh Long
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AttendanceItemRestControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private String gid = "z001rfah";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Employee employee;

    private List<AttendanceItem> attendanceItems = new ArrayList<>();

    @Autowired
    private AttendanceItemRepository attendanceItemRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.employee = employeeRepository.save(new Employee(gid, "Andrew McKibbin", null, null));
		this.attendanceItems.add(attendanceItemRepository.save(
				new AttendanceItem(
						employee,
						AttendanceItemStatus.REQUESTED,
						AttendanceItemType.ANNUAL_LEAVE,
						convert( LocalDate.of(2015, 1, 1)),
						convert( LocalDate.of(2015, 1, 1)))));
		this.attendanceItems.add(attendanceItemRepository.save(
				new AttendanceItem(
						employee,
						AttendanceItemStatus.REQUESTED,
						AttendanceItemType.ANNUAL_LEAVE,
						convert( LocalDate.of(2015, 3, 5)),
						convert( LocalDate.of(2015, 3, 10)))));
    }
    
	
	private Date convert( LocalDate localDate ) {
		Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}

    
    @After
    public void tearDown() throws Exception {
        this.attendanceItemRepository.deleteAllInBatch();
        this.employeeRepository.deleteAllInBatch();
    }

    @Test
    public void employeeNotFound() throws Exception {
        mockMvc.perform(get("/george/attendanceItem/" + this.attendanceItems.get(0).getId())
                .content(this.json(new AttendanceItem()))
                .contentType(contentType))
                .andExpect(status().isNotFound());
    }

    @Test
    public void readSingleAttendanceItem() throws Exception {
        mockMvc.perform(get("/" + gid + "/attendanceItem/"
                + this.attendanceItems.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(this.attendanceItems.get(0).getId().intValue())));
    }
/*
    @Test
    public void readBookmarks() throws Exception {
        mockMvc.perform(get("/" + userName + "/bookmarks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(this.bookmarkList.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0].uri", is("http://bookmark.com/1/" + userName)))
                .andExpect(jsonPath("$[0].description", is("A description")))
                .andExpect(jsonPath("$[1].id", is(this.bookmarkList.get(1).getId().intValue())))
                .andExpect(jsonPath("$[1].uri", is("http://bookmark.com/2/" + userName)))
                .andExpect(jsonPath("$[1].description", is("A description")));
    }

    @Test
    public void createAttendanceItem() throws Exception {
        String attendanceItemJson = json(new AttendanceItem(
                this.user, LocalDate.of(2015, 8, 1), LocalDate.of(2015, 8, 10)));
        this.mockMvc.perform(post("/" + userGid + "/attendanceItem")
                .contentType(contentType)
                .content(attendanceItemJson))
                .andDo(print())
                .andExpect(status().isCreated());
    }
*/
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}