package com.atorres.nttdata.debitms.model;

import lombok.Data;

@Data
public class RequestAddDebit {
	private String client;
	private String product;
	private String debit;
}
