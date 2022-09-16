package com.store.backend.data.model.report;

import com.store.backend.data.model.shop.Item;
import com.store.backend.data.model.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "register_history")
@Data
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String workerId;

    @Column
    private String registerId;

    @Column
    private RegisterType registerType;

    @Column
    private RegisterAction registerAction;
}
