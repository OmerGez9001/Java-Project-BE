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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    @Column
    private String shopName;

}
