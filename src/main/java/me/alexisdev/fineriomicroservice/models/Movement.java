package me.alexisdev.fineriomicroservice.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("movements")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Movement {
    @Id
    private String id;

    private double amount;
    private double balance;

    private String customDate;
    private String customDescription;
    private String date;
    private String dateCreated;
    private String description;
    private String lastUpdated;
    private String type;

    private boolean deleted;
    private boolean duplicated;
    private boolean hasConcepts;
    private boolean inResume;

    private Account account;
    private Category category;
}
