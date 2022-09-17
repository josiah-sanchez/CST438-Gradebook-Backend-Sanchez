package com.cst438;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.text.SimpleDateFormat;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cst438.controllers.AssignmentController;
import com.cst438.controllers.GradeBookController;
import com.cst438.domain.Assignment;
import com.cst438.domain.AssignmentGrade;
import com.cst438.domain.AssignmentGradeRepository;
import com.cst438.domain.AssignmentListDTO;
import com.cst438.domain.AssignmentRepository;
import com.cst438.domain.Course;
import com.cst438.domain.CourseRepository;
import com.cst438.domain.Enrollment;
import com.cst438.domain.GradebookDTO;
import com.cst438.services.RegistrationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ContextConfiguration(classes = { AssignmentController.class })
@WebMvcTest
public class TestUpdateAssignment {
	
	@MockBean
	AssignmentRepository assignmentRepository;

	@MockBean
	CourseRepository courseRepository; // must have this to keep Spring test happy

	@Autowired
	private MockMvc mvc;
	
	static final String URL = "http://localhost:8080";
	public static final int TEST_COURSE_ID = 40442;

	@Test
	public void updateAssignment() throws Exception {
		
		MockHttpServletResponse response;
		
		Course course = new Course();
		course.setCourse_id(TEST_COURSE_ID);

		given(courseRepository.findById(TEST_COURSE_ID)).willReturn(Optional.of(course));
		
		Assignment a1 = new Assignment();
		Assignment a2 = new Assignment();
		a1.setId(123);
		
		given(assignmentRepository.save(any())).willReturn(a1);
	
		// verify returned data has non zero primary key
		AssignmentListDTO.AssignmentDTO assignment_1 = new AssignmentListDTO.AssignmentDTO();
		
		assignment_1.assignmentName = "test name";
		assignment_1.assignmentId = a1.getId();
		assignment_1.courseId = TEST_COURSE_ID;
		assignment_1.dueDate = "2022-09-12";
		
		given(assignmentRepository.save(any())).willReturn(a2);
		
		assignment_1.assignmentName = "update test name";
		
		// update assignment name
		response = mvc.perform(
				MockMvcRequestBuilders
				.put("/assignment/123")
				.accept(MediaType.APPLICATION_JSON)
				.content(asJsonString(assignment_1))
				.contentType(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		// verify that repository save for AssignmentRepository method was called
		verify(assignmentRepository).save(any(Assignment.class));
		
//		MockHttpServletResponse response;
//		
//		Course course = new Course();
//		course.setCourse_id(TEST_COURSE_ID);
//		
//		// given -- stubs for database repositories that return test data
//		given(courseRepository.findById(TEST_COURSE_ID)).willReturn(Optional.of(course));
//		// end of mock data
//
//		Assignment assignment = new Assignment();
//		assignment.setName("old name");
//		assignment.setId(TEST_COURSE_ID);
//		
//		given(assignmentRepository.findById(TEST_COURSE_ID)).willReturn(Optional.of(assignment));
//
//		AssignmentListDTO.AssignmentDTO aDTO = new AssignmentListDTO.AssignmentDTO();
//		//setting values for name,courseId, dueDate
//		aDTO.assignmentName = "new name";
//		aDTO.courseId = TEST_COURSE_ID;
//		aDTO.dueDate = "2022-9-12";
//		
//		// do an http get for an assignment with a course id 40442
//		response = mvc.perform(
//				MockMvcRequestBuilders.get("/assignment/40442")
//				.accept(MediaType.APPLICATION_JSON))
//				.andReturn().getResponse();
//				
//
//		// verify return data with entry for one student without no score
//		assertEquals(200, response.getStatus());
//		
//		// get response body and convert to Java object
//		AssignmentListDTO.AssignmentDTO assignment_1 = fromJsonString(response.getContentAsString(), AssignmentListDTO.AssignmentDTO.class);
//		
//		assignment_1.assignmentName = "new assignment";
//		
//		response = mvc.perform(
//				MockMvcRequestBuilders
//				.put("/assignment")
//				.accept(MediaType.APPLICATION_JSON)
//				.content(asJsonString(assignment_1))
//				.contentType(MediaType.APPLICATION_JSON))
//				.andReturn().getResponse();
//		
//		// check that returned assignmentID is not 0
//		assertEquals(200, response.getStatus());
//
//		// verify that a save was called on repository
//		verify(assignmentRepository).save(any(Assignment.class)); // verify that assignment Controller actually did a save to the database
//		


	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T fromJsonString(String str, Class<T> valueType) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
