package com.cst438;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
public class JUnitTestDeleteAssignment {
	
	@MockBean
	AssignmentRepository assignmentRepository;
	
	@MockBean
	CourseRepository courseRepository;
	
	@Autowired
	private MockMvc mvc;
	
	static final String URL = "http://localhost:8080";
	public static final int TEST_COURSE_ID = 40442;
	
	@Test
	public void deleteAssignment() throws Exception {
		
		MockHttpServletResponse response;
		
		AssignmentListDTO.AssignmentDTO aDTO = new AssignmentListDTO.AssignmentDTO();
		aDTO.assignmentId = 123;
		aDTO.assignmentName = "test delete";
		aDTO.courseId = TEST_COURSE_ID;
		aDTO.dueDate = "2022-09-12";
		
		given(assignmentRepository.save(any())).willReturn(aDTO);
		
		response = (MockHttpServletResponse) mvc.perform(
				MockMvcRequestBuilders.delete("/delete/123"))
				.andExpect(status().isOk());
		
		verify(assignmentRepository, times(1)).delete(any());
	}

//	private static String asJsonString(final Object obj) {
//		try {
//			return new ObjectMapper().writeValueAsString(obj);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//	
//	private static <T> T fromJsonString(String str, Class<T> valueType) {
//		try {
//			return new ObjectMapper().readValue(str, valueType);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
}
