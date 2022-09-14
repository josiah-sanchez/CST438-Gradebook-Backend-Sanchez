package com.cst438.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cst438.domain.*;
import com.cst438.domain.AssignmentListDTO.AssignmentDTO;

@RestController
public class AssignmentController {

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
//	
//	@GetMapping("/assignment/{id}")
//	public Assignment getAssignments(@PathVariable("id") Integer assignmentId) {
//		
//		String email = "dwisneski@csumb.edu"; // user name being instructor email
//		Assignment assignment = checkAssignment(assignmentId, email);
//		Assignment result = new Assignment();
//		
//		result.setId(assignment.getId());
//		result.setName(assignment.getName());
//		result.setDueDate(assignment.getDueDate());
//		result.setNeedsGrading(assignment.getNeedsGrading());
//		
//		return result;
//	}
	
	@PostMapping("/assignment")
	public AssignmentListDTO.AssignmentDTO addAssignment(@RequestBody AssignmentListDTO.AssignmentDTO assignmentDTO) {
		
		// lookup the course
		Course c = courseRepository.findById(assignmentDTO.courseId).get();
		if (c == null) {
			//invalid assignment error
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Course not found. " + assignmentDTO.courseId);
		}
		
		// create a new assignment entity
		Assignment assignment = new Assignment();
		
		// copy data from assignmentDTO to assignment
		assignment.setName(assignmentDTO.assignmentName);
		
		//TODO convert duDate String to dueDate java.sql.Date
		//assignment.setDueDate(assignmentDTO.dueDate);
		
		assignment.setCourse(c);
		
		// save the assignment entity, save returns an update assignment etity with assignment id primary key
		Assignment newAssignment = assignmentRepository.save(assignment);
		
		assignmentDTO.assignmentId = newAssignment.getId();
		
		// return assignmentDTO that now contains the primary key
		return assignmentDTO;
	}
	
	@PutMapping("/assignment/{id}")
	public void updateAssignment(@RequestBody AssignmentListDTO assignmentDTO) {
		
	}
	
	@DeleteMapping("/assignment/delete={id}")
	@Transactional
	public void deleteAssignment(@PathVariable("id") Integer assignmentId) {
		assignmentRepository.deleteById(assignmentId);
	}
	
//	private Assignment checkAssignment(int assignmentId, String email) {
//		//get assignment
//		Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
//		if (assignment == null) {
//			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Assignment not found. " + assignmentId);
//		}
//		//check that user is the instructor
//		if (!assignment.getCourse().getInstructor().equals(email)) {
//			throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Not Authorized.");
//		}
//		
//		return assignment;
//	}
}
