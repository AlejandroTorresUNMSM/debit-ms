package com.atorres.nttdata.debitms.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class DebitDto {
	private String id;
	private String client;
	private ArrayList<String> productList;
}
