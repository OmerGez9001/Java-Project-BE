package com.store.backend.data.model.shop;


import lombok.*;

import javax.persistence.*;


@Entity(name = "shop")
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shop {
    @Id
    private Long id;

    @Column
    private String shopName;

}
