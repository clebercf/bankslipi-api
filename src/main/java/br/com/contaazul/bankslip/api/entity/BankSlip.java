package br.com.contaazul.bankslip.api.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import org.hibernate.annotations.GenericGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
public class BankSlip {
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid2")
	@Column(name = "id", unique = true)
	@JsonInclude(Include.NON_NULL)
	private UUID id;
	
	@NotNull
	@NotEmpty
	@Size(min=2)
	private String customer;
	
	@NotNull
	@NotEmpty
	@Size(min=4, max=8)
	private String status;
	
	@NotNull
	@JsonProperty("total_in_cents")
	@Positive
	private BigDecimal totaInCents;
	
	@NotNull
	@JsonProperty("due_date")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="ETC/GMT+3")
	private Date dueDate;
	
	@JsonInclude(Include.NON_NULL)
	private BigDecimal fine;
	
	public BankSlip() {
		super();
	}

	public BankSlip(String customer, String status, BigDecimal totaInCents, Date dueDate) {
		super();
		this.customer = customer;
		this.status = status;
		this.totaInCents = totaInCents;
		this.dueDate = dueDate;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getCustomer() {
		return customer;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getTotaInCents() {
		return totaInCents;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public BigDecimal getFine() {
		return fine;
	}

	public void setFine(BigDecimal fine) {
		this.fine = fine.setScale(2, RoundingMode.FLOOR);
	}
}
