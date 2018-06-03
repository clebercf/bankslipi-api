package br.com.contaazul.bankslip.api.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.contaazul.bankslip.api.exception.BankSlipNotFound;
import br.com.contaazul.bankslip.api.entity.BankSlip;
import br.com.contaazul.bankslip.api.repository.BankSlipRepository;
import br.com.contaazul.bankslip.api.service.BankSlipService;

@Service
public class BankSlipServiceImpl implements BankSlipService {

	@Autowired
	private BankSlipRepository bankSlipRepository;
	
	@Override
	public void save(BankSlip bankSlip) throws Exception {
		bankSlipRepository.save(bankSlip);
	}

	@Override
	public List<BankSlip> getAll() {
		return bankSlipRepository.findAll();
	}

	@Override
	public BankSlip getById(String id) throws IllegalArgumentException, BankSlipNotFound {
		UUID uuid = parseUUID(id);
		Optional<BankSlip> bankSlip = bankSlipRepository.findById(uuid);
		if ( bankSlip.isPresent() ) {
			return calculateFine(bankSlip.get());
		} else {
			throw new BankSlipNotFound();
		}
	}

	@Override
	public void pay(String id) throws IllegalArgumentException, BankSlipNotFound {
		updateStatus(id, "PAID");
	}

	@Override
	public void cancel(String id) throws IllegalArgumentException, BankSlipNotFound {
		updateStatus(id, "CANCELED");
	}
	
	private BankSlip calculateFine(BankSlip bankSlip) {
		if (bankSlip.getStatus().equals("PENDING") && new Date().after(bankSlip.getDueDate())) {
		    long days = TimeUnit.DAYS.convert(Math.abs(bankSlip.getDueDate().getTime() - new Date().getTime()), TimeUnit.MILLISECONDS);		    
		    BigDecimal fine = getFine(days);
		    bankSlip.setFine(bankSlip.getTotaInCents().multiply(new BigDecimal(days)).multiply(fine));
		} else {
			bankSlip.setFine(new BigDecimal(0.00));
		}
		return bankSlip;
	}
	
	private BigDecimal getFine(long days) {
	    BigDecimal fine = new BigDecimal(0.00);
	    if (days > 0 && days <= 10 ) {
	    	fine = BigDecimal.valueOf(0.005);
	    } else if (days > 10) {
	    	fine = BigDecimal.valueOf(0.01);
	    }
	    return fine;		
	}
	
	private void updateStatus(String id, String status) throws IllegalArgumentException, BankSlipNotFound {
		UUID uuid = parseUUID(id);
		Optional<BankSlip> bankSlip = bankSlipRepository.findById(uuid);
		if ( bankSlip.isPresent() ) {
			bankSlip.get().setStatus(status);
			bankSlipRepository.save(bankSlip.get());
		} else {
			throw new BankSlipNotFound();
		}
	}
	
	private UUID parseUUID(String id) throws IllegalArgumentException {
		try {
			return UUID.fromString(id);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
}
