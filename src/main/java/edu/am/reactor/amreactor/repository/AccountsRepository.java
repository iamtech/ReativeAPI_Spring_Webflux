package edu.am.reactor.amreactor.repository;

import edu.am.reactor.amreactor.document.AccountsDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface AccountsRepository extends ReactiveMongoRepository<AccountsDocument,String> {

}
