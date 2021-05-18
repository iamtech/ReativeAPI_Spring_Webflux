package edu.am.reactor.amreactor.repository;

import edu.am.reactor.amreactor.document.AccountsDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
// Test annotation which indicates that the ApplicationContext associated with a test is dirty and should therefore be closed and removed from the context cache.
@DirtiesContext // Required to have a different context of  AccountsRepository for each testcase
public class AccountsRepositoryTest {

    @Autowired
    AccountsRepository accountsRepository;

    private List<AccountsDocument> accountsData = new ArrayList<>();

    @Before
    public void generateAccountsData(){

        accountsData.add(new AccountsDocument(null, 1, 3, List.of("Prod11","Prod12")));
        accountsData.add(new AccountsDocument(null, 2, 2, List.of("Prod21","Prod22")));
        accountsData.add(new AccountsDocument(null, 3, 3, List.of("Prod31","Prod32")));
        accountsData.add(new AccountsDocument("ID123", 4, 2, List.of("Prod41","Prod42","Prod43")));

        accountsRepository.deleteAll()
                .thenMany(Flux.fromIterable(accountsData))
                .flatMap(accountsRepository::save)
                .doOnNext((accdata) -> {
                    System.out.println(accdata.toString());
                })
                .blockLast();

    }

    @Test
    public void getAllAccountsDataTest(){

        StepVerifier.create(accountsRepository.findAll())
                .expectSubscription()
                .expectNextCount(4)
                .verifyComplete();
    }

    @Test
    public void getAccountDataByIDTest(){

        StepVerifier.create(accountsRepository.findById("ID123"))
                .expectSubscription()
                .expectNextMatches((acc) -> acc.getAccountId() == 4)
                .verifyComplete();
    }


    @Test
    public void getAccountDataByLimitTest(){

        StepVerifier.create(accountsRepository.findByLimit(2).log("Account data:"))
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void getAccountDataByProductTest(){

        StepVerifier.create(accountsRepository.findByProductsContaining("Prod41").log("Account data:"))
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }
}
