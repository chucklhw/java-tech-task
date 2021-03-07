package com.rezdy.lunch.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@lombok.Data
@Entity
@Table(name = "ingredient")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ingredient {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "BEST_BEFORE")
    private LocalDate bestBefore;

    @Column(name = "USE_BY")
    private LocalDate useBy;

    @JsonIgnoreProperties("ingredients")
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.REFRESH,
                mappedBy = "ingredients",
                fetch = FetchType.LAZY)
    private List<Recipe> recipes;
}
