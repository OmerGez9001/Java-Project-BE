package com.store.backend.data.model.report;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.UUID;

@Data
@Document(indexName = "transactionlog")
@SuperBuilder()
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog extends BaseActionLog {

    @Id
    @JsonIgnore
    private String _id;

    @Field(type = FieldType.Keyword)
    private String transactionId;

    @MultiField(mainField = @Field(type = FieldType.Text), otherFields = @InnerField(suffix = "keyword", type = FieldType.Keyword))
    private String itemName;

    @Field(type = FieldType.Text)
    private String shopName;

    @Field(type = FieldType.Long)
    private Long quantity;

    @Field(type = FieldType.Double)
    private Double priceAfterDiscount;

    @Field(type = FieldType.Keyword)
    private TransactionAction transactionAction;
}
