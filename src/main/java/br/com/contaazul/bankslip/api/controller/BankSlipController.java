package br.com.contaazul.bankslip.api.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.contaazul.bankslip.api.exception.BankSlipNotFound;
import br.com.contaazul.bankslip.api.exception.CustomResponse;
import br.com.contaazul.bankslip.api.entity.BankSlip;
import br.com.contaazul.bankslip.api.service.impl.BankSlipServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

@RestController
@Api(value="List operations to control bankslip")
@RequestMapping("/rest")
public class BankSlipController {
	
	@Autowired
	private BankSlipServiceImpl bankSlipServiceImpl;

	@PostMapping("/bankslips")
	@ApiOperation(value="Create a bankslip")
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Bankslip created"),
		@ApiResponse(code = 500, message = "Failed to save bankslip")
	})
	public ResponseEntity<?> save(@Valid @RequestBody BankSlip bankSlip) {
		try {
			bankSlipServiceImpl.save(bankSlip);
			return new ResponseEntity<Object>(new CustomResponse("Bankslip created"), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<Object>(new CustomResponse("Failed to save bankslip"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/bankslips")
	@ApiOperation(value="List all bankslips")
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Get the all bankslips")
	})
	public List<BankSlip> getAll() {
		return bankSlipServiceImpl.getAll();
	}

	@GetMapping("/bankslips/{id}")
	@ApiOperation(value="Get a detailed bankslip")
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Get the detailed bankslip"),
		@ApiResponse(code = 400, message = "Invalid id provided - it must be a valid UUID"),
		@ApiResponse(code = 404, message = "Bankslip not found")
	})
	public ResponseEntity<?> getById(@PathVariable String id) {
		try {
			BankSlip bankSlip = bankSlipServiceImpl.getById(id);
			return new ResponseEntity<BankSlip>(bankSlip, HttpStatus.OK);
		} catch (BankSlipNotFound e) {
			return new ResponseEntity<Object>(new CustomResponse("Bankslip not found with the specified id"), HttpStatus.NOT_FOUND);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<Object>(new CustomResponse("Invalid id provided - it must be a valid UUID"), HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/bankslips/{id}")
	@ApiOperation(value="Pay or cancel a bankslip")
	@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Updated the bankslip status"),
		@ApiResponse(code = 400, message = "Invalid id provided - it must be a valid UUID"),
		@ApiResponse(code = 404, message = "Bankslip not found")
	})
	public ResponseEntity<?> updateStatus(@RequestBody BankSlip bankSlipBody, @PathVariable String id) {
		try {
			String status = bankSlipBody.getStatus().toUpperCase().trim();
			
			if (status.equals("PAID")) {
				bankSlipServiceImpl.pay(id);
				return new ResponseEntity<Object>(new CustomResponse("Bankslip paid"), HttpStatus.OK);
			} else if (status.equals("CANCELED")) {
				bankSlipServiceImpl.cancel(id);
				return new ResponseEntity<Object>(new CustomResponse("Bankslip canceled"), HttpStatus.OK);
			} else {
				return new ResponseEntity<Object>(new CustomResponse("It's not possible to update to this status."), HttpStatus.NOT_FOUND);
			}
		} catch (BankSlipNotFound | IllegalArgumentException ex) {
			return new ResponseEntity<Object>(new CustomResponse("Bankslip not found with the specified id"), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<Object>(new CustomResponse("Failed to update bankslip"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
