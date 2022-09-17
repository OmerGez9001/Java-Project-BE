package com.store.backend.data.model.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;


@Entity(name = "return_chat")
@Data
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReturnChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String workerId;
    @Column
    private Long fromShopId;
    @Column
    private Long toShopId;
    @Column
    private String content;
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private Date created;
}
