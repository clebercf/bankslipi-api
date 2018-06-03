package br.com.contaazul.bankslip.api.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import br.com.contaazul.bankslip.api.entity.BankSlip;
import br.com.contaazul.bankslip.api.repository.BankSlipRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BankSlipRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BankSlipRepository bankSlipRepository;
	
	@Test
	public void whenSave_thenCheckById() {
	    // given
		String customer = "Cleber Fonseca";
		String status = "PENDING";
		BigDecimal totalInCents = BigDecimal.ONE;
		BankSlip bankSlip = new BankSlip(customer, status, totalInCents, new Date());
	    entityManager.persist(bankSlip);
	    entityManager.flush();
	 
	    // when
	    Optional<BankSlip> bankSlipFound = bankSlipRepository.findById(bankSlip.getId());
	 
	    // then
	    assertThat(bankSlipFound.get().getCustomer()).isEqualTo(customer);
	    assertThat(bankSlipFound.get().getStatus()).isEqualTo(status);
	    assertThat(bankSlipFound.get().getTotaInCents()).isEqualTo(totalInCents);
	}
	
	@Test
	public void whenSaveTwo_thenCheckTheList() {
	    // given
		BankSlip bankSlipVanessa = new BankSlip("Vanessa", "PENDING", BigDecimal.ONE, new Date());
	    entityManager.persist(bankSlipVanessa);
		BankSlip bankSlipCleber = new BankSlip("Cleber", "PENDING", BigDecimal.ONE, new Date());
	    entityManager.persist(bankSlipCleber);
	    entityManager.flush();
	 
	    // when
	    List<BankSlip> list = bankSlipRepository.findAll();
	 
	    // then
	    assertThat(list.size()).isEqualTo(2);
	    assertThat(list.get(0).getCustomer()).isEqualTo("Vanessa");
	    assertThat(list.get(1).getCustomer()).isEqualTo("Cleber");
	}
	
	@Test
	public void whenNotPersistedId_thenIsNotPresent() {
	    // given
		UUID uuid = UUID.randomUUID();
	 
	    // when
		Optional<BankSlip> bankSlip = bankSlipRepository.findById(uuid);
	 
	    // then
	    assertThat(bankSlip.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void whenChangeStatusToPaid_thenIsPaid() {
	    // given
		BankSlip bankSlipVanessa = new BankSlip("Vanessa", "PENDING", BigDecimal.ONE, new Date());
	    entityManager.persist(bankSlipVanessa);
	    entityManager.flush();
	 
	    // when
	    bankSlipVanessa.setStatus("PAID");
		bankSlipRepository.save(bankSlipVanessa);
		Optional<BankSlip> bankSlipFound = bankSlipRepository.findById(bankSlipVanessa.getId());
	 
	    // then
	    assertThat(bankSlipFound.isPresent()).isEqualTo(true);
	    assertThat(bankSlipFound.get().getStatus()).isEqualTo("PAID");
	}
	
	@Test
	public void whenChangeStatusToCanceled_thenIsCanceled() {
	    // given
		BankSlip bankSlipVanessa = new BankSlip("Vanessa", "PENDING", BigDecimal.ONE, new Date());
	    entityManager.persist(bankSlipVanessa);
	    entityManager.flush();
	    
	    // when
	    bankSlipVanessa.setStatus("CANCELED");
		bankSlipRepository.save(bankSlipVanessa);
		Optional<BankSlip> bankSlipFound = bankSlipRepository.findById(bankSlipVanessa.getId());
	 
	    // then
	    assertThat(bankSlipFound.isPresent()).isEqualTo(true);
	    assertThat(bankSlipFound.get().getStatus()).isEqualTo("CANCELED");
	}
}
