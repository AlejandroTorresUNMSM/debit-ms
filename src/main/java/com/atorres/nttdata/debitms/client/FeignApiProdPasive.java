package com.atorres.nttdata.debitms.client;

import com.atorres.nttdata.debitms.model.accountms.AccountDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(value = "product-pasive-ms", url = "${prodpasive.ms.url}/")
public interface FeignApiProdPasive {
	@GetMapping(value = "{productId}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Mono<AccountDto> getAccount(@PathVariable String productId);

	@GetMapping(value = "client/{id}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<AccountDto> getAllAccountClient(@PathVariable String id);
}