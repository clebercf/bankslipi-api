package br.com.contaazul.bankslip.api.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.contaazul.bankslip.api.entity.BankSlip;

@Repository
public interface BankSlipRepository extends JpaRepository<BankSlip, UUID>{
	public Optional<BankSlip> findById(UUID id);
}