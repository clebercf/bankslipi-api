package br.com.contaazul.bankslip.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;
import static org.mockito.BDDMockito.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.contaazul.bankslip.api.entity.BankSlip;
import br.com.contaazul.bankslip.api.exception.BankSlipNotFound;
import br.com.contaazul.bankslip.api.exception.EntityExceptionHandler;
import br.com.contaazul.bankslip.api.service.impl.BankSlipServiceImpl;

@RunWith(SpringRunner.class)
@WebMvcTest(BankSlipController.class)
public class BankSlipControllerUnitTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BankSlipServiceImpl service;

	@InjectMocks
	private BankSlipController bankSlipController;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(bankSlipController)
				.setControllerAdvice(new EntityExceptionHandler())
				.build();
	}

	@Test
	public void shouldSaveBankSlip() throws Exception {
		BankSlip bankSlip = new BankSlip("Trillian Company", "PENDING", new BigDecimal(100000), new GregorianCalendar(2018, 01, 01).getTime());
		willDoNothing().given(service).save(bankSlip);

		mvc.perform(post("/rest/bankslips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(bankSlip)))
				.andExpect(jsonPath("$.message", is("Bankslip created")))
				.andExpect(status().isCreated());
	}

	@Test
	public void shouldNotSaveBankSlipThenReturnInternalServerError() throws Exception {
		BankSlip bankSlip = new BankSlip("Trillian Company", "PENDING", new BigDecimal(100000), new GregorianCalendar(2018, 01, 01).getTime());
		willThrow(new Exception()).given(service).save(any());

		mvc.perform(post("/rest/bankslips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(bankSlip)))
				.andExpect(jsonPath("$.message", is("Failed to save bankslip")))
				.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void shouldNotSaveBankSlipThenReturn422ToDueDate() throws Exception {
		mvc.perform(post("/rest/bankslips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getInvalidBankSlipPayloadToPost("due_date")))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.message", is("Invalid bankslip provided. The possible reasons are: A field of the provided bankslip was null or with invalid values.")));
	}
	
	@Test
	public void shouldNotSaveBankSlipThenReturn422ToCustomer() throws Exception {
		mvc.perform(post("/rest/bankslips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getInvalidBankSlipPayloadToPost("customer")))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.message", is("Invalid bankslip provided. The possible reasons are: A field of the provided bankslip was null or with invalid values.")));
	}
	
	@Test
	public void shouldNotSaveBankSlipThenReturn422ToStatus() throws Exception {
		mvc.perform(post("/rest/bankslips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getInvalidBankSlipPayloadToPost("status")))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.message", is("Invalid bankslip provided. The possible reasons are: A field of the provided bankslip was null or with invalid values.")));
	}
	
	@Test
	public void shouldNotSaveBankSlipThenReturn422ToTotalInCents() throws Exception {
		mvc.perform(post("/rest/bankslips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(getInvalidBankSlipPayloadToPost("total_in_cents")))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(jsonPath("$.message", is("Invalid bankslip provided. The possible reasons are: A field of the provided bankslip was null or with invalid values.")));
	}
	
	@Test
	public void shouldNotSaveBankSlipThenReturn400ToEmptyBody() throws Exception {
		mvc.perform(post("/rest/bankslips")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Bankslip not provided in the request body")));
	}

	 @Test
	 public void shouldGetAllBankLists() throws Exception {
		 BankSlip bankSlip = new BankSlip("Trillian Company", "PENDING", new BigDecimal(100000), new GregorianCalendar(2018,01,01).getTime() );
		 List<BankSlip> allBankSlips = Arrays.asList(bankSlip);
		 given(service.getAll()).willReturn(allBankSlips);
		
		 mvc.perform(get("/rest/bankslips")
				 .contentType(MediaType.APPLICATION_JSON))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$", hasSize(1)))
				 .andExpect(jsonPath("$[0].customer", is(bankSlip.getCustomer())));
	 }
	 
	 @Test
	 public void shouldGetOneBankLisp() throws Exception {
		 String id = "0de4ea90-f06f-430f-9750-bb3a60abab84";
		 BankSlip bankSlip = new BankSlip("Trillian Company", "PENDING", new BigDecimal(100000), new GregorianCalendar(2018,01,01).getTime() );
		 bankSlip.setFine(BigDecimal.ZERO);
		 given(service.getById(id)).willReturn(bankSlip);
		
		 mvc.perform(get("/rest/bankslips/" + id)
				 .contentType(MediaType.APPLICATION_JSON))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.customer", is(bankSlip.getCustomer())))
				 .andExpect(jsonPath("$.fine").exists());
	 }

	 @Test
	 public void shouldFailedToGetOneBankLispThenReturnNotFound() throws Exception {
		 willThrow(new BankSlipNotFound()).given(service).getById(any());
		
		 mvc.perform(get("/rest/bankslips/0de4ea90-f06f-430f-9750-bb3a60abab84")
				 .contentType(MediaType.APPLICATION_JSON))
				 .andExpect(status().isNotFound())
				 .andExpect(jsonPath("$.message", is("Bankslip not found with the specified id")));
	 }
	 
	 @Test
	 public void shouldFailedToGetOneBankLispThenBadRequest() throws Exception {
		 willThrow(new IllegalArgumentException()).given(service).getById(any());
		
		 mvc.perform(get("/rest/bankslips/7")
				 .contentType(MediaType.APPLICATION_JSON))
				 .andExpect(status().isBadRequest())
				 .andExpect(jsonPath("$.message", is("Invalid id provided - it must be a valid UUID")));
	 }
	 
	 @Test
	 public void shouldPayTheBankSlip() throws Exception {
		JSONObject json = new JSONObject();
		json.put("status", "PAID");
		willDoNothing().given(service).pay(any());
		
		 mvc.perform(put("/rest/bankslips/0de4ea90-f06f-430f-9750-bb3a60abab84")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(json.toString()))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.message", is("Bankslip paid")));
	 }
	 
	 @Test
	 public void shouldNotPayTheBankSlipThenReturnNotFound() throws Exception {
		JSONObject json = new JSONObject();
		json.put("status", "PAID");
		willThrow(new BankSlipNotFound()).given(service).pay(any());
		
		mvc.perform(put("/rest/bankslips/0de4ea90-f06f-430f-9750-bb3a60abab84")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json.toString()))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", is("Bankslip not found with the specified id")));
		
		willThrow(new IllegalArgumentException()).given(service).pay(any());
		
		mvc.perform(put("/rest/bankslips/7")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json.toString()))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", is("Bankslip not found with the specified id")));
	 }
	 
	 @Test
	 public void shouldCancelTheBankSlip() throws Exception {
		JSONObject json = new JSONObject();
		json.put("status", "CANCELED");
		willDoNothing().given(service).cancel(any());
		
		 mvc.perform(put("/rest/bankslips/0de4ea90-f06f-430f-9750-bb3a60abab84")
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(json.toString()))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.message", is("Bankslip canceled")));
	 }
	 
	 @Test
	 public void shouldNotCancelTheBankSlipThenReturnNotFound() throws Exception {
		JSONObject json = new JSONObject();
		json.put("status", "CANCELED");
		willThrow(new BankSlipNotFound()).given(service).cancel(any());
		
		mvc.perform(put("/rest/bankslips/0de4ea90-f06f-430f-9750-bb3a60abab84")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json.toString()))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", is("Bankslip not found with the specified id")));
		
		willThrow(new IllegalArgumentException()).given(service).cancel(any());
		
		mvc.perform(put("/rest/bankslips/7")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json.toString()))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", is("Bankslip not found with the specified id")));
	 }
	 
	 @Test
	 public void shouldNotUpdateToDifferentStatus() throws Exception {
		JSONObject json = new JSONObject();
		json.put("status", "XYZ");
		
		mvc.perform(put("/rest/bankslips/0de4ea90-f06f-430f-9750-bb3a60abab84")
			.contentType(MediaType.APPLICATION_JSON)
			.content(json.toString()))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message", is("It's not possible to update to this status.")));
	 }
	 	 
	 private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	 }
	 
	 private String getInvalidBankSlipPayloadToPost(String invalidField) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("due_date", "2018-01-01");
		json.put("customer", "Trillian Company");
		json.put("status", "PENDING");
		json.put("total_in_cents", "100000");
		json.remove(invalidField);
		json.put(invalidField + "_X", "X");
		return json.toString();
	 }
}