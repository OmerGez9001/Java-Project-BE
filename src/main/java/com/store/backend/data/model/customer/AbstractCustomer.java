package com.store.backend.data.model.customer;


import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonSubTypes.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        include = JsonTypeInfo.As.EXISTING_PROPERTY
)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonSubTypes({
        @Type(name = "NewCustomer", value = NewCustomer.class),
        @Type(name = "RepeatCustomer", value = RepeatCustomer.class),
        @Type(name = "VipCustomer", value = VipCustomer.class)})
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity(name = "customer")
@Data
@DiscriminatorColumn(name = "type")
@ToString
public abstract class AbstractCustomer {
    @JsonInclude
    @Transient
    private String type = this.getClass().getSimpleName();

    @Id
    private String id;
    @Column
    private String fullName;
    @Column
    private String phoneNumber;

    public abstract double sellDiscount(double price);

    public abstract double buyDiscount(double price);
}
