package com.store.backend.data.model.worker;

import com.store.backend.data.model.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "worker")
@Data
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Worker {
    @Id
    private String username;
    @Column
    private String password;
    @Column
    private String workerId;
    @Column
    private String id;
    @Column
    private String fullName;
    @Column
    private String phoneNumber;
    @Column
    private String accountNumber;
    @Column
    private Job job;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shop_id")
    private Shop shop;
}
