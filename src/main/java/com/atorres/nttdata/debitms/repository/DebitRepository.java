package com.atorres.nttdata.debitms.repository;

import com.atorres.nttdata.debitms.model.dao.DebitDao;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitRepository extends ReactiveMongoRepository<DebitDao,String> {
}
