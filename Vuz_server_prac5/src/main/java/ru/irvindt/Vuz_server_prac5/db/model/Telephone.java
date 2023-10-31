package ru.irvindt.Vuz_server_prac5.db.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Telephone
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
    private int batteryCapacity;

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
