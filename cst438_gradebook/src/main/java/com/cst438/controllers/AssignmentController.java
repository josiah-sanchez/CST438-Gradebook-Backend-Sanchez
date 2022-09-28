package com.cst438.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	@GetMapping("/assignment/{id}")
	public AssignmentListDTO.AssignmentDTO getAssignments(@PathVariable("id") Integer assignmentId) {
		
		Assignment assignment = checkAssignment(assignmentId);
		AssignmentListDTO.AssignmentDTO result = new AssignmentListDTO.AssignmentDTO();

		//copy from assignment to result
		result.assignmentId = assignment.getId();
		
		return result;
	}
	
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
		//assignment.setDueDate( Date.valueOf(assignmentDTO.dueDate));
		
		assignment.setCourse(c);
		
		// save the assignment entity, save returns an update assignment entity with assignment id primary key
		Assignment newAssignment = assignmentRepository.save(assignment);
		
		assignmentDTO.assignmentId = newAssignment.getId();
		
		// return assignmentDTO that now contains the primary key
		return assignmentDTO;
	}
	
	@PutMapping("/assignment/{id}")
	@Transactional
	public void updateAssignment(@PathVariable("id") Integer assignmentId, 
			@RequestBody AssignmentListDTO.AssignmentDTO assignmentDTO) {
		
		Course c = courseRepository.findById(assignmentDTO.courseId).get();
		
		Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
		if (assignment == null) {
			//assignment doesn't exist
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Assignment not foudn. " + assignmentId);
		}
		
		Assignment updateAssignment = new Assignment();
		updateAssignment.setName(assignmentDTO.assignmentName);
		updateAssignment.setCourse(c);
		
		assignmentRepository.save(updateAssignment);
	}
	
	@DeleteMapping("/delete/{id}")
	@Transactional
	public void deleteAssignment(@PathVariable("id") Integer assignmentId) {
		Assignment a = checkAssignment(assignmentId);
		// find the assignment and remove it
		
		assignmentRepository.delete(a);
	}
	
	private Assignment checkAssignment(int assignmentId) {
		//get assignment
		Assignment assignment = assignmentRepository.findById(assignmentId).orElse(null);
		if (assignment == null) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Assignment not found. " + assignmentId);
		}
		
		return assignment;
	}
}
