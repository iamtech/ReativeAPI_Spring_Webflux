package edu.am.reactor.amreactor.controller;

import edu.am.reactor.amreactor.document.AccountsDocument;
import edu.am.reactor.amreactor.repository.AccountsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class AccountsControllerUsingEmbeddedDBTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    AccountsRepository  accountsRepository;

    private List<AccountsDocument> accountsData = new ArrayList<>();

    @Before
    public void generateAccountsData() {

        accountsData.add(new AccountsDocument(null, 370583, 10000, List.of("Brokerage", "Commodity", "InvestmentStock")));
        accountsData.add(new AccountsDocument(null, 371138, 9000, List.of("Derivatives", "InvestmentStock")));
        accountsData.add(new AccountsDocument(null, 353465, 10000, List.of("CurrencyService", "InvestmentStock")));
        accountsData.add(new AccountsDocument(null, 785786, 10000, List.of("Derivatives", "InvestmentStock", "InvestmentFund", "CurrencyService")));

        accountsRepository.deleteAll()
                .thenMany(Flux.fromIterable(accountsData))
                .flatMap(accountsRepository::save)
                .log("Adding Data")
                .doOnNext((accdata) -> {
                    System.out.println(accdata.toString());
                })
                .blockLast();
    }

    @Test
    public void getAllAccountsTest() {

        webTestClient.get()
                .uri("/accounts/get")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(AccountsDocument.class)
                .value((response) -> {
                    response.forEach(System.out::println);
                })
                .hasSize(4);
    }

    @Test
    public void addAccountTest() {

        AccountsDocument newAccount = new AccountsDocument(null, 1234, 500, List.of("Prod1","Prod2"));
        webTestClient.post()
                .uri("/accounts/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newAccount),AccountsDocument.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.accountId", 1234);
    }
}
