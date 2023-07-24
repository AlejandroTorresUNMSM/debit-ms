package com.atorres.nttdata.debitms.controller;

import com.atorres.nttdata.debitms.model.DebitDto;
import com.atorres.nttdata.debitms.model.RequestDebit;
import com.atorres.nttdata.debitms.service.DebitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/debit")
@Slf4j
public class DebitController {
	@Autowired
	private DebitService debitService;

	/**
	 * Metodo para traer la tarjeta debito
	 * @param clientId id cliente
	 * @return debitdto
	 */
	@GetMapping(value = "/{clientId}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<DebitDto> getDebit(
					@PathVariable String clientId){
		return debitService.getDebit(clientId)
						.doOnNext(account -> log.info("Debito encontrada con exito"));
	}

	/**
	 * Metodo que crea un tarjeta debito y enlaza con la cuenta principal
	 * @param request request debito
	 * @return debitdto
	 */
	@PostMapping(value = "/create",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<DebitDto> createDebit(@RequestBody RequestDebit request){
		return debitService.createDebit(request)
						.doOnSuccess(v -> log.info("Debito creado con exito"));
	}


}
