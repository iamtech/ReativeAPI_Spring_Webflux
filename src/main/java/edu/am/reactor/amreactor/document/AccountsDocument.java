package edu.am.reactor.amreactor.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsDocument {

    @Id
    String id;

    @Indexed(unique = true)
    @Field(value = "account_id")
    int accountId;

   // @Field(value = "limit")
    int limit;

    // @Field(value = "products")
    List<String> products;
}
