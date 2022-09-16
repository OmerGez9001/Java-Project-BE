package com.store.backend.data.model.report;

import com.store.backend.data.model.shop.Item;
import com.store.backend.data.model.shop.Shop;

import javax.persistence.*;

public class RegisterLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column
    private String workerId;

    @Column
    private String registerId;

    @Column
    private RegisterType registerType;

    @Column
    private RegisterAction registerAction;
}
