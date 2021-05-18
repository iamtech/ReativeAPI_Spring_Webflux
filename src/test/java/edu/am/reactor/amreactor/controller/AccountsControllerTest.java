package edu.am.reactor.amreactor.controller;

import edu.am.reactor.amreactor.document.AccountsDocument;
import edu.am.reactor.amreactor.repository.AccountsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebFluxTest
//@DataMongoTest
//@DirtiesContext
public class AccountsControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    AccountsRepository accountsRepository;

    private List<AccountsDocument> accountsData = new ArrayList<>();

    @Before
    public void generateAccountsData() {

        accountsData.add(new AccountsDocument(null, 370583, 10000, List.of("Brokerage", "Commodity", "InvestmentStock")));
        accountsData.add(new AccountsDocument(null, 371138, 9000, List.of("Derivatives", "InvestmentStock")));
        accountsData.add(new AccountsDocument(null, 353465, 10000, List.of("CurrencyService", "InvestmentStock")));
        accountsData.add(new AccountsDocument(null, 785786, 10000, List.of("Derivatives", "InvestmentStock", "InvestmentFund", "CurrencyService")));

        Mockito.when(this.accountsRepository.findAll())
                .thenReturn(Flux.fromIterable(accountsData));

        Mockito.when(this.accountsRepository.findByAccountId(Mockito.anyInt()))
                .thenReturn(Flux.fromIterable(accountsData).elementAt(2)); // To fetch element at 2nd index as mono

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
    public void getAccountByIdTest() {

        webTestClient.get()
                .uri("/accounts/getById/{id}", 353465)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.accountId", 353465);    // JsonPath expressions always refer to a JSON structure in the same way as XPath expression
    }
}
