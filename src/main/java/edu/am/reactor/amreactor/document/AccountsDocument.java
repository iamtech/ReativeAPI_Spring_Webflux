package edu.am.reactor.amreactor.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountsDocument {

    @Id
    String id;
    int AccountId;
    int limit;
    List<String> products;
}
