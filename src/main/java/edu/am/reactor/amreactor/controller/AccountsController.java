package edu.am.reactor.amreactor.controller;

import edu.am.reactor.amreactor.document.AccountsDocument;
import edu.am.reactor.amreactor.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(path="/accounts")
public class AccountsController {

    @Autowired
    AccountsRepository accountsRepository;

    @GetMapping("/get")
    public Flux<AccountsDocument> getAllAccounts(){
       return accountsRepository.findAll();
    }

    @GetMapping("/getLimited/{limit}")
    public Flux<AccountsDocument> getLimitedAccounts(@PathVariable long limit){
        return accountsRepository.findAll().limitRequest(limit);
    }

    @GetMapping("/getById/{id}")
    public Mono<ResponseEntity<AccountsDocument>> getAccountById(@PathVariable int id){
        return accountsRepository.findByAccountId(id)
                .map((accountsData) -> new ResponseEntity<>(accountsData, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    /*    Output:
        {"id":"5ca4bbc7a2dd94ee5816239a","accountId":370583,"limit":10000,"products":["Brokerage","Commodity","InvestmentStock"]}*/
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountsDocument> addAccount(@RequestBody AccountsDocument accountsDocument){
        return accountsRepository.save(accountsDocument);
    }


    @DeleteMapping(value = "/delete/{id}")
    public Mono<Void> deleteAccount(@PathVariable int id){
        return accountsRepository.deleteByAccountId(id);
    }


    @PutMapping(value = "/update/{id}")
    public Mono<ResponseEntity> updateAccount(@PathVariable int id, @RequestBody AccountsDocument accountsDocument){
        return accountsRepository.findByAccountId(accountsDocument.getAccountId())
                .flatMap(dbDocument -> {
                    dbDocument.setLimit(accountsDocument.getLimit());
                    dbDocument.setProducts(accountsDocument.getProducts());
                    return accountsRepository.save(dbDocument);  })
                .map((updatedAccounts) -> new ResponseEntity(updatedAccounts, HttpStatus.ACCEPTED))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
}
