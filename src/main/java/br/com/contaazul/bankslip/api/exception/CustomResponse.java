package br.com.contaazul.bankslip.api.exception;

public class CustomResponse {
	
	  private String message;

	  public CustomResponse(String message) {
	    this.message = message;
	  }

	  public String getMessage() {
	    return message;
	  }
}