package edu.am.reactor.amreactor.handler;

import edu.am.reactor.amreactor.document.AccountsDocument;
import edu.am.reactor.amreactor.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class AccountHandler {

    @Autowired
    AccountsRepository accountsRepository;

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    public Mono<ServerResponse> getAllAccounts(ServerRequest serverRequest){

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(accountsRepository.findAll(), AccountsDocument.class);
    }


    public Mono<ServerResponse> getAccountById(ServerRequest serverRequest){

        int reqAccountId = Integer.parseInt(serverRequest.pathVariable("accountId"));
        Mono<AccountsDocument> itemMono = accountsRepository.findByAccountId(reqAccountId);

        return itemMono.flatMap(account ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(account)))
                .switchIfEmpty(notFound);
    }


    public Mono<ServerResponse> updateAccount(ServerRequest serverRequest){

       // int reqAccountId = Integer.parseInt(serverRequest.pathVariable("accountId"));

        Mono<AccountsDocument> updatedAccount = serverRequest.bodyToMono(AccountsDocument.class)
                .flatMap(reqAccount -> {

                    return accountsRepository.findByAccountId(reqAccount.getAccountId())
                            .flatMap(dbAccount -> {
                                dbAccount.setLimit(reqAccount.getLimit());
                                dbAccount.setProducts(reqAccount.getProducts());
                                return accountsRepository.save(dbAccount);
                            });
                });

        return updatedAccount.flatMap(account ->
                ServerResponse.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(account)))
                .switchIfEmpty(notFound);
    }
}
