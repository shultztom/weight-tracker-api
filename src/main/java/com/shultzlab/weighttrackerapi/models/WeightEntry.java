package com.shultzlab.weighttrackerapi.models;

import javax.persistence.*;

@Entity
@Table(name = "weightEntries")
public class WeightEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    @Column
    private Double weight;
}
