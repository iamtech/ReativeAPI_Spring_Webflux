package edu.am.reactor.amreactor.repository;

import edu.am.reactor.amreactor.document.AccountsDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountsRepository extends ReactiveMongoRepository<AccountsDocument,String> {

    Flux<AccountsDocument> findByLimit(int limit);

    Flux<AccountsDocument> findByProductsContaining(String product);

    Mono<AccountsDocument> findByAccountId(int accountId);
}
