package com.abridged.forestrymanagementsystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {
	@ExceptionHandler(value = NotFoundException.class)
	public ResponseEntity<Object> exception(NotFoundException exception) {
		return new ResponseEntity<>("Record not found", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = EmptyRecordException.class)
	public ResponseEntity<Object> exception(EmptyRecordException exception) {
		return new ResponseEntity<>("Record is empty", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = AlreadyPresentException.class)
	public ResponseEntity<Object> exception(AlreadyPresentException exception) {
		return new ResponseEntity<>("Duplicate record or record is not present", HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = ValidationException.class)
	public ResponseEntity<Object> exception(ValidationException exception) {
		return new ResponseEntity<>("Id or Password is wrong", HttpStatus.NOT_FOUND);
	}
}