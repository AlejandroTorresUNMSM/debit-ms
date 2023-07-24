package com.atorres.nttdata.debitms.service;

import com.atorres.nttdata.debitms.client.FeignApiClient;
import com.atorres.nttdata.debitms.client.FeignApiProdActive;
import com.atorres.nttdata.debitms.client.FeignApiProdPasive;
import com.atorres.nttdata.debitms.exception.CustomException;
import com.atorres.nttdata.debitms.model.DebitDto;
import com.atorres.nttdata.debitms.model.RequestDebit;
import com.atorres.nttdata.debitms.model.clientms.ClientDto;
import com.atorres.nttdata.debitms.repository.DebitRepository;
import com.atorres.nttdata.debitms.utils.RequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class DebitService {
	@Autowired
	private DebitRepository debitRepository;
	@Autowired
	private RequestMapper requestMapper;
	@Autowired
	private FeignApiClient feignApiClient;
	@Autowired
	private FeignApiProdActive feignApiProdActive;
	@Autowired
	private FeignApiProdPasive feignApiProdPasive;

	/**
	 * Metodo que trae un debitdto de un cliente
	 * @param clientId id cliente
	 * @return debitdto
	 */
	public Flux<DebitDto> getDebit(String clientId){
		return debitRepository.findAll()
						.filter(deb -> deb.getClient().equals(clientId))
						.switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "No tiene tarjeta debito")))
						.map(requestMapper::toDto);
	}

	/**
	 * Metodo que crea una tarjeta de debito
	 * @param request request
	 * @return debitdto
	 */
	public Mono<DebitDto> createDebit(RequestDebit request){
		return this.checkDebts(request.getClient())
						.switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "El cliente no existe")))
						.flatMap(clientDto -> checkAccountAdd(request.getClient(), request.getProduct()))
						.flatMap(accountId -> debitRepository.save(requestMapper.toDao(request,accountId)))
						.map(requestMapper::toDto);
	}


	/**
	 * Metodo que verifica que el cliente no tenga deudas vencidas y retorna un client
	 * @param clientId client id
	 * @return clientDto
	 */
	private Mono<ClientDto> checkDebts(String clientId){
		return feignApiProdActive.getDeuda(clientId)
						.single()
						.flatMap(value -> {
							if(Boolean.TRUE.equals(value))
								return Mono.error(new CustomException(HttpStatus.CONFLICT, "Cliente tiene deudas vencidas"));
							else
								return feignApiClient.getClient(clientId).single();
						});
	}

	/**
	 * Metodo que verifica que si una cuenta se puede agregar
	 * @param clientId client id
	 * @param productId product id
	 * @return accountId
	 */
	private Mono<String> checkAccountAdd(String clientId,String productId){
		return feignApiProdPasive.getAllAccountClient(clientId)
						.filter(ac -> ac.getId().equals(productId))
						.single()
						.switchIfEmpty(Mono.error(new CustomException(HttpStatus.CONFLICT, "La cuenta no existe o no pertenece al cliente")))
						.flatMap(ac -> checkDebitAccount(ac.getId(),clientId));
	}

	/**
	 * Metodo que verifica si la cuenta ya existe en algun debito creado por el cliente
	 * @param accountId account id
	 * @return accountId
	 */
	private Mono<String> checkDebitAccount(String accountId,String clientId){
		return debitRepository.findAll()
						.filter(deb -> deb.getClient().equals(clientId))
						.any(deb -> deb.getProductList().contains(accountId))
						.flatMap(hasElement -> {
							if (Boolean.TRUE.equals(hasElement)) {
								return Mono.error(new CustomException(HttpStatus.BAD_REQUEST,"El accountId ya existe en algún Debit"));
							} else {
								return Mono.just(accountId);
							}
						});
	}



}
