package com.store.backend.data.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity(name = "return_chat")
@Data
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnChat {
    @Id
    private String workerId;
    @Column
    private Long fromShopId;
    @Column
    private Long toShopId;
    @Column
    private String content;
}
