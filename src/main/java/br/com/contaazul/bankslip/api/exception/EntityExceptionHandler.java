package br.com.contaazul.bankslip.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class EntityExceptionHandler extends ResponseEntityExceptionHandler {

	  @Override
	  protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		  return new ResponseEntity<Object>(new CustomResponse("Bankslip not provided in the request body"), HttpStatus.BAD_REQUEST);
	  }
	  
	  @Override
	  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		  return new ResponseEntity<Object>(new CustomResponse("Invalid bankslip provided. The possible reasons are: A field of the provided bankslip was null or with invalid values."),HttpStatus.UNPROCESSABLE_ENTITY);
	  }
}
