package com.atorres.nttdata.debitms.model.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("debits")
public class DebitDao {
	@Id
	private String id;
	private String client;
}
