package com.cst438.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.cst438.domain.*;
import com.cst438.domain.AssignmentListDTO.AssignmentDTO;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
public class AssignmentController {

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	CourseRepository courseRepository;
	
	@GetMapping("/assignment/{id}")
	public AssignmentListDTO.AssignmentDTO getAssignments(@PathVariable("id") Integer assignmentId) throws ResponseStatusException{
		
		Assignment assignment = checkAssignment(assignmentId);
		AssignmentListDTO.AssignmentDTO result = new AssignmentListDTO.AssignmentDTO();

		//copy from assignment to result
		result.assignmentId = assignment.getId();
		result.assignmentName = assignment.getName();
		
		return result;
	}
	
	@PostMapping("/assignment")
	@Transactional
	public AssignmentListDTO.AssignmentDTO addAssignment(@RequestBody AssignmentListDTO.AssignmentDTO adto) {
		
		// lookup the course
		Course c = courseRepository.findById(adto.courseId).orElse(null);
		if (c == null) {
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Course not valid");
		}
		
		Assignment assignment = new Assignment();
		
		assignment.setName(adto.assignmentName);
		assignment.setDueDate(Date.valueOf(adto.dueDate));
		assignment.setCourse(c);
		assignment.setNeedsGrading(1);
		
		Assignment anew = assignmentRepository.save(assignment);
		adto.assignmentId = anew.getId();
		System.out.println(adto.assignmentId);
		System.out.println(adto.assignmentName);
		
		return adto;
	}
	
	@PutMapping("/assignment/{id}")
	@Transactional
	public void updateAssignment(@PathVariable("id") Integer assignmentId, 
			@RequestBody AssignmentListDTO.AssignmentDTO assignmentDTO) {
		
		Course c = courseRepository.findById(assignmentDTO.courseId).get();
		
		Optional<Assignment> assignment = assignmentRepository.findById(assignmentId);
		if (assignment == null) {
			//assignment doesn't exist
			throw new ResponseStatusException( HttpStatus.BAD_REQUEST, "Assignment not found. " + assignmentId);
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
