package com.example.pinokkio.api.room;

import com.example.pinokkio.api.teller.Teller;
import com.example.pinokkio.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Entity
@Table(name = "room")
@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Room extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "BINARY(16)", name = "room_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teller_id")
    private Teller teller;

    @ColumnDefault("0")
    private Integer numberOfPeople;

    @Builder
    public Room(UUID id, Teller teller, Integer numberOfPeople) {
        this.id = id;
        this.teller = teller;
        this.numberOfPeople = numberOfPeople;
    }
}
