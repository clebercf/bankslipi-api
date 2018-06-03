package br.com.contaazul.bankslip.api.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.List;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.contaazul.bankslip.api.BankslipApiApplication;
import br.com.contaazul.bankslip.api.entity.BankSlip;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = BankslipApiApplication.class
)
@AutoConfigureMockMvc
public class BankSlipControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;
	
	@Test
	public void IntegrationTest() throws Exception {
		BankSlip bankSlipFirst = new BankSlip("Trillian Company", "PENDING", new BigDecimal(100000), new GregorianCalendar(2018, 01, 01).getTime());
		BankSlip bankSlipSecond = new BankSlip("Campbell Company", "PENDING", new BigDecimal(500000), new GregorianCalendar(2018, 01, 05).getTime());

		mvc.perform(post("/rest/bankslips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(bankSlipFirst)))
				.andExpect(jsonPath("$.message", is("Bankslip created")))
				.andExpect(status().isCreated());
		
		mvc.perform(post("/rest/bankslips")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(bankSlipSecond)))
				.andExpect(jsonPath("$.message", is("Bankslip created")))
				.andExpect(status().isCreated());
		
		 MvcResult getAllResult =  mvc.perform(get("/rest/bankslips")
				 .contentType(MediaType.APPLICATION_JSON))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$", hasSize(2)))
				 .andExpect(jsonPath("$[0].customer", is(bankSlipFirst.getCustomer())))
				 .andExpect(jsonPath("$[1].customer", is(bankSlipSecond.getCustomer()))).andReturn();
		 
		 ObjectMapper mapper = new ObjectMapper();
		 List<BankSlip> list = mapper.readValue(getAllResult.getResponse().getContentAsString(), new TypeReference<List<BankSlip>>() {});
		 
		 mvc.perform(get("/rest/bankslips/" + list.get(0).getId().toString())
				 .contentType(MediaType.APPLICATION_JSON))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.customer", is(bankSlipFirst.getCustomer())))
				 .andExpect(jsonPath("$.fine").exists());
		 
		JSONObject payJson = new JSONObject();
		payJson.put("status", "PAID");
		
		 mvc.perform(put("/rest/bankslips/" + list.get(0).getId().toString())
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(payJson.toString()))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.message", is("Bankslip paid")));
			 
		JSONObject cancelJson = new JSONObject();
		cancelJson.put("status", "CANCELED");
		
		 mvc.perform(put("/rest/bankslips/" + list.get(1).getId().toString())
				 .contentType(MediaType.APPLICATION_JSON)
				 .content(cancelJson.toString()))
				 .andExpect(status().isOk())
				 .andExpect(jsonPath("$.message", is("Bankslip canceled")));
		 
	}
	
	 private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	 }
}
