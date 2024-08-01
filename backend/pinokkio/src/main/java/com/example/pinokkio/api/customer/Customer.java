//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.pinokkio.api.customer;

import com.example.pinokkio.api.pos.Pos;
import com.example.pinokkio.common.BaseEntity;
import com.example.pinokkio.common.type.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customer")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)", name = "customer_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pos_id")
    private Pos pos;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] faceEmbedding;

    @Builder
    public Customer(Pos pos, Gender gender, String phoneNumber, int age, byte[] faceEmbedding) {
        this.pos = pos;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.faceEmbedding = faceEmbedding;
    }

}
