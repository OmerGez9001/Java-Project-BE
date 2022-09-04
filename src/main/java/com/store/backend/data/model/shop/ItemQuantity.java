package com.store.backend.data.model.shop;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemQuantity {
    @EmbeddedId
    private ItemQuantityKey id;

    @ManyToOne
    @MapsId("itemId")
    private Item item;

    @ManyToOne
    @MapsId("shopId")
    private Shop shop;

    @Column
    @NotNull
    private long quantity;
}
