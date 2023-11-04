package ru.irvindt.Vuz_server_prac5.db.model;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;


@Entity
//@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
public class Book
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private String author;

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
    private String title;

    @Column(nullable = false)
    @Getter
    @Setter
    private int inStock; // -1 - нет в наличии, >0 - количество в наличии
}
