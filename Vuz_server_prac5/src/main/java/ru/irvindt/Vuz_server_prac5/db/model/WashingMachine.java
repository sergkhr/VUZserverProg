package ru.irvindt.Vuz_server_prac5.db.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WashingMachine
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String manufacturer;

    @Column(nullable = false)
    @Getter
    @Setter
    private int tankCapacity;

    @Column(nullable = false)
    @Getter
    @Setter
    private String sellerNumber;

    @Column(nullable = false)
    @Getter
    @Setter
    private String productType;

    @Column(nullable = false)
    @Getter
    @Setter
    private double price;

    @Column(nullable = false)
    @Getter
    @Setter
    private String name;
}
