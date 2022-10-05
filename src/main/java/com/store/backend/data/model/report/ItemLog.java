package com.store.backend.data.model.report;

import com.store.backend.data.model.shop.Item;
import com.store.backend.data.model.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "transaction_history")
@Data
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private String transactionId;
    @Column
    private Long quantity;
    @Column
    private Double totalPrice;
    @ManyToOne
    private Item item;
    @ManyToOne
    private Shop shop;
}
