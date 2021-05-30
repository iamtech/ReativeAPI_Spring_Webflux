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
        accountsData.add(new AccountsDocument(null, 12345, 700, List.of("Prod11","Prod21")));

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
                .hasSize(5);
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

    @Test
    public void deleteAccountTest(){

        webTestClient.delete()
                .uri("/accounts/delete/{id}",12345)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }


    @Test
    public void updateAccountTest() {

        List<String> productsList = List.of("Prod1Derivatives","Prod2InvestmentStock");
        AccountsDocument updatedAccount = new AccountsDocument(null, 12345, 775, productsList);
        webTestClient.put()
                .uri("/accounts/update/{id}",12345)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedAccount),AccountsDocument.class)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .jsonPath("$.limit").isEqualTo(775)
                .jsonPath("$.products",productsList);
    }
}
