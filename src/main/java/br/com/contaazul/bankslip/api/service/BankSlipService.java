package br.com.contaazul.bankslip.api.service;

import java.util.List;
import br.com.contaazul.bankslip.api.exception.BankSlipNotFound;
import br.com.contaazul.bankslip.api.entity.BankSlip;

public interface BankSlipService {

	public void save(BankSlip bankSlip) throws Exception;
	
	public List<BankSlip> getAll();
	
	public BankSlip getById(String id) throws IllegalArgumentException, BankSlipNotFound;
	
	public void pay(String id) throws IllegalArgumentException, BankSlipNotFound;
	
	public void cancel(String id) throws IllegalArgumentException, BankSlipNotFound;
	
}
