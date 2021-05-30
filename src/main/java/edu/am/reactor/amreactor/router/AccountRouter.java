package edu.am.reactor.amreactor.router;

import edu.am.reactor.amreactor.constant.AccountConstant;
import edu.am.reactor.amreactor.handler.AccountHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class AccountRouter {

    @Bean
    public RouterFunction<ServerResponse> AccountRouteRegister(AccountHandler accountHandler){

        return RouterFunctions.route(GET(AccountConstant.GET_ALL_ACCOUNTS.getUrl())
                .and(accept(MediaType.APPLICATION_JSON)), accountHandler::getAllAccounts)
                .andRoute(GET(AccountConstant.GET_ACCOUNT_BY_ID.getUrl()+"{accountId}")
                .and(accept(MediaType.APPLICATION_JSON)), accountHandler::getAccountById)
                .andRoute(PUT(AccountConstant.UPDATE_ACCOUNT.getUrl())
                .and(accept(MediaType.APPLICATION_JSON)), accountHandler::updateAccount);
    }
}
