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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.cst438.domain.*;

@RestController
public class AssignmentController {

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	AssignmentGradeRepository assignmentGradeRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@GetMapping("/assignment/{id}")
	public Assignment getAssignments(@PathVariable("id") Integer assignmentId) {
		
		String email = "dwisneski@csumb.edu"; // user name being instructor email
		Assignment assignment = checkAssignment(assignmentId, email);
		Assignment result = new Assignment();
		
		result.setId(assignment.getId());
		result.setName(assignment.getName());
		result.setDueDate(assignment.getDueDate());
		result.setNeedsGrading(assignment.getNeedsGrading());
		
		return result;
	}
	
	@PostMapping("/assignment/new")
	public void createAssignment() {
		String email = "dwisneski@csumb.edu";
		
		
	}
	
	@DeleteMapping("/assignment/{id}")
	@Transactional
	public void deleteAssignment(@PathVariable("id") Integer assignmentId) {
		String email = "dwisneski@csumb.edu";
		Assignment assignment = checkAssignment(assignmentId, email);
	}
	
	private Assignment checkAssignment(int assignmentId, String email) {
		//get assignment
		Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
		if (assignment == null) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Assignment not found. " + assignmentId);
		}
		//check that user is the instructor
		if (!assignment.getCourse().getInstructor().equals(email)) {
			throw new ResponseStatusException( HttpStatus.UNAUTHORIZED, "Not Authorized.");
		}
		
		return assignment;
	}
}
