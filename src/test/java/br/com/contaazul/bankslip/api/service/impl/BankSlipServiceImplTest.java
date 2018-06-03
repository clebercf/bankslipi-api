package br.com.contaazul.bankslip.api.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import br.com.contaazul.bankslip.api.exception.BankSlipNotFound;
import br.com.contaazul.bankslip.api.entity.BankSlip;
import br.com.contaazul.bankslip.api.repository.BankSlipRepository;
import br.com.contaazul.bankslip.api.service.BankSlipService;

@RunWith(SpringRunner.class)
public class BankSlipServiceImplTest {

	@TestConfiguration
	static class BankSlipServiceImplTestContextConfiguration {

		@Bean
		public BankSlipService employeeService() {
			return new BankSlipServiceImpl();
		}
	}

	@Autowired
	private BankSlipService bankSlipService;

	@MockBean
	private BankSlipRepository bankSlipRepository;

	BankSlip bankSlipVanessa;
	BankSlip bankSlipCleber;

	@Before
	public void setUp() {
		bankSlipVanessa = new BankSlip("Vanessa", "PENDING", BigDecimal.ONE, new Date());
		bankSlipCleber = new BankSlip("Cleber", "PENDING", BigDecimal.ONE, new Date());
	}

	@Test
	public void whenSavingValidObject_thenDontThrowsObject() {
		try {
			Mockito.when(bankSlipRepository.save(bankSlipVanessa)).thenReturn(bankSlipVanessa);
			bankSlipService.save(bankSlipVanessa);
			assertTrue(true);
		} catch (Exception e) {
			Assert.fail("Not expecting any exception");
		}
	}

	@Test
	public void whenSavingInvalidObject_thenThrowsException() {
		try {
			Mockito.when(bankSlipRepository.save(bankSlipCleber)).thenThrow(Exception.class);
			bankSlipService.save(bankSlipCleber);
			Assert.fail("Expecting the exception");
		} catch (Exception e) {
			assertTrue(true);
		}
	}

	@Test
	public void whenQueryingForAll_thenReturnEmptyArray() {
		Mockito.when(bankSlipRepository.findAll()).thenReturn(new ArrayList<BankSlip>());
		List<BankSlip> emptyList = bankSlipService.getAll();
		assertThat(emptyList.size()).isEqualTo(0);
	}

	@Test
	public void whenQueryingForAll_thenReturnFilledList() {
		List<BankSlip> filledList = new ArrayList<BankSlip>();
		filledList.add(bankSlipCleber);
		filledList.add(bankSlipVanessa);
		Mockito.when(bankSlipRepository.findAll()).thenReturn(filledList);
		List<BankSlip> emptyList = bankSlipService.getAll();
		assertThat(emptyList.size()).isEqualTo(2);
	}

	@Test
	public void whenPaying_thenThrowsIllegalArgumentException() {
		try {
			bankSlipService.pay("7");
			Assert.fail("Expecting the exception");
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			assertThat(e.getClass()).isEqualTo(IllegalArgumentException.class);
		}
	}

	@Test
	public void whenPaying_thenThrowsBankSlipNotFound() {
		UUID uuid = UUID.randomUUID();
		Optional<BankSlip> emptyBankSlip = Optional.empty();
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(emptyBankSlip);
		try {
			bankSlipService.pay(uuid.toString());
			Assert.fail("Expecting the exception");
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			assertThat(e.getClass()).isEqualTo(BankSlipNotFound.class);
		}
	}

	@Test
	public void whenPaying_thenDontThrowsException() {
		UUID uuid = UUID.randomUUID();
		BankSlip bankSlipVanessaSaved = new BankSlip("Vanessa", "PAID", BigDecimal.ONE, new Date());
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(Optional.of(bankSlipVanessa));
		Mockito.when(bankSlipRepository.save(bankSlipVanessa)).thenReturn(bankSlipVanessaSaved);
		try {
			bankSlipService.pay(uuid.toString());
			assertTrue(true);
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			Assert.fail("Not expecting any exception");
		}
	}

	@Test
	public void whenCanceling_thenThrowsIllegalArgumentException() {
		try {
			bankSlipService.cancel("7");
			Assert.fail("Expecting the exception");
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			assertThat(e.getClass()).isEqualTo(IllegalArgumentException.class);
		}
	}

	@Test
	public void whenCanceling_thenThrowsBankSlipNotFound() {
		UUID uuid = UUID.randomUUID();
		Optional<BankSlip> emptyBankSlip = Optional.empty();
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(emptyBankSlip);
		try {
			bankSlipService.cancel(uuid.toString());
			Assert.fail("Expecting the exception");
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			assertThat(e.getClass()).isEqualTo(BankSlipNotFound.class);

		}
	}

	@Test
	public void whenCanceling_thenDontThrowsException() {
		UUID uuid = UUID.randomUUID();
		BankSlip bankSlipVanessaSaved = new BankSlip("Vanessa", "CANCELED", BigDecimal.ONE, new Date());
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(Optional.of(bankSlipVanessa));
		Mockito.when(bankSlipRepository.save(bankSlipVanessa)).thenReturn(bankSlipVanessaSaved);
		try {
			bankSlipService.cancel(uuid.toString());
			assertTrue(true);
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			Assert.fail("Not expecting any exception");
		}
	}

	@Test
	public void whenGettingAnObject_thenReturnTheObject() {
		UUID uuid = UUID.randomUUID();
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(Optional.of(bankSlipVanessa));
		try {
			BankSlip bankSlip = bankSlipService.getById(uuid.toString());
			assertThat(bankSlip.getCustomer()).isEqualTo(bankSlipVanessa.getCustomer());
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			Assert.fail("Not expecting any exception");
		}
	}

	@Test
	public void whenGettingPendingObjectWithDueDateSameDay_thenFineMustBeZero() {
		UUID uuid = UUID.randomUUID();
		BankSlip bankSlipPending = new BankSlip("Vanessa", "PENDING", BigDecimal.ONE, new Date());
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(Optional.of(bankSlipPending));
		try {
			BankSlip bankSlip = bankSlipService.getById(uuid.toString());
			assertTrue(bankSlip.getFine().compareTo(new BigDecimal(0.00)) == 0);
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			Assert.fail("Not expecting any exception");
		}
	}

	@Test
	public void whenGettingPendingObjectInTwoDaysAdvance_thenFineMustBeZero() {
		UUID uuid = UUID.randomUUID();
		BankSlip bankSlipPending = new BankSlip("Vanessa", "PENDING", new BigDecimal(100), addDays(10));
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(Optional.of(bankSlipPending));
		try {
			BankSlip bankSlip = bankSlipService.getById(uuid.toString());
			assertTrue(bankSlip.getFine().compareTo(new BigDecimal(0.00)) == 0);
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			Assert.fail("Not expecting any exception");
		}
	}
	
	@Test
	public void whenGettingPendingObjectInFiveDays_thenMustHaveFine() {
		UUID uuid = UUID.randomUUID();
		BankSlip bankSlipPending = new BankSlip("Vanessa", "PENDING", new BigDecimal(100), addDays(-5));
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(Optional.of(bankSlipPending));
		try {
			BankSlip bankSlip = bankSlipService.getById(uuid.toString());
			assertTrue(bankSlip.getFine().compareTo(new BigDecimal(2.5)) == 0);
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			Assert.fail("Not expecting any exception");
		}
	}
	
	@Test
	public void whenGettingPendingObjectInTenDays_thenMustHaveFine() {
		UUID uuid = UUID.randomUUID();
		BankSlip bankSlipPending = new BankSlip("Vanessa", "PENDING", new BigDecimal(100), addDays(-10));
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(Optional.of(bankSlipPending));
		try {
			BankSlip bankSlip = bankSlipService.getById(uuid.toString());
			assertTrue(bankSlip.getFine().compareTo(new BigDecimal(5)) == 0);
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			Assert.fail("Not expecting any exception");
		}
	}

	@Test
	public void whenGettingPendingObjectInFifteenDays_thenMustHaveFine() {
		UUID uuid = UUID.randomUUID();
		BankSlip bankSlipPending = new BankSlip("Vanessa", "PENDING", new BigDecimal(100), addDays(-15));
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(Optional.of(bankSlipPending));
		try {
			BankSlip bankSlip = bankSlipService.getById(uuid.toString());
			assertTrue(bankSlip.getFine().compareTo(new BigDecimal(15)) == 0);
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			Assert.fail("Not expecting any exception");
		}
	}
	
	@Test
	public void whenGettingNotSavedObject_thenReturnBankSlipNotFoundException() {
		UUID uuid = UUID.randomUUID();
		Optional<BankSlip> emptyBankSlip = Optional.empty();
		Mockito.when(bankSlipRepository.findById(uuid)).thenReturn(emptyBankSlip);
		try {
			bankSlipService.getById(uuid.toString());
			Assert.fail("Expecting the exception");
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			assertThat(e.getClass()).isEqualTo(BankSlipNotFound.class);

		}
	}

	@Test
	public void whenGettingAnObjectWithInvalidID_thenReturnIllegalArgumentException() {
		try {
			bankSlipService.getById("7");
			Assert.fail("Expecting the exception");
		} catch (IllegalArgumentException | BankSlipNotFound e) {
			assertThat(e.getClass()).isEqualTo(IllegalArgumentException.class);
		}
	}
	
	private Date addDays(int amount) {
		Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, amount);
        return c.getTime();		
	}
}