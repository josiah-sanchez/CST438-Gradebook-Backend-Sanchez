package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_MOCKS;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentRepository;

@SpringBootTest
public class EndToEndCreateNewAssignmentTest {
	
	public static final String CHROME_DRIVER_FILE_LOCATION = "C:/chromedriver_win32/chromedriver.exe";
	
	public static final String URL = "http://localhost:3000";
	public static final String TEST_INSTRUCTOR_EMAIL = "dwisneski@csumb.edu";
	public static final int SLEEP_DURATION = 1000;
	public static final String TEST_ASSIGNMENT_NAME = "End to End Test";
	public static String TEST_COURSE_TITLE = "Test Course";
	public static String TEST_DUE_DATE = "2022-10-04";
	public static Integer TEST_COURSE_ID = 494949;
	
	@Autowired 
	CourseRepository courseRepository;
	
	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Test
	public void addAssignmentTest() throws Exception {
		
		//Create the course
		Course c = new Course();
		c.setCourse_id(TEST_COURSE_ID);
		c.setInstructor(TEST_INSTRUCTOR_EMAIL);
		c.setSemester("Fall");
		c.setYear(2022);
		c.setTitle(TEST_COURSE_TITLE);
		
		courseRepository.save(c);
		
		Assignment a = new Assignment();
		a.setName(TEST_ASSIGNMENT_NAME);
		a.setDueDate(Date.valueOf(TEST_DUE_DATE));
		a.setCourse(c);
		a.setNeedsGrading(0);
		
		a = assignmentRepository.save(a);
		
		//set and start selenium driver
		System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_FILE_LOCATION);
		WebDriver driver = new ChromeDriver();
		//wait 10 seconds before throwing exception
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.get(URL);
		Thread.sleep(SLEEP_DURATION);
		
		try {
			WebElement we;
			
			driver.get(URL);
			Thread.sleep(SLEEP_DURATION);
			
			we = driver.findElement(By.id("addAssignment"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			// enter an assignment name
			we = driver.findElement(By.name("assignmentName"));
			we.sendKeys(TEST_ASSIGNMENT_NAME);
			
			// enter a due date
			we = driver.findElement(By.name("dueDate"));
			we.sendKeys(TEST_DUE_DATE);
			
			// enter a course id
			we = driver.findElement(By.name("courseId"));
			we.sendKeys(Integer.toString(TEST_COURSE_ID));
			
			// find and click submit
			we = driver.findElement(By.id("Add"));
			we.click();
			Thread.sleep(SLEEP_DURATION);
			
			// verify correct message
			we = driver.findElement(By.className("Toastify"));
			String toast = we.getText();
			System.out.println(toast);
			assertEquals("Assignment successfully added!", toast);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		assignmentRepository.deleteById(a.getId()+1);
		assignmentRepository.deleteById(a.getId());
		courseRepository.deleteById(TEST_COURSE_ID);
		
		driver.quit();
	}
}
