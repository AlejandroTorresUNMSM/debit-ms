package com.atorres.nttdata.debitms.controller;

import com.atorres.nttdata.debitms.service.DebitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debit")
public class DebitController {
	@Autowired
	private DebitService debitService;


}
